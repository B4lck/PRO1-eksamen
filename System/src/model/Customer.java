package model;

/**
 * En klasse der opretter en kund
 */
public class Customer extends Person {
    /**
     * kundens telefon nummer
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
     * @param phone kundens telfon nummer
     * @param email kundens mailadresse
     */

    public Customer(String name, long phone, String email) {
        super(name);
        setPhone(phone);
        setEmail(email);
    }

    /**
     * @return telefon nummer på kunden
     */
    public long getPhone() {
        return phone;
    }

    /**
     * @return mailen på kunden
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
     * sætter telefonnummeret i objektet
     *
     * @param phone kundens telefon nummer
     */
    public void setPhone(long phone) {
        this.phone = phone;
    }

    /**
     * sætter mailen i objektet
     *
     * @param email kundens mailadresse
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param obj Objekt skal sammenlignes
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
