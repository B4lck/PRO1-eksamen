package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import model.Customer;
import model.VIAPetsModelManager;

public class CustomersController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;

    private final ObservableList<Customer> list = FXCollections.observableArrayList();

    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Long.toString(cellData.getValue().getPhone())));

        reset();
    }

    public void reset() {
        list.clear();
        list.addAll(model.getCustomerList().getList());
        customersTable.setItems(list);
    }

    public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        // TODO
        viewHandler.openView("MainMenu");
    }

    @FXML
    public void createCustomer() {
        viewHandler.openView("ManageCustomer");
    }

    @FXML
    public void editCustomer() {
        // TODO
        viewHandler.openView("MainMenu");
    }

    @FXML
    public void deleteCustomer() {
        // TODO
    }
}
