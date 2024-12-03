package controllers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.*;

public class AnimalModel 
{
    private StringProperty owner;
    private StringProperty category;
    private StringProperty name;
    private StringProperty food;
    private StringProperty forSale;
    private StringProperty price;
    private StringProperty water;
    private StringProperty venomous;
    private StringProperty tame;
    private StringProperty commentColumn;
    
    public AnimalModel(Animal animal, VIAPetsModel model) {
        Customer customer = model.getCustomerList().getCustomerById(animal.getOwnerId()) ;
        owner = new SimpleStringProperty(animal.isForSale() ? "-" : customer == null ? "Findes ikke" : customer.getName());
        category = new SimpleStringProperty(animal.getCategory());
        name = new SimpleStringProperty(animal.getName());
        food = new SimpleStringProperty(animal.getFood());
        forSale = new SimpleStringProperty(animal.isForSale() ? "Til salg" : "Til pasning");
        price = new SimpleStringProperty(animal.isForSale() ? String.format("%.2f kr.", animal.getPrice()) : "-");
        water = new SimpleStringProperty(animal instanceof AnimalFish ? (((AnimalFish) animal).isFreshWater() ? "Ferskvand" : "Saltvand") : "-");
        venomous = new SimpleStringProperty(animal instanceof AnimalReptile ? (((AnimalReptile) animal).isVenomous() ? "Giftig" : "Ikke giftig") : "-");
        tame = new SimpleStringProperty(animal instanceof AnimalBird ? (((AnimalBird) animal).isTamed() ? "Tæmmet" : "Ikke tæmmet") : "-");
        commentColumn = new SimpleStringProperty(animal.getComment());
    }
    
    public StringProperty getOwner() {
        return owner;
    }
    
    public StringProperty getCategory() {
        return category;
    }
    
    public StringProperty getName() {
        return name;
    }
    
    public StringProperty getFood() {
        return food;
    }
    
    public StringProperty getForSale() {
        return forSale;
    }
    
    public StringProperty getPrice() {
        return price;
    }
    
    public StringProperty getWater() {
        return water;
    }
    
    public StringProperty getVenomous() {
        return venomous;
    }
    
    public StringProperty getCommentColumn() {
        return commentColumn;
    }

    public StringProperty getTamed() {return tame;}
}
