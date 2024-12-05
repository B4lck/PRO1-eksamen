package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import model.VIAPetsModelManager;

public class ManageCustomerController {
    private ViewHandler viewHandler;
    private Region root;

    @FXML
    public Label error;
    @FXML
    public Button selectEmployee;
    @FXML
    public Button createEmployee;

    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.root = root;
    }

    public void reset() {}

    public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        viewHandler.openView("Customers");
    }

    @FXML
    public void confirm() {
        viewHandler.openView("ManageCustomer");
    }
}
