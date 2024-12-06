package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Employee;
import model.VIAPetsModel;

import java.io.IOException;

/**
 * Controller til opret/rediger medarbejder
 */
public class ManageEmployeeController {
    private Region root;
    private VIAPetsModel model;

    private int employeeId;
    private ManageEmployeeCallback callback;
    
    // Elementer
    @FXML
    public Label error;
    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private Text title;

    /**
     * Callback der returnere nye/redigerede employeeId
     */
    @FunctionalInterface
    public interface ManageEmployeeCallback {
        void callback(int employeeId);
    }

    /**
     * Åbn view til oprettelse eller redigering af medarbejder
     * @param model VIAPetsModel
     * @param employeeId Medarbejderen der skal redigeres, eller -1 for at oprette ny
     * @param callback Callback der returnere nye/redigerede employeeId
     */
    public static void load(VIAPetsModel model, int employeeId, ManageEmployeeController.ManageEmployeeCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ManageEmployeeController.class.getResource("/views/ManageEmployeeGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Opret/Rediger medarbejder");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((ManageEmployeeController) loader.getController()).init(root, model, employeeId, callback);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Init viewet
     */
    private void init(Region root, VIAPetsModel model, int employeeId, ManageEmployeeCallback callback) {
        this.root = root;
        this.model = model;
        this.employeeId = employeeId;
        this.callback = callback;

        this.error.setVisible(false);

        if (employeeId != -1) {
            this.name.setText(model.getEmployeeList().getById(employeeId).getName());
            this.description.setText(model.getEmployeeList().getById(employeeId).getDescription());
            this.title.setText("Rediger: " + model.getEmployeeList().getById(employeeId).getName());
        }
    }

    /**
     * Action til at lukke viewet
     */
    @FXML
    public void close() {
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action til at oprette eller redigere medarbejder
     */
    @FXML
    public void confirm() {
        // Tjek for tomme inputs
        if (name.getText().isEmpty()) {error.setVisible(true); error.setText("Indtast et navn"); return;}
        if (description.getText().isEmpty()) {error.setVisible(true); error.setText("Indtast en beskrivelse"); return;}

        if (employeeId == -1) {
            Employee employee = model.getEmployeeList().createNewEmployee(name.getText());
            employee.setDescription(description.getText());
            model.getEmployeeList().add(employee);
            this.employeeId = employee.getEmployeeId();
        } else {
            Employee employee = model.getEmployeeList().getById(employeeId);
            employee.setName(name.getText());
            employee.setDescription(description.getText());
        }

        model.save();
        callback.callback(employeeId);
        ((Stage) root.getScene().getWindow()).close();
    }
}

