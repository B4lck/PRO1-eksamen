package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.*;

/**
 * Controller til oversigt af kunder
 */
public class CustomersController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModel model;

    // Elementer
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    public Label filteringEnabledLabel;

    // Liste til tabel
    private final ObservableList<Customer> list = FXCollections.observableArrayList();

    // Nuværende filter
    private CustomersFilteringController.CustomerFilter filter;

    /**
     * Init
     */
    public void init(ViewHandler viewHandler, VIAPetsModel model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Long.toString(cellData.getValue().getPhone())));

        reset();
    }

    /**
     * Opdatere tabellen
     */
    public void reset() {
        list.clear();
        CustomerList customerList = model.getCustomerList();
        if (filter != null) customerList = filter.filterList(customerList);
        list.addAll(customerList.getAllCustomers());
        customersTable.setItems(list);
    }

    /**
     * Returnere roden
     */
    public Region getRoot() {
        return root;
    }

    /**
     * Action til at gå tilbage til hovedmenuen
     */
    @FXML
    public void back() {
        viewHandler.openView("MainMenu");
    }

    /**
     * Action til at oprette kunder
     */
    @FXML
    public void createCustomer() {
        ManageCustomerController.load(model, -1, customerId -> {
            reset();
            // Vælger den nye kunde i tabellen
            customersTable.getSelectionModel().select(model.getCustomerList().getById(customerId));
        });
    }

    /**
     * Action til at ændre en kunde
     */
    @FXML
    public void editCustomer() {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        ManageCustomerController.load(model, selectedCustomer.getCustomerId(), customerId -> {
            reset();
            // Vælger den redigerede kunde i tabellen
            customersTable.getSelectionModel().select(model.getCustomerList().getById(customerId));
        });
    }

    /**
     * Action til at slette kunde
     */
    @FXML
    public void deleteCustomer() {
        Customer selection = customersTable.getSelectionModel().getSelectedItem();
        // Vis confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Er du sikker på at du vil slette " + selection.getName() + "?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.setGraphic(null);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setTitle("Slet Kunden");
        confirmationAlert.showAndWait();

        // Stop hvis bruger har valgt nej
        if (confirmationAlert.getResult() == ButtonType.NO) return;

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, selection.getName() + " er slettet", ButtonType.OK);
        successAlert.setGraphic(null);
        successAlert.setHeaderText(null);
        successAlert.setTitle("Slet kunden");
        successAlert.show();

        Reservation[] futureReservations = model.getReservationList().getReservationsForOwner(selection.getCustomerId()).getReservationsInFuture().getAllReservations();
        if (futureReservations.length > 0) {
            Alert warning = new Alert(Alert.AlertType.INFORMATION,
                    selection.getName() + " kunne ikke slettes, da de har en reservation i fremtiden", ButtonType.OK);
            warning.setGraphic(null);
            warning.setHeaderText(null);
            warning.setTitle("Kunne ikke slettes");
            warning.show();
            return;
        }
        model.getCustomerList().removeById(selection.getCustomerId());

        model.save();

        reset();
    }

    /**
     * Action til at åbne filter vælgeren
     */
    @FXML
    public void filterCustomers() {
        CustomersFilteringController.load(model, (filter) -> {
            this.filter = filter;
            filteringEnabledLabel.setText(filter == null ? "" : "Aktivt filter");
            reset();
        });
    }
}
