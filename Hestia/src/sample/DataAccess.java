package src.sample;

/**
 * Created by Владислав on 26.10.2016.
 */

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataAccess {
    private static DataSource dataSource = null;

    public DataAccess(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public List<Person> getAll() throws SQLException {
        List<Person> result = new ArrayList<Person>();
        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM person")
        ) {
            while (rs.next()) {
                int id = rs.getInt(1);
                String firstname = rs.getString(2);
                String lastname = rs.getString(3);
                int phone = rs.getInt(4);
                result.add(new Person(id, firstname, lastname, phone));
            }
            rs.close();
            stmt.close();
        }
        return result;
    }

    public int addPerson(Person person) throws SQLException {
        int rowsUpdated;
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO person (pid,name,email,phone) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1,this.getAll().size()+1);
            stmt.setString(2, person.getName());
            stmt.setString(3, person.getEmail());
            stmt.setInt(4, person.getPhone());
            rowsUpdated = stmt.executeUpdate();
            stmt.close();
        }
        return rowsUpdated;
    }

    public int addOwner(Owner owner) throws SQLException {
        int rowsUpdated;
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO owner (oid,rating) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1,this.getAll().size());
            stmt.setDouble(2, owner.getRating());
            rowsUpdated = stmt.executeUpdate();
            stmt.close();
        }
        return rowsUpdated;
    }

    public int addRenter(Renter renter) throws SQLException {
        int rowsUpdated;
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO renter (rid,rating) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1,this.getAll().size());
            stmt.setDouble(2, renter.getRating());
            rowsUpdated = stmt.executeUpdate();
            stmt.close();
        }
        return rowsUpdated;
    }

    public List<Apartment> getApartment(String location,double price)throws SQLException{
        List<Apartment> list=new ArrayList<>();

            try (Connection connection = getConnection();
                    PreparedStatement stm = connection.prepareStatement("SELECT  A.size,A.price,A.anumber,L.district,L.city,L.crime_rating FROM" +
                    " apartment A NATURAL JOIN building B NATURAL JOIN location L WHERE A.price<? AND L.city=?")) {
                stm.setDouble(1, price);
                stm.setString(2, location);
               // int row = stm.executeUpdate();
                try {
                    ResultSet res = stm.executeQuery();
                    while (res.next()) {
                        StringBuilder str = new StringBuilder();
                        double s = res.getDouble("size");
                        double p = res.getDouble("price");
                        int n = res.getInt("anumber");
                        String dis = res.getString("district");
                        String cit = res.getString("city");
                        double ra = res.getDouble("crime_rating");
                        str.append("District: " + dis + " \n" + "City :" + cit + " Crime Rating" + ra);
                        list.add(new Apartment(n, s, p, str.toString()));
                    }
                    res.close();
                }catch (SQLException st){
                    st.printStackTrace();

            }finally {
                    stm.close();
                }


        }



        return list;
    }



    public int addApartment(Apartment apartment) throws SQLException {
        int rowsUpdated;
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO apartment (aid,anumber,bid,oid,size,price) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1,this.getAll().size());
            stmt.setInt(2,23256);
            stmt.setInt(3,apartment.getOwner().getPid());
            stmt.setInt(4,this.getAll().size());
            stmt.setDouble(5,apartment.getSize());
            stmt.setDouble(6,apartment.getPrice());
            rowsUpdated = stmt.executeUpdate();
            stmt.close();
        }
        return rowsUpdated;
    }

}
