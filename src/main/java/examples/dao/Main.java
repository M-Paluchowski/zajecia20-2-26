package examples.dao;


import java.util.List;

public class Main {
    public static void main(String[] args) {
        Employee employee = new Employee("Karol", "Nowak", 10000);
        EmployeeDao employeeDao = new EmployeeDao();
        employeeDao.create(employee);
//        employeeDao.delete(1L);
//        employeeDao.read(2L)
//                .ifPresent(System.out::println);

//        employee.setId(2L);
//        employee.setFirstName("Jan");
//        employeeDao.update(employee);

        employeeDao.updateWithFunction(List.of(2L), optionalEmployee -> {
            Employee employee1 = optionalEmployee.orElseThrow(IllegalArgumentException::new);
            employee1.setSalary(11000);
            return employee1;
        });
        employeeDao.close();
    }
}
