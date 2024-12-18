package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AnimalList {
    private final ArrayList<Animal> animals = new ArrayList<>();

    /**
     * Opretter et helt nyt dyr, der bagefter skal lægges i listen.
     * Brug set metoderne for dyret til at indstille resten af felterne.
     *
     * @param category Dyrets kategori.
     * @param name     Dyrets navn.
     * @param price    Dyrets pris.
     * @return Animal objekt eller objekt der nedarver animal efter kategori.
     */
    public Animal createNewAnimal(String category, String name, double price) {
        int id = getUniqueId();
        return switch (category) {
            case Animal.CATEGORY_BIRD -> new AnimalBird(name, price, id);
            case Animal.CATEGORY_FISH -> new AnimalFish(name, price, id);
            case Animal.CATEGORY_REPTILE -> new AnimalReptile(name, price, id);
            default -> new Animal(name, price, id);
        };
    }


    /**
     * Opretter et helt nyt dyr, der bagefter skal lægges i listen.
     * Brug set metoderne for dyret til at indstille resten af felterne.
     *
     * @param category Dyrets kategori.
     * @param name     Dyrets navn.
     * @param ownerId  Dyrets ejer.
     * @return Animal objekt eller objekt der nedarver animal efter kategori.
     */
    public Animal createNewAnimal(String category, String name, int ownerId) {
        int id = getUniqueId();
        return switch (category) {
            case Animal.CATEGORY_BIRD -> new AnimalBird(name, ownerId, id);
            case Animal.CATEGORY_FISH -> new AnimalFish(name, ownerId, id);
            case Animal.CATEGORY_REPTILE -> new AnimalReptile(name, ownerId, id);
            default -> new Animal(name, ownerId, id);
        };
    }

    /**
     * Hent et nyt id
     */
    public int getUniqueId() {
        int highestId = 0;
        for (Animal animal : animals) {
            if (animal.getAnimalId() > highestId) highestId = animal.getAnimalId();
        }
        return highestId + 1;
    }

    /**
     * Tøm animal list
     */
    public void clear() {
        animals.clear();
    }

    /**
     * Tilføj et dyr til animal listen
     *
     * @param animal Dyret der skal tilføjes til listen
     */
    public void add(Animal animal) {
        animals.add(animal);
    }

    /**
     * Fjerner et dyr via reference
     *
     * @param animal En reference til det dyr der skal fjernes
     */
    public void remove(Animal animal) {
        animals.remove(animal);
    }

    /**
     * Fjerner et dyr via id
     *
     * @param id ID'et på det dyr der skal fjernes
     */
    public void removeById(int id) {
        animals.remove(getAnimalById(id));
    }

    /**
     * Returner en reference til et dyr via dens id
     *
     * @param id ID'et på dyret
     * @return En reference til dyrets klasse
     */
    public Animal getAnimalById(int id) {
        for (Animal animal : animals) {
            if (animal.getAnimalId() == id) {
                return animal;
            }
        }
        return null;
    }

    /**
     * Henter alle dyr i listen som et array
     */
    public Animal[] getAllAnimals() {
        return animals.toArray(new Animal[0]);
    }

    /**
     * Sorter listen
     *
     * @param sorting Sorteringsmetoden: Skal være name, name-reverse, price, price-reverse, category, category-reverse, creation, creation-reverse, age, age-reverse
     */
    public void sortBy(String sorting) {
        boolean reverse = false;
        switch (sorting) {
            case "name-reverse":
                reverse = true;
            case "name":
                animals.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
                break;
            case "price-reverse":
                reverse = true;
            case "price":
                animals.sort(Comparator.comparingDouble(Animal::getPrice));
                break;
            case "category-reverse":
                reverse = true;
            case "category":
                animals.sort((a, b) -> a.getCategory().compareToIgnoreCase(b.getCategory()));
                break;
            case "creation-reverse":
                reverse = true;
            case "creation":
                animals.sort(Date.comparingDates(Animal::getCreationDate));
                break;
            case "age-reverse":
                reverse = true;
            case "age":
                animals.sort(Date.comparingDates(Animal::getBirthday));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort method: Valid ones are name, name-reverse, price, price-reverse, category, category-reverse, creation, creation-reverse, age, age-reverse.");
        }

        if (reverse) {
            Collections.reverse(animals);
        }
    }

    /**
     * Returner en ny AnimalList, kun med dyr der har navnet, eller en del af navnet
     *
     * @param name Navnet der skal filtreres efter
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsByName(String name) {
        name = name.toLowerCase();
        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            if (animal.getName().toLowerCase().contains(name)) {
                list.add(animal);
            }
        }
        return list;
    }

    /**
     * Returner en ny AnimalList, kun med dyr der har en pris i intervallet
     *
     * @param min Minimum pris
     * @param max Maximum pris
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsByPrice(double min, double max) {
        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            if (!animal.isForSale() || animal.getPrice() >= min && animal.getPrice() <= max) {
                list.add(animal);
            }
        }
        return list;
    }

    /**
     * Returner en ny AnimalList, kun med dyr der er til salg
     *
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsForSale() {
        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            if (animal.isForSale()) {
                list.add(animal);
            }
        }
        return list;
    }

    /**
     * Returner en ny AnimalList, kun med dyr der er til pasning
     *
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsForPension() {
        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            if (!animal.isForSale()) {
                list.add(animal);
            }
        }
        return list;
    }

    /**
     * Returner en ny AnimalList, kun med dyr fra en bestemt kategori
     *
     * @param category Kategori
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsFromCategory(String category) {
        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            if (animal.getCategory().equals(category)) {
                list.add(animal);
            }
        }
        return list;
    }

    /**
     * Returner en ny AnimalList, kun med dyr ejet af en bestemt ejer
     *
     * @param ownerId Ejerens ID
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsByOwner(int ownerId) {
        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            if (animal.getOwnerId() == ownerId) {
                list.add(animal);
            }
        }
        return list;
    }

    /**
     */
    public AnimalList getAnimalsByOwnerName(String name, CustomerList customerList) {
        name = name.toLowerCase();
        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            Customer owner = customerList.getById(animal.getOwnerId());
            if (owner != null && owner.getName().toLowerCase().contains(name)) {
                list.add(animal);
            }
        }
        return list;
    }

    /**
     * Returner en ny AnimalList, kun med dyr der skal have en bestemt fodder
     *
     * @param food Mad
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsByFood(String food) {
        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            if (animal.getFood().equals(food)) {
                list.add(animal);
            }
        }
        return list;
    }

    /**
     * Returner en ny AnimalList, kun med dyr der har fødselsdagsdato imellem min og max
     *
     * @param min Minimum age
     * @param max Maximum age
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsByAge(int min, int max) {
        // kaster fejl, hvis min er større end max. 1 til sammenligningen
        if (min > max) throw new IllegalArgumentException("Minimum age cannot be higher than maximum!");

        Date today = new Date(); // 1 – oprettelse

        AnimalList list = new AnimalList(); // 1 – oprettelse
        
        // n, for hver animal i for-løkken
        for (Animal animal : animals) {
            int age = animal.getBirthday().yearsBetween(today); // 1 - kald
            if (age >= min && age <= max) { // 2 - sammenligninger
                list.add(animal); // 1 tilføj til liste
            }
        }

        return list; // 1 return
    }


    /**
     * Returner en ny AnimalList, kun med dyr der har oprettelsesdatoer i DatoIntervallet
     *
     * @param dateInterval Interval for oprettelsesdatoer der skal filteres imellem
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsByCreationDate(DateInterval dateInterval) {
        if (dateInterval == null) throw new IllegalArgumentException("Date interval cannot be null!");

        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            if (dateInterval.contains(animal.getCreationDate())) {
                list.add(animal);
            }
        }
        return list;
    }

    public AnimalList getAnimalsWithReservation(DateInterval interval, ReservationList reservations) {
        AnimalList list = new AnimalList();
        
        for (Animal animal : animals) {
            for (Reservation reservation : reservations.getReservationsForAnimal(animal.getAnimalId()).getAllReservations()) {
                if (interval.intersects(reservation.getPeriod())) {
                    list.add(animal);
                    break;
                }
            }
        }
        
        return list;
    }
}
