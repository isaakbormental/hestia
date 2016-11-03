package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Владислав on 03.11.2016.
 */
public class Building {

    private final SimpleIntegerProperty bid = new SimpleIntegerProperty();
    private final SimpleDoubleProperty lat = new SimpleDoubleProperty();
    private final SimpleDoubleProperty lon = new SimpleDoubleProperty();
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleBooleanProperty pets = new SimpleBooleanProperty();

    public Building(int bid, double lat, double lon, String type, boolean pets) {
        setBid(bid);
        setLat(lat);
        setLon(lon);
        setType(type);
        setPets(pets);
    }

    public int getBid() {
        return bid.get();
    }

    public void setBid(int bid) {
        this.bid.set(bid);
    }

    public double getLat() {
        return lat.get();
    }

    public void setLat(double lat) {
        this.lat.set(lat);
    }

    public double getLon() {
        return lon.get();
    }

    public void setLon(double lon) {
        this.lon.set(lon);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public boolean getPets() {
        return pets.get();
    }

    public void setPets(boolean pets) {
        this.pets.set(pets);
    }

}
