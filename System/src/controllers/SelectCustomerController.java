package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import model.CustomerList;
import model.VIAPetsModel;

import java.io.IOException;

/**
 * Controller til at vælge kunder
 */
public class SelectCustomerController {
    private Region root;
    private VIAPetsModel model;

    // Elementer
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;

    // Liste til tabellen
    private final ObservableList<Customer> list = FXCollections.observableArrayList();
    
    private CustomersFilteringController.CustomerFilter filter;
    private SelectedCustomerCallback selectedCustomerCallback;

    /**
     * Indlæser og åbner customer vælgeren
     *
     * @param model              Modellen
     * @param callback           Et callback, der returnere det valgte customerId
     */
    public static void load(VIAPetsModel model, SelectedCustomerCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SelectCustomerController.class.getResource("/views/SelectCustomerGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vælg kunde");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((SelectCustomerController) loader.getController()).init(model, root, callback);
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
    private void init(VIAPetsModel model, Region root, SelectedCustomerCallback callback) {
        this.model = model;
        this.root = root;
        this.selectedCustomerCallback = callback;

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Long.toString(cellData.getValue().getPhone())));

        reset();
    }

    /**
     * Opdatere tabellen
     */
    private void reset() {
        list.clear();
        CustomerList customers = model.getCustomerList();
        if (filter != null) customers = filter.filterList(customers);
        list.addAll(customers.getAllCustomers());
        customerTable.setItems(list);
    }

    /**
     * Callback der returnere det valgte kunde id
     */
    @FunctionalInterface
    public interface SelectedCustomerCallback {
        void callback(int customerId);
    }

    /**
     * Action til at lukke viewet
     */
    @FXML
    public void close() {
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action til at bekræfte valgte kunde
     */
    @FXML
    public void confirm() {
        selectedCustomerCallback.callback(getSelectedCustomerId());
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action til at filtre i kunder
     */
    @FXML
    public void filterCustomers() {
        CustomersFilteringController.load(model, filter -> {
            this.filter = filter;
            reset();
        });
    }

    /**
     * Hjælpemetode der giver id'et af det nuværende valg
     */
    public int getSelectedCustomerId() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        return selectedCustomer != null ? selectedCustomer.getCustomerId() : -1;
    }
}
