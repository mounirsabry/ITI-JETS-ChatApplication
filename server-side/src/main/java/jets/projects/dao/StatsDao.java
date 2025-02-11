package jets.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jets.projects.classes.RequestResult;

import jets.projects.db_connections.ConnectionManager;
import jets.projects.entities.Country;
import jets.projects.entities.Gender;

public class StatsDao {

    public RequestResult<List<Integer>> getOnlineOfflineStats() {
        String query = "SELECT" +
                    " SUM(CASE WHEN status"
                    + " IN ('AVAILABLE', 'BUSY', 'AWAY')"
                    + " THEN 1 ELSE 0 END)"
                    + " AS online_count,"
                    + " SUM(CASE WHEN status = 'OFFLINE'"
                    + " THEN 1 ELSE 0 END) AS offline_count"
                    + " FROM NormalUser;";
        
        List<Integer> stats = new ArrayList<>();
        int onlineCount = 0;
        int offlineCount = 0;

        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    onlineCount = resultSet.getInt("online_count");
                    offlineCount = resultSet.getInt("offline_count");
                }
            }
            stats.add(onlineCount);
            stats.add(offlineCount);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
        return new RequestResult<>(stats, null);
    }

    public RequestResult<List<Integer>> getMaleFemaleStats() {
        String query = "SELECT gender, COUNT(*) AS count" 
                    + " FROM NormalUser"
                    + " WHERE gender IN ('MALE', 'FEMALE')"
                    + " GROUP BY gender";
        
        List<Integer> stats = new ArrayList<>();
        int maleCount = 0;
        int femaleCount = 0;

        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            
            try (ResultSet resultSet = stmt.executeQuery();) {
                while (resultSet.next()) {
                    String genderString 
                            = resultSet.getString("gender");
                    Gender gender = Gender.valueOf(genderString);
                    int count = resultSet.getInt("count");

                    if (Gender.MALE.equals(gender)) {
                        maleCount = count;
                    } else if (Gender.FEMALE.equals(gender)) {
                        femaleCount = count;
                    }
                }
            }

            stats.add(maleCount);
            stats.add(femaleCount);
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
        return new RequestResult<>(stats, null);
    }

    public RequestResult<Map<Country, Integer>> getTopCountries() {
        String query = "SELECT country, COUNT(*) AS user_count"
                    + " FROM NormalUser"
                    + " GROUP BY country"
                    + " ORDER BY user_count DESC"
                    + " LIMIT 3";
        
        Map<Country, Integer> topCountries = new HashMap<>();

        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            
            try (ResultSet resultSet = stmt.executeQuery();) {
                while (resultSet.next()) {
                    String countryString
                            = resultSet.getString("country");
                    Country country = Country.valueOf(countryString);
                    int userCount = resultSet.getInt("user_count");

                    topCountries.put(country, userCount);
                }
            }
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error :"
                    + ex.getMessage());
        }
        return new RequestResult<>(topCountries, null);
    }

//    public RequestResult<Map<String, Integer>> getAllCountries() {
//        Map<String, Integer> countries = new HashMap<>();
//
//        try (Connection connection = ConnectionManager.getConnection()) {
//            String query = "SELECT `country`, COUNT(*) AS user_count " +
//                           "FROM `User` " +
//                           "GROUP BY `country` " +
//                           "ORDER BY `country` ASC";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            while (resultSet.next()) {
//                String country = resultSet.getString("country");
//                int userCount = resultSet.getInt("user_count");
//
//                countries.put(country, userCount);
//            }
//        } catch (SQLException e) {
//            return new RequestResult<>(null,
//                    e.getMessage());
//        }
//        return new RequestResult<>(countries, null);
//    }

    public RequestResult<Integer> getCountryUsers(Country country) {
        String query = "SELECT COUNT(*) AS user_count"
                           + " FROM NormalUser"
                           + " WHERE country = ?";
        Integer userCount = 0;

        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setString(1, country.toString());

            try (ResultSet resultSet = stmt.executeQuery();) {
                if (resultSet.next()) {
                    userCount = resultSet.getInt("user_count");
                }
            }
        } catch (SQLException ex) {
            return new RequestResult<>(null, "DB Error: "
                    + ex.getMessage());
        }
        return new RequestResult<>(userCount, null);
    } 
}
