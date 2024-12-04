package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import model.Employee;
import model.VIAPetsModelManager;

public class ManageEmployeeController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;
    @FXML
    public Label error;
    @FXML
    public TextField birthday;
    @FXML
    public Button selectEmployee;
    @FXML
    public Button createEmployee;

public ManageEmployeeController() {
    }

public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;
    }

public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        viewHandler.openView("MainMenu");
    }

    @FXML
    public void createEmployee() {
        viewHandler.openView("ManageEmployee");
    }

    @FXML
    public void editEmployee() {

    }

}
