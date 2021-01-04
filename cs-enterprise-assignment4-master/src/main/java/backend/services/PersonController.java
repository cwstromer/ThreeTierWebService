package backend.services;

import backend.db.DBConnect;
import backend.db.GatewayException;
import backend.db.PersonGatewayMySQL;
import model.Person;
import model.PersonAudit;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@RestController
public class PersonController {

    private static final Logger logger = LogManager.getLogger();
    private Connection connection;

    // create a connection on startup
    @PostConstruct
    public void startup() {
        try {
            connection = DBConnect.connectToDB();
            logger.info("*** MySQL connection created");
        } catch (SQLException | IOException e) {
            logger.error("*** " + e);
            // TODO: find a better way to force shutdown on connect failure
            System.exit(0);
        }
    }

    // close a connection on shutdown
    @PreDestroy
    public void cleanup() {
        try {
            connection.close();
            logger.info("*** MySQL connection closed");
        } catch (SQLException e) {
            logger.error("*** " + e);
        }
    }

    // GET /people/id
    // get a single person
    @GetMapping("/people/{id}/audittrail")
    public ResponseEntity<String> fetchPerson(@RequestHeader Map<String, String> headers, @PathVariable("id") int id) {

        // check for valid credentials
//        int authVal = CheckAuthorization(headers);
//        if (authVal == 401){
//            return new ResponseEntity<String>("", HttpStatus.valueOf(401));
//        }

        try {
            ArrayList <PersonAudit> peopleArray = new PersonGatewayMySQL(connection).fetchPersonAudit(id);
            if(peopleArray.size() == 0){
                ResponseEntity<String> response = new ResponseEntity<String>("Person " + id + " not found", HttpStatus.valueOf(404));
                return response;
            }
            JSONArray peopleJsonArray = new JSONArray();
            for ( PersonAudit person: peopleArray ){
                JSONObject personJSON = new JSONObject();
                personJSON.put("id", person.getId());
                personJSON.put("change_msg", person.getChange_msg());
                personJSON.put("change_by", person.getChange_by());
                personJSON.put("when_occurred", person.getWhen_Occured());
                personJSON.put("name", person.getName());

                peopleJsonArray.put(personJSON);
            }

            ResponseEntity<String> response = new ResponseEntity<String>(peopleJsonArray.toString(), HttpStatus.valueOf(200));
            return response;
        } catch(GatewayException | SQLException | IOException | IndexOutOfBoundsException e) {
            ResponseEntity<String> response = new ResponseEntity<String>("Person " + id + " not found", HttpStatus.valueOf(404));
            return response;
        }
    }

    // GET /people
    // get everyone in the people table
//    @GetMapping("/people")
//    public ResponseEntity<String> fetchPeople(@RequestHeader Map<String, String> headers) {
//
//        int authVal = CheckAuthorization(headers);
//        if (authVal == 401){
//            return new ResponseEntity<String>("", HttpStatus.valueOf(401));
//        }
//
//        // create the gateway and fetch the people
//        try {
//            // traverse arraylist
//            PersonGatewayMySQL gateWay = new PersonGatewayMySQL(connection);
//            ArrayList<Person> people =  gateWay.fetchListPeople();
//            JSONArray peopleJSON = new JSONArray();
//            for (int i = 0; i < people.size(); i++){
//                JSONObject personJSON = new JSONObject();
//                personJSON.put("id", people.get(i).getId());
//                personJSON.put("firstName", people.get(i).getMsg());
//                personJSON.put("lastName", people.get(i).getBy());
//                personJSON.put("dateOfBirth", people.get(i).getWhen_occured());
//                peopleJSON.put(personJSON);
//            }
//
//            ResponseEntity<String> response = new ResponseEntity<String>(peopleJSON.toString(), HttpStatus.valueOf(200));
//            return response;
//        } catch(GatewayException | SQLException | IOException | IndexOutOfBoundsException e) {
//            ResponseEntity<String> response = new ResponseEntity<String>("People not found", HttpStatus.valueOf(404));
//            return response;
//        }
//    }

    // check login
    @PostMapping("/login")
    public ResponseEntity<String> postLogin(@RequestHeader Map<String, String> headers, @RequestBody  Map<String, String> payload ) {

        // query dogId from the database and return it as a json object
        int authVal = CheckAuthorization(headers);
        if (authVal == 401){
            return new ResponseEntity<String>("", HttpStatus.valueOf(401));
        }
        System.out.print(payload);

        // create the gateway and fetch the person
        try {
            String sessionId = new PersonGatewayMySQL(connection).postLogin(payload.get("username"), payload.get("password"));
            if (sessionId.length() == 0 ){
                ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.valueOf(401));
                return response;
            }
            // hash the Timestamp sessionId
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String sha256hex = DigestUtils.sha256Hex(ts.toString());
            JSONObject sessionJSON = new JSONObject();
            sessionJSON.put("sessionId", sha256hex);
            ResponseEntity<String> response = new ResponseEntity<String>(sessionJSON.toString(), HttpStatus.valueOf(200));
            return response;
        } catch(GatewayException | SQLException | IOException | IndexOutOfBoundsException e) {
            ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.valueOf(401));
            return response;
        }
    }

    // inserts a single person into the people table
    @PostMapping("/people")
    public ResponseEntity<String> insertPerson(@RequestHeader Map<String, String> headers, @RequestBody  Map<String, String> payload) {
        // query dogId from the database and return it as a json object
//        int authVal = CheckAuthorization(headers);
//        if (authVal == 401){
//            return new ResponseEntity<String>("", HttpStatus.valueOf(401));
//        }
//        System.out.println(payload);

//        if(!payload.containsKey("id") | !payload.containsKey("first_name") | !payload.containsKey("last_name") | !payload.containsKey("date_of_birth")){
//            return new ResponseEntity<String>("invalid input", HttpStatus.valueOf(400));
//        }

        // create the gateway and fetch the person
        try {

            PersonGatewayMySQL gateWay = new PersonGatewayMySQL(connection);
            HashMap < String , List<String>> fieldName = new HashMap<>();

            // calling method to find the person exist or not in the people database
            ArrayList<Person> person = gateWay.fetchPerson(Integer.parseInt(payload.get("id")));
            // not found the person
            if ( person.size() < 1 ) {

                int sessionId = gateWay.insertPerson(payload.get("id"), payload.get("first_name"), payload.get("last_name"), payload.get("date_of_birth"));

                JSONObject sessionJSON = new JSONObject();
                if ( sessionId == 200 ){
                    gateWay.insertLogs("new", Integer.parseInt(payload.get("id")), fieldName);

                }

                ResponseEntity<String> response = new ResponseEntity<String>(sessionJSON.toString(), HttpStatus.valueOf(200));
                return response;
            }

            // the person is exist , we just need to update the info
            else {





                if (payload.get("id").length() > 0 ){
                    fieldName.put("id", new ArrayList<>(){{
                        add(String.valueOf(person.get(0).getId()));
                        add(payload.get("id"));

                    }});
                }

                if (payload.get("first_name").length() > 0 ){
                    fieldName.put("first_name", new ArrayList<>(){{
                        add(String.valueOf(person.get(0).getFirstName()));
                        add(payload.get("first_name"));

                    }});
                }


                if (payload.get("last_name").length() > 0 ){
                    fieldName.put("last_name", new ArrayList<>(){{
                        add(String.valueOf(person.get(0).getLastName()));
                        add(payload.get("last_name"));

                    }});
                }


                if (payload.get("date_of_birth").length() > 0 ){
                    fieldName.put("date_of_birth", new ArrayList<>(){{
                        add(String.valueOf(person.get(0).getDateOfBirth()));
                        add(payload.get("date_of_birth"));

                    }});
                }



//                System.out.println(fieldName);


                Map <String, String> user = new HashMap<>();
                user.put("first_name", payload.get("first_name"));
                user.put("last_name", payload.get("last_name"));

                user.put("id", payload.get("id"));

                user.put("date_of_birth", payload.get("date_of_birth"));

                int rows = gateWay.updatePerson(user);

                JSONObject sessionJSON = new JSONObject();
                sessionJSON.put("sessionId",  rows);
                System.out.println(sessionJSON);

                gateWay.insertLogs("update", Integer.parseInt(payload.get("id")), fieldName);

                ResponseEntity<String> response = new ResponseEntity<String>(sessionJSON.toString(), HttpStatus.valueOf(200));
                return response;
            }

        } catch(GatewayException | SQLException | IOException | IndexOutOfBoundsException | ParseException e) {
            ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.valueOf(401));
            return response;
        }
    }

    @DeleteMapping("/people/{id}")
    public ResponseEntity<String> deletePerson(@RequestHeader Map<String, String> headers,
                                               @PathVariable("id") int id) {
        // query dogId from the database and return it as a json object
        int authVal = CheckAuthorization(headers);
        if (authVal == 401){
            return new ResponseEntity<String>("", HttpStatus.valueOf(401));
        }

        // create the gateway and delete the person
        try {
            int res = new PersonGatewayMySQL(connection).deletePerson(id);

            ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.valueOf(res));
            return response;
        } catch(GatewayException | SQLException | IOException | IndexOutOfBoundsException e) {
            ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.valueOf(404));
            return response;
        }
    }

    @PutMapping("/people/{id}")
    public ResponseEntity<String> updatePerson (@RequestHeader Map<String, String> headers,
                                               @RequestBody Map <String,String> user) {
        // query dogId from the database and return it as a json object
        int authVal = CheckAuthorization(headers);
        if (authVal == 401){
            return new ResponseEntity<String>("", HttpStatus.valueOf(401));
        }

        // create the gateway and delete the person
        try {
            int res = new PersonGatewayMySQL(connection).updatePerson(user);

            ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.valueOf(res));
            return response;
        } catch(GatewayException | SQLException | IOException | IndexOutOfBoundsException | ParseException e) {
            ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.valueOf(404));
            return response;
        }
    }
    @GetMapping("/hello")
    public String hello () {
        return "Hello";
    }

    // checks to see if the header is valid
    public int CheckAuthorization(@RequestHeader Map<String, String> headers){
        int response = 0;
        String sessionToken = "";
        Set<String> keys = headers.keySet();
        for(String key : keys) {
            if(key.equalsIgnoreCase("authorization"))
                sessionToken = headers.get(key);
        }
        if(!sessionToken.equals("i am a session token")) {
            response = 401;
        }
        return response;
    }
}
