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
import model.Employee;
import model.VIAPetsModel;

import java.io.IOException;

/**
 * Controller til at vælge employees
 */
public class SelectEmployeeController {
    private Region root;
    private VIAPetsModel model;

    // Elementer
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> descriptionColumn;

    // Liste til tabel
    private final ObservableList<Employee> list = FXCollections.observableArrayList();

    // Valgte callback
    private SelectedEmployeeCallback selectedAnimalCallback;
    
    /**
     * Indlæser og åbner employee vælgeren
     *
     * @param model              Modellen
     * @param callback           Et callback, der returnere det valgte employee id
     */
    public static void load(VIAPetsModel model, SelectedEmployeeCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SelectEmployeeController.class.getResource("/views/SelectEmployeeGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vælg medarbejder");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((SelectEmployeeController) loader.getController()).init(model, root, callback);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Init
     */
    private void init(VIAPetsModel model, Region root, SelectedEmployeeCallback callback) {
        this.model = model;
        this.root = root;
        this.selectedAnimalCallback = callback;

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        reset();
    }

    /**
     * Opdatere tabellen
     */
    private void reset() {
        list.clear();
        list.addAll(model.getEmployeeList().getAllEmployees());
        employeeTable.setItems(list);
    }

    /**
     * Callback der returnere det valgte medarbejder id
     */
    @FunctionalInterface
    public interface SelectedEmployeeCallback {
        void callback(int animalId);
    }

    /**
     * Action til at lukke viewet
     */
    @FXML
    public void close() {
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action til at bekræfte valgte medarbejder
     */
    @FXML
    public void confirm() {
        selectedAnimalCallback.callback(getSelectedAnimalId());
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Hjælpemetode der giver id'et af det nuværende valg
     */
    public int getSelectedAnimalId() {
        // TODO
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        return selectedEmployee != null ? selectedEmployee.getEmployeeId() : -1;
    }
}
