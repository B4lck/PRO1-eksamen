package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;

/**
 * Controller til ManageAnimalGUI - Viewet der opretter og redigere dyr
 */
public class ManageAnimalController {
    private Region root;
    private VIAPetsModel model;

    // Elementer
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

    /**
     * Callback der returner det nye eller redigerede animalId
     */
    @FunctionalInterface
    public interface ManageAnimalCallback {
        void callback(int animalId);
    }

    private ManageAnimalCallback callback;

    /**
     * Kategorier af dyr
     */
    private final String[] animalTypes = {"(andet)", "Fisk", "Fugl", "Reptil"};

    /**
     * Hjælpe methode til at få kategori id'er ud fra vist string
     */
    private String getCategory() {
        return switch (category.getValue()) {
            case "Fisk" -> Animal.CATEGORY_FISH;
            case "Fugl" -> Animal.CATEGORY_BIRD;
            case "Reptil" -> Animal.CATEGORY_REPTILE;
            default -> Animal.CATEGORY_DEFAULT;
        };
    }

    // Andre instans variabler
    public int currentAnimalId = -1;

    private Date selectedBirthday = new Date();
    private double selectedPrice = 0;
    private int selectedOwnerId = -1;

    /**
     * Indlæser og åbner ManageAnimalGUI
     *
     * @param model              Modellen
     * @param animalId           ID på det dyr der skal redigeres, eller -1 for at oprette et nyt dyr
     * @param callback           Et callback, der returnere det nye eller redigerede animalId
     * @param forceSaleOrPension Hvis null, slås denne funktion fra. Hvis true SKAL man oprette et dyr til salg, og hvis false SKAL man oprette et dyr til pasning.
     */
    public static void load(VIAPetsModel model, int animalId, ManageAnimalCallback callback, Boolean forceSaleOrPension) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ManageAnimalController.class.getResource("/views/ManageAnimalGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Opret/rediger dyr");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((ManageAnimalController) loader.getController()).init(root, model, animalId, callback, forceSaleOrPension);
            stage.showAndWait();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Fejl");
            errorAlert.showAndWait();
        }
    }

    /**
     * Init
     */
    private void init(Region root, VIAPetsModel model, int animalId, ManageAnimalCallback callback, Boolean forceSaleOrPension) {
        this.model = model;
        this.root = root;
        this.callback = callback;

        this.reset(animalId);

        if (forceSaleOrPension != null) {
            notForSale.setDisable(true);
            notForSale.setSelected(!forceSaleOrPension);
        }

        // Event listeners
        birthday.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedBirthday = new Date(newValue);
            update();
        });

        age.setOnKeyReleased(event -> {
            if (selectedBirthday == null) selectedBirthday = new Date();
            try {
                selectedBirthday.set(selectedBirthday.getDay(), selectedBirthday.getMonth(), new Date().getYear() - Integer.parseInt(age.getText()));
                error.setText("");
                update();
            } catch (NumberFormatException e) {
                error.setText("Alder er ugyldig indtastet");
            }
        });

        price.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                price.setText(String.format("%.2f", selectedPrice));
            } else {
                try {
                    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                    Number number = format.parse(price.getText());
                    selectedPrice = number.doubleValue();
                    price.setText(String.format("%.2f kr.", selectedPrice));
                    error.setText("");
                } catch (ParseException e) {
                    error.setText("Pris er ugyldig indtastet");
                }
            }
        });

        update();
    }

    /**
     * Reset
     */
    public void reset(int animalId) {
        currentAnimalId = animalId;

        // Reset alle ting og sager
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
            selectedOwnerId = animal.getOwnerId();
        }

        update();
    }

    /**
     * Opdater viewet
     */
    @FXML
    public void update() {
        venomous.setDisable(true);
        tamed.setDisable(true);
        saltwater.setDisable(true);
        // Ik sikker på hvorfor, men kategorien kan godt være null men sættes med det samme til standard værdien
        // Programmet er dog ikke så vild med det
        if (category.getValue() != null) {
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
        createCustomer.setDisable(!notForSale.isSelected());
        selectCustomer.setDisable(!notForSale.isSelected());
        price.setDisable(notForSale.isSelected());

        // Hvis selectedBirthday er null, så nulstil den
        if (selectedBirthday == null) selectedBirthday = new Date();
        birthday.setValue(selectedBirthday.getLocalDate());
        // Hvis age værdierne er de samme, så lad vær med at kalde setText, da det flytter tekst markøren
        String newAgeValue = Integer.toString(new Date().yearsBetween(selectedBirthday));
        if (!Objects.equals(age.getText(), newAgeValue)) age.setText(newAgeValue);

        // Sæt teksten ved vælg customer knappen til den valgt customer's navn
        selectCustomer.setText(selectedOwnerId != -1 ? model.getCustomerList().getById(selectedOwnerId).getName() + "..." : "Vælg kunde");
    }

    /**
     * Action til at lukke vinduet
     */
    @FXML
    public void close() {
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action til at oprette/redigere dyr
     */
    @FXML
    public void createEdit() {
        // Advar hvis ingen ejer er valgt
        if (notForSale.isSelected() && selectedOwnerId == -1) {
            Alert successAlert = new Alert(Alert.AlertType.WARNING, "Ingen ejer er valgt!", ButtonType.OK);
            successAlert.setGraphic(null);
            successAlert.setHeaderText(null);
            successAlert.setTitle("Fejl");
            successAlert.show();
            return;
        }

        // Opret dyr
        if (currentAnimalId == -1) {
            Animal newAnimal;
            if (notForSale.isSelected()) {
                newAnimal = model.getAnimalList().createNewAnimal(getCategory(), name.getText(), selectedOwnerId);
            } else {
                newAnimal = model.getAnimalList().createNewAnimal(getCategory(), name.getText(), selectedPrice);
            }

            currentAnimalId = newAnimal.getAnimalId();

            newAnimal.setComment(comment.getText());
            newAnimal.setFood(food.getText());
            newAnimal.setCreationDate(new Date());

            if (newAnimal instanceof AnimalFish) ((AnimalFish) newAnimal).setIsFreshWater(!saltwater.isSelected());
            if (newAnimal instanceof AnimalBird) ((AnimalBird) newAnimal).setTamed(tamed.isSelected());
            if (newAnimal instanceof AnimalReptile) ((AnimalReptile) newAnimal).setVenomous(venomous.isSelected());

            model.getAnimalList().add(newAnimal);
        }
        // Rediger dyr
        else {
            Animal animal = model.getAnimalList().getAnimalById(currentAnimalId);

            if (notForSale.isSelected() == animal.isForSale()) {
                if (notForSale.isSelected()) animal.convertToOwnedAnimal(selectedOwnerId);
                else animal.convertToSale(selectedPrice);
            }

            animal.setName(name.getText());
            animal.setComment(comment.getText());
            animal.setFood(food.getText());
            animal.setPrice(selectedPrice);
            animal.setOwnerId(selectedOwnerId);

            if (animal instanceof AnimalFish) ((AnimalFish) animal).setIsFreshWater(!saltwater.isSelected());
            if (animal instanceof AnimalBird) ((AnimalBird) animal).setTamed(tamed.isSelected());
            if (animal instanceof AnimalReptile) ((AnimalReptile) animal).setVenomous(venomous.isSelected());
        }

        close();

        model.save();

        if (callback != null) callback.callback(currentAnimalId);
    }

    /**
     * Action der åbner vælg customer vinduet
     */
    @FXML
    public void selectCustomer() {
        SelectCustomerController.load(model, (customerId) -> {
            selectedOwnerId = customerId;
            update();
        });
    }

    /**
     * Action til at oprette en kunde/ejer
     */
    @FXML
    public void createCustomer() {
        ManageCustomerController.load(model, -1, customerId -> {
            selectedOwnerId = customerId;
            update();
        });
    }
}
