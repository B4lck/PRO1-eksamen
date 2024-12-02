package model;

/**
 * Lidt ekstra metoder til reptiler
 */
public class AnimalReptile extends Animal {

    private boolean venomous;

    /**
     * Konstruktør til dyr til salg
     * @param name Navnet på dyret
     * @param price Pris på dyret
     * @param animalId Id til selve dyret
     */
    public AnimalReptile(String name, double price, int animalId) {
        // Har valgt at venomous ikke indgår i konstruktøren, for at gøre den ens med de andre klasser, skal opdateres i klassediagram
        super(name, price, animalId);
        this.setCategory("Reptile");
    }

    /**
     * Konstruktør til dyr til salg
     * @param name Navnet på dyret
     * @param ownerId Id til ejer af dyret
     * @param animalId Id til selve dyret
     */
    public AnimalReptile(String name, int ownerId, int animalId) {
        super(name, ownerId, animalId);
        this.setCategory("Reptile");
    }

    /**
     * Er reptilet giftigt?
     */
    public boolean isVenomous() {
        return venomous;
    }

    /**
     * Set om reptilet giftigt eller ej
     * @param venomous
     */
    public void setVenomous(boolean venomous) {
        this.venomous = venomous;
    }
}
