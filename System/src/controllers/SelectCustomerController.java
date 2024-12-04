package controllers;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Customer;
import model.VIAPetsModelManager;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;

public class SelectCustomerController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;
    private Stage stage;

    @FXML
    private TableView<Customer> customerTable;

    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> emailColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;

    private ObservableList<Customer> list = FXCollections.observableArrayList();

    private SelectedCustomerCallback selectedCustomerCallback;

    public SelectCustomerController() {}

    public void init(VIAPetsModelManager model, Stage stage, SelectedCustomerCallback callback) {
        this.model = model;
        this.stage = stage;
        this.selectedCustomerCallback = callback;

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Long.toString(cellData.getValue().getPhone())));

        reset();
    }

    public void loadSelf(VIAPetsModelManager model, SelectedCustomerCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/SelectCustomerGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Vælg kunde");
            stage.setScene(new Scene(root, 400, 400));
            stage.show();
            this.model = model;
            this.root = root;

            SelectCustomerController controller = loader.getController();
            controller.init(model, stage, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset(){
        list.clear();
        list.addAll(model.getCustomerList().getList());
        customerTable.setItems(list);
    }

    @FunctionalInterface
    public interface SelectedCustomerCallback {
        void callback(int customerId);
    }

    public Region getRoot() {
        return root;
    }

    public void back() {
        stage.close();
    }

    public void confirm() {
        // TODO
        selectedCustomerCallback.callback(getSelectedCustomerId());
        stage.close();
    }

    public int getSelectedCustomerId() {
        // TODO
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        return selectedCustomer != null ? selectedCustomer.getCustomerId() : -1;
    }
}
