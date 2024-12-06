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
import model.Animal;
import model.AnimalList;
import model.Customer;
import model.VIAPetsModel;

import java.io.IOException;

/**
 * Controller til at vælge animals
 */
public class SelectAnimalController {
    private Region root;
    private VIAPetsModel model;

    // Elementer
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

    // Liste til tabellen
    private final ObservableList<Animal> list = FXCollections.observableArrayList();

    private SelectedAnimalCallback selectedAnimalCallback;
    private AnimalsFilteringController.AnimalFilter filter;
    private Boolean forceSaleOrPension;

    /**
     * Indlæser og åbner animal vælgeren
     *
     * @param model              Modellen
     * @param callback           Et callback, der returnere det valgte animalId
     * @param forceSaleOrPension Hvis null, slås denne funktion fra. Hvis true SKAL man vælge et dyr til salg, og hvis false SKAL man vælge et dyr til pasning.
     */
    public static void load(VIAPetsModel model, SelectedAnimalCallback callback, Boolean forceSaleOrPension) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SelectAnimalController.class.getResource("/views/SelectAnimalGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vælg dyr");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((SelectAnimalController) loader.getController()).init(model, root, callback, forceSaleOrPension);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Init
     */
    private void init(VIAPetsModel model, Region root, SelectedAnimalCallback callback, Boolean forceSaleOrPension) {
        this.model = model;
        this.root = root;
        this.selectedAnimalCallback = callback;
        this.forceSaleOrPension = forceSaleOrPension;

        if (forceSaleOrPension != null)
            filter = animalList -> forceSaleOrPension ? animalList.getAnimalsForSale() : animalList.getAnimalsForPension();

        ownerNameColumn.setCellValueFactory(cellData -> {
            Animal animal = cellData.getValue();
            if (animal.isForSale()) return new SimpleStringProperty("-");
            Customer customer = model.getCustomerList().getById(animal.getOwnerId());
            if (customer == null) return new SimpleStringProperty("Kunde findes ikke");
            return new SimpleStringProperty(customer.getName());
        });
        animalCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        animalNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        forSaleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isForSale() ? "Til salg" : "Til pasning"));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isForSale() ? Double.toString(cellData.getValue().getPrice()) : "-"));


        reset();
    }

    /**
     * Opdatere tabellen
     */
    private void reset() {
        list.clear();
        AnimalList animals = model.getAnimalList();
        if (filter != null) animals = filter.filterList(animals);
        list.addAll(animals.getAllAnimals());
        animalsTable.setItems(list);
    }

    /**
     * Callback der returnere det valgte dyr id
     */
    @FunctionalInterface
    public interface SelectedAnimalCallback {
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
     * Action til at bekræfte valgte dyr
     */
    @FXML
    public void confirm() {
        selectedAnimalCallback.callback(getSelectedAnimalId());
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action til at filtre i dyr
     */
    @FXML
    void pickFilters() {
        AnimalsFilteringController.load(model, (filter) -> {
            this.filter = filter;
            reset();
        }, forceSaleOrPension);
    }

    /**
     * Hjælpemetode der giver id'et af det nuværende valg
     */
    private int getSelectedAnimalId() {
        Animal selectedAnimal = animalsTable.getSelectionModel().getSelectedItem();
        return selectedAnimal != null ? selectedAnimal.getAnimalId() : -1;
    }
}
