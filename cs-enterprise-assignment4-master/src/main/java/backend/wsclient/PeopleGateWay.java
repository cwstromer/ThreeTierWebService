//package backend.wsclient.wsclient;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.*;
//
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//
//public class PeopleGateWay {
//    private String wsURL;
//    private String sessionId;
//    private String method;
//
//    public PeopleGateWay(String url, String sessionId , String method) {
//        this.sessionId = sessionId;
//        this.wsURL = url;
//        this.method = method;
//    }
//
//    public ArrayList<Person> getPeople() {
//        ArrayList<Person> people = new ArrayList<Person>();
//        String response;
//        try {
//            // we know this is a GET request so create a get request and pass it to getResponseAsString
//            // build the request
//            if (this.method.equalsIgnoreCase("DELETE")) {
//                HttpDelete request = new HttpDelete(wsURL);
//                // specify Authorization header
//                request.setHeader("Authorization", sessionId);
//                response = waitForResponseAsString(request);
//
//                for(Object obj : new JSONArray(response)) {
//                    JSONObject jsonObject = (JSONObject) obj;
//                    people.add(new Person(jsonObject.getInt("id"), jsonObject.getString("firstName")));
//                }
//            }
//            if (this.method.equalsIgnoreCase("ADD")) {
//                HttpPost requestInsert = new HttpPost(wsURL);
//                // specify Authorization header
//                JSONObject insertPerson = new JSONObject();
//                insertPerson.put("firstName", "Lou");
//                insertPerson.put("lastName", "Smith");
//                insertPerson.put("dateOfBirth","1990-01-01");
//                String personObj = insertPerson.toString();
//                StringEntity reqEntity = new StringEntity(personObj);
//                // Setup request requirement
//                requestInsert.setEntity(reqEntity);
//                requestInsert.setHeader("Authorization", sessionId);
//                requestInsert.setHeader("Accept", "application/json");
//                requestInsert.setHeader("Content-type", "application/json");
//                // reponse object
//                response = waitForResponseAsString(requestInsert); // return successfully code 200
//                JSONObject responseJSON = new JSONObject(response);
//                Person addNew = new Person(responseJSON.getInt("id"), insertPerson.getString("firstName") );
//                addNew.setDateOfBirth(insertPerson.getString("dateOfBirth"));
//                addNew.setDateOfBirth(insertPerson.getString("lastName"));
//                people.add(addNew);
//            }
//
//
//            if (this.method.equalsIgnoreCase("FETCH")) {
//                HttpPost request = new HttpPost(wsURL);
//                // specify Authorization header
//                request.setHeader("Authorization", sessionId);
//                response = waitForResponseAsString(request);
//
//                for(Object obj : new JSONArray(response)) {
//                    JSONObject jsonObject = (JSONObject) obj;
//                    people.add(new Person(jsonObject.getInt("id"), jsonObject.getString("firstName")));
//                }
//            }
//        } catch (Exception e) {
//            throw new PersonException(e);
//        }
//
//        return people;
//    }
//
//    public String waitForResponseAsString(HttpRequestBase request) throws IOException {
//        CloseableHttpClient httpclient = null;
//        CloseableHttpResponse response = null;
//
//        try {
//
//            httpclient = HttpClients.createDefault();
//            response = httpclient.execute(request);
//
//            switch(response.getStatusLine().getStatusCode()) {
//                case 200:
//                    // peachy
//                    break;
//                case 401:
//                    throw new PersonException("401");
//                default:
//                    throw new PersonException("Non-200 status code returned: " + response.getStatusLine());
//            }
//
//            return parseResponseToString(response);
//
//        } catch(Exception e) {
//            throw new PersonException(e);
//        } finally {
//            if(response != null)
//                response.close();
//            if(httpclient != null)
//                httpclient.close();
//        }
//    }
//
//    public String parseResponseToString(CloseableHttpResponse response) throws IOException {
//        HttpEntity entity = response.getEntity();
//        // use org.apache.http.util.EntityUtils to read json as string
//        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
//    }
//
//}