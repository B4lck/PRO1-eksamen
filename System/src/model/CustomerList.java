package model;

import java.util.ArrayList;

public class CustomerList {
    /**
     * Liste over alle kunder
     */
    private ArrayList<Customer> customerList = new ArrayList<>();

    /**
     * Tilføjer kunde til liste
     * @param customer Kunde
     */
    public void addCustomer(Customer customer) {
        this.customerList.add(customer);
    }

    /**
     * Fjerner kunde ud fra objekt
     * @param customer Kunde objekt
     */
    public void removeCustomer(Customer customer) {
        this.customerList.remove(customer);
    }

    /**
     * Fjerner kunde ud fra ID
     * @param customer Kunde ID
     */
    public void removeCustomerById(int customer) {
        for (Customer c : this.customerList) {
            if (c.getCustomerId() == customer) {
                this.customerList.remove(c);
            }
        }
    }

    /**
     * Henter kunde ud fra ID
     * @param customer Kunde ID
     * @return Customer
     */
    public Customer getCustomerById(int customer) {
        for (Customer c : this.customerList) {
            if (c.getCustomerId() == customer) {
                return c;
            }
        }
        return null;
    }

    /**
     * Henter listen som Array
     * @return Customer[]
     */
    public Customer[] getAllCustomers() {
        return this.customerList.toArray(new Customer[this.customerList.size()]);
    }
    //sortBY skal laves

    /**
     * Henter alle kunder ud fra deres tlf nummer
     * @param phone Tlf nummer
     * @return CustomerList
     */
    public CustomerList getCustomerByPhone(long phone) {
        CustomerList list = new CustomerList();
        for (Customer c : this.customerList) {
            if (c.getPhone() == phone) {
                list.addCustomer(c);
            }
        }
        return list;
    }

    /**
     * Henter alle kunder ud fra navn
     * @param name Kunde navn
     * @return CustomerList
     */
    public CustomerList getCustomerByName(String name) {
        CustomerList list = new CustomerList();
        for (Customer c : this.customerList) {
            if (c.getName() == name) {
                list.addCustomer(c);
            }
        }
        return list;
    }

    /**
     * Henter alle kunder ud fra deres email
     * @param email Kunde email
     * @return CustomerList
     */
    public CustomerList getCustomerByEmail(String email) {
        CustomerList list = new CustomerList();
        for (Customer c : this.customerList) {
            if (c.getEmail() == email) {
                list.addCustomer(c);
            }
        }
        return list;
    }
}
