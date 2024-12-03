package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import model.VIAPetsModelManager;

public class CustomerController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    public CustomerController() {
    }

    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;
    }

    public void reset() {

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
        // TODO
        viewHandler.openView("MainMenu");
    }

    @FXML
    public void editCustomer() {
        // TODO
        viewHandler.openView("MainMenu");
    }
}
