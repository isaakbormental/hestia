package src.sample;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Владислав on 27.10.2016.
 */
public class Renter extends Person {

    private final SimpleIntegerProperty rid = new SimpleIntegerProperty();
    private final SimpleDoubleProperty rating = new SimpleDoubleProperty();

    public Renter(int pid, String name, String email, int phone, int rid, double rating) {
        super(pid, name, email, phone);
        setRid(rid);
        setRating(rating);
    }

    public Renter(String name, String email, int phone, int rid, double rating) {
        super(name, email, phone);
        setRid(rid);
        setRating(rating);
    }

    public int getRid() {
        return rid.get();
    }

    public void setRid(int rid) {
        this.rid.set(rid);
    }

    public double getRating() {
        return rating.get();
    }

    public void setRating(double rating) {
        this.rating.set(rating);
    }

}
