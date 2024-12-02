package model;

import java.util.ArrayList;

public class CustomerList {
    private ArrayList<Customer> customerList =new ArrayList<>();

    public void addCustomer(Customer customer) {
        this.customerList.add(customer);
    }

    public void removeCustomer(Customer customer) {
        this.customerList.remove(customer);
    }

    public void removeCustomerById(int customer) {
        for (Customer c : this.customerList) {
            if (c.getCustomerId() == customer) {
                this.customerList.remove(c);
            }
        }
    }

    public Customer getCustomerById(int customer) {
        for (Customer c : this.customerList) {
            if (c.getCustomerId() == customer) {
                return c;
            }
        }
        return null;
    }

    public Customer[] getAllCustomers() {
        return this.customerList.toArray(new Customer[this.customerList.size()]);
    }
    //sortBY skal laves

    public CustomerList getCustomerByPhone(long phone) {
       CustomerList list = new CustomerList();
        for (Customer c : this.customerList) {
            if (c.getPhone() == phone) {
                list.addCustomer(c);
            }
        }
        return list;
    }
    public CustomerList getCustomerByName(String name) {
        CustomerList list = new CustomerList();
        for (Customer c : this.customerList) {
            if (c.getName() == name) {
                list.addCustomer(c);
            }
        }
        return list;
    }
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
