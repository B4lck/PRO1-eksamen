package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import model.CustomerList;
import model.VIAPetsModel;

import java.io.IOException;

public class SelectCustomerController {
    private Region root;
    private VIAPetsModel model;

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;

    private final ObservableList<Customer> list = FXCollections.observableArrayList();
    
    private CustomersFilteringController.CustomerFilter filter;

    private SelectedCustomerCallback selectedCustomerCallback;

    public void init(VIAPetsModel model, Region root, SelectedCustomerCallback callback) {
        this.model = model;
        this.root = root;
        this.selectedCustomerCallback = callback;

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Long.toString(cellData.getValue().getPhone())));

        reset();
    }

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
            e.printStackTrace();
        }
    }

    public void reset() {
        list.clear();
        CustomerList customers = model.getCustomerList();
        if (filter != null) customers = filter.filterList(customers);
        list.addAll(customers.getAllCustomers());
        customerTable.setItems(list);
    }

    @FunctionalInterface
    public interface SelectedCustomerCallback {
        void callback(int customerId);
    }

    public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    public void confirm() {
        selectedCustomerCallback.callback(getSelectedCustomerId());
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    public void filterCustomers() {
        CustomersFilteringController.load(model, filter -> {
            this.filter = filter;
            reset();
        });
    }

    public int getSelectedCustomerId() {
        // TODO
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        return selectedCustomer != null ? selectedCustomer.getCustomerId() : -1;
    }
}
