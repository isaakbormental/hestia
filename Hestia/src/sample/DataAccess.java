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

    private Connection getConnection() throws SQLException {
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
    private List<Integer> getIdApartementrented(int idRenter) throws SQLException {
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
                        des.append("Crime Rating : " + res.getDouble("crime_rating"));
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
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO renter (renter_id,rating) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
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
            stmt.setInt(4, user);
            stmt.setDouble(5, apartment.getSize());
            stmt.setInt(2, apartment.getNumberRoom());
            stmt.setDouble(6, apartment.getPrice());
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
        System.out.println("description" );
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("SELECT DISTINCT A.size,A.price,A.num_of_rooms,L.district,L.city,L.crime_rating FROM" +
                     " apartment A NATURAL JOIN building B NATURAL JOIN location L WHERE A.buildin_id=B.building_id and B.lid=L.location_id and A.price<? AND L.city=?")) {
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
                    str.append("District: " + dis + " \n" + "City :" + cit + " Crime Rating: " + ra);
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

//     public  List<Apartment> getApartment(String location,double price,double size){
//         List<Apartment> list=new ArrayList<>();
//         System.out.println("description" );
//         try (Connection connection = getConnection();
//              PreparedStatement stm = connection.prepareStatement("SELECT  A.size,A.price,A.num_of_rooms,L.district,L.city,L.crime_rating FROM" +
//                      " apartment A NATURAL JOIN building B NATURAL JOIN location L WHERE A.buildin_id=B.building_id and B.lid=L.location_id and  A.price<? AND L.city=? and A.size>?" )) {
//             stm.setDouble(1, price);
//             stm.setString(2, location);
//             stm.setDouble(3, size);
//             try {
//                 ResultSet res = stm.executeQuery();
//                 while (res.next()) {
//                     StringBuilder str = new StringBuilder();
//                     double s = res.getDouble("size");
//                     double p = res.getDouble("price");
//                     int n = res.getInt("num_of_rooms");
//                     String dis = res.getString("district");
//                     String cit = res.getString("city");
//                     double ra = res.getDouble("crime_rating");
//                     str.append("District: " + dis + " \n" + "City :" + cit + " Crime Rating" + ra);
//                     list.add(new Apartment(n, s, p, str.toString()));
//                     System.out.println("description" + str.toString());
//
//                 }
//                // res.close();
//                 stm.close();
//             } catch (SQLException st) {
//                 st.printStackTrace();
//
//             }
//         }catch (SQLException ex){
//
//             }
//
//
//
//
//         return list;
//     }

//    public  List<Apartment> getApartment(String location,double price,double size,double distance){
//        List<Apartment> list=new ArrayList<>();
//        try (Connection connection = getConnection();
//             PreparedStatement stm = connection.prepareStatement("SELECT DISTINCT A.size,A.price,A.num_of_rooms,L.district,L.city,L.crime_rating FROM" +
//                     " apartment A NATURAL JOIN building B NATURAL JOIN location L , institution I WHERE A.buildin_id=B.building_id and B.lid=L.location_id and A.price<? AND L.city=? and A.size>?" +
//                     "and |/((B.longitude - I.longitude)*(B.longitude - I.longitude) + (B.latitude - I.latitude)*(B.latitude - I.latitude)) <=? and I.location_id=L.location_id" )) {
//            stm.setDouble(1, price);
//            stm.setString(2, location);
//            stm.setDouble(3, size);
//            stm.setDouble(4, distance);
//            try {
//                ResultSet res = stm.executeQuery();
//               // System.out.print(stm.execute());
//                System.out.print(res.next());
//                while (res.next()) {
//                    StringBuilder str = new StringBuilder();
//                    double s = res.getDouble("size");
//                    double p = res.getDouble("price");
//                    int n = res.getInt("num_of_rooms");
//                    String dis = res.getString("district");
//                    String cit = res.getString("city");
//                    double ra = res.getDouble("crime_rating");
//                    str.append("District: " + dis + " \n" + "City :" + cit + " Crime Rating" + ra);
//                    //list.add(new Apartment(n, s, p, str.toString()));
//                    list.add(new Apartment(n, s, p, str.toString()));
//                    System.out.println("description" + str.toString());
//                }
//               // res.close();
//                stm.close();
//            } catch (SQLException st) {
//                st.printStackTrace();
//
//            }
//        }
//        catch (SQLException ex){
//
//            }
//
//        return list;
//    }


    public List<Apartment> getApartment(String location, double price, double size, double distance, String facility) throws SQLException {
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
                        "      AND a.size >= ? ";
                di++;
            }
            if(distance>0){
                query= query + " intersect select a.price,a.size, lo.city, lo.crime_rating, lo.district, a.apart_id " +
                        "from apartment a, location lo, building bi " +
                        "where a.buildin_id in (select B.building_id " +
                        "from building B, institution I " +
                        "where B.lid = I.location_id " +
                        "AND 6371*2*atan2(sqrt(sin((I.latitude-B.latitude)*pi()/360) * sin((I.latitude-B.latitude)*pi()/360) + " +
                        "cos(B.latitude*pi()/180) * cos(I.latitude*pi()/180) * " +
                        "sin((I.longitude-B.longitude)*pi()/360) * sin((I.longitude-B.longitude)*pi()/360)), sqrt(1-(sin((I.latitude-B.latitude)*pi()/360) * sin((I.latitude-B.latitude)*pi()/360) + " +
                        "cos(B.latitude*pi()/180) * cos(I.latitude*pi()/180) * " +
                        "sin((I.longitude-B.longitude)*pi()/360) * sin((I.longitude-B.longitude)*pi()/360)))) <= ? " +
                        "AND I.type_institution = ? )" +
                        "AND a.buildin_id=bi.building_id " +
                        "AND bi.lid=lo.location_id; ";
            }

            PreparedStatement stm = connection.prepareStatement(query);
            if(!location.equals("default")){stm.setString(l, location);}
            if(price>0){stm.setDouble(pr, price);}
            if(size>0){stm.setDouble(si, size);}
            if(distance>0){stm.setDouble(di, distance); stm.setString(di+1, facility);}

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
                    int aid = res.getInt("apart_id");
                    str.append("District: " + dis + " \n" + "City :" + cit + " Crime Rating: " + ra);
                    Apartment a = new Apartment(s, p, str.toString());
                    a.setAid(aid);
                    a.setRating(getAverageApartmentRating(aid));
                    a.setOid(findOwner(aid));
                    a.setNameOwner(getPersonNameById(a.getOid()));
                    list.add(a);
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
                PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT  lastname,firstname ,RA.date_begin,RA.date_end ,L.city, L.district ,L.crime_rating,A.price,A.size,A.num_of_rooms,A.oid,A.apart_id " +
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
                    //My additions
                    int oi = resultSet.getInt("oid");
                    int ai = resultSet.getInt("apart_id");

                    String fname = resultSet.getString("firstname");
                    String lname = resultSet.getString("lastname");
                    String loca = resultSet.getString("city");
                  //  Owner person = new Owner(fname, lname);
                    Person person = new Person(fname, lname, oi);
                    StringBuilder str = new StringBuilder();
                    double s = resultSet.getDouble("size");
                    double p = resultSet.getDouble("price");
                    int n = resultSet.getInt("num_of_rooms");
                    String dis = resultSet.getString("district");
                    String cit = resultSet.getString("city");
                    double ra = resultSet.getDouble("crime_rating");
                    str.append("District: " + dis + " \n" + "City :" + cit + " Crime Rating: " + ra);
                    Date date_be=resultSet.getDate("date_begin");
                    Date date_en=resultSet.getDate("date_end");
                    String duration="From :"+date_be.toString().concat(" to: "+date_en.toString());
                    //list.add(new Apartment(person,n,s,p,str.toString(),duration));
                    Apartment ap = new Apartment(ai,person,n,s,p,str.toString(),duration);
                    ap.setLocation(loca);
                    list.add(ap);

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
                PreparedStatement stmt=con.prepareStatement("SELECT DISTINCT  lastname,firstname ,RA.date_begin,RA.date_end ,L.city, L.district, P.person_id, RA.apart_id " +
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
                    Person person=new Person(fname,lname);
                    StringBuilder str = new StringBuilder();
                    String dis = resultSet.getString("district");
                    String cit = resultSet.getString("city");
                    Date date_be=resultSet.getDate("date_begin");
                    Date date_en=resultSet.getDate("date_end");
                    String duration="From :"+date_be.toString().concat(" to: "+date_en.toString());
                    str.append("District: " + dis + " \n" + "City :" + cit  + " \n" );
                    Apartment apa = new Apartment(person,str.toString(),duration);
                    apa.setAid(resultSet.getInt("apart_id"));
                    apa.setOid(ownerid);
                    int rid = resultSet.getInt("person_id");
                    apa.setRid(rid);
                    apa.setNameOwner(lname+" "+ fname);
                    list.add(apa);

                }
                resultSet.close();
            }catch (SQLException ext){
               System.out.println(ext.getMessage()); ext.printStackTrace();
            }

            stmt.close();
        }
        return list;
    }

    public int findOwner(int aid) throws SQLException{
        ArrayList<Apartment> list=new ArrayList<>();
        int owner = 0;
        try(
                Connection con=getConnection();
                PreparedStatement stmt=con.prepareStatement("SELECT p.person_id FROM person p, apartment a WHERE a.oid = p.person_id AND a.apart_id = ? ;")
        ){
            stmt.setInt(1,aid);
            try {
                ResultSet resultSet= stmt.executeQuery();
                while (resultSet.next()){
                    owner = resultSet.getInt("person_id");
                }
                resultSet.close();
            }catch (SQLException ext){
                System.out.println(ext.getMessage()); ext.printStackTrace();
            }

            stmt.close();
        }
        return owner;
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

    public Hashtable<Integer, String> getPersonById(int id) throws SQLException {
        Hashtable<Integer, String> person = new Hashtable<Integer, String>();
        int pid = 0;
        String name = "";
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("SELECT person_id, firstname,lastname FROM person WHERE person_id = ? ")) {
            stm.setInt(1, id);

            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();

                    pid = res.getInt("person_id");
                    name = res.getString("firstname") + " " + res.getString("lastname");
                    person.put(pid, name);

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

    public String getPersonNameById(int id) throws SQLException {

        String name = "";
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement("SELECT person_id, firstname,lastname FROM person WHERE person_id = ? ")) {
            stm.setInt(1, id);

            // int row = stm.executeUpdate();
            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();

                    name = res.getString("firstname") + " " + res.getString("lastname");

                }
                res.close();
            } catch (SQLException st) {
                st.printStackTrace();

            } finally {
                stm.close();
            }


        }

        return name;
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
   public List<Message> getall(int receiverId){
       List<Message> list =new ArrayList<>();
         try(Connection con=getConnection();
             PreparedStatement stmt=con.prepareStatement(" SELECT  firstname,lastname, message,date,person_id " +
                     " FROM person P, message M WHERE P.person_id=M.sender_id and M.reciever_id=?")){
             stmt.setInt(1,receiverId);
             ResultSet res=stmt.executeQuery();
             try {
                 while (res.next()){
                     int id=res.getInt("person_id");
                     String firsname=res.getString("firsname");
                     String lastname=res.getString("lastname");
                     String mes=res.getString("message");
                     Date d=res.getDate("date");
                     Message msg=new Message(firsname,lastname,id,mes,d);

                     list.add(msg);
                 }
                 res.close();
             }catch (SQLException esp){
              System.out.println(esp.getMessage());
             }
             stmt.close();
         }catch (SQLException ex){

         }
       return list;
   }


    public List<Message> findMsgs(int reciever, int sender) throws SQLException {
        List<Message> list = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String query = " SELECT message, sender_id,reciever_id,msg_number,date FROM message WHERE reciever_id = ? AND sender_id = ? "+
            " UNION SELECT message, sender_id,reciever_id,msg_number,date FROM message WHERE reciever_id = ? AND sender_id = ? " +
            " ORDER BY msg_number ";

            PreparedStatement stm = connection.prepareStatement(query);

            stm.setInt(1, reciever);
            stm.setInt(2, sender);
            stm.setInt(3, sender);
            stm.setInt(4, reciever);


            try {
                ResultSet res = stm.executeQuery();
                while (res.next()) {
                    StringBuilder str = new StringBuilder();
                    int send = res.getInt("sender_id");
                    int rec = res.getInt("reciever_id");
                    String msg = res.getString("message");
                    Date data = res.getDate("date");
                    System.out.println("Date: "+data);
                    //str.append("District: " + dis + " \n" + "City :" + cit + " Crime Rating" + ra);
                    Message msgs = new Message(msg,rec,send,data);
                    msgs.setName(getPersonNameById(send));
                    list.add(msgs);
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



    public void addMessage(String msg, int reciever, int sender, int numb) throws SQLException {
        try (
                Connection connection = getConnection();    //2016-01-28
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO message (message, reciever_id, sender_id, date,msg_number) VALUES (?,?,?,'2016-12-28',?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(2, reciever);
            stmt.setInt(3, sender);
            stmt.setInt(4, numb);
            stmt.setString(1, msg);

            stmt.executeUpdate();
            stmt.close();
        }
    }

    public void rateOwner(int idperson,double rating){
        try(            Connection con=getConnection();
            PreparedStatement stmt=con.prepareStatement("insert into owner (rating)VALUE (rating)")){
            try {
                stmt.executeQuery();

             }catch (SQLException ex){}

        }catch (SQLException ex){

        }

    }

    public void addApartmentRate(double rating, int rated, int rater) throws SQLException {
        try (
                Connection connection = getConnection();    //2016-01-28
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO renter_rate_owner (renter_rate, apartment_id, renter_id) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(2, rated);
            stmt.setInt(3, rater);
            stmt.setDouble(1, rating);

            stmt.executeUpdate();

            stmt.close();
        }
    }

    public void addRequest(int apartment, int sender, int reciever) throws SQLException {
        try (
                Connection connection = getConnection();    //2016-01-28
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO request (apart_id, sender_id, reciever_id) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(2, sender);
            stmt.setInt(3, reciever);
            stmt.setInt(1, apartment);

            stmt.executeUpdate();

            stmt.close();
        }
    }

    public void addRenterRate(double rating, int renter, int owner) throws SQLException {
        try (
                Connection connection = getConnection();    //2016-01-28
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO owner_rate_renter (owner_rate, renter_id, owner_id) VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(2, renter);
            stmt.setInt(3, owner);
            stmt.setDouble(1, rating);

            stmt.executeUpdate();

            stmt.close();
        }
    }

    public void deleteRequest(int apartment, int requester, int owner) throws SQLException {
        try (
                Connection connection = getConnection();    //2016-01-28
                PreparedStatement stmt = connection.prepareStatement("DELETE FROM request WHERE apart_id = ? AND reciever_id = ? AND sender_id = ? ;", Statement.RETURN_GENERATED_KEYS)) {
            System.out.println(apartment+" " +owner+" "+requester);
            stmt.setInt(1, apartment);
            stmt.setInt(2, owner);
            stmt.setInt(3, requester);

            stmt.executeUpdate();

            stmt.close();
        }
    }

    public void approveRequest(int apartment, int requester, int owner) throws SQLException {
        deleteRequest(apartment,requester,owner);
        try (
                Connection connection = getConnection();    //2016-01-28
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO renter_apart (apart_id,renter_id,owner_id, date_begin,date_end) VALUES (?,?,?,'2016-05-25','2017-08-25'); ", Statement.RETURN_GENERATED_KEYS)) {
            System.out.println(apartment+" " +owner+" "+requester);
            stmt.setInt(1, apartment);
            stmt.setInt(3, owner);
            stmt.setInt(2, requester);

            stmt.executeUpdate();

            stmt.close();
        }
    }

    public double getAverageApartmentRating(int rated) throws SQLException {
        double rating = 0;
        try (
                Connection connection = getConnection();    //2016-01-28
                PreparedStatement stmt = connection.prepareStatement("SELECT AVG(renter_rate) as average From renter_rate_owner WHERE apartment_id = ?;", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rated);
            try {
                ResultSet res = stmt.executeQuery();
                while (res.next()) {

                    rating = res.getDouble("average");

                }
                res.close();
            } catch (SQLException st) {
                st.printStackTrace();

            } finally {
                stmt.close();
            }
            //stmt.executeUpdate();
            //stmt.close();

        }
        return rating;
    }

    public double getAverageRenterRating(int renter) throws SQLException {
        double rating = 0;
        try (
                Connection connection = getConnection();    //2016-01-28
                PreparedStatement stmt = connection.prepareStatement("SELECT AVG(owner_rate) as average From owner_rate_renter WHERE renter_id = ?; ", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, renter);
            try {
                ResultSet res = stmt.executeQuery();
                while (res.next()) {

                    rating = res.getDouble("average");

                }
                res.close();
            } catch (SQLException st) {
                st.printStackTrace();

            } finally {
                stmt.close();
            }
            //stmt.executeUpdate();
            //stmt.close();

        }
        return rating;
    }

    public List<Request> getRequests(int ownerid) throws SQLException{
        ArrayList<Request> list=new ArrayList<>();
        try(
                Connection con=getConnection();
                PreparedStatement stmt=con.prepareStatement(" SELECT p.firstname, p.lastname, p.person_id as requester, req.apart_id, ap.price, ap.size, loc.city, loc.district " +
                        " From person p, request req, apartment ap, location loc, building b " +
                        " WHERE p.person_id IN (SELECT req.sender_id " +
                        " WHERE ap.apart_id =req.apart_id " +
                        " AND ap.buildin_id = b.building_id " +
                        " AND b.lid = loc.location_id " +
                        " AND reciever_id = ?);")
        ){
            stmt.setInt(1,ownerid);
            try {
                ResultSet resultSet= stmt.executeQuery();
                while (resultSet.next()){

                    String fname=resultSet.getString("firstname");
                    String lname=resultSet.getString("lastname");
                    int requester = resultSet.getInt("requester");
                    Person person=new Person(fname,lname,requester);
                    double rat = getAverageRenterRating(requester);
                    StringBuilder str = new StringBuilder();
                    String dis = resultSet.getString("district");
                    String cit = resultSet.getString("city");
                    double siz = resultSet.getDouble("size");
                    double pri = resultSet.getDouble("price");
                    int apa = resultSet.getInt("apart_id");

                    str.append("District: " + dis + " \n" + "City :" + cit  + " \n" + "Price: "+ pri + " Size: " +siz);
                    Request reque = new Request(apa, person, ownerid , str.toString());
                    reque.setRating(rat);
                    list.add(reque);

                }
                resultSet.close();
            }catch (SQLException ext){
                System.out.println(ext.getMessage()); ext.printStackTrace();
            }

            stmt.close();
        }
        return list;
    }


}
