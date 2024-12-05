package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Animal;
import model.Customer;
import model.Employee;
import model.VIAPetsModel;

import java.io.IOException;

public class SelectEmployeeController {
    private Region root;
    private VIAPetsModel model;

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> descriptionColumn;

    private final ObservableList<Employee> list = FXCollections.observableArrayList();

    private SelectedEmployeeCallback selectedAnimalCallback;

    public void init(VIAPetsModel model, Region root, SelectedEmployeeCallback callback) {
        this.model = model;
        this.root = root;
        this.selectedAnimalCallback = callback;

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        reset();
    }

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

    public void reset() {
        list.clear();
        list.addAll(model.getEmployeeList().getAllEmployees());
        employeeTable.setItems(list);
    }

    @FunctionalInterface
    public interface SelectedEmployeeCallback {
        void callback(int animalId);
    }

    public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    public void confirm() {
        selectedAnimalCallback.callback(getSelectedAnimalId());
        ((Stage) root.getScene().getWindow()).close();
    }

    public int getSelectedAnimalId() {
        // TODO
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        return selectedEmployee != null ? selectedEmployee.getEmployeeId() : -1;
    }
}
