package model;

public class DateInterval {
    /**
     * Start dato
     */
    private Date startDate;
    /**
     * Slut dato
     */
    private Date endDate;

    /**
     * Konstruktør for Date Interval
     * @param startDate Start dato
     * @param endDate Slut dato
     */
    public DateInterval(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Input can't be null");
        }
        set(startDate, endDate);
    }

    /**
     * @return Start Dato
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @return Slut Dato
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Retunere længden af intervallet i antal dage
     * @return Længden af intervallet i antal dage
     */
    public int getLengthInDays() {
        return startDate.daysBetween(endDate);
    }

    /**
     * Få antal år mellem 2 datoer
     * @return Antal år mellem datoer
     */
    public int getYearsBetween() {
        return getLengthInDays() / 365;
    }

    /**
     * Sæt start dato og slut dato i Date Interval objektet
     * @param startDate
     * @param endDate
     */
    public void set(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Sæt start dato
     * @param startDate start dato
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Sæt slut dato
     * @param endDate slut dato
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Tjekker om 2 Date Intervaller overlapper på datoer
     * @param interval Interval at sammenligne med
     * @return Om den overlapper eller ikke overlapper
     */
    public boolean intersects(DateInterval interval) {
        if (this.startDate == interval.endDate) return true;
        if (this.endDate == interval.startDate) return true;
        if (this.endDate.isBefore(interval.startDate)) return false;
        if (interval.endDate.isBefore(this.startDate)) return false;
        return true;
    }

    /**
     * Tjekker om en bestemt dato er inde i dato intervallet
     * @param date Dato at tjekke
     * @return Om den er inde i intervallet eller ikke
     */
    public boolean contains(Date date) {
        return this.startDate.equals(date) || this.endDate.equals(date) || (this.startDate.isBefore(date) && date.isBefore(this.endDate));
    }

    /**
     * Retunere en String med formattet dd/mm/yyyy - dd/mm/yyyy
     * @return Formatteret String
     */
    @Override
    public String toString() {
        return startDate.toString() + " - " + endDate.toString();
    }

    /**
     * Tjekker om objektet er det samme som et andet objekt
     * @param obj Objektet der skal sammenlignes
     * @return Om objektet er det samme
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj.getClass() ==  getClass())) {
            return false;
        }
        DateInterval other = (DateInterval) obj;
        return startDate.equals(other.getStartDate()) && endDate.equals(other.getEndDate());
    }

    /**
     * Retunere en kopi af Date Interval objektet
     * @return en kopi af Date Interval
     */
    public DateInterval copy() {
        return new DateInterval(startDate, endDate);
    }
}