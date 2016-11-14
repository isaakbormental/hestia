package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by kouakam on 30.10.16.
 */

public class Apartment {

    private Person owner;
    private final SimpleIntegerProperty numberRoom=new SimpleIntegerProperty();
    private final SimpleIntegerProperty aid =new SimpleIntegerProperty();
    private final SimpleIntegerProperty anumber=new SimpleIntegerProperty();
    private final SimpleIntegerProperty bid=new SimpleIntegerProperty();
    private final SimpleIntegerProperty oid=new SimpleIntegerProperty();
    private  final SimpleDoubleProperty size = new SimpleDoubleProperty();
    private  final SimpleDoubleProperty price = new SimpleDoubleProperty();
    private final SimpleStringProperty description =new SimpleStringProperty("");
    private final SimpleStringProperty nameOwner =new SimpleStringProperty("");

    /**
     *  constructor to set the fields of the tableView
     * @param numr
     * @param sizeap
     * @param pricea
     * @param des
     */
    public Apartment(int numr,double sizeap,double pricea,String des) {
        setNumberRoom(numr);
        setPrice(pricea);
        setSize(sizeap);
        setDescription(des);
    }
    public Apartment(Person owner, int num,double si,double pri,String des){

        setNameOwner(owner.getName());
        setNumberRoom(num);
        setPrice(pri);
        setSize(si);
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

    public SimpleIntegerProperty numberRoomProperty() {
        return numberRoom;
    }

    public double getSize() {
        return size.get();
    }

    public SimpleDoubleProperty sizeProperty() {
        return size;
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setOwner(Person owner) {
        this.owner=owner;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setNumberRoom(int numberRoom) {
        this.numberRoom.set(numberRoom);
    }

    public void setSize(double size) {
        this.size.set(size);
    }

    public void setPrice(double price) {
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
    public void setBid(int bid) {
        this.bid.set(bid);
    }

    public int getOid() {
        return oid.get();
    }
    public void setOid(int oid) {
        this.oid.set(oid);
    }

    public int getAnumber() {
        return anumber.get();
    }
    public void setAnumber(int anumber) {
        this.anumber.set(anumber);
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
