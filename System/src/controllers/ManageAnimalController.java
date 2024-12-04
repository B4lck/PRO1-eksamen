package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import model.*;

public class ManageAnimalController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

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

    public int currentAnimalId = -1;

    public ManageAnimalController() {


    }

    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root, int animalId) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        this.reset(animalId);

        price.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    price.setText("0.00");
                } else {
                    price.setText("0,00 kr.");
                }
            }
        });
    }

    public void reset(int animalId) {
        this.currentAnimalId = animalId;
        String[] animalTypes = {"(andet)", "Fisk", "Fugl", "Reptil"};
        category.setItems(FXCollections.observableArrayList(animalTypes));
        if (animalId == -1) {
            title.setText("Opret nyt dyr");
            error.setText("");
            category.setValue("(andet)");
            notForSale.setSelected(false);
            venomous.setSelected(false);
            tamed.setSelected(false);
            saltwater.setSelected(false);
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

    private double getPrice() {
        return 0.0;
    }

    private int selectedOwnerId = -1;

    @FXML
    public void createEdit() {
        if (currentAnimalId == -1) {
            Animal newAnimal;
            if (notForSale.isSelected()) {
                newAnimal = model.getAnimalList().createNewAnimal(getCategory(), name.getText(), selectedOwnerId);
            }
            else {
                newAnimal = model.getAnimalList().createNewAnimal(getCategory(), name.getText(), getPrice());
            }
            
            newAnimal.setComment(comment.getText());
            newAnimal.setFood(food.getText());
            newAnimal.setCreationDate(new Date());
            
            if (newAnimal instanceof AnimalFish) ((AnimalFish) newAnimal).setIsFreshWater(!saltwater.isSelected());
            if (newAnimal instanceof AnimalBird) ((AnimalBird) newAnimal).setTamed(tamed.isSelected());
            if (newAnimal instanceof AnimalReptile) ((AnimalReptile) newAnimal).setVenomous(venomous.isSelected());
            
            model.getAnimalList().add(newAnimal);

            viewHandler.openView("Animals");
        }
    }
}
