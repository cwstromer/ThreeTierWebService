
package backend.wsclient;

import com.mysql.cj.jdbc.MysqlDataSource;
import model.Person;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class UpdatePeople {


    public static int update (String  id , String  firstname, String lastName , String dob) {
        Connection conn = null;
        ResultSet rs ;
        String sessionID = " ";
        PreparedStatement st;
        ArrayList<Person> people = new ArrayList<>();
        //connect to data source and create a connection instance
        //read backend.db credentials from properties file
        InputStream is = null;
        Properties props = new Properties();
        BufferedInputStream propsFile = null;

        String  UPDATE_QUERY =  "UPDATE PEOPLE_TABLE SET LAST_NAME  = ?, FIRST_NAME = ? , DOB = ?  WHERE ID =  ?";

        try {
            //read backend.db credentials from properties file
            is = new FileInputStream("src/resources/backend.db.properties");
            propsFile = new BufferedInputStream(is);
            props.load(propsFile);
            propsFile.close();

            //create the datasource
            MysqlDataSource ds = new MysqlDataSource();
            ds.setURL(props.getProperty("MYSQL_DB_URL"));
            ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));

            // Establish connection
            conn = ds.getConnection();

            //
            st  = conn.prepareStatement(UPDATE_QUERY,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, lastName);
            st.setString(2,firstname);
            st.setString(3,dob);
            st.setInt(4,Integer.parseInt(id));
            int affectedRows = st.executeUpdate();
            System.out.println(affectedRows);
            if  ( affectedRows == 0 ) {
                return 401;

            }

            st.close();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        return 200;
    }

}
