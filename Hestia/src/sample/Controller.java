package sample;

import com.sun.javafx.scene.control.skin.IntegerFieldSkin;
import com.sun.org.apache.regexp.internal.RE;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private DataAccess dataAccess;
    private ObservableList<Person> personsCollection;

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

}