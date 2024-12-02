package model;

import java.util.ArrayList;

public class EmployeeList {
    private ArrayList<Employee> employees;

    public EmployeeList(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public void remove(Employee employee) {
        employees.remove(employee);
    }

    public void removeById(int id) {
        for (Employee employee : employees) {
            if (employee.getEmployeeId() == id) {
                employees.remove(employee);
            }
        }
    }

    public int getById(int employeeId) {
        for (Employee employee : employees) {
            if (employee.getEmployeeId() == employeeId) {
                return employee.getEmployeeId();
            }
        }
        return employeeId;
    }

    public Employee[] getAllEmployees() {
        return employees.toArray(new Employee[employees.size()]);
    }

    public Employee getEmployee(String sortBy) {
        for (Employee employee : employees) {
            //waiting for animalList
        }
        return null;
    }

    public Employee getEmployeeByName(String name) {
        for (Employee employee : employees) {
            //Waiting
        }
        return null;
    }
}
