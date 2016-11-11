package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import maps.main.java.com.lynden.gmapsfx.GoogleMapView;
import maps.main.java.com.lynden.gmapsfx.MapComponentInitializedListener;
import org.postgresql.ds.PGPoolingDataSource;


import java.io.IOException;

public class Main extends Application implements MapComponentInitializedListener {

    public static Stage theStage;
    public static DataAccess dataAccess;
    public static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        theStage = primaryStage;
        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setDataSourceName("Databases");
        source.setServerName("localhost");
        source.setDatabaseName("hestia_1");
        source.setUser("postgres");
        source.setPassword("robot");
        source.setMaxConnections(10);
        dataAccess = new DataAccess(source);
        controller = new Controller(dataAccess);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Registration.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        primaryStage.setTitle("Hestia Registration");
        primaryStage.setScene(new Scene(root, 800, 300));
        primaryStage.show();
    }

    public void changeScene(String arg) throws IOException {
        Parent root = null;
        if (arg == "OwnerCabinet") {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerCabinet.fxml"));
            loader.setController(controller);
            root = loader.load();
            theStage.setTitle("Owner Personal Cabinet");
            Scene scene = new Scene(root, 1000, 750);
            theStage.setScene(scene);
        } else if (arg == "RenterCabinet") {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RenterCabinet.fxml"));
            loader.setController(controller);
            root = loader.load();
            theStage.setTitle("Renter Personal Cabinet");
            Scene scene = new Scene(root, 1000, 750);
            theStage.setScene(scene);
        } else if (arg == "Registration") {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Registration.fxml"));
            loader.setController(controller);
            root = loader.load();
            theStage.setTitle("Hestia Registration");
            Scene scene = new Scene(root, 1000, 750);
            theStage.setScene(scene);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void mapInitialized() {

    }
}
