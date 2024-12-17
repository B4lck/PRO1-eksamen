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

/**
 * Controller til at oprette og redigere salg
 */
public class ManageSaleController {
    private Region root;
    private VIAPetsModel model;

    private Sale selectedSale;

    private int selectedAnimalId = -1;
    private int selectedCustomerId = -1;
    private int selectedEmployeeId = -1;
    private double selectedFinalPrice;

    // Elementer
    @FXML
    private Text title;
    @FXML
    private Button selectCustomerButton;
    @FXML
    private Button selectAnimalButton;
    @FXML
    private Button selectEmployeeButton;
    @FXML
    private TextField priceTextField;
    @FXML
    private Label error;

    /**
     * Åbner view til at oprette og redigere salg
     *
     * @param model VIAPets Modellen
     * @param sale  Salget der skal redigeres, null for at oprette et nyt
     */
    public static void load(VIAPetsModel model, Sale sale) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ManageSaleController.class.getResource("/views/ManageSaleGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Opret/Rediger salg");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((ManageSaleController) loader.getController()).init(root, model, sale);
            stage.showAndWait();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Fejl");
            errorAlert.showAndWait();
        }
    }

    private void init(Region root, VIAPetsModel model, Sale sale) {
        this.model = model;
        this.root = root;

        if (sale != null) {
            // Hvis redigering af reservation
            // Sætter alt teksten til at være matchene for den åbnede reservation
            this.selectedSale = sale;
            this.selectedCustomerId = sale.getCustomerId();
            this.selectedAnimalId = sale.getAnimalId();
            this.selectedEmployeeId = sale.getEmployeeId();
            this.selectedFinalPrice = sale.getFinalPrice();
            this.priceTextField.setText(String.format("%.2f", selectedFinalPrice));
            title.setText("Redigering af salg");
        } else {
            title.setText("Oprettelse af salg");
        }

        update();

        this.error.setVisible(false);

        priceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                selectedFinalPrice = Double.parseDouble(newValue.replace(',', '.'));
                error.setVisible(false);
            } catch (NumberFormatException e) {
                selectedFinalPrice = -1;
                error.setVisible(true);
                error.setText("Indtast kun et tal");
            }
        });
    }

    /**
     * Opdater
     */
    private void update() {
        this.selectCustomerButton.setText(selectedCustomerId != -1 ? model.getCustomerList().getById(selectedCustomerId).getName() + "..." : "Vælg kunde");
        this.selectAnimalButton.setText(selectedAnimalId != -1 ? model.getAnimalList().getAnimalById(selectedAnimalId).getName() + "..." : "Vælg dyr");
        this.selectEmployeeButton.setText(selectedEmployeeId != -1 ? model.getEmployeeList().getById(selectedEmployeeId).getName() + "..." : "Vælg medarbejder");
        this.priceTextField.setText(String.format("%.2f", selectedFinalPrice));
    }

    /**
     * Action til at lukke/annullere viewet
     */
    @FXML
    public void close() {
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action til at oprette eller redigere et salg
     */
    @FXML
    public void confirm() {
        // Tjek for tomme inputs
        if (selectedFinalPrice == 0) {
            error.setVisible(true);
            error.setText("Indtast en pris");
            return;
        }
        if (selectedCustomerId == -1) {
            error.setVisible(true);
            error.setText("Vælg en kunde");
            return;
        }
        if (selectedAnimalId == -1) {
            error.setVisible(true);
            error.setText("Vælg et dyr");
            return;
        }
        if (selectedEmployeeId == -1) {
            error.setVisible(true);
            error.setText("Vælg en medarbejder");
            return;
        }

        if (selectedSale == null) {
            Sale newSale = new Sale(selectedFinalPrice, selectedAnimalId, selectedCustomerId, selectedEmployeeId, new Date());
            model.getSalesList().add(newSale);
        } else {
            selectedSale.set(selectedFinalPrice, selectedAnimalId, selectedCustomerId, selectedEmployeeId, selectedSale.getDateOfSale());
        }

        Animal animal = model.getAnimalList().getAnimalById(selectedAnimalId);
        animal.convertToOwnedAnimal(selectedCustomerId);

        model.getAnimalList().getAnimalById(selectedAnimalId).setOwnerId(selectedCustomerId);
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

            Animal animal = model.getAnimalList().getAnimalById(animalId);

            if (!animal.isForSale()) {
                this.selectedAnimalId = -1;
                error.setVisible(true);
                error.setText("Dette dyr er ikke til salg");
            } else {
                error.setVisible(false);
            }

            // Udfyld prisen på dyret
            this.selectedFinalPrice = animal.getPrice();

            update();
        }, true);
    }

    /**
     * Action til at oprette dyr direkte
     */
    @FXML
    public void createAnimal() {
        ManageAnimalController.load(model, -1, animalId -> {
            this.selectedAnimalId = animalId;
            update();
        }, true);
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
        ManageCustomerController.load(model, -1, selectedCustomerId -> {
            this.selectedCustomerId = selectedCustomerId;
            update();
        });
    }

    /**
     * Action til at vælge medarbejder med select employee vælgeren
     */
    @FXML
    public void selectEmployee() {
        SelectEmployeeController.load(model, selectedEmployeeId -> {
            this.selectedEmployeeId = selectedEmployeeId;
            update();
        });
    }
}
