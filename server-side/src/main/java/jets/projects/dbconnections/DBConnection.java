package jets.projects.dbconnections;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection{

    private static Connection connection;

    private DBConnection(){

    }
    public static Connection getConnection(){
        if(connection == null) {
            DataSource ds = getMySQLDataSource();
            try{
                connection = ds.getConnection();
                System.out.println("Connected to DB");
            }catch (SQLException e){
                System.out.println("Could not connect");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void stopConnection(){
        if(connection == null){
            System.out.println("No DB connection");
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static DataSource getMySQLDataSource(){
        Properties props = new Properties();
        FileInputStream fis = null;
        MysqlDataSource mysqlDS = null;
        try {
            File file = new File("src/main/java/jets/projects/dbconnections/db.properties");
            fis = new FileInputStream(file);
            props.load(fis);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }
}