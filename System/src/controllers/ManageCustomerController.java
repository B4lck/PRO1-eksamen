package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import model.VIAPetsModel;

import java.io.IOException;

public class ManageCustomerController {
    private Region root;
    private VIAPetsModel model;
    private int customerId;

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
     *
     */
    @FunctionalInterface
    public interface ManageCustomerCallback {
        void callback(int customerId);
    }

    private ManageCustomerCallback callback;

    public void init(Region root, VIAPetsModel model, int customerId, ManageCustomerCallback callback) {
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

    public static void load(VIAPetsModel model, int customerId, ManageCustomerCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ManageCustomerController.class.getResource("/views/ManageCustomer.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Opret/Rediger reservation");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((ManageCustomerController) loader.getController()).init(root, model, customerId, callback);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        // Tjek for tomme input
        if (this.name.getText().equals("")) {error.setVisible(true); error.setText("Indtast et navn"); return;}
        if (this.mail.getText().equals("")) {error.setVisible(true); error.setText("Indtast en email adresse"); return;}
        if (this.phone.getText().equals("")) {error.setVisible(true); error.setText("Indtast et tlf nummer"); return;}


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
