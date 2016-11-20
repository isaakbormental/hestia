package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

/**
 * Created by Hestia Team on 11/20/2016.
 */
public class Message {


    private final SimpleIntegerProperty sender_id=new SimpleIntegerProperty();
    private final SimpleIntegerProperty reciever_id=new SimpleIntegerProperty();
    private Date date = new Date(1,1,1994);
    private final SimpleStringProperty message =new SimpleStringProperty("");
    private final Time time = new Time(18,54,34);

    public Message(String msg, int rec, int send, Date date){
        setMessage(msg);
        setReciever_id(rec);
        setSender_id(send);
        setDate(date);
    }


    public int getSender_id() {
        return sender_id.get();
    }

    public SimpleIntegerProperty sender_idProperty() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id.set(sender_id);
    }

    public int getReciever_id() {
        return reciever_id.get();
    }

    public SimpleIntegerProperty reciever_idProperty() {
        return reciever_id;
    }

    public void setReciever_id(int reciever_id) {
        this.reciever_id.set(reciever_id);
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message.get();
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public Time getTime() {
        return time;
    }
    public void setDate(Date data){
        this.date = data;
    }




}
