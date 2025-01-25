package jets.projects.dbconnections;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final String SQL_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String PROPERTIES_FILE = "/db_info.properties";
    
    private static String URL;
    private static String USER_NAME;
    private static String PASSWORD;
    
    public static boolean initDeviceManager() {
        try {
            Class.forName(SQL_CLASS);
        } catch (ClassNotFoundException ex) {
            System.err.println("""
                            Could not find the database class name.
                            With the name: """ + SQL_CLASS);
            return false;
        }
        
        InputStream inputStream = ConnectionManager.class.
                getResourceAsStream(PROPERTIES_FILE);
        if (inputStream == null) {
            System.err.println("Could not find the " + PROPERTIES_FILE
                    + " in the resources directory.");
            return false;
        }
        
        Properties dbInfo = new Properties();
        try {
            dbInfo.load(inputStream);
        } catch (IOException ex) {
            System.err.println("""
                            Could not load the properties correctly
                            From the properties file.""");
            return false;
        }
        
        URL = dbInfo.getProperty("URL");
        USER_NAME = dbInfo.getProperty("USER_NAME");
        PASSWORD = dbInfo.getProperty("PASSWORD");
        
        System.out.println("Initial DB connection test.");
        int testResult = testConnection();
        switch (testResult) {
            case 0 -> {
                System.err.println("""
                                Could not open a connection with DB, check if it exists,
                                and that your credientals are correct.""");
                return false;
            }
            case 1 -> { 
                System.err.println("Connection opened but could not be closed.");
                return false;
            }
            default -> System.out.println("Connection opened and closed successfully.");
        }
        return true;
    }
    
    public static int testConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException ex) {
            return 0;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                return 1;
            }
        }
        return 2;
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException ex) {
            System.err.println("Could not open a connection with the DB.");
            ex.printStackTrace();
        }
        return connection;
    }
}
