package model;

import java.util.ArrayList;

public class CustomerList {
    /**
     * Liste over alle kunder
     */
    private final ArrayList<Customer> customers = new ArrayList<>();
    
    public void clear() {
        customers.clear();
    }


    /**
     * Opretter en ny kunde, der bagefter kan lægges i customer listen
     * @param name Navn på kunde
     * @param phone Telefon nr. på kunde
     * @param email Kundens email adresse
     * @return Et nyt kunde objekt
     */
    public Customer createNewCustomer(String name, long phone, String email) {
        return new Customer(name, phone, email, getUniqueId());
    }

    /**
     * Hent et nyt id
     */
    public int getUniqueId() {
        int highestId = 0;
        for (Customer customer : customers) {
            if (customer.getCustomerId() > highestId) highestId = customer.getCustomerId();
        }
        return highestId + 1;
    }

    /**
     * Tilføjer kunde til liste
     * @param customer Kunde
     */
    public void add(Customer customer) {
        this.customers.add(customer);
    }

    /**
     * Fjerner kunde ud fra objekt
     * @param customer Kunde objekt
     */
    public void remove(Customer customer) {
        this.customers.remove(customer);
    }

    /**
     * Fjerner kunde ud fra ID
     * @param customer Kunde ID
     */
    public void removeById(int customer) {
        for (Customer c : this.customers) {
            if (c.getCustomerId() == customer) {
                this.customers.remove(c);
                break;
            }
        }
    }

    /**
     * Henter kunde ud fra ID
     * @param customer Kunde ID
     * @return Customer
     */
    public Customer getById(int customer) {
        for (Customer c : this.customers) {
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
        return this.customers.toArray(new Customer[0]);
    }
    //sortBY skal laves

    /**
     * Henter alle kunder ud fra deres tlf nummer
     * @param phone Tlf nummer
     * @return CustomerList
     */
    public CustomerList getCustomersByPhone(long phone) {
        String phoneStr = Long.toString(phone);
        CustomerList list = new CustomerList();
        for (Customer c : this.customers) {
            if (Long.toString(c.getPhone()).contains(phoneStr)) {
                list.add(c);
            }
        }
        return list;
    }

    /**
     * Søger og returnere alle kunder hvor `name` indgår i deres navn
     * @param name Kunde navn
     * @return CustomerList
     */
    public CustomerList getCustomersByName(String name) {
        name = name.toLowerCase();
        CustomerList list = new CustomerList();
        for (Customer c : this.customers) {
            if (c.getName().toLowerCase().contains(name)) {
                list.add(c);
            }
        }
        return list;
    }

    /**
     * Søger og returnere alle kunder hvor `email` indgår i deres email
     * @param email Kunde email
     * @return CustomerList
     */
    public CustomerList getCustomersByEmail(String email) {
        email = email.toLowerCase();
        CustomerList list = new CustomerList();
        for (Customer c : this.customers) {
            if (c.getEmail().toLowerCase().contains(email)) {
                list.add(c);
            }
        }
        return list;
    }

    public ArrayList<Customer> getList() {return this.customers;}
}
