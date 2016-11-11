package sample;


/**
 * Created by Владислав on 26.10.2016.
 */

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
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

    public List<Apartment> getAllAps() throws SQLException {
        List<Apartment> result = new ArrayList<>();
        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM apartment")
        ) {
            while (rs.next()) {
                int aid = rs.getInt(1);
                int anumber = rs.getInt(2);
                int bid = rs.getInt(3);
                int oid = rs.getInt(4);
                int size = rs.getInt(5);
                int rooms = rs.getInt(6);
                int price = rs.getInt(7);
                result.add(new Apartment(aid, anumber, bid, oid, size, rooms, price));
            }
            rs.close();
            stmt.close();
        }
        return result;
    }

    public List<Building> getAllBuildings() throws SQLException {
        List<Building> result = new ArrayList<>();
        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM building")
        ) {
            while (rs.next()) {
                int bid = rs.getInt(1);
                double lat = rs.getDouble(2);
                double lon = rs.getDouble(3);
                String type = rs.getString(4);
                boolean pets = rs.getBoolean(5);
                result.add(new Building(bid, lat, lon, type, pets));
            }
            rs.close();
            stmt.close();
        }
        return result;
    }

    public List<Integer> findBuilding(double lat, double lon) throws SQLException {
        List<Integer> result = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT B.bid FROM Building B where B.latitude = ? AND B.longitude = ?")) {
            stmt.setDouble(1, lat);
            stmt.setDouble(2, lon);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int bid = rs.getInt(1);
                result.add(bid);
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
            stmt.setInt(1, this.getAll().size() + 1);
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
            stmt.setInt(1, this.getAll().size());
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
            stmt.setInt(1, this.getAll().size());
            stmt.setDouble(2, renter.getRating());
            rowsUpdated = stmt.executeUpdate();
            stmt.close();
        }
        return rowsUpdated;
    }

    public void addApartment(Apartment apartment, int user) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO apartment (aid,anumber,bid,oid,size,num_of_rooms,price) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, apartment.getAid());
            stmt.setInt(2, apartment.getAnumber());
            stmt.setInt(3, apartment.getBid());
            stmt.setInt(4, apartment.getOid());
            stmt.setDouble(5, apartment.getSize());
            stmt.setInt(6, apartment.getNumberRoom());
            stmt.setDouble(7, apartment.getPrice());
            stmt.executeUpdate();
            stmt.close();
        }
    }

    public void addBuilding(Building building) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO building (bid,latitude,longitude,type,pet_allow) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, building.getBid());
            stmt.setDouble(2, building.getLat());
            stmt.setDouble(3, building.getLon());
            stmt.setString(4, building.getType());
            stmt.setBoolean(5, building.getPets());
            stmt.executeUpdate();
            stmt.close();
        }
    }

    public List<Apartment> getApartment(String location, double price) throws SQLException {
        List<Apartment> list = new ArrayList<>();

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
            } catch (SQLException st) {
                st.printStackTrace();

            } finally {
                stm.close();
            }


        }

        return list;
    }

    public List<Apartment> getApartment(int distance) throws SQLException {
        List<Apartment> list = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("select a.price,a.size,a.anumber, lo.city, lo.crime_rating, lo.district " +
                     "from apartment a, location lo, building bi " +
                     "where a.bid in (select B.bid " +
                     "from building B, institution I " +
                     "where B.lid = I.lid" +
                     " AND |/((B.longitude - I.longitude)*(B.longitude - I.longitude) + (B.latitude - I.latitude)*(B.latitude - I.latitude)) <= ?)" +
                     " AND a.bid=bi.bid " +
                     " AND bi.lid=lo.lid;")) {
            stm.setInt(1, distance);
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
                    str.append("District: " + dis + " \n" + "City :" + cit + " Crime Rating" + ra + " \n" + "Flat number: " + n);
                    list.add(new Apartment(n, s, p, str.toString()));
                }
                res.close();
            } catch (SQLException st) {
                st.printStackTrace();

            } finally {
                stm.close();
            }
        }
        return list;
    }




    //Returns a person HashTable. Used in logIn(Controller)
    public Hashtable<Integer,String> getPerson(String logi, String pass) throws SQLException {
        Hashtable<Integer,String> person = new Hashtable<Integer,String>();
        int pid = 0;
        String name = "";
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("SELECT pid, name FROM person WHERE email = ? AND password =?")) {
            stm.setString(1, logi);
            stm.setString(2, pass);
            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();

                    pid = res.getInt("pid");
                    name = res.getString("name");
                    person.put(pid,name);
                    System.out.println("You selected: "+"pid: "+pid+"name: "+name);

                }
                res.close();
            } catch (SQLException st) {
                st.printStackTrace();

            } finally {
                stm.close();
            }


        }

        return person;
    }
    //This one only for getting person id, used once. Stupid.
    public int getPersonId(String logi, String pass) throws SQLException {
        int pid = 0;
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("SELECT pid FROM person WHERE email = ? AND password =?")) {
            stm.setString(1, logi);
            stm.setString(2, pass);
            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();
                    pid = res.getInt("pid");
                }
                res.close();
            } catch (SQLException st) {
                st.printStackTrace();

            } finally {
                stm.close();
            }
        }

        return pid;
    }
    //SELECT rid FROM renter WHERE rid = 1;
    public int getOwnerId(int pid) throws SQLException {
        int oid = 0;
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("SELECT oid FROM owner WHERE oid = ?")) {
            stm.setInt(1, pid);
            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();
                    oid = res.getInt("oid");
                }
                res.close();
            } catch (SQLException st) {
                st.printStackTrace();

            } finally {
                stm.close();
            }
        }
        return oid;
    }

    public int getRenterId(int pid) throws SQLException {
        int rid = 0;
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("SELECT rid FROM renter WHERE rid = ?")) {
            stm.setInt(1, pid);
            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();
                    rid = res.getInt("rid");
                }
                res.close();
            } catch (SQLException st) {
                st.printStackTrace();

            } finally {
                stm.close();
            }
        }
        return rid;
    }

}
