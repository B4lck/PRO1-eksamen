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
import model.Employee;
import model.VIAPetsModelManager;

/**
 * Controller til medarbejder oversigt
 */
public class EmployeesController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    // Elementer
    @FXML
    private TableView<Employee> employeesTable;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> descriptionColumn;

    // Liste til tabel
    private final ObservableList<Employee> list = FXCollections.observableArrayList();

    /**
     * Init viewet
     * @param viewHandler ViewHandler
     * @param model VIAPets Modellen
     * @param root Roden
     */
    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        reset();
    }

    /**
     * Opdater tabellen
     */
    public void reset() {
        list.clear();
        list.addAll(model.getEmployeeList().getAllEmployees());
        employeesTable.setItems(list);
    }

    /**
     * Returnere roden
     */
    public Region getRoot() {
        return root;
    }

    /**
     * Action til at gå tilbage til hovedmenuen
     */
    @FXML
    public void back() {
        viewHandler.openView("MainMenu");
    }

    /**
     * Action til at oprette medarbejdere
     */
    @FXML
    public void createEmployee() {
        ManageEmployeeController.load(model, -1, costumerId -> {
            reset();
            // Vælger den nye medarbejder i tabellen
            employeesTable.getSelectionModel().select(model.getEmployeeList().getById(costumerId));
        });
    }

    /**
     * Action til at redigere medarbejdere
     */
    @FXML
    public void editEmployee() {
        int selectedEmployeeId = employeesTable.getSelectionModel().getSelectedItem().getEmployeeId();
        ManageEmployeeController.load(model, selectedEmployeeId, costumerId -> {
            reset();
            // Vælger den redigerede medarbejder i tabellen
            employeesTable.getSelectionModel().select(model.getEmployeeList().getById(costumerId));
        });
    }

    /**
     * Action til at slette medarbejdere
     */
    @FXML
    public void deleteEmployee() {
        Employee selectedEmployee = employeesTable.getSelectionModel().getSelectedItem();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Er du sikker på at du vil slette: " + selectedEmployee.getName(), ButtonType.YES, ButtonType.NO);
        confirmation.setGraphic(null);
        confirmation.setHeaderText(null);
        confirmation.setTitle("Slet Medarbejder");
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.NO) {return;}

        model.getEmployeeList().remove(selectedEmployee);
        model.save();
        reset();
    }
}
