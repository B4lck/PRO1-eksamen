package model;

import java.util.ArrayList;
import java.util.Collections;

public class EmployeeList {
    /**
     * Liste over alle employees
     */
    private final ArrayList<Employee> employees = new ArrayList<>();
    
    public void clear() {
        employees.clear();
    }

    /**
     * Opretter en ny medarbejder, der bagefter kan lægges i medarbejder listen
     * @param name Navn på medarbejder
     * @return Et nyt medarbejder objekt
     */
    public Employee createNewEmployee(String name) {
        return new Employee(name, getUniqueId());
    }

    /**
     * Hent et nyt id
     */
    public int getUniqueId() {
        int highestId = 0;
        for (Employee employee : employees) {
            if (employee.getEmployeeId() > highestId) highestId = employee.getEmployeeId();
        }
        return highestId + 1;
    }

    /**
     * Tilføjer en ny employee til listen
     * @param employee Employee
     */
    public void add(Employee employee) {
        this.employees.add(employee);
    }

    /**
     * Fjerner en employee fra listen, ud fra objektet
     * @param employee Employee objekt
     */
    public void remove(Employee employee) {
        employees.remove(employee);
    }

    /**
     * Fjerner en employee fra listen, ud fra deres ID
     * @param id ID
     */
    public void removeById(int id) {
        Employee employee = null;
        for (Employee e : employees) {
            if (e.getEmployeeId() == id) {
                employee = e;
            }
        }
        if (employee != null) {employees.remove(employee);}
    }

    /**
     * Henter en employee fra listen ud fra deres ID
     * @param employeeId ID
     * @return Employee
     */
    public Employee getById(int employeeId) {
        for (Employee e : employees) {
            if (e.getEmployeeId() == employeeId) {
                return e;
            }
        }
        return null;
    }

    /**
     * Henter alle employees og laver den om til array
     * @return Employee[]
     */
    public Employee[] getAllEmployees() {
        return employees.toArray(new Employee[0]);
    }

    /**
     * Henter Employee ud fra deres navn
     * @param name Navn
     * @return Employee
     */
    public Employee getEmployeeByName(String name) {
        for (Employee employee : employees) {
            if (employee.getName().equals(name)) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Sortere listen<br><br>
     * Gyldig sortering er "employee-name-reversed" og "employee-name"
     * @param sortBy Sorterings type
     */
    public void sortBy(String sortBy) {
        boolean reverse = false;
        switch (sortBy) {
            case "employee-name-reverse":
                reverse = true;
            case "employee-name":
                employees.sort((a,b) -> a.getName().compareToIgnoreCase(b.getName()));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by. Valid ones are employee-name-reverse, employee-name");
        }

        if (reverse) {
            Collections.reverse(employees);
        }
    }


    public ArrayList<Employee> getList() {return employees;}
}
