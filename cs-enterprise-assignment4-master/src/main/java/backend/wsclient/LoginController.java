
package backend.wsclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Person;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField userName;

    @FXML

    private PasswordField password;

    @FXML
    void doLogin(ActionEvent event) {
        try {
            // authenticate using given credentials
            String sessionId = AuthenticationGateWay.authenticate(userName.getText(), password.getText());
            // dont need to check validity of sessionId as all other web service calls will check that



            // fetch people

            ArrayList<Person> people =  FetchPeople.fetchAll();


            Stage newStage = new Stage();
            PeopleListController controller = new PeopleListController(people);
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/widgetlistview.fxml"));
            loader.setController(controller);
            Parent rootNode = loader.load();
            Scene scene = new Scene(rootNode);
            newStage.setScene(scene);
            newStage.show();

        } catch(RuntimeException | SQLException | IOException e) {
            //TODO: show nice GUI popup saying I dont know you
            System.out.println("Authentication failed!");
            e.printStackTrace();
        }

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName.setText("ragnar");
        password.setText("flapjacks");
    }
}
