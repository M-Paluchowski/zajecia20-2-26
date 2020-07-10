package examples.dao;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class EmployeeDao {
    private static final String URL = "jdbc:mysql://localhost:3306/javastart?characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static final String INSERT_QUERY = "INSERT INTO employee(first_name, last_name, salary) VALUES (?, ?, ?)";
    private static final String SELECT_QUERY = "SELECT id, first_name, last_name, salary FROM employee WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE employee SET first_name = ?, last_name = ?, salary = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM employee WHERE id = ?";

    private Connection connection;

    public void updateWithFunction(final List<Long> ids, final Function<Optional<Employee>, Employee> updateFunction) {
        ids.stream()
                .map(this::read)
                .map(updateFunction)
                .forEach(this::update);
    }

    public EmployeeDao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException exception) {
            System.err.println("Błąd podczas ładowania sterownika");
        } catch (SQLException exception) {
            System.err.println("Błąd podczas nawiązywania połączenia z bazą danych");
        }
    }

    //C - create
    public void create(Employee employee) {
        try {
            PreparedStatement createSql = connection.prepareStatement(INSERT_QUERY);
            createSql.setString(1, employee.getFirstName());
            createSql.setString(2, employee.getLastName());
            createSql.setDouble(3, employee.getSalary());
            createSql.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    //R - read
    public Optional<Employee> read(long id) {
        try {
            PreparedStatement selectSql = connection.prepareStatement(SELECT_QUERY);
            selectSql.setLong(1, id);

            ResultSet resultSet = selectSql.executeQuery();
            if (resultSet.next()) {
                long idFromDatabase = resultSet.getLong("id");
                String firstNameFromDatabase = resultSet.getString("first_name");
                String lastNameFromDatabase = resultSet.getString("last_name");
                double salary = resultSet.getDouble("salary");
                Employee employee = new Employee(idFromDatabase, firstNameFromDatabase, lastNameFromDatabase, salary);
                return Optional.of(employee);
            } else {
                return Optional.empty();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return Optional.empty();
    }

    //U - update
    public void update(Employee employee) {
        try {
            PreparedStatement updateSql = connection.prepareStatement(UPDATE_QUERY);
            updateSql.setString(1, employee.getFirstName());
            updateSql.setString(2, employee.getLastName());
            updateSql.setDouble(3, employee.getSalary());
            updateSql.setLong(4, employee.getId());
            updateSql.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    //D - delete
    public void delete(long id) {
        try {
            PreparedStatement deleteSql = connection.prepareStatement(DELETE_QUERY);
            deleteSql.setLong(1, id);
            deleteSql.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            System.err.println("Błąd podczas zamykania połączenia");
            exception.printStackTrace();
        }
    }
}
