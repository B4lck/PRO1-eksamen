package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
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

    public void reset() {
        if (customerId != -1) {
            this.name.setText(model.getCustomerList().getById(customerId).getName());
            this.phone.setText(Long.toString(model.getCustomerList().getById(customerId).getPhone()));
            this.mail.setText(model.getCustomerList().getById(customerId).getEmail());
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
        Customer c = model.getCustomerList().createNewCustomer(this.name.getText(), Long.parseLong(this.phone.getText()), this.mail.getText());
        model.getCustomerList().add(c);
        model.save();
        callback.callback(customerId);
        ((Stage) root.getScene().getWindow()).close();
    }
}
