package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Hestia team on 11/23/2016.
 */
public class Request {

    private Person owner;
    private final SimpleIntegerProperty numberRoom=new SimpleIntegerProperty();



    private final SimpleIntegerProperty aid =new SimpleIntegerProperty();
    private final SimpleIntegerProperty oid=new SimpleIntegerProperty();
    private final SimpleIntegerProperty rid=new SimpleIntegerProperty();
    private final SimpleStringProperty description =new SimpleStringProperty("");
    private final SimpleStringProperty nameRenter =new SimpleStringProperty();
    private final SimpleDoubleProperty rating = new SimpleDoubleProperty();

    public Request(int ap, Person requester, int oid, String description){
        setAid(ap);
        setRid(requester.getPid());
        setOid(oid);
        setDescription(description);
        setNameRenter(requester.getName());
    }


    public int getAid() {
        return aid.get();
    }

    public SimpleIntegerProperty aidProperty() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid.set(aid);
    }

    public int getOid() {
        return oid.get();
    }

    public SimpleIntegerProperty oidProperty() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid.set(oid);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getNameRenter() {
        return nameRenter.get();
    }

    public SimpleStringProperty nameRenterProperty() {
        return nameRenter;
    }

    public void setNameRenter(String nameRenter) {
        this.nameRenter.set(nameRenter);
    }

    public int getRid() {
        return rid.get();
    }

    public SimpleIntegerProperty ridProperty() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid.set(rid);
    }

    public double getRating() {
        return rating.get();
    }

    public SimpleDoubleProperty ratingProperty() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating.set(rating);
    }

}
