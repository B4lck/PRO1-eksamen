package model;

public class VIAPetsModelManager implements VIAPetsModel {

    private final AnimalList animalList;
    private final ReservationList reservationList;
    private final SalesList salesList;
    private final CustomerList customerList;
    private final EmployeeList employeeList;

    public VIAPetsModelManager() {
        animalList = new AnimalList();
        reservationList = new ReservationList();
        salesList = new SalesList();
        customerList = new CustomerList();
        employeeList = new EmployeeList();
    }

    @Override
    public AnimalList getAnimalList() {
        return animalList;
    }

    @Override
    public ReservationList getReservationList() {
        return reservationList;
    }

    @Override
    public SalesList getSalesList() {
        return salesList;
    }

    @Override
    public CustomerList getCustomerList() {
        return customerList;
    }

    @Override
    public EmployeeList getEmployeeList() {
        return employeeList;
    }
}
