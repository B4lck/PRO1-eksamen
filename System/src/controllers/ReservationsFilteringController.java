package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.time.LocalDate;

/**
 * View til valg af filtre for dyr oversigten
 */
public class ReservationsFilteringController {
    private Region root;
    private VIAPetsModel model;

    // Nuværende callback
    private FilteringCallback callback;

    /**
     * Lambda funktion der skal filtre en CustomerList
     */
    @FunctionalInterface
    public interface ReservationFilter {
        ReservationList filterList(ReservationList reservationList);
    }

    /**
     * Lambda funktion der skal modtage en CustomerList
     * Dette filter er resultatet af brugerens valg
     */
    @FunctionalInterface
    public interface FilteringCallback {
        void callback(ReservationFilter filter);
    }

    int selectedOwner = -1;

    int selectedAnimal = -1;

    /**
     * De forskellige inputs fra FXML
     */
    @FXML
    public CheckBox dateToggle;
    @FXML
    public DatePicker startDate;
    @FXML
    public DatePicker endDate;
    @FXML
    public Button ownerSelector;
    @FXML
    public Button animalSelector;
    @FXML
    public TextField positionField;

    /**
     * @param model    Modellen
     * @param callback Filteret returneres via et callback når brugeren har valgt muligheder og trykket OK
     */
    public static void load(VIAPetsModel model, FilteringCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ReservationsFilteringController.class.getResource("/views/ReservationsFilteringGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vælg filtering og sortering");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((ReservationsFilteringController) loader.getController()).init(root, model, callback);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Init viewet, reset behøves ikke, da der åbnes et nyt view hver gang
     *
     * @param root     FXML roden
     * @param model    Model
     * @param callback Tilbagekald med filter
     */
    public void init(Region root, VIAPetsModel model, FilteringCallback callback) {
        this.root = root;
        this.model = model;
        this.callback = callback;
        
        startDate.setValue(LocalDate.now());
        startDate.setDisable(true);
        endDate.setValue(LocalDate.now());
        endDate.setDisable(true);
    }

    @FXML
    public void toggleDateFilter() {
        startDate.setDisable(!dateToggle.isSelected());
        endDate.setDisable(!dateToggle.isSelected());
    }
    
    /**
     * Vælg ejer
     */
    @FXML
    public void selectOwner() {
        SelectCustomerController.load(model, customerId -> {
            selectedOwner = customerId;
            ownerSelector.setText(model.getCustomerList().getById(customerId).getName() + "...");
        });
    }

    /**
     * Ryd ejer
     */
    @FXML
    public void clearOwner() {
        selectedOwner = -1;
        ownerSelector.setText("Vælg ejer");
    }

    /**
     * Vælg dyr
     */
    @FXML
    public void selectAnimal() {
        SelectAnimalController.load(model, animalId -> {
            selectedAnimal = animalId;
            animalSelector.setText(model.getAnimalList().getAnimalById(animalId).getName() + "...");
        }, false);
    }

    /**
     * Ryd dyr
     */
    @FXML
    public void clearAnimal() {
        selectedAnimal = -1;
        animalSelector.setText("Vælg dyr");
    }

    /**
     * Nulstil
     */
    @FXML
    public void clear() {
        this.callback.callback(null);
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Luk
     */
    @FXML
    public void close() {
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action der opretter filteret og returnere det via callback
     */
    @FXML
    public void confirm() {
        this.callback.callback((customerList) -> {
            
            // Filtre efter periode
            if (dateToggle.isSelected()) {
                Date start = new Date(startDate.getValue());
                Date end = new Date(endDate.getValue());
                customerList = customerList.getReservationsForPeriod(new DateInterval(start, end));
            }
            
            // Filtre efter ejer
            if (selectedOwner != -1) {
                customerList = customerList.getReservationsForOwner(selectedOwner);
            }
            
            // Filtre efter dyr
            if (selectedAnimal != -1) {
                customerList = customerList.getReservationsForAnimal(selectedAnimal);
            }
            
            // Filtre efter position
            if (!positionField.getText().isEmpty()) {
                try {
                    customerList = customerList.getReservationsForPosition(Integer.parseInt(positionField.getText()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            return customerList;
        });

        ((Stage) root.getScene().getWindow()).close();
    }
}
