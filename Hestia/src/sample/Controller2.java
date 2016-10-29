//package sample;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
//import javafx.scene.control.cell.TextFieldTableCell;
//import javafx.util.converter.IntegerStringConverter;
//
//import java.net.URL;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.ResourceBundle;
//
//public class Controller2 implements Initializable {
//
//    private DataAccess dataAccess;
//    private ObservableList<Person> personsCollection;
//
//    public Controller2(DataAccess dataAccess) {
//        this.dataAccess = dataAccess;
//    }
//
//    @FXML
//    TableView tableView;
//    @FXML
//    TableColumn<Person, String> nameColumn;
//    @FXML
//    TableColumn<Person, String> emailColumn;
//    @FXML
//    TableColumn<Person, Integer> phoneColumn;
//    @FXML
//    TextField nameField;
//    @FXML
//    TextField emailField;
//    @FXML
//    TextField phoneField;
//
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        tableView.setEditable(true);
//        try {
//            List<Person> persons = dataAccess.getAll();
//            personsCollection = FXCollections.observableArrayList(persons);
//            tableView.setItems(personsCollection);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
//    }
//
//    public void addPerson(ActionEvent event) {
//        Person person = new Person(nameField.getText(), emailField.getText(), Integer.parseInt(phoneField.getText()));
//        try {
//            dataAccess.addPerson(person);
//        } catch (SQLException e1) {
//            e1.printStackTrace();
//        }
//        personsCollection.add(person);
//        nameField.clear();
//        emailField.clear();
//        phoneField.clear();
//    }
//
//}