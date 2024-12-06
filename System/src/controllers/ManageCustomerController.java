package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import model.VIAPetsModel;

import java.io.IOException;

/**
 * Controller til opret/rediger kunde
 */
public class ManageCustomerController {
    private Region root;
    private VIAPetsModel model;
    private int customerId;

    // Elementer
    @FXML
    public Label error;
    @FXML
    private TextField name;
    @FXML
    private TextField phone;
    @FXML
    private TextField mail;
    @FXML
    private Text title;

    /**
     * Callback der returnere det nye eller redigerede kundeid
     */
    @FunctionalInterface
    public interface ManageCustomerCallback {
        void callback(int customerId);
    }

    private ManageCustomerCallback callback;

    /**
     * Åbn opret/rediger kunde view
     * @param customerId KundeID: -1 for at oprette kunde, ellers redigere den kunden med id'et
     * @param callback Returnere det nye eller redigere kundeid
     */
    public static void load(VIAPetsModel model, int customerId, ManageCustomerCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ManageCustomerController.class.getResource("/views/ManageCustomerGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Opret/Rediger reservation");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((ManageCustomerController) loader.getController()).init(root, model, customerId, callback);
            stage.showAndWait();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Fejl");
            errorAlert.showAndWait();
        }
    }

    /**
     * Init
     */
    private void init(Region root, VIAPetsModel model, int customerId, ManageCustomerCallback callback) {
        this.root = root;
        this.model = model;
        this.customerId = customerId;
        this.callback = callback;

        this.error.setVisible(false);

        if (customerId != -1) {
            this.name.setText(model.getCustomerList().getById(customerId).getName());
            this.phone.setText(Long.toString(model.getCustomerList().getById(customerId).getPhone()));
            this.mail.setText(model.getCustomerList().getById(customerId).getEmail());
            this.title.setText("Rediger: " + model.getCustomerList().getById(customerId).getName());
        }
    }

    /**
     * Action til at annullere/lukke viewet
     */
    @FXML
    public void close() {
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action til at oprette kunde
     */
    @FXML
    public void confirm() {
        // Tjek for tomme input
        if (this.name.getText().isEmpty()) {error.setVisible(true); error.setText("Indtast et navn"); return;}
        if (this.mail.getText().isEmpty()) {error.setVisible(true); error.setText("Indtast en email adresse"); return;}
        if (this.phone.getText().isEmpty()) {error.setVisible(true); error.setText("Indtast et tlf nummer"); return;}

        long phoneParsed;
        try {
            phoneParsed = Long.parseLong(this.phone.getText());
        } catch (Exception e) {
            error.setVisible(true);
            error.setText("Indtast et gyldigt tlf nummer");
            return;
        }

        if (customerId == -1) {
            Customer c = model.getCustomerList().createNewCustomer(this.name.getText(), phoneParsed, this.mail.getText());
            customerId = c.getCustomerId();
            model.getCustomerList().add(c);
        } else {
            Customer c = model.getCustomerList().getById(customerId);
            c.setPhone(phoneParsed);
            c.setEmail(mail.getText());
            c.setName(name.getText());
        }

        model.save();
        callback.callback(customerId);
        ((Stage) root.getScene().getWindow()).close();
    }
}
