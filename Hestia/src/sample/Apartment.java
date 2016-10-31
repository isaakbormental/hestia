package src.sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by kouakam on 30.10.16.
 */

   public class Apartment {
    private  Person owner;
    private final SimpleIntegerProperty numberRoom=new SimpleIntegerProperty();
    private  final SimpleDoubleProperty size = new SimpleDoubleProperty();
    private  final SimpleDoubleProperty price = new SimpleDoubleProperty();
    private final SimpleStringProperty description =new SimpleStringProperty(" ");

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
}
