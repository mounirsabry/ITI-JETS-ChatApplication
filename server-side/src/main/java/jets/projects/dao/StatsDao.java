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

import jets.projects.dbconnections.ConnectionManager;

public class StatsDao {

    public RequestResult<List<Integer>> getOnlineOfflineStats() {
        List<Integer> stats = new ArrayList<>();
        int onlineCount = 0;
        int offlineCount = 0;

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT " +
                           "SUM(CASE WHEN `status` IN ('AVAILABLE', 'BUSY', 'AWAY') THEN 1 ELSE 0 END) AS online_count, " +
                           "SUM(CASE WHEN `status` = 'OFFLINE' THEN 1 ELSE 0 END) AS offline_count " +
                           "FROM `User`";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                onlineCount = resultSet.getInt("online_count");
                offlineCount = resultSet.getInt("offline_count");
            }

            stats.add(onlineCount);
            stats.add(offlineCount);

        } catch (SQLException e) {
            return new RequestResult<>(null,
                    e.getMessage());
        }

        return new RequestResult<>(stats, null);
    }


    public RequestResult<List<Integer>> getMaleFemaleStats() {
        List<Integer> stats = new ArrayList<>();
        int maleCount = 0;
        int femaleCount = 0;

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT `gender`, COUNT(*) AS count " +
                           "FROM `User` " +
                           "WHERE `gender` IN ('M', 'F') " +
                           "GROUP BY `gender`";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int count = resultSet.getInt("count");

                if ("M".equals(gender)) {
                    maleCount = count;
                } else if ("F".equals(gender)) {
                    femaleCount = count;
                }
            }

            
            stats.add(maleCount);
            stats.add(femaleCount);

        } catch (SQLException e) {
            return new RequestResult<>(null,
                    e.getMessage());
        }

        return new RequestResult<>(stats, null);
    }

     public RequestResult<Map<String, Integer>> getTopCountries() {
        Map<String, Integer> topCountries = new HashMap<>();

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT `country`, COUNT(*) AS user_count " +
                           "FROM `User` " +
                           "GROUP BY `country` " +
                           "ORDER BY user_count DESC " +
                           "LIMIT 3";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String country = resultSet.getString("country");
                int userCount = resultSet.getInt("user_count");

                topCountries.put(country, userCount);
            }

        } catch (SQLException e) {
            return new RequestResult<>(null,
                    e.getMessage());
        }

        return new RequestResult<>(topCountries, null);
    }

    public RequestResult<Map<String, Integer>> getAllCountries() {
        Map<String, Integer> countries = new HashMap<>();

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT `country`, COUNT(*) AS user_count " +
                           "FROM `User` " +
                           "GROUP BY `country` " +
                           "ORDER BY `country` ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String country = resultSet.getString("country");
                int userCount = resultSet.getInt("user_count");

                countries.put(country, userCount);
            }

        } catch (SQLException e) {
            return new RequestResult<>(null,
                    e.getMessage());
        }

        return new RequestResult<>(countries, null);
    }

    public RequestResult<Integer> getCountryUsers(String countryName) {
        Integer userCount = 0;

        try (Connection connection = ConnectionManager.getConnection()) {
            String query = "SELECT COUNT(*) AS user_count " +
                           "FROM `User` " +
                           "WHERE `country` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

        
            preparedStatement.setString(1, countryName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userCount = resultSet.getInt("user_count");
            }

        } catch (SQLException e) {
            return new RequestResult<>(null,
                    e.getMessage());
        }

        return new RequestResult<>(userCount, null);
    }
    
}
