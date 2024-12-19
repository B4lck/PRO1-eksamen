package model;

/**
 * En reservation til pensionen.
 */
public class Reservation {
    /**
     * Id'et på terrariet/akvariet/fugleburet, -1 hvis det ikke har en position
     */
    private int positionId;
    /**
     *Id'et på kunden
     */
    private int customerId;
    /**
     * Id'et på dyret
     */
    private int animalId;
    /**
     * Dato interval for perioden over reservation
     */
    private DateInterval period;

    /**
     * Konstruktør for Reservation
     * @param customerId Id'et på kunden
     * @param animalId Id'et på dyret
     * @param period Dato Interval med start of slut dato
     */
    public Reservation(int customerId, int animalId, DateInterval period) {
        this.positionId = -1;
        this.customerId = customerId;
        this.animalId = animalId;
        this.period = period;
    }

    /**
     * @return Kunden's ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return Dyret's ID
     */
    public int getAnimalId() {
        return animalId;
    }

    /**
     * @return Akvariet/Terrariet/Fugleburet's ID, -1 hvis ingen position
     */
    public int getPositionId() {
        return positionId;
    }

    /**
     * @return En kopi af perioden som DateInterval
     */
    public DateInterval getPeriod() {
        return period.copy();
    }

    /**
     * Sætter kunden's ID og dyret's ID
     * @param customerId Kunden's ID
     * @param animalId Dyret's ID
     */
    public void set(int customerId, int animalId) {
        this.customerId = customerId;
        this.animalId = animalId;
    }

    /**
     * Sætter position for reservationen
     * @param positionId Position ID
     */
    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    /**
     * Sætter perioden for reservationen
     * @param period Periode som DateInterval
     */
    public void setPeriod(DateInterval period) {
        this.period = period.copy();
    }

    /**
     * Tjekker om objektet er det samme som et andet objekt
     * @param obj Objektet der skal sammenlignes
     * @return Om objektet er det samme
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj.getClass() == getClass())) {
            return false;
        }
        Reservation r = (Reservation) obj;
        return positionId == r.positionId && customerId == r.customerId && animalId == r.animalId && period.equals(r.period);
    }
}
