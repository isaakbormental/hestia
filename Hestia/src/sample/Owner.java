package sample;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Владислав on 27.10.2016.
 */
public class Owner extends Person{

    private final SimpleIntegerProperty oid = new SimpleIntegerProperty();
    private final SimpleDoubleProperty rating = new SimpleDoubleProperty();

    public Owner(int pid, String name, String email, int phone, int oid, double rating) {
        super(pid, name, email, phone);
        setOid(oid);
        setRating(rating);
    }

    public Owner(String name, String email, int phone, int oid, double rating) {
        super(name, email, phone);
        setOid(oid);
        setRating(rating);
    }

    public int getOid() {
        return oid.get();
    }

    public void setOid(int oid) {
        this.oid.set(oid);
    }

    public double getRating() {
        return rating.get();
    }

    public void setRating(double rating) {
        this.rating.set(rating);
    }

}
