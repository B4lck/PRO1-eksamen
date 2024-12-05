package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import model.Animal;
import model.Customer;
import model.Reservation;
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
        ManageCustomerController.load(model,-1);
        reset();
    }

    @FXML
    public void editCustomer() {
        // TODO
        viewHandler.openView("MainMenu");
    }

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
            model.getCustomerList().removeById(selection.getCustomerId());

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, selection.getName() + " er slettet", ButtonType.OK);
            successAlert.setGraphic(null);
            successAlert.setHeaderText(null);
            successAlert.setTitle("Slet kunden");
            successAlert.show();

            model.save();

            reset();
        }
    }
