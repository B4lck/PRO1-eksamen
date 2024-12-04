package controllers;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import model.Employee;
import model.VIAPetsModel;
import model.VIAPetsModelManager;

public class EmployeesController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    @FXML
    private TableView<Employee> employeesTable;

    @FXML private TableColumn<Employee, String> nameColumn;
    @FXML private TableColumn<Employee, String> descriptionColumn;

    public EmployeesController() {}
    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;
    }
    public void reset(){

    }
    public Region getRoot() {
        return root;
    }

    public void back() {
        viewHandler.openView("MainMenu");
    }

    public void createEmployee() {}
    public void editEmployee() {}
    public void deleteEmployee() {}
}
