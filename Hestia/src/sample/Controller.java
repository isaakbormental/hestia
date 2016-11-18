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
    GoogleMapView mapView;
    GoogleMap map;
    private double lat, lon;
    private Marker myMarker;
    private int cabinetMarker = 0;


    public Controller(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @FXML TextField firstnameField;
    @FXML TextField lastnameField;
    //@FXML TextField fisrtnameField;
    @FXML  TextField emailField;
    @FXML TextField phoneField;
    @FXML RadioButton ownerRadio;
    @FXML RadioButton renterRadio;
    @FXML TableView listHouse;
    @FXML Button validate;
    @FXML Button reset;
    @FXML TextField location;
    @FXML TextField distance;
    @FXML TextField numOfAp;
    @FXML TextField size;
    @FXML TextField price;
    @FXML TextField numOfRooms;
    @FXML TextField location_city;
    @FXML TextField location_district;
    @FXML RadioButton petsAllowed;
    @FXML RadioButton petsNotAllowed;
    @FXML RadioButton hostelRoomType;
    @FXML RadioButton privateHouseType;
    @FXML RadioButton apartmentType;
    @FXML PasswordField password;
    @FXML TextField login;
    @FXML TableView history;
    @FXML Button openmap;
    @FXML Button addadvert;
    @FXML Hyperlink link;
    @FXML Button addperson;
    @FXML Button loglog;
    @FXML PasswordField passregistration;
    @FXML TableView historyRenter;


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
    public void addAdvert(ActionEvent event){
        Main main=new Main();

            try {
                main.changeScene("AddAdvert");
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public void createAccount(ActionEvent event){
        Main main=new Main();

        try {
            main.changeScene("newAccount");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addAdvertisement(ActionEvent event) throws IOException {
        if (event.getSource()==addadvert)
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
        Apartment apartment = new Apartment(apartmentsCollection.size() + 1, Integer.parseInt(numOfRooms.getText()), bid, personsCollection.size() - 1,
                Double.parseDouble(size.getText()),
                Double.parseDouble(price.getText()));
        try {
            dataAccess.addApartment(apartment, personsCollection.size() - 1);
            Main a = new Main();
            a.changeScene("Registration");

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
        Person person = new Person(firstnameField.getText(),lastnameField.getText(), emailField.getText(), passregistration.getText(),Integer.parseInt(phoneField.getText()));
        try {
            dataAccess.addPerson(person);
            Main a = new Main();
            if (ownerRadio.isSelected()) {
                //Owner owner = new Owner(person.getPid(), person.getFirsName(), person.getEmail(), person.getPhone(), person.getPid(), 0.0);
                //dataAccess.addOwner(owner);
                int oid=dataAccess.getPersonId(person.getEmail(),person.getPassword());
                dataAccess.addOwner(oid,0.0);
                a.changeScene("OwnerCabinet");
            } else {
                  int rid=dataAccess.getPersonId(person.getEmail(),person.getPassword());
                  dataAccess.addRenter(rid,0.0);
                a.changeScene("RenterCabinet");
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        personsCollection.add(person);
        firstnameField.clear();
        emailField.clear();
        phoneField.clear();
    }

    public void FindAppartment(ActionEvent event) throws IOException {
        List<Apartment> listap;

        Main main = new Main();
        String locat = "default";
        if(location.getText().equals("")){
            System.out.println("NO LOCAT");
        }
        else{
            locat = location.getText();
        }

        int pric = 0;
        if(price.getText().equals("")){
            System.out.println("NO PRICE");
            pric = -1;
        }
        else{
            pric = Integer.parseInt(price.getText());
        }
        int siz = -1;
        if(size.getText().equals("")){
            System.out.println("NO SIZE");
        }
        else{
            siz = Integer.parseInt(size.getText());
        }
        int distanc = -1;
        if(distance.getText().equals("")){
            System.out.println("NO DIST");
        }
        else{
            distanc = Integer.parseInt(distance.getText());
        }

        listHouse.setItems(apartmentsCollection);
        System.out.println(0);
        try {
            System.out.print("Selected location: "+locat+" ");
            System.out.print("Selected price: "+pric+" ");
            System.out.print("Selected size: "+siz+" ");
            System.out.println("Selected price: "+distanc);

            listap = dataAccess.getApartment(locat,pric,siz,distanc);
            apartmentsCollection = FXCollections.observableArrayList(listap);
            listHouse.setItems(apartmentsCollection);
        } catch (Exception e3) {
            System.out.println("EX");

        }

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



    public void logIn(ActionEvent event) throws IOException{
        List<Apartment> listap;
        Hashtable<Integer,String> person = new Hashtable<Integer,String>();
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
                        main.changeScene("OwnerCabinet");
                        listap = dataAccess.findRenterById(oid);
                        apartmentsCollection = FXCollections.observableArrayList(listap);
                        historyRenter.setItems(apartmentsCollection);
                    } else if (rid != 0) {
                        main.changeScene("RenterCabinet");
                        listap = dataAccess.findApartRentedById(rid);
                        apartmentsCollection = FXCollections.observableArrayList(listap);
                        history.setItems(apartmentsCollection);

                }
                else{
                    System.out.println("Invalid login or password");
                }

            } catch (Exception e3) {

            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

   public  void logOut(ActionEvent event) throws IOException {
       Main main=new Main();
       main.changeScene("Regitration");
   }

}