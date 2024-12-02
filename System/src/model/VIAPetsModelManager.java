package model;

public class VIAPetsModelManager implements VIAPetsModel {

    private AnimalList animalList;
    private ReservationList reservationList;
    private SalesList salesList;
    private CustomerList customerList;
    private EmployeeList employeeList;

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
    public CustomerList getCostumerList() {
        return customerList;
    }

    @Override
    public EmployeeList getEmployeeList() {
        return employeeList;
    }
}
