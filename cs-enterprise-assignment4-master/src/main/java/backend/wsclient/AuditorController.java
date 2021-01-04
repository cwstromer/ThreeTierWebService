package backend.wsclient;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.LogsView;
import model.Person;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.event.ActionEvent;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.function.Function;

public class AuditorController implements Initializable {
    @FXML
    Tab detailTab;
    @FXML
    Tab auditTab;
    @FXML
    TableColumn<LogsView, String>  a;
    @FXML
    TableColumn <LogsView, String> b;
    @FXML
    TableColumn <LogsView, String> c;
    @FXML
    TableView <LogsView> infoDetail;
    @FXML
    TextField id;
    @FXML
    TextField first_name;
    @FXML
    TextField last_name;

    @FXML
    TextField dob;
    @FXML
    TextField age;
    @FXML
    Button save;
    @FXML
    Button cancel;

    @FXML
    public void onSave (ActionEvent e ) throws IOException {

        String wsURL= "http://localhost:8080/people";
        String sessionId = "i am a session token";
        HttpPost requestInsert = new HttpPost(wsURL);
        JSONObject insertPerson = new JSONObject();
        insertPerson.put("first_name", first_name.getText());
        insertPerson.put("last_name", last_name.getText());
        insertPerson.put("date_of_birth", dob.getText());
        insertPerson.put("id", id.getText());
        String personObj = insertPerson.toString();
        StringEntity reqEntity = new StringEntity(personObj);
        // Setup request requirement
        requestInsert.setEntity(reqEntity);
        requestInsert.setHeader("Authorization", sessionId);
        requestInsert.setHeader("Accept", "*/*");
        requestInsert.setHeader("Content-type", "application/json");
        String reponse = waitForResponseAsString(requestInsert);
//        System.out.println(reponse);
    }


    public void onDetail () throws IOException {


    }
    public void onAudit () throws IOException {

        ObservableList<LogsView> data = FXCollections.observableArrayList( );

        String wsURL= "http://localhost:8080/people/1/audittrail";
        HttpGet request = new HttpGet(wsURL);
        String result = waitForResponseAsString(request);
        System.out.println (result);
        JSONArray peopleList = new JSONArray(result);
        for (int i = 0; i < peopleList.length(); i++) {
            JSONObject jsonobject = peopleList.getJSONObject(i);
            String msg= jsonobject.getString("change_msg");
            String occur = jsonobject.getString("when_occurred");
            String name = jsonobject.getString("name");
            int id =  jsonobject.getInt("id");
            int change_by = jsonobject.getInt("change_by");

            LogsView g = new LogsView(occur,  "Ragnar", msg, 1000);
            data.add(g);
        }

        infoDetail.setItems(data);



    }
    public String waitForResponseAsString(HttpRequestBase request) throws IOException {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;

        try {

            httpclient = HttpClients.createDefault();
            response = httpclient.execute(request);

            switch(response.getStatusLine().getStatusCode()) {
                case 200:
                    // peachy
                    break;
                case 401:
                    throw new PersonException("401");
                default:
                    throw new PersonException("Non-200 status code returned: " + response.getStatusLine());
            }


            return parseResponseToString(response);

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(response != null)
                response.close();
            if(httpclient != null)
                httpclient.close();
        }
        return response.toString();
    }

    public String parseResponseToString(CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        // use org.apache.http.util.EntityUtils to read json as string
        return EntityUtils.toString(entity, StandardCharsets.UTF_8);
    }



    public void initialize(URL url, ResourceBundle resourceBundle) {

        a.setCellValueFactory(new PropertyValueFactory<>("name"));

        b.setCellValueFactory(new PropertyValueFactory<>("allegiance"));


        c.setCellValueFactory(new PropertyValueFactory<>("position"));


        infoDetail.getColumns().addAll(a,b,c);


        detailTab.setOnSelectionChanged(event -> {
            if (detailTab.isSelected()) {
                try {
                    onDetail();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Do stuff here
            }
        });
        auditTab.setOnSelectionChanged(event -> {
            if (auditTab.isSelected()) {
                try {
                    onAudit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Do stuff here
            }
        });

    }
}
