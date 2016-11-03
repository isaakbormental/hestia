package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import maps.main.java.com.lynden.gmapsfx.GoogleMapView;
import maps.main.java.com.lynden.gmapsfx.MapComponentInitializedListener;
import maps.main.java.com.lynden.gmapsfx.javascript.event.UIEventType;
import maps.main.java.com.lynden.gmapsfx.javascript.object.*;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable, MapComponentInitializedListener {

    private DataAccess dataAccess;
    private ObservableList<Person> personsCollection;
    private ObservableList<Apartment> apartmentsCollection;
    private ObservableList<Building> buildingsCollection;
    GoogleMapView mapView;
    GoogleMap map;
    private double lat, lon;
    private Marker myMarker;

    public Controller(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @FXML
    TextField nameField;
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
    TextField distance;
    @FXML
    TextField numOfAp;
    @FXML
    TextField sizeOfAp;
    @FXML
    TextField priceOfAp;
    @FXML
    TextField numOfRooms;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Person> persons = dataAccess.getAll();
            personsCollection = FXCollections.observableArrayList(persons);
            List<Apartment> apartments = dataAccess.getAllAps();
            apartmentsCollection = FXCollections.observableArrayList(apartments);
            List<Building> buildings = dataAccess.getAllBuildings();
            buildingsCollection = FXCollections.observableArrayList(buildings);
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

    public void addAdvertisement(ActionEvent event) throws IOException {
        try {
            List<Integer> found = dataAccess.findBuilding(round3(lat), round3(lon));
            if (found.size() == 0) {
                addBuilding(round3(lat), round3(lon));
            }
            found = dataAccess.findBuilding(round3(lat), round3(lon));
            addApartment(found.get(0));
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
//        System.out.println(lat + " " + lon);
//        System.out.println(round3(lat) + " " + round3(lon));
    }

    public void addApartment(int bid) throws IOException {
        Apartment apartment = new Apartment(apartmentsCollection.size() + 1, Integer.parseInt(numOfAp.getText()), bid, personsCollection.size() - 1,
                Double.parseDouble(sizeOfAp.getText()), Integer.parseInt(numOfRooms.getText()),
                Double.parseDouble(priceOfAp.getText()));
        try {
            dataAccess.addApartment(apartment, personsCollection.size() - 1);
            Main a = new Main();
            a.changeScene("Registration");

        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        apartmentsCollection.add(apartment);
        numOfAp.clear();
        sizeOfAp.clear();
        numOfRooms.clear();
        priceOfAp.clear();
    }

    public void addBuilding(double lat, double lon) throws IOException {
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
        Building building = new Building(buildingsCollection.size() + 1, lat, lon, type, petsAllowed.isSelected());
        try {
            dataAccess.addBuilding(building);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        buildingsCollection.add(building);
    }

    public void addPerson(ActionEvent event) throws IOException {
        Person person = new Person(nameField.getText(), emailField.getText(), Integer.parseInt(phoneField.getText()));
        try {
            dataAccess.addPerson(person);
            Main a = new Main();
            if (ownerRadio.isSelected()) {
                Owner owner = new Owner(person.getPid(), person.getName(), person.getEmail(), person.getPhone(), person.getPid(), 0.0);
                dataAccess.addOwner(owner);
                a.changeScene("OwnerCabinet");
            } else {
                Renter renter = new Renter(person.getPid(), person.getName(), person.getEmail(), person.getPhone(), person.getPid(), 0.0);
                dataAccess.addRenter(renter);
                a.changeScene("RenterCabinet");
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        personsCollection.add(person);
        nameField.clear();
        emailField.clear();
        phoneField.clear();
    }

    public void FindAppartment(ActionEvent event) throws IOException {
        List<Apartment> listap;
        try {
            Main main = new Main();

            String locat = location.getText();
            double pric = Double.parseDouble(priceOfAp.getText());
            double siz = Double.parseDouble(sizeOfAp.getText());
            try {
                int distanc = Integer.parseInt(distance.getText());
                listap = dataAccess.getApartment(distanc);
                apartmentsCollection = FXCollections.observableArrayList(listap);
                listHouse.setItems(apartmentsCollection);
            } catch (Exception e3) {
                listap = dataAccess.getApartment(locat, pric);
                apartmentsCollection = FXCollections.observableArrayList(listap);
                listHouse.setItems(apartmentsCollection);
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

    }

    public void reset(ActionEvent event) {
        location.clear();
        sizeOfAp.clear();
        priceOfAp.clear();
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
}