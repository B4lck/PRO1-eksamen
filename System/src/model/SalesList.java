package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SalesList {
    private ArrayList<Sale> sales;

    /**
     * Tilføj et salg til listen
     *
     * @param animal Salget der skal tilføjes til listen
     */
    public void add(Sale sale) {
        sales.add(sale);
    }

    /**
     * Fjerner et salg via reference
     *
     * @param sale En reference til det salg der skal fjernes
     */
    public void remove(Sale sale) {
        sales.remove(sale);
    }

    public Sale getSale(int index) {
        return sales.get(index);
    }

    /**
     * Henter alle dyr i listen som et array
     */
    public Sale[] getAllSales() {
        return sales.toArray(new Sale[sales.size()]);
    }

    /**
     * Sorter listen
     *
     * @param sorting Sorteringsmetoden: Skal være price, price-reverse, date, date-reverse
     */
    public void sortBy(String sorting) {
        boolean reverse = false;
        switch (sorting) {
            case "price-reverse":
                reverse = true;
            case "price":
                sales.sort(Comparator.comparingDouble(Sale::getFinalPrice));
                break;
            case "date-reverse":
                reverse = true;
            case "date":
                sales.sort(Date.comparingDates(Sale::getDateOfSale));
                break;
            case "customer-reverse", "customer":
                throw new IllegalArgumentException("CustomerList is missing");
            case "animal-type-reverse", "animal-type", "animal-name-reverse",
                 "animal-name":
                throw new IllegalArgumentException("AnimalList is missing");
            default:
                throw new IllegalArgumentException("Invalid sort method: Valid ones are price, price-reverse, date, date-reverse; customer-reverse and customer with a customer list; and animal-type-reverse, animal-type, animal-name-reverse, animal-name with animal list");
        }

        if (reverse) {
            Collections.reverse(sales);
        }
    }

    /**
     * Sorter listen med customers
     *
     * @param sorting Sorteringsmetoden: Skal være customer-reverse eller customer
     */
    public void sortBy(String sorting, CustomerList customerList) {
        if (customerList == null) throw new IllegalArgumentException("CustomerList cannot be null!");

        boolean reverse = false;
        switch (sorting) {
            case "customer-reverse":
                reverse = true;
            case "customer":
                sales.sort((a,b) -> customerList.getCustomerById(a.getCustomerId()).getName().compareToIgnoreCase(customerList.getCustomerById(b.getCustomerId()).getName()));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort method: Valid ones are price, price-reverse, date, date-reverse; customer-reverse and customer with a customer list; and animal-type-reverse, animal-type, animal-name-reverse, animal-name with animal list");
        }

        if (reverse) {
            Collections.reverse(sales);
        }
    }

    /**
     * Sorter listen med animals
     *
     * @param sorting Sorteringsmetoden: Skal være animal-type-reverse, animal-type, animal-name-reverse eller animal-name
     */
    public void sortBy(String sorting, AnimalList animalList) {
        if (animalList == null) throw new IllegalArgumentException("AnimalList cannot be null!");

        boolean reverse = false;
        switch (sorting) {
            case "animal-type-reverse":
                reverse = true;
            case "animal-type":
                break;
            case "animal-name-reverse":
                reverse = true;
            case "animal-name":
                sales.sort((a,b) -> animalList.getAnimalById(a.getAnimalId()).getName().compareToIgnoreCase(animalList.getAnimalById(b.getAnimalId()).getName()));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort method: Valid ones are price, price-reverse, date, date-reverse; customer-reverse and customer with a customer list; and animal-type-reverse, animal-type, animal-name-reverse, animal-name with animal list");
        }

        if (reverse) {
            Collections.reverse(sales);
        }
    }

    /**
     * Returner en ny SalesList, kun med salg der har datoen
     *
     * @param date Datoen
     * @return En ny SalesList
     */
    public SalesList getSalesForDate(Date date) {
        SalesList list = new SalesList();
        for (Sale sale : sales) {
            if (sale.getDateOfSale().equals(date)) {
                list.add(sale);
            }
        }
        return list;
    }

    /**
     * Returner en ny SalesList, kun med salg der har salgsdato i perioden
     *
     * @param period Perioden
     * @return En ny SalesList
     */
    public SalesList getSalesForPeriod(DateInterval period) {
        SalesList list = new SalesList();
        for (Sale sale : sales) {
            if (period.contains(sale.getDateOfSale())) {
                list.add(sale);
            }
        }
        return list;
    }

    /**
     * Returner en ny SalesList, kun med salg der er solgt af employee
     *
     * @param employeeId Medarbejderens ID
     * @return En ny SalesList
     */
    public SalesList getSalesByEmployee(int employeeId) {
        SalesList list = new SalesList();
        for (Sale sale : sales) {
            if (sale.getEmployeeId() == employeeId) {
                list.add(sale);
            }
        }
        return list;
    }

    /**
     * Returner en ny SalesList, kun med salg der er solgt til kunden
     *
     * @param customerId Kundens ID
     * @return En ny SalesList
     */
    public SalesList getSalesForCustomer(int customerId) {
        SalesList list = new SalesList();
        for (Sale sale : sales) {
            if (sale.getCustomerId() == customerId) {
                list.add(sale);
            }
        }
        return list;
    }

    /**
     * Returner en ny SalesList, kun med salg der er solgt til en pris imellem min og max
     *
     * @param min Minimum pris
     * @param max Maximum pris
     * @return En ny SalesList
     */
    public SalesList getSalesByPrice(int min, int max) {
        SalesList list = new SalesList();
        for (Sale sale : sales) {
            if (sale.getFinalPrice() >= min && sale.getFinalPrice() <= max) {
                list.add(sale);
            }
        }
        return list;
    }

    /**
     * Henter det totale salg af alt i listen
     * @return Totale salg
     */
    public double getTotalSales() {
        double total = 0;
        for (Sale sale : sales) {
            total += sale.getFinalPrice();
        }
        return total;
    }
}
