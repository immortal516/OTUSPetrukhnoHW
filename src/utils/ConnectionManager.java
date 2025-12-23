package utils;

import java.sql.*;

public class ConnectionManager implements AutoCloseable {

    private final String connection_url = System.getenv("url");
    private final String login = System.getenv("login");
    private final String password = System.getenv("password");

    private Connection connection;
    private Statement statement;

    public ConnectionManager() {
        try {
            connection = DriverManager.getConnection(connection_url, login, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void executeUpdate(String query) {
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            if (statement != null && !statement.isClosed()) statement.close();
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}