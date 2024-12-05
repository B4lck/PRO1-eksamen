package controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

public class ManageReservationController {
    private Region root;
    private VIAPetsModel model;

    private Reservation selectedReservation;

    private int selectedAnimalId = -1;
    private int selectedCustomerId = -1;
    private Date selectedStartDate;
    private Date selectedEndDate;
    private int selectedPosition;

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

    public static void load(VIAPetsModelManager model, Reservation reservation) {
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
            e.printStackTrace();
        }
    }

    public void init(Region root, VIAPetsModelManager model, Reservation reservation) {
        this.model = model;
        this.root = root;

        this.reset(reservation);

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
            } catch (NumberFormatException e) {
                selectedPosition = 0;
                error.setVisible(true);
                error.setText("Indtast kun et hel tal");
            }
        });
    }

    public void update() {
        this.selectedStartDate = new Date(dateStart.getValue());
        this.selectedEndDate = new Date(dateEnd.getValue());
        this.selectedPosition = Integer.parseInt(positionSelector.getText());

        if (selectedStartDate == null) {selectedStartDate = new Date();}
        if (selectedEndDate == null) {selectedEndDate = new Date();}

        this.selectCustomerButton.setText(selectedCustomerId != -1 ?model.getCustomerList().getById(selectedCustomerId).getName() + "..." : "Vælg kunde");
        this.selectAnimalButton.setText(selectedAnimalId != -1 ? model.getAnimalList().getAnimalById(selectedAnimalId).getName() + "..." : "Vælg dyr");
    }

    public void reset(Reservation reservation) {
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
        }
        update();
    }


    @FXML
    public void close() {
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    public void confirm() {
        if (selectedReservation == null) {
            Reservation newReservation = new Reservation(selectedCustomerId, selectedAnimalId, new DateInterval(selectedStartDate, selectedEndDate));
            model.getReservationList().add(newReservation);
        } else {
            selectedReservation.set(selectedCustomerId, selectedAnimalId);
            selectedReservation.setPeriod(new DateInterval(selectedStartDate, selectedEndDate));
            selectedReservation.setPositionId(selectedPosition);
        }
        close();
    }

    @FXML
    public void selectAnimal() {

    }

    @FXML
    public void createAnimal() {

    }

    @FXML
    public void selectCustomer() {

    }

    @FXML
    public void createCustomer() {

    }
}
