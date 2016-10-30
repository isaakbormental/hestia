package src.sample;

/**
 * Created by Владислав on 26.10.2016.
 */

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person {

    private final SimpleIntegerProperty pid = new SimpleIntegerProperty();
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private final SimpleIntegerProperty phone = new SimpleIntegerProperty();


    public Person(int pid, String name, String email, int phone) {
        setPid(pid);
        setName(name);
        setEmail(email);
        setPhone(phone);
    }

    public Person(String name, String email, int phone) {
        setName(name);
        setEmail(email);
        setPhone(phone);
    }

    public int getPid() {
        return pid.get();
    }

    public void setPid(int pid) {
        this.pid.set(pid);
    }

    public String getPidString() {
        return "" + pid.get();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
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

}
