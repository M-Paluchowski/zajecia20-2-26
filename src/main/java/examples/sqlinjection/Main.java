package examples.sqlinjection;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        //musi być utworzona baza danych "world" w MySQL
        String url = "jdbc:mysql://localhost:3306/world?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
        String username = "root";
        String password = "root";

        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj nazwe miasta");
        String cityName = scanner.nextLine();

        String selectSql = "SELECT * FROM city WHERE Name='" + cityName + "'";
        //asdf' OR 1=1;#
        //SELECT * FROM city WHERE Name='asdf' OR 1=1;#'
        //--

        //asdf'; DROP TABLE city;#
        //SELECT * FROM city WHERE Name='asdf'; DROP TABLE city;#'

//        ResultSet resultSet = statement.executeQuery(selectSql);
//        while (resultSet.next()) {
//            String name = resultSet.getString("Name");
//            System.out.println(name);
//        }

        //Poprawna wersja
        String selectSqlPrepared = "SELECT * FROM city WHERE Name=?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSqlPrepared);
        preparedStatement.setString(1, cityName);

        //'asdf' OR 1=1;#'
        ResultSet resultSet1 = preparedStatement.executeQuery();
        while (resultSet1.next()) {
            String name = resultSet1.getString("Name");
            System.out.println(name);
        }

        preparedStatement.close();
        scanner.close();
        statement.close();
        connection.close();
    }
}
