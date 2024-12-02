package model;

import java.time.LocalDate;

/**
 *
 * En klasse der holder styr på datoer
 *
 */
public class Date {
    /**
     * Dagen i måneden (1-30)
     */
    private int day;
    /**
     * Måneden i året (1-12)
     */
    private int month;
    /**
     * Års-tallet (ex. 2024)
     */
    private int year;

    /**
     * Konstruktør til Date
     * @param day dagen i måneden
     * @param month måneden i året
     * @param year års-tallet
     */
    public Date(int day, int month, int year) {
        set(day, month, year);
    }

    /**
     * Konstruktør til Date, hvor den opretter ud fra dags dato
     */
    public Date() {
        LocalDate date = LocalDate.now();
        set(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }

    /**
     * @return Dagen på måneden
     */
    public int getDay() {
        return day;
    }

    /**
     * @return Måneden på året
     */
    public int getMonth() {
        return month;
    }

    /**
     * @return Års-tallet
     */
    public int getYear() {
        return year;
    }

    /**
     * Retunere tiden i unix tid
     * @return Dage i unix tid
     */
    public int getDays() {
        return day + month * 30 + year * 366;
    }

    /**
     * Sætter dagen, måneden og året i Date objektet
     * @param day Dagen i måneden
     * @param month Måneden i året
     * @param year Års-tallet
     */
    public void set(int day, int month, int year) {
        // Tjek for fejl
        if (day == 0 || day > 30)       throw new IllegalArgumentException("Invalid day");
        if (month == 0 || month > 12)   throw new IllegalArgumentException("Invalid month");
        if (year < 2024)                throw new IllegalArgumentException("Invalid year");

        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Konvertere datoen til en String i formattet dd/mm/yyyy
     * @return String i formattet dd/mm/yyyy
     */
    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }

    /**
     * Retunere en kopi af objektet
     * @return En kopi af objektet
     */
    public Date copy() {
        return new Date(day, month, year);
    }

    /**
     * @param obj Objekt der skal sammenlignes
     * @return Om objekterne er ens
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj.getClass() == getClass())) {
            return false;
        }
        Date d = (Date) obj;
        return day == d.day && month == d.month && year == d.year;
    }
}
