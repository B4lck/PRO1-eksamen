package model;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * En klasse der holder styr på datoer
 */
public class Date {
    /**
     * Dagen i måneden (1-31)
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
     *
     * @param day   dagen i måneden
     * @param month måneden i året
     * @param year  års-tallet
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
     * (til sammenligning af dato) En funktion der laver T om til et date objekt
     * @param <T>
     */
    @FunctionalInterface
    public interface DateFunction<T> {
        Date getDate(T a);
    }

    /**
     * Sammenligner datoer
     */
    public static <T> Comparator<T> comparingDates(@NotNull DateFunction<? super T> tToDate) {
        return (T a, T b) -> {
            Date aDate = tToDate.getDate(a);
            Date bDate = tToDate.getDate(b);
            if (aDate.equals(bDate)) return 0;
            return aDate.isBefore(bDate) ? -1 : 1;
        };
    }

    /**
     * Sætter dagen, måneden og året i Date objektet
     *
     * @param day   Dagen i måneden
     * @param month Måneden i året
     * @param year  Års-tallet
     */
    public void set(int day, int month, int year) {
        this.year = Math.abs(year); // Vi tager ikke imod 2000+ år gamle dyr
        this.month = Math.clamp(month, 1, 12);
        this.day = Math.clamp(day, 1, numberOfDaysInMonth());
    }

    /**
     * Returner antal dage i den nuværende måned
     */
    public int numberOfDaysInMonth() {
        return switch (this.month) {
            case 2 -> isLeapYear() ? 29 : 28;
            case 4, 6, 9, 11 -> 30;
            default -> 31;
        };
    }

    /**
     * Er det nuværende år et skudår?
     */
    public boolean isLeapYear() {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * Tjekker om en anden dato er før den nuværende dato
     *
     * @param other Dato til sammenligning
     * @return Om dato til sammenligning er før den nuværende dato
     */
    public boolean isBefore(Date other) {
        if (year < other.year) {
            return true;
        } else if (year == other.year && month < other.month) {
            return true;
        } else if (year == other.year && month == other.month && day < other.day) {
            return true;
        }

        return false;
    }

    /**
     * Antal år imellem den nuværende og en anden dato
     *
     * @param other Den anden dato
     * @return Antal år
     */
    public int yearsBetween(Date other) {
        return month < other.month || month == other.month && day < other.day ? year - other.year - 1 : year - other.year;
    }

    /**
     * Antal dage imellem den nuværende og en anden dato
     *
     * @param other Den anden dato
     * @return Antal dage
     */
    public int daysBetween(Date other) {
        Date upper;
        Date lower;

        if (this.equals(other)) return 0;

        if (this.isBefore(other)) {
            upper = other;
            lower = this.copy();
        } else {
            upper = this;
            lower = other.copy();
        }

        int days = 0;

        while (upper.year > lower.year || upper.month > lower.month) {
            days += lower.numberOfDaysInMonth();
            month += 1;
            if (month > 12) {
                month = 1;
                year += 1;
            }
        }

        return days + upper.day - lower.day;
    }

    /**
     * Konvertere datoen til en String i formattet dd/mm/yyyy
     *
     * @return String i formattet dd/mm/yyyy
     */
    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }

    /**
     * Retunere en kopi af objektet
     *
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
