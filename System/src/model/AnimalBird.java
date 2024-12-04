package model;

/**
 * Lidt ekstra metoder til fugle
 */
public class AnimalBird extends Animal {

    private boolean tamed;

    /**
     * Konstruktør til dyr til salg
     * @param name Navnet på dyret
     * @param price Pris på dyret
     * @param animalId Id til selve dyret
     */
    public AnimalBird(String name, double price, int animalId) {
        // Har valgt at tamed ikke indgår i konstruktøren, for at gøre den ens med de andre klasser, skal opdateres i klassediagram
        super(name, price, animalId);
    }

    /**
     * Konstruktør til dyr til salg
     * @param name Navnet på dyret
     * @param ownerId Id til ejer af dyret
     * @param animalId Id til selve dyret
     */
    public AnimalBird(String name, int ownerId, int animalId) {
        super(name, ownerId, animalId);
    }

    @Override
    public String getCategory() {
        return Animal.CATEGORY_BIRD;
    }

    /**
     * Er fuglen tæmmet?
     */
    public boolean isTamed() {
        return tamed;
    }

    /**
     * Set om fuglen er tæmmet eller ej
     * @param tamed
     */
    public void setTamed(boolean tamed) {
        this.tamed = tamed;
    }
}
