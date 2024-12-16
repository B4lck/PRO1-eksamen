package controllers;


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
import java.time.LocalDate;

/**
 * Controller til at oprette og redigere reservationer
 */
public class ManageReservationController {
    private Region root;
    private VIAPetsModel model;

    private Reservation selectedReservation;

    private int selectedAnimalId = -1;
    private int selectedCustomerId = -1;
    private Date selectedStartDate;
    private Date selectedEndDate;
    private int selectedPosition = -1;

    // Elementer
    @FXML
    private Text title;
    @FXML
    private Button selectCustomerButton;
    @FXML
    private Button selectAnimalButton;
    @FXML
    private TextField positionSelector;
    @FXML
    private DatePicker dateStart;
    @FXML
    private DatePicker dateEnd;
    @FXML
    private Label error;

    /**
     * Åbner viewet til at oprette eller redigere reservationer
     * @param model VIAPets modellen
     * @param reservation Reservation der skal redigeres, eller null for at oprette en ny
     */
    public static void load(VIAPetsModel model, Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ManageReservationController.class.getResource("/views/ManageReservationGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Opret/Rediger reservation");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((ManageReservationController) loader.getController()).init(root, model, reservation);
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
     * Init viewet
     */
    private void init(Region root, VIAPetsModel model, Reservation reservation) {
        this.model = model;
        this.root = root;

        if (reservation != null) {
            // Hvis redigering af reservation
            // Sætter alt teksten til at være matchene for den åbnede reservation
            this.selectedReservation = reservation;
            this.selectedCustomerId = reservation.getCustomerId();
            this.selectedAnimalId = reservation.getAnimalId();
            this.selectCustomerButton.setText(model.getCustomerList().getById(reservation.getCustomerId()).getName());
            this.selectAnimalButton.setText(model.getAnimalList().getAnimalById(reservation.getAnimalId()).getName());
            this.positionSelector.setText(Integer.toString(reservation.getPositionId()));
            this.dateStart.setValue(reservation.getPeriod().getStartDate().getLocalDate());
            this.dateEnd.setValue(reservation.getPeriod().getEndDate().getLocalDate());
            title.setText("Redigering af reservation");
        } else {
            this.dateStart.setValue(LocalDate.now());
            this.dateEnd.setValue(LocalDate.now());
            title.setText("Oprettelse af reservation");
        }
        
        update();

        this.error.setVisible(false);

        // Event listeners
        dateStart.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedStartDate = new Date(newValue);
            update();
        });

        dateEnd.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedEndDate = new Date(newValue);
            update();
        });

        positionSelector.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                selectedPosition = Integer.parseInt(newValue);
                error.setVisible(false);
            } catch (NumberFormatException e) {
                selectedPosition = -1;
                error.setVisible(true);
                error.setText("Indtast kun et hel tal");
            }
        });
    }

    /**
     * Opdater felterne
     */
    private void update() {
        this.selectedStartDate = new Date(dateStart.getValue());
        this.selectedEndDate = new Date(dateEnd.getValue());

        if (selectedStartDate == null) {selectedStartDate = new Date();}
        if (selectedEndDate == null) {selectedEndDate = new Date();}

        this.selectCustomerButton.setText(selectedCustomerId != -1 ?model.getCustomerList().getById(selectedCustomerId).getName() + "..." : "Vælg kunde");
        this.selectAnimalButton.setText(selectedAnimalId != -1 ? model.getAnimalList().getAnimalById(selectedAnimalId).getName() + "..." : "Vælg dyr");
    }

    /**
     * Action til at annullere/lukke viewet
     */
    @FXML
    public void close() {
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action til at oprette/redigere reservation
     */
    @FXML
    public void confirm() {
        // Tjek for tomme input
        if (selectedAnimalId == -1) {error.setVisible(true); error.setText("Vælg et dyr"); return;}
        if (selectedCustomerId == -1) {error.setVisible(true); error.setText("Vælg en kunde"); return;}
        if (selectedPosition <= 0 && !model.getAnimalList().getAnimalById(selectedAnimalId).getCategory().equals(Animal.CATEGORY_DEFAULT)) {error.setVisible(true); error.setText("Vælg en position"); return;}

        if (selectedReservation == null) {
            // Tjek om der er plads
            int cases = model.getReservationList().checkForSpace(model.getAnimalList().getAnimalById(selectedAnimalId).getCategory(),new DateInterval(selectedStartDate, selectedEndDate), model.getAnimalList());
            if (cases != -1) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Der er ikke tilstrækkelig plads i den angivne periode. \nDu har allerede " + cases + " reservationer af den kategori i den periode. \nTryk 'Yes' for at forsætte alligevel.", ButtonType.YES, ButtonType.NO);
                confirmationAlert.setGraphic(null);
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setTitle("Grænsen for denne kategori er nået i denne periode");
                confirmationAlert.showAndWait();

                // Stop hvis bruger har valgt nej
                if (confirmationAlert.getResult() == ButtonType.NO) return;
            }

            // Ellers opret nyt dyr
            Reservation newReservation = new Reservation(selectedCustomerId, selectedAnimalId, new DateInterval(selectedStartDate, selectedEndDate));
            newReservation.setPositionId(selectedPosition);
            model.getReservationList().add(newReservation);
        } else {
            selectedReservation.set(selectedCustomerId, selectedAnimalId);
            selectedReservation.setPeriod(new DateInterval(selectedStartDate, selectedEndDate));
            selectedReservation.setPositionId(selectedPosition);
        }
        model.save();
        close();
    }

    /**
     * Action til at vælge dyr med select animal vælgeren
     */
    @FXML
    public void selectAnimal() {
        SelectAnimalController.load(model, animalId -> {
           this.selectedAnimalId = animalId;

           if (model.getAnimalList().getAnimalById(this.selectedAnimalId).isForSale()) {
               error.setVisible(true);
               error.setText("Dette dyr kan ikke vælges, da det er til salg");
               this.selectedAnimalId = -1;
           } else {
               error.setVisible(false);
               this.selectedCustomerId = model.getAnimalList().getAnimalById(animalId).getOwnerId();
           }

           update();
        }, false);
    }

    /**
     * Action til at oprette dyr direkte
     */
    @FXML
    public void createAnimal() {
        ManageAnimalController.load(model, -1, animalId -> {
            this.selectedAnimalId = animalId;
            this.selectedCustomerId = model.getAnimalList().getAnimalById(animalId).getOwnerId();
            update();
        }, false);
    }

    /**
     * Action til at vælge kunde med select customer vælgeren
     */
    @FXML
    public void selectCustomer() {
        SelectCustomerController.load(model, selectedCustomerId -> {
            this.selectedCustomerId = selectedCustomerId;
            update();
        });
    }
    
    /**
     * Action til at oprette medarbejder direkte
     */
    @FXML
    public void createCustomer() {
        ManageCustomerController.load(model, -1, customerId -> {
            this.selectedCustomerId = customerId;
            System.out.println(customerId);
            update();
        });
    }
}
