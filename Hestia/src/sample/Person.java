package sample;


/**
 * Created by Владислав on 26.10.2016.
 */

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person {

    private final SimpleIntegerProperty pid = new SimpleIntegerProperty();
    private final SimpleStringProperty firstname = new SimpleStringProperty("");
    private final SimpleStringProperty lastname = new SimpleStringProperty("");
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private final SimpleIntegerProperty phone = new SimpleIntegerProperty();
    private final SimpleStringProperty password=new SimpleStringProperty("");


    public Person(int pid, String name, String email, int phone) {
        setPid(pid);
        setFirstname(name);
        setEmail(email);
        setPhone(phone);
    }
    public Person(String firstname,String lastname,String email,String pass,int phon){
        setFirstname(firstname);
        setLastname(lastname);
        setEmail(email);
        setPhone(phon);
        setPassword(pass);
    }
   public Person(String firstname,String email,int phone){
       setFirstname(firstname);
       setEmail(email);
       setPhone(phone);
   }




    public Person(String firstname, String lastname) {
        setFirstname(firstname);
        setLastname(lastname);
        setName(firstname+"  "+lastname);
        
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getPid() {
        return pid.get();
    }

    public void setPid(int pid) {
        this.pid.set(pid);
    }

    public String getFirsName() {
        return firstname.get();
    }

    public String getLastname() {
        return lastname.get();
    }



    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public int getPhone() {
        return phone.get();
    }

    public void setPhone(int phone) {
        this.phone.set(phone);
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }
    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getPassword(){
        return this.password.get();
    }
}
