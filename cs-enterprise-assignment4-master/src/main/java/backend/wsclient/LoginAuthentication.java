
package backend.wsclient;


import model.Person;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.io.InputStream;

public class LoginAuthentication {

    public static void main(String [] args) {
        Connection conn = null;

        //connect to data source and create a connection instance
        //read backend.db credentials from properties file
        InputStream is = null;
        Properties props = new Properties();
        BufferedInputStream propsFile = null;
        String updateStatement = "UPDATE PEOPLE_TABLE SET LAST_NAME  = ? WHERE ID =  ?";
        String insert = "insert into PEOPLE_TABLE (ID, FIRST_NAME, LAST_NAME, DOB) values (?, ?, ?, ?)";
        String select = "SELECT * FROM PEOPLE_TABLE";
        String LOGIN = "SELECT ID WHERE  FIRST_NAME = ? AND LAST_NAME = ? ";
        try {
            //read backend.db credentials from properties file
            String  sessionId = AuthenticationGateWay.authenticate( "ragnar", "flapjacks");
            // dont need to check validity of sessionId as all other web service calls will check that

            // switch to the dog view
            // we will create a new stage but don't do this in your assignment


            ArrayList<Person> people =  FetchPeople.fetchAll();
            System.out.println(people);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

}
