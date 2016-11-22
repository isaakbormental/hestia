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

    private static Stage theStage;
    private static DataAccess dataAccess;
    public static Controller controller;
    private Apartment selected;
    private int selectedUser;


    @Override
    public void start(Stage primaryStage) throws Exception {
        theStage = primaryStage;
        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setDataSourceName("Databases");
        source.setServerName("localhost");
        source.setDatabaseName("Hestia1.0");
        source.setUser("postgres");
        source.setPassword("buxal3842");
        source.setMaxConnections(10);
        dataAccess = new DataAccess(source);
        controller = new Controller(dataAccess);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Registration.fxml"));
        loader.setController(controller);
        Parent root = loader.load();
        primaryStage.setTitle("Hestia Registration");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        //System.out.println(i);
    }

    public void changeScene(String arg) throws IOException {
        Parent root = null;

        if (arg.equals("OwnerCabinet")) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("OwnerCabinet.fxml"));
            loader.setController(controller);
            root = loader.load();
            theStage.setTitle("Owner Personal Cabinet");
            Scene scene = new Scene(root, 1000, 750);
            theStage.setScene(scene);
        } else if (arg.equals("RenterCabinet")) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("RenterCabinet.fxml"));
            loader.setController(controller);
            root = loader.load();
            theStage.setTitle("Renter Personal Cabinet");
            Scene scene = new Scene(root, 1000, 750);
            theStage.setScene(scene);
            controller.history.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                    printApInfo(newValue) );

        } else if (arg.equals("Registration")) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Registration.fxml"));
            loader.setController(controller);
            root = loader.load();
            theStage.setTitle("Hestia Registration");
            Scene scene = new Scene(root, 1000, 750);
            theStage.setScene(scene);
        }
        else if(arg.equals("AddAdvert")){

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAvertisment.fxml"));
           loader.setController(controller);
            root = loader.load();
            theStage.setTitle("Add Advert");
            Scene scene = new Scene(root, 1000, 750);
            theStage.setScene(scene);
        }
        else if(arg.equals("newAccount")){

            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
            loader.setController(controller);
            root = loader.load();
            theStage.setTitle("New account");
            Scene scene = new Scene(root, 1000, 750);
            theStage.setScene(scene);
        }
        else if(arg.equals("Chat")){

            FXMLLoader loader = new FXMLLoader(getClass().getResource("messagesOwner.fxml"));
            loader.setController(controller);
            root = loader.load();
            theStage.setTitle("Chat");
            Scene scene = new Scene(root, 300, 600);
            theStage.setScene(scene);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void mapInitialized() {

    }

    private void printApInfo(Object object){
        selected = (Apartment) object;
        System.out.println();
        System.out.println("You have selected: ");
        selectedUser = selected.getOwner().getPid();
        controller.sel = selectedUser;
        controller.selectedApartmentID = selected.getAid();
        System.out.println("Owner is: "+selectedUser);
        System.out.println("apart_id: "+selected.getAid());

    }

}
