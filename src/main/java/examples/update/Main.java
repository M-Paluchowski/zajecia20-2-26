package examples.update;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        //musi być utworzona baza danych "world" w MySQL
        String url = "jdbc:mysql://localhost:3306/world?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
        String username = "root";
        String password = "root";

        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        String insert = "INSERT INTO city(Name, CountryCode, District, Population) VALUES ('Psinka Dolna', 'POL', 'Dolny Slask', 1234)";
        int quantity = statement.executeUpdate(insert);
        System.out.println(quantity);

        statement.close();
        connection.close();
    }
}
