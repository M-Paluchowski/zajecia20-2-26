package excercises.second;

import java.sql.*;
import java.util.Scanner;

public class DatabaseUpdateApp {

    private static final String URL = "jdbc:mysql://localhost:3306/world?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Connection connection;
    private Scanner scanner;

    public void run() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            System.err.println("Błąd przy wczytywaniu sterownika");
            return;
        }

        try {
             connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Błąd przy nawiązywaniu połączenia");
            return;
        }

        scanner = new Scanner(System.in);

        System.out.println("Podaj nazwę miasta");
        String cityName = scanner.nextLine();

        System.out.println("Podaj liczbę ludności");
        int population = scanner.nextInt();
        scanner.nextLine();

        try {
            updateCity(cityName, population);
            ResultSet resultSet = selectCity(cityName);
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                int cityPopulation = resultSet.getInt("Population");
                System.out.println(String.format("Name: %s, Population: %d", name, cityPopulation));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        close();
    }

    private ResultSet selectCity(String cityName) throws SQLException {
        PreparedStatement selectQuery = connection.prepareStatement("SELECT * FROM city WHERE Name=?");
        selectQuery.setString(1, cityName);
        return selectQuery.executeQuery();
    }

    private void updateCity(String cityName, int population) throws SQLException {
        PreparedStatement updateQuery = connection.prepareStatement("UPDATE city SET Population = ? WHERE Name = ?");
        updateQuery.setInt(1, population);
        updateQuery.setString(2, cityName);
        int changed = updateQuery.executeUpdate();

        System.out.println("Zaktualizowano: " + changed + " rekordów");
    }

    private void close() {
        try {
            scanner.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
