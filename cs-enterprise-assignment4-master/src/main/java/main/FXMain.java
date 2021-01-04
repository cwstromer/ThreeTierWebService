package main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import backend.wsclient.AuditorController;

/*

        CS 4743 Assignment 3 by Kevin Nguyen and Christopher Stromer
        Enpterprise SWE CLASS
        Professor: Dr Robinson
 */

public class FXMain extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    public static void main(final String[] args) {
        launch(args);
    }

    /*
    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(FXMain.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(FXMain.class.getResource("/Auditor.fxml"));
        AuditorController auditor  = new AuditorController();
        loader.setController(auditor);
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);
        stage.setScene(scene);
        stage.setTitle("People FX Demo");
        stage.show();
    }

    /*
    @Override
    public void stop() throws Exception {
        springContext.close();
    }

     */
}