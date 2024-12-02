package model;

public class VIAPetsModelManager implements VIAPetsModel {

    private AnimalList animalList;
    private ReservationList reservationList;
    private SalesList salesList;
    private CostumerList costumerList;
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
    public CostumerList getCostumerList() {
        return costumerList;
    }

    @Override
    public EmployeeList getEmployeeList() {
        return employeeList;
    }
}
