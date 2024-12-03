package model;

/**
 * En klasse der opretter medarbejder
 */
public class Employee extends Person {
    /**
     * Medarbejderens beskrivelse
     */
    private String description;
    /**
     * Url med et billede af medarbejderen
     */
    private String portraitUrl;
    /**
     * Medarbejderens ID
     */
    private int employeeId;

    /**
     * Konstruktør til medarbejder
     *
     * @param name navnet på medarbejder
     */

    public Employee(String name, int employeeId) {
        super(name);
        this.employeeId = employeeId;
    }

    /**
     * @return beskrivelse på medarbejderen
     */

    public String getDescription() {
        return description;
    }

    /**
     * Sætter en beskrivelse om medarbejderen
     *
     * @param description beskrivelse om medarbejder
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return linket til billedet af medarbejder
     */

    public String getPortraitUrl() {
        return portraitUrl;
    }

    /**
     * sætter url til billedet
     *
     * @param portraitUrl URL med billedet af medarbejderen
     */
    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    /**
     * @return medarbejderen ID
     */

    public int getEmployeeId() {
        return employeeId;
    }

    /**
     * @param obj Objekt skal sammenlignes
     * @return Om objektet er ens
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee user = (Employee) obj;
        return getName().equals(user.getName()) && description.equals(user.description) && portraitUrl.equals(user.portraitUrl);
    }
}
