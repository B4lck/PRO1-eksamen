package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

/**
 * View til valg af filtre for dyr oversigten
 */
public class SalesFilteringController {
    private Region root;
    private VIAPetsModel model;

    // Nuværende callback
    private FilteringCallback callback;

    /**
     * Lambda funktion der skal filtre en SalesList
     */
    @FunctionalInterface
    public interface SalesFilter {
        SalesList filterList(SalesList salesList);
    }

    /**
     * Lambda funktion der skal modtage en SalesList
     * Dette filter er resultatet af brugerens valg
     */
    @FunctionalInterface
    public interface FilteringCallback {
        void callback(SalesFilter filter);
    }

    int selectedOwner = -1;

    int selectedEmployee = -1;

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
    public TextField minimumPrice;
    @FXML
    public TextField maximumPrice;
    @FXML
    public Button ownerSelector;
    @FXML
    public ChoiceBox<String> categorySelector;
    @FXML
    public Button employeeSelector;
    

    /**
     * Åbner view til valg af filtre for salg
     * @param model    Modellen
     * @param callback Filteret returneres via et callback når brugeren har valgt muligheder og trykket OK
     */
    public static void load(VIAPetsModel model, FilteringCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SalesFilteringController.class.getResource("/views/SalesFilteringGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vælg filtering og sortering");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((SalesFilteringController) loader.getController()).init(root, model, callback);
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
    private void init(Region root, VIAPetsModel model, FilteringCallback callback) {
        this.root = root;
        this.model = model;
        this.callback = callback;
        
        startDate.setValue(LocalDate.now());
        startDate.setDisable(true);
        endDate.setValue(LocalDate.now());
        endDate.setDisable(true);
        
        categorySelector.setItems(FXCollections.observableArrayList(AnimalsFilteringController.categoryIdsToDisplay.values()));
        categorySelector.setValue(AnimalsFilteringController.categoryIdsToDisplay.get("Any"));
    }

    /**
     * Action til at toggle periode vælgeren
     */
    @FXML
    public void toggleDateFilter() {
        startDate.setDisable(!dateToggle.isSelected());
        endDate.setDisable(!dateToggle.isSelected());
    }
    
    /**
     * Action til at vælge ejer
     */
    @FXML
    public void selectOwner() {
        SelectCustomerController.load(model, customerId -> {
            selectedOwner = customerId;
            ownerSelector.setText(model.getCustomerList().getById(customerId).getName() + "...");
        });
    }

    /**
     * Action til at rydde valgt ejer
     */
    @FXML
    public void clearOwner() {
        selectedOwner = -1;
        ownerSelector.setText("Vælg ejer");
    }

    /**
     * Action til at vælge medarbejder
     */
    @FXML
    public void selectEmployee() {
        SelectEmployeeController.load(model, employeeId -> {
            selectedEmployee = employeeId;
            employeeSelector.setText(model.getEmployeeList().getById(employeeId).getName() + "...");
        });
    }

    /**
     * Action til at rydde valgt medarbejder
     */
    @FXML
    public void clearEmployee() {
        selectedEmployee = -1;
        employeeSelector.setText("Vælg medarbejder");
    }

    /**
     * Action til at nulstille filteret
     */
    @FXML
    public void clear() {
        this.callback.callback(null);
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action til at lukke/annullere viewet
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
        double minPrice;
        double maxPrice;
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        // Prisgruppe
        try {
            minPrice = format.parse(minimumPrice.getText()).doubleValue();
            maxPrice = format.parse(maximumPrice.getText()).doubleValue();
        }
        catch (ParseException e) {
            // Vis error alert
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Kunne ikke filtre i pris, ulovligt format.", ButtonType.YES, ButtonType.NO);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke filtre i pris");
            errorAlert.showAndWait();
            return;
        }
        
        this.callback.callback((salesList) -> {
            
            // Filtre efter periode
            if (dateToggle.isSelected()) {
                Date start = new Date(startDate.getValue());
                Date end = new Date(endDate.getValue());
                salesList = salesList.getSalesForPeriod(new DateInterval(start, end));
            }
            
            // Filtre efter pris
            salesList = salesList.getSalesByPrice(minPrice, maxPrice);
            
            // Filtre efter ejer
            if (selectedOwner != -1) {
                salesList = salesList.getSalesForCustomer(selectedOwner);
            }
            
            // Filtre efter medarbejder
            if (selectedEmployee != -1) {
                salesList = salesList.getSalesByEmployee(selectedEmployee);
            }
            
            // Filtre efter dyr type
            if (categorySelector.getValue() != null && !AnimalsFilteringController.categoryDisplayToIds.get(categorySelector.getValue()).equals("Any")) {
                salesList = salesList.getSalesByCategory(AnimalsFilteringController.categoryDisplayToIds.get(categorySelector.getValue()), model.getAnimalList());
            }
            
            return salesList;
        });

        ((Stage) root.getScene().getWindow()).close();
    }
}
