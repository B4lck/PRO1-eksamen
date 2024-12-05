package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import model.CustomerList;
import model.VIAPetsModelManager;

public class ManageCustomerController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    @FXML
    public Label error;
    @FXML
    public Button selectEmployee;
    @FXML
    public Button createEmployee;

    public void load(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.root = root;
        this.model = model;
    }

    public void reset() {
        // Hent kunder liste i model
        CustomerList Customer = model.getCustomerList();
    }

    public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        viewHandler.openView("Customers");
    }

    @FXML
    public void confirm() {
        reset();
        viewHandler.openView("ManageCustomer");
    }
}
