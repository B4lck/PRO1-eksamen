package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private ObservableList<Customer> list;

    public CustomersController() {
    }

    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Long.toString(cellData.getValue().getPhone())));

        reset();

        customersTable.setItems(list);
    }

    public void reset() {
        list = FXCollections.observableArrayList(model.getCustomerList().getList());
    }

    public Region getRoot() {
        return root;
    }

    public void back() {
        // TODO
        viewHandler.openView("MainMenu");
    }

    public void createCustomer() {
        // TODO
        viewHandler.openView("MainMenu");
    }

    public void editCustomer() {
        // TODO
        viewHandler.openView("MainMenu");
    }

    public void deleteCustomer() {
        // TODO
    }
}
