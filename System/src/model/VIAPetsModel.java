package model;

/**
 * Modellen
 */
public interface VIAPetsModel {
    AnimalList getAnimalList();
    ReservationList getReservationList();
    SalesList getSalesList();
    CustomerList getCustomerList();
    EmployeeList getEmployeeList();
    void save();
}
