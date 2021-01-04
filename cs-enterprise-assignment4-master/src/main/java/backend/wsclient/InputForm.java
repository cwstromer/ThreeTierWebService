
package backend.wsclient;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InputForm implements Initializable {


    @FXML
    private TextField id;
    @FXML
    private  TextField first_name;
    @FXML
    private  TextField last_name;
    @FXML
    private Text title;
    @FXML
    private  TextField dob;
    @FXML
    private Button submit;

    private  String command =  " ";
    public InputForm (String command ) {
            this.command = command;
    }


    @FXML
    public  void handleForm () throws IOException {


        if ( command.equalsIgnoreCase("UPDATE") ) {
            int status = UpdatePeople.update( id.getText(), first_name.getText(), last_name.getText(), dob.getText() );
            title.setText("Code " + status);
            newScene();

        }
        if ( command.equalsIgnoreCase("INSERT") ) {
            int status = InsertPeople.insert( id.getText(), first_name.getText(), last_name.getText(), dob.getText() );
            title.setText("Code " + status);
            newScene();
        }
        if ( command.equalsIgnoreCase("DELETE") ) {
            int status = DeletePeople.delete( id.getText() );
            title.setText("Code" + status);
            newScene();


        }

    }
    public void  newScene () throws IOException {
        ArrayList<Person> people  = FetchPeople.fetchAll();
        Stage newStage = new Stage();
        PeopleListController controller = new PeopleListController(people);
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/widgetlistview.fxml"));
        loader.setController(controller);
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);
        newStage.setScene(scene);
        newStage.show();
    }
    public void initialize (URL url, ResourceBundle resourceBundle) {
        if ( command.equalsIgnoreCase("DELETE")) {

            id.setText("Please enter ID");
            title.setText("Delete Form ");
            last_name.setVisible(false);
            first_name.setVisible(false);
            dob.setVisible(false);
        }
        if ( command.equalsIgnoreCase("INSERT")) {
            id.setText("Please enter ID");
            title.setText("Insert Form");
            first_name.setText("Please enter first name");
            last_name.setText("Please enter last name ");
            dob.setText("YYYY-MM-DD");
        }
        if ( command.equalsIgnoreCase("UPDATE")) {

            id.setText("Please enter ID");
            title.setText("Update Form");
            first_name.setText("Please enter first name");
            last_name.setText("Please enter last name ");
            dob.setText("YYYY-MM-DD");
        }
    };
}
