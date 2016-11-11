package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Владислав on 11.11.2016.
 */
public class Location {

    private final SimpleIntegerProperty lid = new SimpleIntegerProperty();
    private final SimpleStringProperty city = new SimpleStringProperty("");
    private final SimpleStringProperty district = new SimpleStringProperty("");
    private final SimpleDoubleProperty crime_rate = new SimpleDoubleProperty();

    public Location(int lid, String city, String district, double crime_rate) {
        setLid(lid);
        setCity(city);
        setDistrict(district);
        setCrime_rate(crime_rate);
    }

    public int getLid() {
        return lid.get();
    }

    public void setLid(int lid) {
        this.lid.set(lid);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getDistrict() {
        return district.get();
    }

    public void setDistrict(String district) {
        this.district.set(district);
    }

    public double getCrime_rate() {
        return crime_rate.get();
    }

    public void setCrime_rate(double crime_rate) {
        this.crime_rate.set(crime_rate);
    }
}
