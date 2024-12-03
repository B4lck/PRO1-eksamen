package model;

public class Customer extends Person {
    /**
     * Kundens telefon nummer
     */
    private long phone;
    /**
     * Kundens mail adresse
     */
    private String email;
    /**
     * Kundens ID
     */
    private int customerId;

    /**
     * Konstruktør til kunden
     *
     * @param name  navnet på kunden
     * @param phone kundens telefon nummer
     * @param email kundens mailadresse
     * @param customerId ID'et på kunden
     */

    public Customer(String name, long phone, String email, int customerId) {
        super(name);
        setPhone(phone);
        setEmail(email);
        this.customerId = customerId;
    }

    /**
     * @return Telefon nummer på kunden
     */
    public long getPhone() {
        return phone;
    }

    /**
     * @return Mailen på kunden
     */

    public String getEmail() {
        return email;
    }

    /**
     * @return Id'et på kunden
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sætter telefonnummeret på kunden
     * @param phone kundens telefon nummer
     */
    public void setPhone(long phone) {
        this.phone = phone;
    }

    /**
     * Sætter mailen på kunden
     * @param email kundens mailadresse
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param obj Objekt der skal sammenlignes
     * @return Om objektet er ens
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Customer user = (Customer) obj;
        return getName().equals(user.getName()) && phone == user.getPhone() && email.equals(user.getEmail());
    }
}
