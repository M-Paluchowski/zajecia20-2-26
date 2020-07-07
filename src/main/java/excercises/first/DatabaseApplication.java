package excercises.first;

import java.sql.*;
import java.util.Scanner;

public class DatabaseApplication {

    private static final String URL = "jdbc:mysql://localhost:3306/world?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private Connection connection;
    private Scanner scanner;

    public void run() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            System.err.println("Sterownik nie został znaleziony");
            return;
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException exception) {
            System.err.println("Nie udało się nawiązać połączenia");
            return;
        }

        while (true) {
            System.out.println();
            System.out.println("1. Wyświetl Polskie miasta");
            System.out.println("2. Miasta z kodem kraju");
            System.out.println("3. Kraje z danym językiem");
            System.out.println("0. Zakończ");

            scanner = new Scanner(System.in);
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    queryAndShowAllPolishCities();
                    break;
                case "2":
                    queryAndShowAllCitiesForGivenCountryCode();
                    break;
                case "3":
                    queryAndShowLanguageInformation();
                    break;
                case "0":
                    close();
                    return;
                default:
                    System.err.println("Nie ma takiej opcji");
            }
        }
    }

    private void queryAndShowLanguageInformation() {
        System.out.println("Podaj język:");
        String language = scanner.nextLine();

        try {
            String query = "SELECT Name, IsOfficial, Percentage FROM countrylanguage JOIN country ON Code = CountryCode WHERE Language = '" + language + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String isOfficial = resultSet.getString("IsOfficial");
                double percentage = resultSet.getDouble("Percentage");
                System.out.println(String.format("Name: %s, isOfficial: %s, percentage: %f", name, isOfficial, percentage));
            }
        } catch (SQLException exception) {
            System.err.println("Błąd przy zapytaniu do bazy danych");
            exception.printStackTrace();
        }
    }

    private void queryAndShowAllCitiesForGivenCountryCode() {
        System.out.println("Wpisz kod kraju:");
        String countryCode = scanner.nextLine();

        queryAndShowAllCitiesWithCountryCode(countryCode);
    }

    private void queryAndShowAllPolishCities() {
        queryAndShowAllCitiesWithCountryCode("POL");
    }

    private void queryAndShowAllCitiesWithCountryCode(String countryCode) {
        try {
            String query = "SELECT * FROM city WHERE CountryCode = '" + countryCode + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                System.out.println(name);
            }
        } catch (SQLException e) {
            System.err.println("Błąd przy zapytaniu do bazy danych");
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            connection.close();
            scanner.close();
        } catch (SQLException exception) {
            System.err.println("Błąd przy zamykaniu połączenia");
            exception.printStackTrace();
        }
    }
}
