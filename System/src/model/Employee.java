package model;

/**
 * En klasse der opretter medarbejder
 */
public class Employee extends Person {
    /**
     * medarbejderen beskrivelse
     */
    private String description;
    /**
     * Url med et billede af medarbejderen
     */
    private String portraitUrl;
    /**
     * medarbejderen ID
     */
    private int employeeId;

    /**
     * Konstruktør til medarbejder
     *
     * @param name navnet på medarbejder
     */

    public Employee(String name) {
        super(name);
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

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
