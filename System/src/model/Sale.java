package model;

import java.util.Objects;

public class Sale {
    /**
     * Endelige pris af salget
     */
    private double finalPrice;
    /**
     * Dyret's ID
     */
    private int animalId;
    /**
     * Kunden's ID
     */
    private int customerId;
    /**
     * Medarbejderen der betjente's ID
     */
    private int employeeId;
    /**
     * Dato for salget
     */
    private Date dateOfSale;

    /**
     * Konstruktør for salg
     * @param finalPrice Pris
     * @param animalId Dyret's ID
     * @param customerId Kunden's ID
     * @param employeeId Medarbejderen's ID
     * @param dateOfSale Dato for salget
     */
    public Sale(double finalPrice, int animalId, int customerId, int employeeId, Date dateOfSale) {
        this.set(finalPrice, animalId, customerId, employeeId, dateOfSale);
    }

    /**
     * Hent datoen for salget
     * @return Date
     */
    public Date getDateOfSale() {
        return dateOfSale.copy();
    }

    /**
     * Hent den endelige pris
     * @return Pris
     */
    public double getFinalPrice() {
        return finalPrice;
    }

    /**
     * Hent dyret's ID
     * @return Dyret's ID
     */
    public int getAnimalId() {
        return animalId;
    }

    /**
     * Hent kunden's ID
     * @return Kunden's ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Hent medarbejderen's ID
     * @return Medarbejderen's ID
     */
    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * Sæt alle variabler og tjekker for fejl
     * @param finalPrice Pris
     * @param animalId Dyret's ID
     * @param customerId Kunden's ID
     * @param employeeId Medarbejderen's ID
     * @param dateOfSale Datoen for salg
     */
    public void set(double finalPrice, int animalId, int customerId, int employeeId, Date dateOfSale) {
        if (dateOfSale == null) throw new IllegalArgumentException("dateOfSale cannot be null");
        this.finalPrice = finalPrice;
        this.animalId = animalId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.dateOfSale = dateOfSale.copy();
    }

    /**
     * Sammenlign med andet objekt, og tjek om de er ens
     * @param o Objekt
     * @return Om objekterne er ens
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Double.compare(finalPrice, sale.finalPrice) == 0 && animalId == sale.animalId && customerId == sale.customerId && employeeId == sale.employeeId && Objects.equals(dateOfSale, sale.dateOfSale);
    }

    /**
     * Skriver objektet om til HashCode
     * @return Objektet i HashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(finalPrice, animalId, customerId, employeeId, dateOfSale);
    }
}
