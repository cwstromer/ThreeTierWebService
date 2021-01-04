package backend.db;

import model.Person;
import model.PersonAudit;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class PersonGatewayMySQL {
    private Connection connection;
    public PersonGatewayMySQL(Connection connection) {
        this.connection = connection;
    }

    // fetch a single person's  with the given id
    public ArrayList<PersonAudit> fetchPersonAudit(int id) throws GatewayException, SQLException, IOException, IndexOutOfBoundsException {
        String  FETCH_QUERY = "SELECT * FROM AUDIT";
        String  findPerson = "SELECT * from people where id = ? ";
        PreparedStatement st;
        PreparedStatement st2;
        ResultSet rs ;
        ResultSet as ;
        ArrayList <PersonAudit> people = new ArrayList<>();

        try{
            st = this.connection.prepareStatement(FETCH_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
//            st.setInt(1, id);
            st.executeQuery();
            rs  = st.getResultSet();

            while ( rs.next()) {
                PersonAudit personAudit = new PersonAudit( rs.getInt("id"),rs.getString("change_msg"),rs.getInt("changed_by"), rs.getInt("person_id"), rs.getString("when_occurred"), "" );

                people.add(personAudit);
            }

//             modifying the name personAudit
            for ( PersonAudit p : people ) {
                st2 = this.connection.prepareStatement(findPerson, PreparedStatement.RETURN_GENERATED_KEYS);
                st2.setInt(1,p.getPerson_id());
                st2.executeQuery();
                as = st2.getResultSet();
                System.out.println(p.getPerson_id());
                while ( as.next()) {
                    p.setName (as.getString("first_name"));
                    System.out.println(p.getName());
                }
            }
            System.out.println(people.get(0));
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return people;
    }

    // fetch everyone in the people table
    public ArrayList <Person> fetchListPeople() throws GatewayException, SQLException, IOException, IndexOutOfBoundsException {
        String  FETCH_QUERY = "SELECT * FROM people";

        PreparedStatement st;
        ResultSet rs ;
        ArrayList <Person> people = new ArrayList<>();

        try{
            st = this.connection.prepareStatement(FETCH_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);

            st.executeQuery();
            rs  = st.getResultSet();

            while ( rs.next()) {
                people.add(new Person( rs.getInt("ID"),rs.getString("FIRST_NAME"),rs.getString("LAST_NAME"), rs.getString("date_of_birth") ));
            }
            System.out.println(people);

        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        st.close();

        return people;
    }

    // fetch everyone in the people table
    public String postLogin(String username, String password) throws GatewayException, SQLException, IOException, IndexOutOfBoundsException {
        String  FETCH_QUERY = "SELECT * FROM login where username = ? and password = ?";
        String sessionID = "";
        PreparedStatement st;
        ResultSet rs;
        ArrayList <Person> people = new ArrayList<>();

        try{
            st = this.connection.prepareStatement(FETCH_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, username);
            st.setString(2,password);
            st.executeQuery();
            rs  = st.getResultSet();
            if (rs.next()) {
                sessionID = rs.getString(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        st.close();

        return sessionID;
    }

    // delete a single person with the given id
    public int deletePerson(int id) throws GatewayException, SQLException, IOException, IndexOutOfBoundsException {
        String  FETCH_QUERY = "DELETE FROM people where id = ?";

        PreparedStatement st;
        ResultSet rs ;

        try{
            st = this.connection.prepareStatement(FETCH_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setInt(1, id);
            int affectedRows = st.executeUpdate();
            System.out.println(affectedRows);
            if  ( affectedRows == 0 ) {
                return 404;
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        st.close();

        return 200;
    }


    public int updatePerson(Map<String, String> user) throws GatewayException, SQLException, IOException, IndexOutOfBoundsException, ParseException {



//        if ( !checkKeys(user) ) {
//            return 400;
//        }



        Person person= fetchPerson( Integer.parseInt(user.get("id"))).get(0);

        String UPDATE_QUERY = "UPDATE people SET last_name = ?, first_name = ? , date_of_birth = ?  WHERE ID =  ?";;

        PreparedStatement st;
        ResultSet rs;


        try {

            st = this.connection.prepareStatement(UPDATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);

            String first_name = (user.containsKey("first_name") && user.get("first_name").length() > 1 )? user.get("first_name"): person.getFirstName();
            String last_name = (user.containsKey("last_name") && user.get("last_name").length() > 1) ? user.get("last_name"): person.getLastName();
            String dob = (user.containsKey("date_of_birth")&& user.get("date_of_birth").length() > 1 )? user.get("date_of_birth"): person.getDateOfBirth();




            System.out.println(user.keySet());
            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
            Date input = sdformat.parse(dob);
            Date today = new Date();
            if (input.compareTo(today) == 0 ||input.compareTo(today) > 0 ) {
                System.out.print("The Date of Birth is not valid ");
                return 404;
            }


            st.setString(1, last_name);
            st.setString(2,first_name);
            st.setString(3,dob);
            st.setInt(4, Integer.parseInt(user.get("id")));


            int affectedRows = st.executeUpdate();
            System.out.println(affectedRows);
            if (affectedRows == 0) {
                return 404;
            }
            if (affectedRows < 0) {
                return 400;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        st.close();
        return 200 ;
    }

    // fetch everyone in the people table
    public int insertPerson(String id, String first_name, String last_name, String date_of_birth) throws GatewayException, SQLException, IOException, IndexOutOfBoundsException {
        String  FETCH_QUERY = "insert into people (id, first_name, last_name, date_of_birth) values (?, ?, ?, ?)";
        PreparedStatement st;
        ResultSet rs;
        ArrayList <Person> people = new ArrayList<>();

        try{
            st = this.connection.prepareStatement(FETCH_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setInt(1, Integer.parseInt(id));
            st.setString(2, first_name);
            st.setString(3, last_name);
            st.setString(4, date_of_birth);
            int affectedRows = st.executeUpdate();
            System.out.println(affectedRows);
            if (affectedRows == 0) {
                return 400;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        st.close();

        return 200;
    }

    // fetch a single person with the given id
    public ArrayList <Person> fetchPerson(int id) throws GatewayException, SQLException, IOException, IndexOutOfBoundsException {
        String  FETCH_QUERY = "SELECT * FROM people where id = ?";
        PreparedStatement st;
        ResultSet rs ;
        ArrayList <Person> people = new ArrayList<>();

        try{
            st = this.connection.prepareStatement(FETCH_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setInt(1, id);
            st.executeQuery();
            rs  = st.getResultSet();
            while ( rs.next()) {
                people.add(new Person( rs.getInt("ID"),rs.getString("FIRST_NAME"),rs.getString("LAST_NAME"), rs.getString("date_of_birth") ));
            }
            System.out.println(people);

        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return people;
    }

    public void insertLogs ( String command, int  person_id, HashMap <String, List<String>> fieldName) throws SQLException {
        if ( command.equalsIgnoreCase("new") ) {
            String sql = "INSERT INTO AUDIT (change_msg, changed_by, person_id) VALUES (?,?,?)";
            PreparedStatement st;
            ResultSet rs;
            st = this.connection.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS);

            st.setString(1, "added");
            st.setInt(2, 0 );
            st.setInt(3,person_id);
            st.executeUpdate();
            st.close();
        }
        if ( command.equalsIgnoreCase("update")) {
            String sql = "INSERT INTO AUDIT (change_msg, changed_by, person_id) VALUES (?,?,?)";
            PreparedStatement st;
            ResultSet rs;
            System.out.println(fieldName);
            for (String key : fieldName.keySet()) {
                if (! fieldName.get(key).get(0).equalsIgnoreCase(fieldName.get(key).get(1))){
                    st = this.connection.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    System.out.println(key + " changed from " + fieldName.get(key).get(0) + " to " + fieldName.get(key).get(1) );
                    st.setString(1, key + " changed from " + fieldName.get(key).get(0) + " to " + fieldName.get(key).get(1) );
                    st.setInt(2, 0 );
                    st.setInt(3,person_id);
                    st.executeUpdate();
                    st.close();
                }

            }

        }
    }
    public Boolean checkKeys (Map<String, String> values) {
        List<String> keys  =  Arrays.asList("first_name", "last_name", "date_of_birth", "id");
        for ( String i :values.keySet()) {
            if (!keys.contains(i)) {
                return false;
            }

        }
        return true;
    }

}
