package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import model.*;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

public class ManageAnimalController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    private SelectCustomerController selectCustomerController = new SelectCustomerController();

    @FXML
    private Text title;
    @FXML
    public ChoiceBox<String> category;
    @FXML
    public Label error;
    @FXML
    public CheckBox notForSale;
    @FXML
    public CheckBox venomous;
    @FXML
    public CheckBox tamed;
    @FXML
    public CheckBox saltwater;
    @FXML
    public TextArea comment;
    @FXML
    public TextField price;
    @FXML
    public TextField name;
    @FXML
    public TextField food;
    @FXML
    public DatePicker birthday;
    @FXML
    public TextField age;
    @FXML
    public Button selectCustomer;
    @FXML
    public Button createCustomer;

    public int currentAnimalId = -1;

    public ManageAnimalController() {


    }

    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root, int animalId) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        this.reset(animalId);

        notForSale.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                update();
            }
        });
        
        birthday.valueProperty().addListener(new ChangeListener<LocalDate>() {
           @Override
           public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
               selectedBirthday = new Date(newValue);
               update();
           } 
        });
        
        age.setOnKeyReleased(event -> {
            try {
                selectedBirthday.set(selectedBirthday.getDay(), selectedBirthday.getMonth(), new Date().getYear() - Integer.parseInt(age.getText()));
                update();
            } catch (Exception ignored) {
            }
        });

        price.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    price.setText(String.format("%.2f", selectedPrice));
                } else {
                    try {
                        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                        Number number = format.parse(price.getText());
                        selectedPrice = number.doubleValue();
                        price.setText(String.format("%.2f kr.", selectedPrice));
                    } catch (ParseException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
    }

    public void reset(int animalId) {
        currentAnimalId = animalId;
        String[] animalTypes = {"(andet)", "Fisk", "Fugl", "Reptil"};
        category.setItems(FXCollections.observableArrayList(animalTypes));
        error.setText("");
        if (animalId == -1) {
            title.setText("Opret nyt dyr");
            category.setValue("(andet)");
            category.setDisable(false);
            notForSale.setSelected(false);
            venomous.setSelected(false);
            tamed.setSelected(false);
            saltwater.setSelected(false);
            name.setText("");
            food.setText("");
            comment.setText("");
            price.setText("0,00 kr.");
            selectedBirthday = new Date();
            selectedPrice = 0;
            selectedOwnerId = -1;
        } else {
            Animal animal = model.getAnimalList().getAnimalById(animalId);
            title.setText("Rediger: " + animal.getName());
            category.setValue(switch (animal.getCategory()) {
                case Animal.CATEGORY_FISH -> "Fisk";
                case Animal.CATEGORY_BIRD -> "Fugl";
                case Animal.CATEGORY_REPTILE -> "Reptil";
                default -> "(andet)";
            });
            category.setDisable(true);
            notForSale.setSelected(!animal.isForSale());
            venomous.setSelected(animal instanceof AnimalReptile && ((AnimalReptile) animal).isVenomous());
            tamed.setSelected(animal instanceof AnimalBird && ((AnimalBird) animal).isTamed());
            saltwater.setSelected(animal instanceof AnimalFish && !((AnimalFish) animal).isFreshWater());
            name.setText(animal.getName());
            food.setText(animal.getFood());
            comment.setText(animal.getComment());
            selectedPrice = animal.getPrice();
            selectedBirthday = animal.getBirthday();
            price.setText(String.format("%.2f kr.", selectedPrice));
            selectedOwnerId = -1;
        }

        this.update();
    }

    public Region getRoot() {
        return root;
    }

    @FXML
    public void update() {
        venomous.setDisable(true);
        tamed.setDisable(true);
        saltwater.setDisable(true);
        switch (category.getValue()) {
            case "Reptil":
                venomous.setDisable(false);
                break;
            case "Fugl":
                tamed.setDisable(false);
                break;
            case "Fisk":
                saltwater.setDisable(false);
                break;
        }
        createCustomer.setDisable(!notForSale.isSelected());
        selectCustomer.setDisable(!notForSale.isSelected());
        price.setDisable(notForSale.isSelected());
        birthday.setValue(selectedBirthday.getLocalDate());
        // Hvis age værdierne er de samme, så lad vær med at kalde setText, da det flytter tekst markøren
        String newAgeValue = Integer.toString(new Date().yearsBetween(selectedBirthday));
        if (!Objects.equals(age.getText(), newAgeValue)) age.setText(newAgeValue);

        // Sæt teksten ved vælg customer knappen til den valgt customer's navn
        selectCustomer.setText(selectedOwnerId != -1 ? model.getCustomerList().getById(selectedOwnerId).getName() + "..." : "Vælg kunde");
    }

    @FXML
    public void back() {
        viewHandler.openView("Animals");
    }

    private String getCategory() {
        return switch (category.getValue()) {
            case "Fisk" -> Animal.CATEGORY_FISH;
            case "Fugl" -> Animal.CATEGORY_BIRD;
            case "Reptil" -> Animal.CATEGORY_REPTILE;
            default -> "Other";
        };
    }

    private Date selectedBirthday = new Date();
    private double selectedPrice = 0;
    private int selectedOwnerId = -1;

    @FXML
    public void createEdit() {
        if (currentAnimalId == -1) {
            Animal newAnimal;
            if (notForSale.isSelected()) {
                newAnimal = model.getAnimalList().createNewAnimal(getCategory(), name.getText(), selectedOwnerId);
            } else {
                newAnimal = model.getAnimalList().createNewAnimal(getCategory(), name.getText(), selectedPrice);
            }

            newAnimal.setComment(comment.getText());
            newAnimal.setFood(food.getText());
            newAnimal.setCreationDate(new Date());

            if (newAnimal instanceof AnimalFish) ((AnimalFish) newAnimal).setIsFreshWater(!saltwater.isSelected());
            if (newAnimal instanceof AnimalBird) ((AnimalBird) newAnimal).setTamed(tamed.isSelected());
            if (newAnimal instanceof AnimalReptile) ((AnimalReptile) newAnimal).setVenomous(venomous.isSelected());

            model.getAnimalList().add(newAnimal);

            viewHandler.openView("Animals");
        } else {
            Animal animal = model.getAnimalList().getAnimalById(currentAnimalId);
            
            if (notForSale.isSelected() == animal.isForSale()) {
                if (notForSale.isSelected()) animal.convertToOwnedAnimal(selectedOwnerId);
                else animal.convertToSale(selectedPrice);
            }

            animal.setName(name.getText());
            animal.setComment(comment.getText());
            animal.setFood(food.getText());
            animal.setPrice(selectedPrice);
            
            if (animal instanceof AnimalFish) ((AnimalFish) animal).setIsFreshWater(!saltwater.isSelected());
            if (animal instanceof AnimalBird) ((AnimalBird) animal).setTamed(tamed.isSelected());
            if (animal instanceof AnimalReptile) ((AnimalReptile) animal).setVenomous(venomous.isSelected());
            
            viewHandler.openView("Animals");
        }
    }

    public void selectCustomer() {
        selectCustomerController.loadSelf(model, (customerId) -> {
            selectedOwnerId = customerId;
            update();
        });
    }
}
