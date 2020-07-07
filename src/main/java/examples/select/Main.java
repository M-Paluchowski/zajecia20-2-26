package examples.select;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        //musi być utworzona baza danych "world" w MySQL
        String url = "jdbc:mysql://localhost:3306/world?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
        String username = "root";
        String password = "root";

        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM city";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString("Name");
            String countryCode = resultSet.getString(3);
            System.out.println(id + " " + name + " " + countryCode);
        }

        statement.close();
        connection.close();
    }
}
