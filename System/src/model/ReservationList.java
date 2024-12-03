package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    /**
     * Henter reservation ud fra index
     * @param index Index
     * @return Reservation
     */
    public Reservation getReservation(int index) {
        try {
            return reservations.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Henter alle reservationer som en array
     * @return Reservation[]
     */
    public Reservation[] getAllReservations() {
        return reservations.toArray(new Reservation[reservations.size()]);
    }

    /**
     * Sortere efter ud fra datoer<br><br>
     * Gyldig sortering er "start-date-reverse", "start-date", "end-date-reverse", "end-date"
     * @param sortBy Sorterings type
     */
    public void sortBy(String sortBy) {
        boolean reverse = false;
        switch (sortBy) {
            case "start-date-reverse":
                reverse = true;
            case "start-date":
                reservations.sort(Date.comparingDates(reservation -> reservation.getPeriod().getStartDate()));
                break;
            case "end-date-reverse":
                reverse = true;
            case "end-date":
                reservations.sort(Date.comparingDates(reservation -> reservation.getPeriod().getEndDate()));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by. Valid ones are start-date-reverse, start-date, end-date-reverse, end-date");
        }

        if (reverse) {
            Collections.reverse(reservations);
        }
    }

    /**
     * Sortere ud fra dyrene i reservationerne<br><br>
     * Gyldige sorteringer er "animal-name-reverse", "animal-name", "animal-category-reverse", "animal-category", "animal-food-reverse", "animal-food"
     * @param sortBy Sorterings type
     * @param animalList Listen over dyr, hvor informationerne kommer fra
     */
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

    /**
     * Sortere ud fra kunderne i reservationerne<br><br>
     * Gyldige sorteringer er "customer-name-reverse", "customer-name", "customer-email-reverse", "customer-email"
     * @param sortBy Sorterings type
     * @param customerList Listen over kunder, hvor informationerne kommer fra
     */
    public void sortBy(String sortBy, CustomerList customerList) {
        boolean reverse = false;
        switch (sortBy) {
            case "customer-name-reverse":
                reverse = true;
            case "customer-name":
                reservations.sort((a,b) -> customerList.getCustomerById(a.getCustomerId()).getName().compareToIgnoreCase(customerList.getCustomerById(b.getCustomerId()).getName()));
                break;
            case "customer-email-reverse":
                reverse = true;
            case "customer-email":
                reservations.sort((a,b) -> customerList.getCustomerById(a.getCustomerId()).getEmail().compareToIgnoreCase(customerList.getCustomerById(b.getCustomerId()).getEmail()));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by. Valid ones are customer-name-reverse, customer-name, customer-email-reverse, customer-email");
        }

        if (reverse) {
            Collections.reverse(reservations);
        }
    }

    /**
     * Henter alle reservationer med bestemt dyr, ud fra et dyrets ID
     * @param animalId Dyret's ID
     * @return ReservationList
     */
    public ReservationList getReservationsForAnimal(int animalId) {
        ReservationList reservationList = new ReservationList();
        for (Reservation reservation : reservations) {
            if (reservation.getAnimalId() == animalId) {
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }

    /**
     * Henter alle reservationer med bestemt kunde, ud fra kundens ID
     * @param customerId Kunden's ID
     * @return ReservationList
     */
    public ReservationList getReservationsForOwner(int customerId) {
        ReservationList reservationList = new ReservationList();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomerId() == customerId) {
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }

    /**
     * Henter alle reservationer, indenfor en given periode
     * @param period DateInterval over periode
     * @return ReservationList
     */
    public ReservationList getReservationsForPeriod(DateInterval period) {
        ReservationList reservationList = new ReservationList();
        for (Reservation reservation : reservations) {
            if (reservation.getPeriod().intersects(period)) {
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }

    /**
     * Henter alle reservationer som indeholder en bestemt dato
     * @param date Dato
     * @return ReservationList
     */
    public ReservationList getReservationsForDate(Date date) {
        ReservationList reservationList = new ReservationList();
        for (Reservation reservation : reservations) {
            if (reservation.getPeriod().contains(date)) {
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }

    /**
     * Henter alle reservationer på bestemt position, ud fra et positions ID
     * @param positionId Positions ID
     * @return ReservationList
     */
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
