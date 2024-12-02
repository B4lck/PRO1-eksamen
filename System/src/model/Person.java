package model;

public class Person {
    /**
     * @param name
     */
    private String name;

    /**
     * konstruktør med en parameter
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
     * sætter navnet på personen i name objektet
     *
     * @param name personens navn
     */
    public void setName(String name) {
        this.name = name;
    }
}
