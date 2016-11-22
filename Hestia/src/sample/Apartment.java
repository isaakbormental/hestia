package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hestia Team on 30.10.16.
 */

public class Apartment {

    private Person owner;
    private final SimpleIntegerProperty numberRoom=new SimpleIntegerProperty();
    private final SimpleIntegerProperty aid =new SimpleIntegerProperty();
   // private final SimpleIntegerProperty anumber=new SimpleIntegerProperty();
    private final SimpleIntegerProperty bid=new SimpleIntegerProperty();
    private final SimpleIntegerProperty oid=new SimpleIntegerProperty();
    private  final SimpleDoubleProperty size = new SimpleDoubleProperty();
    private  final SimpleDoubleProperty price = new SimpleDoubleProperty();
    private final SimpleStringProperty description =new SimpleStringProperty("");
    private final SimpleStringProperty nameOwner =new SimpleStringProperty();

    public String getLoanduration() {
        return loanduration.get();
    }

    public SimpleStringProperty loandurationProperty() {
        return loanduration;
    }

    public void setLoanduration(String loanduration) {
        this.loanduration.set(loanduration);
    }

    private final SimpleStringProperty loanduration=new SimpleStringProperty("");
    private final SimpleStringProperty location = new SimpleStringProperty("");
    private final SimpleDoubleProperty rating = new SimpleDoubleProperty();

    public int getRid() {
        return rid.get();
    }

    public SimpleIntegerProperty ridProperty() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid.set(rid);
    }

    private final SimpleIntegerProperty rid = new SimpleIntegerProperty();

//Yolo

    /**
     *  constructor to set the fields of the tableView
     * @param numr
     * @param sizeap
     * @param pricea
     * @param des
     */
    public Apartment(int numr, double sizeap, double pricea, String des) {
        setNumberRoom(numr);
        setPrice(pricea);
        setSize(sizeap);
        setDescription(des);
    }
    public Apartment(Person owner, int num,double si,double pri,String des,String duration){
        setLoanduration(duration);
        setNameOwner(owner.getName());
        setNumberRoom(num);
        setPrice(pri);
        setSize(si);
        setDescription(des);
        setOwner(owner);
    }
    //My addition
    public Apartment(int owner, int num,int aid, double si,double pri,String des,String duration, String loc){
        setLoanduration(duration);
        setOid(owner);
        setAid(aid);
        setNumberRoom(num);
        setPrice(pri);
        setSize(si);
        setDescription(des);
        setLocation(loc);


    }

    public Apartment(int aid, Person owner, int num,double si,double pri,String des,String duration){
        setLoanduration(duration);
        setNameOwner(owner.getName());
        setNumberRoom(num);
        setPrice(pri);
        setSize(si);
        setDescription(des);
        setOwner(owner);
        setAid(aid);
        setOid(owner.getPid());
    }

    public Apartment(Person owner,String des,String duration){
        setLoanduration(duration);
        setNameOwner(owner.getName());
        setDescription(des);
    }

    public Apartment(int oid, double sizeap, int numr, double pricea) {
        setNumberRoom(numr);
        setPrice(pricea);
        setSize(sizeap);
        setOid(oid);
    }

    public Apartment(int aid, int anumber, int bid, int oid, double sizeap, double pricea) {
        setNumberRoom(anumber);
        setPrice(pricea);
        setSize(sizeap);
        setAid(aid);
        setBid(bid);
        setOid(oid);

    }

    public Apartment(double sizeap, int pricea, String des) {
        setPrice(pricea);
        setSize(sizeap);
        setDescription(des);
        setRating(0.0);
    }

    public Apartment(){

    }
    /**
     * getter and setters
     * @return
     */
    public Person getOwner() {
        return owner;
    }

    public int getNumberRoom() {
        return numberRoom.get();
    }


    public double getSize() {
        return size.get();
    }
    public double getPrice() {
        return price.get();
    }
    private void setOwner(Person owner) {
        this.owner=owner;
    }

    private void setDescription(String desc) {
        this.description.set(desc);
    }

    private void setNumberRoom(int numberRoom) {
        this.numberRoom.set(numberRoom);
    }

    private void setSize(double size) {
        this.size.set(size);
    }


    private void setPrice(double price) {
        this.price.set(price);
    }

    public int getAid() {
        return aid.get();
    }
    public void setAid(int aid) {
        this.aid.set(aid);
    }

    public int getBid() {
        return bid.get();
    }
    private void setBid(int bid) {
        this.bid.set(bid);
    }

    public int getOid() {
        return oid.get();
    }
    public void setOid(int oid) {
        this.oid.set(oid);
    }



    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
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

    public String getNameOwner() {
        return nameOwner.get();
    }

    public SimpleStringProperty nameOwnerProperty() {
        return nameOwner;
    }

    public void setNameOwner(String nameOwner) {
        this.nameOwner.set(nameOwner);
    }

}


