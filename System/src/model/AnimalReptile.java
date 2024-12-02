package model;

/**
 * Lidt ekstra metoder til reptiler
 */
public class AnimalReptile extends Animal {

    /**
     * Om dyret er giftigt
     */
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
     * Henter om reptilet er giftigt?
     */
    public boolean isVenomous() {
        return venomous;
    }

    /**
     * Sæt om reptilet giftigt eller ikke
     * @param venomous Giftigt eller ikke
     */
    public void setVenomous(boolean venomous) {
        this.venomous = venomous;
    }
}
