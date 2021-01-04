
package backend.wsclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Person;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PeopleListController implements Initializable {
    private ObservableList peopleList;

    @FXML
    private ListView<Person> widgetListView;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;



    private List<Person> listPeopleDefault;

    // setup PeopleListController instance class
    public PeopleListController(List<Person> listOfPeople) {
        listPeopleDefault = listOfPeople;
        peopleList = FXCollections.observableArrayList(listOfPeople);
    }

    @FXML
    // handle add a person
    void handleAdd(ActionEvent event) throws IOException {
        Stage newStage = new Stage();
        InputForm controller = new InputForm("INSERT");
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/Input.fxml"));
        loader.setController(controller);
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);
        newStage.setScene(scene);
        newStage.show();
    }
    @FXML
    // handle update info of a person
    void handleUpdate(ActionEvent event) throws IOException {
        Stage newStage = new Stage();
        InputForm controller = new InputForm("UPDATE");
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/Input.fxml"));
        loader.setController(controller);
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);
        newStage.setScene(scene);
        newStage.show();

    }
    @FXML
    // handle delete a person
    void handleDelete(ActionEvent event) throws IOException {


        Stage newStage = new Stage();
        InputForm controller = new InputForm("DELETE");
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/Input.fxml"));
        loader.setController(controller);
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);
        newStage.setScene(scene);
        newStage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        widgetListView.setItems(peopleList);
    }
}


