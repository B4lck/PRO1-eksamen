package model;

public class Date {
    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) {
        set(day, month, year);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getDays() {
        return day + month * 30 + year * 366;
    }

    public void set(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("%02d/%02d/%02d", day, month, year);
    }

    public Date copy() {
        return new Date(day, month, year);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj.getClass() == getClass())) {
            return false;
        }
        Date d = (Date) obj;
        return day == d.day && month == d.month && year == d.year;
    }
}
