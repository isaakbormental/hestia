package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import maps.main.java.com.lynden.gmapsfx.GoogleMapView;
import maps.main.java.com.lynden.gmapsfx.MapComponentInitializedListener;
import maps.main.java.com.lynden.gmapsfx.javascript.event.UIEventType;
import maps.main.java.com.lynden.gmapsfx.javascript.object.*;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("all")
public class Controller implements Initializable, MapComponentInitializedListener {

    private DataAccess dataAccess;
    private ObservableList<Person> personsCollection;
    private ObservableList<Apartment> apartmentsCollection;
    private ObservableList<Building> buildingsCollection;
    private ObservableList<Location> locationsCollection;
    private ObservableList<Message> messages;
    private ObservableList<Request> reque;
    GoogleMapView mapView;
    Window chat;
    GoogleMap map;
    private double lat, lon;
    private Marker myMarker;
    private int cabinetMarker = 0;
    int sel = 0;
    private Stage dialogStage;
    int selectedApartmentID = 0;


    public Controller(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @FXML
    TextField firstnameField;
    @FXML
    TextField lastnameField;
    //@FXML TextField fisrtnameField;
    @FXML
    TextField emailField;
    @FXML
    TextField phoneField;
    @FXML
    RadioButton ownerRadio;
    @FXML
    RadioButton renterRadio;
    @FXML
    TableView listHouse;
    @FXML
    Button validate;
    @FXML
    Button reset;
    @FXML
    TextField location;
    @FXML
    TextField facility;
    @FXML
    ChoiceBox facilit;
    @FXML
    Label distvalue;
    @FXML
    Slider distance;
    @FXML
    TextField numOfAp;
    @FXML
    TextField size;
    @FXML
    TextField price;
    @FXML
    TextField numOfRooms;
    @FXML
    TextField location_city;
    @FXML
    TextField location_district;
    @FXML
    RadioButton petsAllowed;
    @FXML
    RadioButton petsNotAllowed;
    @FXML
    RadioButton hostelRoomType;
    @FXML
    RadioButton privateHouseType;
    @FXML
    RadioButton apartmentType;
    @FXML
    PasswordField password;
    @FXML
    TextField login;
    @FXML
    TableView history;
    @FXML
    Button openmap;
    @FXML
    PasswordField passregistration;
    @FXML
    TableView historyRenter;
    @FXML
    TableView requests;
    @FXML
    TableView listMsg;
    @FXML
    TextField inputMsg;
    @FXML
    Label username;
    @FXML
    Label warning;
    @FXML
    ChoiceBox rating;
    @FXML
    ChoiceBox ratin;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Person> persons = dataAccess.getAll();
            personsCollection = FXCollections.observableArrayList(persons);
            List<Apartment> apartments = dataAccess.getAllAps();
            apartmentsCollection = FXCollections.observableArrayList(apartments);
            List<Building> buildings = dataAccess.getAllBuildings();
            buildingsCollection = FXCollections.observableArrayList(buildings);
            List<Location> locations = dataAccess.getAllLocations();
            locationsCollection = FXCollections.observableArrayList(locations);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public double round3(double number) {
        number = Math.round(number * 1000);
        number = number / 1000;
        return number;
    }

    public void openMap(ActionEvent event) throws IOException {
        mapView = new GoogleMapView();
        mapView.addMapInializedListener(this);
        Scene scene = new Scene(mapView);
        Stage mapstage = new Stage();
        mapstage.setTitle("JavaFX and Google Maps");
        mapstage.setScene(scene);
        mapstage.show();
    }

    public void openChat(ActionEvent event) throws IOException {

        Main main = new Main();
        int owner = sel;

        try {
            main.changeScene("Chat");
            System.out.println(sel + " And " + cabinetMarker);
            List<Message> msgs = dataAccess.findMsgs(cabinetMarker, sel);
            messages = FXCollections.observableArrayList(msgs);
            for (Message m : messages) {
                System.out.println(m.getMessage());
            }
            listMsg.setItems(messages);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void addAdvert(ActionEvent event) {
        Main main = new Main();

        try {
            main.changeScene("AddAdvert");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createAccount(ActionEvent event) {
        Main main = new Main();

        try {
            main.changeScene("newAccount");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAdvertisement(ActionEvent event) throws IOException {

        try {
            List<Integer> found_locations = dataAccess.findLocation(location_city.getText(), location_district.getText());
            if (found_locations.size() == 0) {
                addLocation();
                System.out.println("LOC");
            }
            found_locations = dataAccess.findLocation(location_city.getText(), location_district.getText());
            int lid = found_locations.get(0);
            System.out.println(lid);
            List<Integer> found_buildings = dataAccess.findBuilding(round3(lat), round3(lon));
            if (found_buildings.size() == 0) {
                addBuilding(round3(lat), round3(lon), lid);
                System.out.println("BUI");
            }
            found_buildings = dataAccess.findBuilding(round3(lat), round3(lon));
            int bid = found_buildings.get(0);
            System.out.println(bid);
            addApartment(bid);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void addApartment(int bid) throws IOException {
//        Apartment apartment = new Apartment(apartmentsCollection.size() + 1, Integer.parseInt(numOfRooms.getText()), bid, personsCollection.size() - 1,
//                Double.parseDouble(size.getText()),
//                Double.parseDouble(price.getText()));
        Apartment apartment = new Apartment(apartmentsCollection.size() + 1, Integer.parseInt(numOfRooms.getText()), bid, cabinetMarker,
                Double.parseDouble(size.getText()),
                Double.parseDouble(price.getText()));
        try {
//            dataAccess.addApartment(apartment, personsCollection.size() - 1);
            dataAccess.addApartment(apartment, cabinetMarker);
            Main a = new Main();
            //a.changeScene("Registration");
            //a.changeScene("OwnerCabinet");
            reload();

            System.out.println(cabinetMarker + " : Vsyo eshe tam!");


        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        apartmentsCollection.add(apartment);
        numOfAp.clear();
        size.clear();
        numOfRooms.clear();
        price.clear();
    }

    public void addLocation() throws IOException {
        Location location = new Location(locationsCollection.size() + 1, location_city.getText(), location_district.getText(), 0.0);
        try {
            dataAccess.addLocation(location);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        locationsCollection.add(location);
    }

    public void addBuilding(double lat, double lon, int lid) throws IOException {
        String type;
        if (hostelRoomType.isSelected()) {
            type = "Hostel";
        } else if (privateHouseType.isSelected()) {
            type = "Private House";
        } else if (apartmentType.isSelected()) {
            type = "Apartment";
        } else {
            type = "";
        }
        Building building = new Building(buildingsCollection.size() + 1, lat, lon, type, petsAllowed.isSelected(), lid);
        try {
            dataAccess.addBuilding(building);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        buildingsCollection.add(building);
    }

    public void addPerson(ActionEvent event) throws IOException {
        Person person = new Person(firstnameField.getText(), lastnameField.getText(), emailField.getText(), passregistration.getText(), Integer.parseInt(phoneField.getText()));
        try {
            dataAccess.addPerson(person);
            Main a = new Main();
            if (ownerRadio.isSelected()) {
                //Owner owner = new Owner(person.getPid(), person.getFirsName(), person.getEmail(), person.getPhone(), person.getPid(), 0.0);
                //dataAccess.addOwner(owner);
                int oid = dataAccess.getPersonId(person.getEmail(), person.getPassword());
                dataAccess.addOwner(oid, 0.0);
                cabinetMarker = oid;
                reload();
                //a.changeScene("OwnerCabinet");
            } else {
                int rid = dataAccess.getPersonId(person.getEmail(), person.getPassword());
                cabinetMarker = rid;
                dataAccess.addRenter(rid, 0.0);
                reload();
                //a.changeScene("RenterCabinet");
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        personsCollection.add(person);
        firstnameField.clear();
        emailField.clear();
        phoneField.clear();
    }

    public void FindAppartment(ActionEvent event) throws IOException, SQLException {
        List<Apartment> listap = new ArrayList<>();
        Main main = new Main();

        String loc = "default";
        if (!location.getText().equals("")) {
            loc = location.getText();
        }

        int pri = -1;
        if (!price.getText().equals("")) {
            pri = Integer.parseInt(price.getText());
        }

        int siz = -1;
        if (!size.getText().equals("")) {
            siz = Integer.parseInt(size.getText());
        }

        double dis = -1;
        if (!distance.equals(null)) {
            dis = distance.getValue();
        }
        String incomingFacility = "default";
//        String fac = "default";
        if (!facilit.getValue().toString().equals("")) {
//            incomingFacility = facility.getText();
            incomingFacility = (String) (facilit.getValue().toString());
            System.out.println(incomingFacility);
        } else {
            System.out.println("EMPTY");
        }

        listap = dataAccess.getApartment(loc, pri, siz, dis, incomingFacility);

        System.out.println(listap.size());
        apartmentsCollection = FXCollections.observableArrayList(listap);
        listHouse.setItems(apartmentsCollection);

    }

    public void reset(ActionEvent event) {
        location.clear();
        size.clear();
        price.clear();
    }


    @Override
    public void mapInitialized() {
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(55.753474, 48.743395))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);

        map = mapView.createMap(mapOptions);
        Label lblClick = new Label();

        map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
            lblClick.setText(ll.toString());
            lat = ll.getLatitude();
            lon = ll.getLongitude();
            MarkerOptions markerOptions = new MarkerOptions();
            LatLong markerLatLong = new LatLong(lat, lon);
            markerOptions.position(markerLatLong)
                    .title("My new Marker")
                    .visible(true);
            myMarker = new Marker(markerOptions);
            map.addMarker(myMarker);
            System.out.println(ll.getLatitude() + " " + ll.getLongitude());
        });

        map.addUIEventHandler(UIEventType.rightclick, (JSObject obj) -> {
            map.removeMarker(myMarker);
        });
    }


    public void logIn(ActionEvent event) throws IOException {
        List<Apartment> listap;
        List<Request> listreq;
        Hashtable<Integer, String> person = new Hashtable<Integer, String>();
        try {
            Main main = new Main();

            String logi = login.getText();
            String pass = password.getText();

            try {
                // Main a = new Main();
                int pid = dataAccess.getPersonId(logi, pass);
                person = dataAccess.getPerson(logi, pass);
                if (person.isEmpty()) {
                    System.out.println("There is no such person!");
                }
                System.out.println(person.get(pid));
                int oid = dataAccess.getOwnerId(pid);
                int rid = dataAccess.getRenterId(pid);
                if (oid != 0) {
                    cabinetMarker = oid;
                    main.changeScene("OwnerCabinet");
                    String name = person.get(oid);
                    username.setText(name);
                    listap = dataAccess.findRenterById(oid);
                    apartmentsCollection = FXCollections.observableArrayList(listap);
                    historyRenter.setItems(apartmentsCollection);

                    listreq = dataAccess.getRequests(cabinetMarker);
                    reque = FXCollections.observableList(listreq);
                    requests.setItems(reque);


                } else if (rid != 0) {
                    cabinetMarker = rid;
                    main.changeScene("RenterCabinet");
                    String name = person.get(rid);
                    username.setText(name);
                    listap = dataAccess.findApartRentedById(rid);
                    apartmentsCollection = FXCollections.observableArrayList(listap);
                    history.setItems(apartmentsCollection);
                } else {
                    //System.out.println("Invalid login or password");
                    warning.setText("Invalid login or password");
                }

            } catch (Exception e3) {
                System.out.println("No");

            }
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println("No");
        }

    }

    public void logOut(ActionEvent event) {

        Main main = new Main();
        try {
            sel = 0;
            cabinetMarker = 0;
            selectedApartmentID = 0;
            main.changeScene("Registration");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void sendMessage(ActionEvent actionEvent) throws SQLException {
        String incomingMessage = inputMsg.getText();
        int msgNumber = messages.size() + 1;

        dataAccess.addMessage(incomingMessage, sel, cabinetMarker, msgNumber);
        List<Message> msgs = dataAccess.findMsgs(cabinetMarker, sel);
        messages = FXCollections.observableArrayList(msgs);

        listMsg.setItems(messages);

    }
    public void decline(ActionEvent actionEvent) throws SQLException {
        if (sel == 0) {
            //System.out.println("Please select a reciever");
            warning.setText("Select a request");
        } else {
            Main main = new Main();
            try {

                dataAccess.deleteRequest(selectedApartmentID,sel,cabinetMarker);
                List<Request> listreq;
                listreq = dataAccess.getRequests(cabinetMarker);
                reque = FXCollections.observableList(listreq);
                requests.setItems(reque);
                sel = 0;
                selectedApartmentID = 0;

            }
            catch (SQLException s){
                warning.setText("No request to delete");
            }
        }

    }

    public void approve(ActionEvent actionEvent) throws SQLException {
        if (sel == 0) {
            //System.out.println("Please select a reciever");
            warning.setText("Select a request");
        } else {
            Main main = new Main();
            try {
                dataAccess.approveRequest(selectedApartmentID,sel,cabinetMarker);
                List<Apartment> listap;
                listap = dataAccess.findRenterById(cabinetMarker);
                apartmentsCollection = FXCollections.observableArrayList(listap);
                historyRenter.setItems(apartmentsCollection);

                List<Request> listreq;
                listreq = dataAccess.getRequests(cabinetMarker);
                reque = FXCollections.observableList(listreq);
                requests.setItems(reque);
                sel = 0;
                selectedApartmentID = 0;

            }
            catch (SQLException s){
                warning.setText("No request to delete");
            }
        }

    }
    public void showPersonEditDialog(ActionEvent event) throws SQLException {

        if (sel == 0) {
            System.out.println("Please select a reciever");
            warning.setText("Select a row in a history to talk about it");
        } else {
            Main main = new Main();
            try {

                // Load the fxml file and create a new stage for the popup
                FXMLLoader loader = new FXMLLoader(getClass().getResource("messagesOwner.fxml"));
                // Controller controller = new Controller(dataAccess);
                loader.setController(this);
                GridPane page = (GridPane) loader.load();
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Message");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                //  dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                // Set the person into the controller
                // controller = loader.getController();
                this.setDialogStage(dialogStage);
                this.initTable();
                // Show the dialog and wait until the user closes it
                dialogStage.show();

            } catch (IOException e) {
                // Exception gets thrown if the fxml file could not be loaded
                e.printStackTrace();

            }
        }
    }

    public void rate(ActionEvent actionEvent) throws SQLException {
        if (selectedApartmentID == 0) {
            warning.setText("Select an apartment for rating");
            System.out.println("Select an apartment for rating");
        } else {
            try {
                Double incomingRating = Double.parseDouble(rating.getValue().toString());
                //dataAccess.addRequest(incomingRating,sel,cabinetMarker);
                System.out.println(selectedApartmentID);
                dataAccess.addApartmentRate(incomingRating, selectedApartmentID, cabinetMarker);
            } catch (SQLException e) {
                //System.out.println("You have already rated this apartment. ");
                warning.setText("You have already rated this apartment. ");
            }

            //Check for getting average rating. Works
            System.out.println(dataAccess.getAverageApartmentRating(sel));
        }

    }

    public void rateRenter(ActionEvent actionEvent) throws SQLException {
        if (sel == 0) {
            warning.setText("Select user for rating!");
            //System.out.println("Select user for rating!");
        } else {
            try {
                Double incomingRating = Double.parseDouble(ratin.getValue().toString());
                dataAccess.addRenterRate(incomingRating, sel, cabinetMarker);
            } catch (SQLException e) {
                warning.setText("You have already rated this renter. ");
                //System.out.println("You have already rated this renter. ");
            }
        }
    }

    public void bookAp(ActionEvent actionEvent) throws SQLException {
        if (selectedApartmentID == 0) {
            warning.setText("Select an apartment for booking");
        } else {
            try {

                dataAccess.addRequest(selectedApartmentID,cabinetMarker,sel);
                System.out.println(selectedApartmentID);
                System.out.println(sel);
                System.out.println(cabinetMarker);
                warning.setText("Successful book");
            } catch (SQLException e) {
                //System.out.println("You have already rated this apartment. ");
                warning.setText("You have already booked this apartment. ");
            }

            //Check for getting average rating. Works
            System.out.println(dataAccess.getAverageApartmentRating(sel));
        }

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void initTable() throws SQLException {
        List<Message> msgs = dataAccess.findMsgs(cabinetMarker, sel);
        messages = FXCollections.observableArrayList(msgs);
        listMsg.setItems(messages);
    }


    public void reload() throws IOException {
        List<Apartment> listap;
        Hashtable<Integer, String> person = new Hashtable<Integer, String>();
        try {
            Main main = new Main();
            try {
                int pid = cabinetMarker;
                person = dataAccess.getPersonById(pid);
                int oid = dataAccess.getOwnerId(pid);
                int rid = dataAccess.getRenterId(pid);
                if (oid != 0) {
                    main.changeScene("OwnerCabinet");
                }
                else{
                    main.changeScene("RenterCabinet");
                }
                String name = person.get(pid);
                username.setText(name);
                listap = dataAccess.findRenterById(oid);
                apartmentsCollection = FXCollections.observableArrayList(listap);
                historyRenter.setItems(apartmentsCollection);
            } catch(Exception e2){
            e2.printStackTrace();
        }


        } catch (Exception e3) {

        }


}

}