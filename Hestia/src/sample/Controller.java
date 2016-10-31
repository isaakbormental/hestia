package src.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private DataAccess dataAccess;
    private ObservableList<Person> personsCollection;
    private ObservableList<Apartment> listApartment;

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
    TextField size;
    @FXML
    TextField price;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Person> persons = dataAccess.getAll();
            personsCollection = FXCollections.observableArrayList(persons);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPerson(ActionEvent event) throws IOException {
        Person person = new Person(nameField.getText(), emailField.getText(), Integer.parseInt(phoneField.getText()));
        try {
            dataAccess.addPerson(person);
            Main a = new Main();
            if (ownerRadio.isSelected()){
                Owner owner = new Owner(person.getPid(),person.getName(),person.getEmail(),person.getPhone(), person.getPid(), 0.0);
                dataAccess.addOwner(owner);
                a.changeScene("OwnerCabinet");
            } else {
                Renter renter = new Renter(person.getPid(),person.getName(),person.getEmail(),person.getPhone(), person.getPid(), 0.0);
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

    public void FindAppartment(ActionEvent event) throws IOException{
        List<Apartment> listap;
        try {
            Main main=new Main();

                String locat =location.getText();
                double pric =Double.parseDouble(price.getText());
                double siz =Double.parseDouble(size.getText());
                listap=dataAccess.getApartment(locat,pric);
                listApartment=FXCollections.observableArrayList(listap);
                listHouse.setItems(listApartment);

        }catch (SQLException e2){
            e2.printStackTrace();
        }

    }
    public void reset(ActionEvent event ){
            location.clear();
            size.clear();
            price.clear();

    }

}