package model;

import java.util.Objects;

/**
 * Et kæledyr til salg eller pasning
 */
public class Animal {
    /**
     * Om dyret er til salg, eller til pasning
     */
    private boolean forSale;

    /**
     * Dyrets kategori: (andet), Bird, Fish eller Reptile
     */
    private String category = "Other";

    /**
     * Navnet på dyret
     */
    private String name;

    /**
     * URL til billede af dyret
     */
    private String imageUrl;

    /**
     * Den mad dyret skal have
     */
    private String food;

    /**
     * Kommentar om dyret
     */
    private String comment;

    /**
     * Dyrets pris, hvis det er til salg
     */
    private double price = 0;

    /**
     * Id
     */
    private int animalId;

    /**
     * Ejer ID, -1 hvis ingen ejer
     */
    private int ownerId = -1;

    /**
     * Static
     */
    public static final String CATEGORY_BIRD = "Bird";
    public static final String CATEGORY_FISH = "Fish";
    public static final String CATEGORY_REPTILE = "Reptile";

    /**
     * Konstruktør til dyr til salg
     * @param name Navnet på dyret
     * @param price Pris på dyret
     * @param animalId Id til selve dyret
     */
    public Animal(String name, double price, int animalId) {
        this.name = name;
        this.price = price;
        this.animalId = animalId;
        this.forSale = true;
    }

    /**
     * Konstruktør til dyr til salg
     * @param name Navnet på dyret
     * @param ownerId Id til ejer af dyret
     * @param animalId Id til selve dyret
     */
    public Animal(String name, int ownerId, int animalId) {
        this.name = name;
        this.ownerId = ownerId;
        this.animalId = animalId;
        this.forSale = false;
    }

    /**
     * Er dyret til salg?
     */
    public boolean isForSale() {
        return forSale;
    }

    /**
     * Henter ejers id, eller returner -1 hvis der ingen ejer er.
     */
    public int getOwnerId() {
        return ownerId;
    }

    /**
     * Henter prisen på dyret
     */
    public double getPrice() {
        return price;
    }

    /**
     * Henter dyrets id
     */
    public int getAnimalId() {
        return animalId;
    }

    /**
     * Henter dyrets kategori
     */
    public String getCategory() {
        return category;
    }

    /**
     * Henter dyrets kommentar
     */
    public String getComment() {
        return comment;
    }

    /**
     * Henter dyrets mad
     */
    public String getFood() {
        return food;
    }

    /**
     * Henter URL til dyrets billede
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Henter dyrets navn
     */
    public String getName() {
        return name;
    }

    /**
     * Konverter dyret til et dyr til pasning
     * @param ownerId Ejerens Id
     */
    public void convertToOwnedAnimal(int ownerId) {
        this.ownerId = ownerId;
        this.forSale = false;
    }

    /**
     * Konverter dyret til et dyr til salg
     * @param price Pris på dyret
     */
    public void convertToSale(double price) {
        this.price = price;
        this.forSale = true;
    }

    /**
     * Set dyrets kategori
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Set dyrets navn
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set dyrets imageUrl
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Set dyrets mad
     * @param food
     */
    public void setFood(String food) {
        this.food = food;
    }

    /**
     * Set dyrets kommentar
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Set dyrets pris
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return forSale == animal.forSale && Double.compare(price, animal.price) == 0 && animalId == animal.animalId && ownerId == animal.ownerId && Objects.equals(category, animal.category) && Objects.equals(name, animal.name) && Objects.equals(imageUrl, animal.imageUrl) && Objects.equals(food, animal.food) && Objects.equals(comment, animal.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forSale, category, name, imageUrl, food, comment, price, animalId, ownerId);
    }
}
