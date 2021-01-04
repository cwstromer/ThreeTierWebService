package backend.wsclient;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class AuthenticationGateWay {

    public static String authenticate(String username , String password ) throws SQLException {
        Connection conn = null;
        ResultSet rs ;
        String sessionID = " ";
        PreparedStatement st;
        //connect to data source and create a connection instance
        //read backend.db credentials from properties file
        InputStream is = null;
        Properties props = new Properties();
        BufferedInputStream propsFile = null;
        String updateStatement = "UPDATE PEOPLE_TABLE SET LAST_NAME  = ? WHERE ID =  ?";
        String insert = "insert into PEOPLE_TABLE (ID, FIRST_NAME, LAST_NAME, DOB) values (?, ?, ?, ?)";
        String select = "SELECT * FROM PEOPLE_TABLE";



        String LOGIN = "SELECT ID FROM PEOPLE_TABLE WHERE  USER_NAME =  ?  AND PASSWORD = ? ";
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
            st  = conn.prepareStatement(LOGIN,
                    PreparedStatement.RETURN_GENERATED_KEYS);


            st.setString(1, username);
            st.setString(2,password);
            st.executeQuery();
            rs  = st.getResultSet();
            if (rs.next()) {
                sessionID = rs.getString(1);
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
        return sessionID;
    }

}
