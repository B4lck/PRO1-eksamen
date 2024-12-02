package model;

import java.util.ArrayList;

public class AnimalList {
    private ArrayList<Animal> animals;

    public Animal createAnimal(String name, double price) {
        return new Animal(name, price, getUniqueId());
    }

    public Animal createAnimal(String name, int ownerId) {
        return new Animal(name, ownerId, getUniqueId());
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
        return animals.toArray(new Animal[animals.size()]);
    }

    /**
     * Sorter listen
     * @param sorting
     */
    public void sortBy(String sorting) {
        switch (sorting) {
            case "name":
                // gør noget sortering
                break;
            // og for alle de andre cases
        }
    }

    /**
     * Returner en ny AnimalList, kun med dyr der har navnet, eller en del af navnet
     * @param name Navnet der skal filtreres efter
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsByName(String name) {
        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            if (animal.getName().contains(name)) {
                list.add(animal);
            }
        }
        return list;
    }

    /**
     * Returner en ny AnimalList, kun med dyr der har en pris i intervallet
     * @param min Minimum pris
     * @param max Maximum pris
     * @return En ny AnimalList
     */
    public AnimalList getAnimalsByPrice(double min, double max) {
        AnimalList list = new AnimalList();
        for (Animal animal : animals) {
            if (animal.isForSale() && animal.getPrice() >= min && animal.getPrice() <= max) {
                list.add(animal);
            }
        }
        return list;
    }

    /**
     * Returner en ny AnimalList, kun med dyr der er til salg
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
     * Returner en ny AnimalList, kun med dyr der skal have en bestemt fodder
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

    public AnimalList getAnimalsByAge() {
        // TODO
        return new AnimalList();
    }

    public AnimalList getAnimalsByCreationDate() {
        // TODO
        return new AnimalList();
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
}
