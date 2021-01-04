//package backend.wsclient.wsclient;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//
//public class LoginAndFetch {
//    private static final String WS_URL = "http://localhost:8080";
//    private static final Logger logger = LogManager.getLogger();
//
//    // swiped from https://hc.apache.org/httpcomponents-client-ga/quickstart.html
//    public static String getResponseAsString(CloseableHttpResponse response) throws IOException {
//        HttpEntity entity = response.getEntity();
//        // use org.apache.http.util.EntityUtils to read json as string
//        String strResponse = EntityUtils.toString(entity, StandardCharsets.UTF_8);
//        EntityUtils.consume(entity);
//
//        return strResponse;
//    }
//
//    // grab a list of dogs from the wiremock /dogs route
//    public static void main(String [] args) {
//        String userName = "ragnar";
//        String pw = "flapjacks";
//
//        ArrayList<Person> listPerson = new ArrayList<Person>();
//
//        CloseableHttpResponse response = null;
//        CloseableHttpClient httpclient = null;
//        try {
//
//            // 1. authenticate and get back session token
//
//            httpclient = HttpClients.createDefault();
//
//            // assemble credentials into a JSON encoded string
//            JSONObject credentials = new JSONObject();
//            credentials.put("username", userName);
//            credentials.put("password", pw);
//            String credentialsString = credentials.toString();
//            logger.info("credentials: " + credentialsString);
//
//            // build the request
//            //HttpGet httpGet = new HttpGet(WS_URL + "/dogs");
//            HttpPost loginRequest = new HttpPost(WS_URL + "/login");
//            // put credentials string into request body (as raw json)
//            // this requires setting it up as a request entity where we can describe what the text is
//            StringEntity reqEntity = new StringEntity(credentialsString);
//            loginRequest.setEntity(reqEntity);
//            loginRequest.setHeader("Accept", "application/json");
//            loginRequest.setHeader("Content-type", "application/json");
//
//            response = httpclient.execute(loginRequest);
//
//            // a special response for invalid credentials
//            if(response.getStatusLine().getStatusCode() == 401) {
//                logger.error("I DON'T KNOW YOU!!!");
//                httpclient.close();
//                return;
//            }
//            if(response.getStatusLine().getStatusCode() != 200) {
//                logger.error("Non-200 status code returned: " + response.getStatusLine());
//                httpclient.close();
//                return;
//            }
//
//            // get the session token
//            String responseString = getResponseAsString(response);
//            logger.info("Login response as a string: " + responseString);
//
//            String token = null;
//            try {
//                JSONObject responseJSON = new JSONObject(responseString);
//                token = responseJSON.getString("session_id");
//            } catch(Exception e) {
//                logger.error("could not get session token: " + e.getMessage());
//                httpclient.close();
//                return;
//            }
//
//            logger.info("Session token: " + token);
//
//            // IF WE GET HERE THEN WE HAVE A VALID SESSION TOKEN!!!
//
//            // 2. call the access restricted fetch dogs web service
//
//            // now fetch dogs and use the session token to identify ourselves
//            HttpGet dogsRequest = new HttpGet(WS_URL + "/dogs");
//
//            //plug in our session token
//            dogsRequest.setHeader("Authorization", token);
//
//            // TODO: do we need the below???
//            dogsRequest.setHeader("Accept", "application/json");
//
//            // make the request and check the responses
//            response = httpclient.execute(dogsRequest);
//
//            // a special response for invalid credentials
//            if(response.getStatusLine().getStatusCode() == 401) {
//                logger.error("I DON'T KNOW YOU!!!");
//                httpclient.close();
//                return;
//            }
//            if(response.getStatusLine().getStatusCode() != 200) {
//                logger.error("Non-200 status code returned: " + response.getStatusLine());
//                httpclient.close();
//                return;
//            }
//
//            responseString = getResponseAsString(response);
//            logger.info("Fetch response: " + responseString);
//
//            // use json.org library to parse response into json array
//            JSONArray jsonArray = new JSONArray(responseString);
//            // from the json array, parse out individual dog objects
//            for(Object obj : jsonArray) {
//                JSONObject jsonObject = (JSONObject) obj;
//                listPerson.add(new Person(jsonObject.getInt("id"), jsonObject.getString("personName")));
//            }
//
//
//
//            System.out.println("List of people:");
//            System.out.println(listPerson);
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                response.close();
//                httpclient.close();
//            } catch(IOException e) {
//                // ignore
//            }
//        }
//    }
//}
