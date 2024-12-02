package model;

/**
 * En klasse der opretter en kund
 */
public class Costumer extends Person {
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
    private int costumerId;

    /**
     * Konstruktør til kunden
     *
     * @param name  navnet på kunden
     * @param phone kundens telfon nummer
     * @param email kundens mailadresse
     */

    public Costumer(String name, long phone, String email) {
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
    public int getCostumerId() {
        return costumerId;
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
        Costumer user = (Costumer) obj;
        return getName().equals(user.getName()) && phone == user.getPhone() && email.equals(user.getEmail());
    }
}
