package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ReservationList {
    private ArrayList<Reservation> reservations = new ArrayList<>();

    /**
     * Tilføjer en reservation til reservations listen
     * @param reservation Reservation der skal tilføjes
     */
    public void add(Reservation reservation) {
        reservations.add(reservation);
    }

    /**
     * Fjerner en reservation fra listen
     * @param reservation Reservation der skal fjernes
     */
    public void remove(Reservation reservation) {
        reservations.remove(reservation);
    }

    public Reservation getReservation(int id) {
        return reservations.get(id);
    }

    public Reservation[] getAllReservations() {
        return reservations.toArray(new Reservation[reservations.size()]);
    }

    public void sortBy(String sortBy) {
        boolean reverse = false;
        switch (sortBy) {
            case "start-date-reverse":
                reverse = true;
            case "start-date":
                reservations.sort(Comparator.comparingInt(reservation -> reservation.getPeriod().getStartDate().getDays()));
                break;
            case "end-date-reverse":
                reverse = true;
            case "end-date":
                reservations.sort(Comparator.comparingInt(reservation -> reservation.getPeriod().getEndDate().getDays()));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by. Valid ones are start-date-reverse, start-date, end-date-reverse, end-date");
        }

        if (reverse) {
            Collections.reverse(reservations);
        }
    }

    public void sortBy(String sortBy, AnimalList animalList) {
        boolean reverse = false;
        switch (sortBy) {
            case "animal-name-reverse":
                reverse = true;
            case "animal-name":
                reservations.sort((a,b) -> animalList.getAnimalById(a.getAnimalId()).getName().compareToIgnoreCase(animalList.getAnimalById(b.getAnimalId()).getName()));
                break;
            case "animal-category-reverse":
                reverse = true;
            case "animal-category":
                reservations.sort((a,b) -> animalList.getAnimalById(a.getAnimalId()).getCategory().compareToIgnoreCase(animalList.getAnimalById(b.getAnimalId()).getCategory()));
                break;
            case "animal-food-reverse":
                reverse = true;
            case "animal-food":
                reservations.sort((a,b) -> animalList.getAnimalById(a.getAnimalId()).getFood().compareToIgnoreCase(animalList.getAnimalById(b.getAnimalId()).getFood()));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by. Valid ones are animal-name-reverse, animal-name, animal-category-reverse, animal-food-reverse, animal-food");
        }
    }

    public sortBy(String sortBy, Cos)

    public ReservationList getReservationsForAnimal(int animalId) {
        ReservationList reservationList = new ReservationList();
        for (Reservation reservation : reservations) {
            if (reservation.getAnimalId() == animalId) {
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }

    public ReservationList getReservationsForOwner(int customerId) {
        ReservationList reservationList = new ReservationList();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomerId() == customerId) {
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }

    public ReservationList getReservationsForPeriod(DateInterval period) {
        ReservationList reservationList = new ReservationList();
        for (Reservation reservation : reservations) {
            if (reservation.getPeriod().intersects(period)) {
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }

    public ReservationList getReservationsForDate(Date date) {
        ReservationList reservationList = new ReservationList();
        for (Reservation reservation : reservations) {
            if (reservation.getPeriod().contains(date)) {
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }

    public ReservationList getReservationsForPosition(int positionId) {
        ReservationList reservationList = new ReservationList();
        for (Reservation reservation : reservations) {
            if (reservation.getPositionId() == positionId) {
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }
}
