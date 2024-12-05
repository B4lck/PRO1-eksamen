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
import javafx.stage.Stage;
import model.Animal;
import model.VIAPetsModelManager;

import java.io.IOException;

public class SelectAnimalController {
    private Region root;
    private VIAPetsModelManager model;

    @FXML
    private TableView<Animal> animalsTable;
    @FXML
    private TableColumn<Animal, String> ownerNameColumn;
    @FXML
    private TableColumn<Animal, String> animalCategoryColumn;
    @FXML
    private TableColumn<Animal, String> animalNameColumn;
    @FXML
    private TableColumn<Animal, String> forSaleColumn;
    @FXML
    private TableColumn<Animal, String> priceColumn;

    private final ObservableList<Animal> list = FXCollections.observableArrayList();

    private SelectedAnimalCallback selectedAnimalCallback;

    public void init(VIAPetsModelManager model, Region root, SelectedAnimalCallback callback) {
        this.model = model;
        this.root = root;
        this.selectedAnimalCallback = callback;

        ownerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getCustomerList().getById(cellData.getValue().getOwnerId()).getName()));
        animalCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        animalNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        forSaleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isForSale()? "Til salg" : "Til pasning"));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isForSale()? Double.toString(cellData.getValue().getPrice()) : "-"));


        reset();
    }

    public static void load(VIAPetsModelManager model, SelectedAnimalCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SelectAnimalController.class.getResource("/views/SelectAnimalGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Vælg dyr");
            stage.setScene(new Scene(root, 500, 400));
            stage.show();
            ((SelectAnimalController) loader.getController()).init(model, root, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        list.clear();
        list.addAll(model.getAnimalList().getAllAnimals());
        animalsTable.setItems(list);
    }

    @FunctionalInterface
    public interface SelectedAnimalCallback {
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
        Animal selectedAnimal = animalsTable.getSelectionModel().getSelectedItem();
        return selectedAnimal != null ? selectedAnimal.getAnimalId() : -1;
    }
}
