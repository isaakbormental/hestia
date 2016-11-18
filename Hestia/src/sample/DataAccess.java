package sample;


/**
 * Created by Владислав on 26.10.2016.
 */

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

import java.util.List;
@SuppressWarnings("all")
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
                int id = rs.getInt("person_id");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String email = rs.getString("email");
                int phone = rs.getInt("phone");
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
                int aid = rs.getInt("apart_id");
                int anumber = rs.getInt("num_of_rooms");
                int bid = rs.getInt("buildin_id");
                int oid = rs.getInt("oid");
                int size = rs.getInt("size");
              //  int rooms = rs.getInt(6);
                int price = rs.getInt("price");
                result.add(new Apartment(aid, anumber, bid, oid, size, price));
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
                int bid = rs.getInt("building_id");
                double lat = rs.getDouble("longitude");
                double lon = rs.getDouble("latitude");
                String type = rs.getString("type");
                boolean pets = rs.getBoolean("pet_allow");
                int lid = rs.getInt("lid");
                result.add(new Building(bid, lat, lon, type, pets, lid));
            }
            rs.close();
            stmt.close();
        }
        return result;
    }

    public List<Location> getAllLocations() throws SQLException {
        List<Location> result = new ArrayList<>();
        try (
                Connection connection = getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM location")
        ) {
            while (rs.next()) {
                int lid = rs.getInt("location_id");
                String city = rs.getString("city");
                String district = rs.getString("district");
                double crime_rating = rs.getDouble("crime_rating");
                result.add(new Location(lid, city, district, crime_rating));
            }
            rs.close();
            stmt.close();
        }
        return result;
    }

    /**
     * return the apartments id been rented by the user whit the given id
     * @param idRenter
     * @return
     * @throws SQLException
     */
    public List<Integer> getIdApartementrented(int idRenter) throws SQLException {
        List<Integer> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement stm = con.prepareStatement("SELECT A.apart_id FROM renter_apart A WHERE  A.renter_id=?")) {
            stm.setInt(1, idRenter);
            ResultSet res = stm.executeQuery();
            while (res.next()) {
                list.add(res.getInt("apart_id"));
            }
        }
        return list;
    }

    public List<Apartment> findListApartmentRented(int idRenter) throws SQLException {
        List<Apartment> listApart = new ArrayList<>();
        List<Integer> lisId = getIdApartementrented(idRenter);
        for (int id : lisId) {
            try (Connection con = getConnection();
                 PreparedStatement stmt = con.prepareStatement("SELECT A.*,L.* FROM " +
                         "apartment A NATURAL  JOIN building B,natural JOIN location L " +
                         "WHERE A.apart_id=?")
            ) {
                stmt.setInt(1, id);
                try {
                    ResultSet res = stmt.executeQuery();
                    while (res.next()) {
                        StringBuilder des = new StringBuilder();
                        des.append("City: " + res.getString("city")).append(" District: " + res.getString("district") + "/n");
                        des.append("Crime Rating :" + res.getDouble("crime_rating"));
                        int pri = res.getInt("price");
                        double size = res.getDouble("size");
                        int num = res.getInt("anumber");
                        listApart.add(new Apartment(num, size, pri, des.toString()));

                    }
                }catch (SQLException ex){
                    ex.printStackTrace();
                }


            }
        }
        return listApart;
    }
    /**
     *
     * @param lat
     * @param lon
     * @return
     * @throws SQLException
     */
    public List<Integer> findBuilding(double lat, double lon) throws SQLException {
        List<Integer> result = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT B.building_id FROM Building B where B.latitude = ? AND B.longitude = ?")) {
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

    public List<Integer> findLocation(String city, String district) throws SQLException {
        List<Integer> result = new ArrayList<>();
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT L.location_id FROM Location L where L.city = ? AND L.district = ?")) {
            stmt.setString(1, city);
            stmt.setString(2, district);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int lid = rs.getInt(1);
                result.add(lid);
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
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO person (firstname,lastname,email,password,phone) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, person.getFirsName());
            stmt.setString(2, person.getLastname());
            stmt.setString(3, person.getEmail());
            stmt.setString(4, person.getPassword());
            stmt.setInt(5, person.getPhone());
            rowsUpdated = stmt.executeUpdate();
            stmt.close();
        }
        return rowsUpdated;
    }

    public int addOwner(int oid,double rating) throws SQLException {
        int rowsUpdated;
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO owner (owner_id,rating) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, oid);
            stmt.setDouble(2, rating);
            rowsUpdated = stmt.executeUpdate();
            stmt.close();
        }
        return rowsUpdated;
    }

    public int addRenter(int rid,double rating) throws SQLException {
        int rowsUpdated;
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO renter (rid,rating) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rid);
            stmt.setDouble(2, rating);
            rowsUpdated = stmt.executeUpdate();
            stmt.close();
        }
        return rowsUpdated;
    }

    public void addApartment(Apartment apartment, int user) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO apartment (apart_id,num_of_rooms,buildin_id,oid,size,price) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, apartment.getAid());
            stmt.setInt(3, apartment.getBid());
            stmt.setInt(4, apartment.getOid());
            stmt.setDouble(5, apartment.getSize());
            stmt.setInt(2, apartment.getNumberRoom());
            stmt.setDouble(7, apartment.getPrice());
            stmt.executeUpdate();
            stmt.close();
        }
    }

    public void addBuilding(Building building) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO building (building_id,latitude,longitude,type,pet_allow,lid) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, building.getBid());
            stmt.setDouble(2, building.getLat());
            stmt.setDouble(3, building.getLon());
            stmt.setString(4, building.getType());
            stmt.setBoolean(5, building.getPets());
            stmt.setInt(6, building.getLid());
            stmt.executeUpdate();
            stmt.close();
        }
    }

    public void addLocation(Location location) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO location (location_id,city,district,crime_rating) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, location.getLid());
            stmt.setString(2, location.getCity());
            stmt.setString(3, location.getDistrict());
            stmt.setDouble(4, location.getCrime_rate());
            stmt.executeUpdate();
            stmt.close();
        }
    }

    public List<Apartment> getApartment(String location, double price) throws SQLException {
        List<Apartment> list = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("SELECT  A.size,A.price,A.num_of_rooms,L.district,L.city,L.crime_rating FROM" +
                     " apartment A NATURAL JOIN building B NATURAL JOIN location L WHERE A.price<? AND L.city=?")) {
            stm.setDouble(1, price);
            stm.setString(2, location);
            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();
                    double s = res.getDouble("size");
                    int p = res.getInt("price");
                    int n = res.getInt("num_of_rooms");
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

    public List<Apartment> getApartment(String location, int price, int size, int distance) throws SQLException {
        List<Apartment> list = new ArrayList<>();
        int l=1;
        int pr=1;
        int si=1;
        int di=1;
        try (Connection connection = getConnection()) {
            String query = "select a.price,a.size, lo.city, lo.crime_rating, lo.district, a.apart_id " +
                    "from apartment a, location lo, building bi " +
                    "where a.buildin_id=bi.building_id " +
                    "      AND bi.lid=lo.location_id " +
                    " ";
            if (!location.equals("default")) {
                query= query + " intersect select a.price,a.size, lo.city, lo.crime_rating, lo.district, a.apart_id " +
                        "from apartment a, location lo, building bi " +
                        "where a.buildin_id=bi.building_id " +
                        "      AND bi.lid=lo.location_id " +
                        "      AND lo.city=? ";
                pr++;
                si++;
                di++;
            }
            if (price>0) {
                query= query + " intersect select a.price,a.size, lo.city, lo.crime_rating, lo.district, a.apart_id " +
                        " from apartment a, location lo, building bi " +
                        " where a.buildin_id=bi.building_id " +
                        "      AND bi.lid=lo.location_id " +
                        "      AND a.price<? ";
                si++;
                di++;
            }
            if(size>0){
                query= query + " intersect select a.price,a.size, lo.city, lo.crime_rating, lo.district, a.apart_id " +
                        "from apartment a, location lo, building bi " +
                        "where a.buildin_id=bi.building_id " +
                        "      AND bi.lid=lo.location_id " +
                        "      AND a.size>? ";
                di++;
            }
            if(distance>0){
                query= query + " intersect select a.price,a.size, lo.city, lo.crime_rating, lo.district, a.apart_id " +
                        "from apartment a, location lo, building bi " +
                        "where a.buildin_id in (select B.building_id " +
                        "from building B, institution I " +
                        "where B.lid = I.location_id " +
                        "AND |/((B.longitude - I.longitude)*(B.longitude - I.longitude) + (B.latitude - I.latitude)*(B.latitude - I.latitude)) <= ?) " +
                        "AND a.buildin_id=bi.building_id " +
                        "AND bi.lid=lo.location_id; ";
            }

            PreparedStatement stm = connection.prepareStatement(query);
            if(!location.equals("default")){stm.setString(l, location);}
            if(price>0){stm.setInt(pr, price);}
            if(size>0){stm.setInt(si, size);}
            if(distance>0){stm.setInt(di, distance);}

            //stm.setInt(1, distance);
            //int row = stm.executeUpdate();

            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();
                    double s = res.getDouble("size");
                    int p = res.getInt("price");
                    String dis = res.getString("district");
                    String cit = res.getString("city");
                    double ra = res.getDouble("crime_rating");
                    str.append("District: " + dis + " \n" + "City :" + cit + " Crime Rating" + ra);
                    list.add(new Apartment(s, p, str.toString()));
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


    public List<Apartment> findApartRentedById(int renterid) throws SQLException {
        ArrayList<Apartment> list = new ArrayList<>();
        try (
                Connection con = getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT  lastname,firstname ,RA.date_begin,RA.date_end ,L.city, L.district ,L.crime_rating,A.price,A.size,A.num_of_rooms" +
                        " FROM person  P NATURAL JOIN owner O ,renter_apart RA,location L ,building B , apartment A" +
                        "   where (RA.owner_id, RA.apart_id, RA.date_begin,RA.date_end)" +
                        "           in(select owner_id,apart_id,date_begin,date_end" +
                        "                FROM renter_apart" +
                        "                where renter_apart.owner_id=O.owner_id AND renter_apart.renter_id=?) and P.person_id=O.owner_id" +
                        "  and (A.apart_id,L.city,L.district,L.crime_rating) in (SELECT DISTINCT apart_id,city,district,crime_rating" +
                        "                                                            FROM location L1 NATURAL JOIN building B1 NATURAL JOIN apartment A1" +
                        "                                                                WHERE A1.apart_id=RA.apart_id and A1.buildin_id=B1.building_id AND B1.lid=L1.location_id)")
        ) {
            stmt.setInt(1, renterid);
            try {
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {

                    String fname = resultSet.getString("firstname");
                    String lname = resultSet.getString("lastname");
                    Owner person = new Owner(fname, lname);
                    StringBuilder str = new StringBuilder();
                    double s = resultSet.getDouble("size");
                    double p = resultSet.getDouble("price");
                    int n = resultSet.getInt("num_of_rooms");
                    String dis = resultSet.getString("district");
                    String cit = resultSet.getString("city");
                    double ra = resultSet.getDouble("crime_rating");
                    str.append("District: " + dis + " \n" + "City :" + cit + " Crime Rating" + ra + " \n" +ra);
                    Date date_be=resultSet.getDate("date_begin");
                    Date date_en=resultSet.getDate("date_end");
                    String duration="From :"+date_be.toString().concat(" to: "+date_en.toString());
                    System.out.println(duration);
                    System.out.println(str.toString());
                    list.add(new Apartment(person,n,s,p,str.toString(),duration));

                }
                resultSet.close();
                stmt.close();
            }catch (SQLException ext){
             ext.printStackTrace();
            }
        }
        return list;
    }

    public List<Apartment> findRenterById(int ownerid) throws SQLException{
        ArrayList<Apartment> list=new ArrayList<>();
        try(
                Connection con=getConnection();
                PreparedStatement stmt=con.prepareStatement("SELECT DISTINCT  lastname,firstname ,RA.date_begin,RA.date_end ,L.city, L.district " +
                                        "FROM person  P NATURAL JOIN renter R ,renter_apart RA,location L ,building B , apartment A" +
                                    " where (RA.owner_id, RA.apart_id, RA.date_begin,RA.date_end)" +
                                    " in(select owner_id,apart_id,date_begin,date_end" +
                                    " FROM renter_apart" +
                                    " where renter_apart.renter_id=R.renter_id AND renter_apart.owner_id=?) and P.person_id=R.renter_id" +
                                    " and (A.apart_id,L.city,L.district) in (SELECT DISTINCT apart_id,city,district" +
                                    " FROM location L1 NATURAL JOIN building B1 NATURAL JOIN apartment A1" +
                                    " WHERE A1.apart_id=RA.apart_id and A1.buildin_id=B1.building_id AND B1.lid=L1.location_id)")
                    ){
            stmt.setInt(1,ownerid);
            try {
                ResultSet resultSet= stmt.executeQuery();
                while (resultSet.next()){

                    String fname=resultSet.getString("firstname");
                    String lname=resultSet.getString("lastname");
                    Owner person=new Owner(fname,lname);
                    StringBuilder str = new StringBuilder();
                    String dis = resultSet.getString("district");
                    String cit = resultSet.getString("city");
                    Date date_be=resultSet.getDate("date_begin");
                    Date date_en=resultSet.getDate("date_end");
                    String duration="From :"+date_be.toString().concat(" to: "+date_en.toString());
                    str.append("District: " + dis + " \n" + "City :" + cit  + " \n" );

                    list.add(new Apartment(person,str.toString(),duration));

                }
                resultSet.close();
            }catch (SQLException ext){
               System.out.println(ext.getMessage()); ext.printStackTrace();
            }

            stmt.close();
        }
        return list;
    }



    //Returns a person HashTable. Used in logIn(Controller)
    public Hashtable<Integer, String> getPerson(String logi, String pass) throws SQLException {
        Hashtable<Integer, String> person = new Hashtable<Integer, String>();
        int pid = 0;
        String name = "";
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("SELECT person_id, firstname,lastname FROM person WHERE email = ? AND password =?")) {
            stm.setString(1, logi);
            stm.setString(2, pass);
            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();

                    pid = res.getInt("person_id");
                    name = res.getString("firstname") + " " + res.getString("lastname");
                    person.put(pid, name);
                    System.out.println("You selected: " + "pid: " + pid + "name: " + name);

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
             PreparedStatement stm = connection.prepareStatement("SELECT person_id FROM person WHERE email = ? AND password =?")) {
            stm.setString(1, logi);
            stm.setString(2, pass);
            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();
                    pid = res.getInt("person_id");
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
             PreparedStatement stm = connection.prepareStatement("SELECT owner_id FROM owner WHERE owner_id = ?")) {
            stm.setInt(1, pid);
            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();
                    oid = res.getInt("owner_id");
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
             PreparedStatement stm = connection.prepareStatement("SELECT renter_id FROM renter WHERE renter_id = ?")) {
            stm.setInt(1, pid);
            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();
                    rid = res.getInt("renter_id");
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
