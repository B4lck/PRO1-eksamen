package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

/**
 * View til valg af filtre for dyr oversigten
 */
public class CustomersFilteringController {
    private Region root;
    private VIAPetsModel model;

    // Nuværende callback
    private FilteringCallback callback;

    /**
     * Lambda funktion der skal filtre en CustomerList
     */
    @FunctionalInterface
    public interface CustomerFilter {
        CustomerList filterList(CustomerList customerList);
    }

    /**
     * Lambda funktion der skal modtage en CustomerList
     * Dette filter er resultatet af brugerens valg
     */
    @FunctionalInterface
    public interface FilteringCallback {
        void callback(CustomerFilter filter);
    }

    /**
     * De forskellige inputs fra FXML
     */
    @FXML
    public TextField nameField;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField emailField;

    /**
     * Indlæser og åbner AnimalsFilteringGUI
     *
     * @param model    Modellen
     * @param callback Filteret returneres via et callback når brugeren har valgt muligheder og trykket OK
     */
    public static void load(VIAPetsModel model, FilteringCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CustomersFilteringController.class.getResource("/views/CustomersFilteringGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vælg filtering og sortering");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((CustomersFilteringController) loader.getController()).init(root, model, callback);
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
            if (!nameField.getText().isEmpty())
                customerList = customerList.getCustomersByName(nameField.getText());
            if (!phoneField.getText().isEmpty())
                customerList = customerList.getCustomersByPhone(Long.parseLong(phoneField.getText()));
            if (!emailField.getText().isEmpty())
                customerList = customerList.getCustomersByEmail(emailField.getText());


            return customerList;
        });

        ((Stage) root.getScene().getWindow()).close();
    }
}
