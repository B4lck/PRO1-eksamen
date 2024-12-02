package model;

import java.util.Objects;

public class Sale {
    private double finalPrice;
    private int animalId;
    private int costumerId;
    private int employeeId;
    private Date dateOfSale;

    public Sale(double finalPrice, int animalId, int costumerId, int employeeId, Date dateOfSale) {
        this.set(finalPrice, animalId, costumerId, employeeId, dateOfSale);
    }

    public Date getDateOfSale() {
        return dateOfSale;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public int getAnimalId() {
        return animalId;
    }

    public int getCostumerId() {
        return costumerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void set(double finalPrice, int animalId, int costumerId, int employeeId, Date dateOfSale) {
        if (dateOfSale == null) throw new IllegalArgumentException("dateOfSale cannot be null");
        this.finalPrice = finalPrice;
        this.animalId = animalId;
        this.costumerId = costumerId;
        this.employeeId = employeeId;
        this.dateOfSale = dateOfSale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Double.compare(finalPrice, sale.finalPrice) == 0 && animalId == sale.animalId && costumerId == sale.costumerId && employeeId == sale.employeeId && Objects.equals(dateOfSale, sale.dateOfSale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(finalPrice, animalId, costumerId, employeeId, dateOfSale);
    }
}
