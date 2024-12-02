package model;

public class Person {
    /**
     * @param name
     */
    private String name;

    /**
     * Konstruktøren for Person
     *
     * @param name personens navn
     */
    public Person(String name) {
        setName(name);
    }

    /**
     * @return personens navn
     */
    public String getName() {
        return name;
    }

    /**
     * Sætter navnet på personen i name objektet
     *
     * @param name personens navn
     */
    public void setName(String name) {
        this.name = name;
    }
}
