package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.*;

import java.util.Collections;

/**
 * Controller til AnimalsGUI - Oversigten over dyr
 */
public class AnimalsController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    // Knapper
    @FXML
    private Button deleteAnimalButton;
    @FXML
    private Button editAnimalButton;
    @FXML
    private Label filteringEnabledLabel;
    // Tabel
    @FXML
    private TableView<Animal> animalsTable;
    @FXML
    private TableColumn<Animal, String> ownerColumn;
    @FXML
    private TableColumn<Animal, String> categoryColumn;
    @FXML
    private TableColumn<Animal, String> nameColumn;
    @FXML
    private TableColumn<Animal, String> foodColumn;
    @FXML
    private TableColumn<Animal, String> forSaleColumn;
    @FXML
    private TableColumn<Animal, String> priceColumn;
    @FXML
    private TableColumn<Animal, String> waterColumn;
    @FXML
    private TableColumn<Animal, String> venomousColumn;
    @FXML
    private TableColumn<Animal, String> tameColumn;
    @FXML
    private TableColumn<Animal, String> commentColumn;

    /**
     * ObservableList som tabellen kan læse
     */
    private final ObservableList<Animal> list = FXCollections.observableArrayList();

    /**
     * Det nuværende filter
     */
    private AnimalsFilteringController.AnimalFilter currentFilter;

    /**
     * Init
     */
    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        // Data til tekst til cellerne
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ownerColumn.setCellValueFactory(cellData -> {
            Animal animal = cellData.getValue();
            if (animal.isForSale()) return new SimpleStringProperty("-");
            Customer customer = model.getCustomerList().getById(animal.getOwnerId());
            if (customer == null) return new SimpleStringProperty("Kunde findes ikke");
            return new SimpleStringProperty(customer.getName());
        });
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(AnimalsFilteringController.categoryIdsToDisplay.get(cellData.getValue().getCategory())));
        foodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFood()));
        forSaleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isForSale() ? "Til salg" : "Til pasning"));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isForSale() ? String.format("%.2f kr.", cellData.getValue().getPrice()) : "-"));
        waterColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue() instanceof AnimalFish ? (((AnimalFish) cellData.getValue()).isFreshWater() ? "Ferskvand" : "Saltvand") : "-"));
        venomousColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue() instanceof AnimalReptile ? (((AnimalReptile) cellData.getValue()).isVenomous() ? "✓" : "✕") : "-"));
        tameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue() instanceof AnimalBird ? (((AnimalBird) cellData.getValue()).isTamed() ? "✓" : "✕") : "-"));
        commentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComment()));

        // Aktiver delete og edit knapper når en række vælges
        animalsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            deleteAnimalButton.setDisable(newValue == null);
            editAnimalButton.setDisable(newValue == null);
        });

        // Opdater tabel
        reset();
    }

    /**
     * Resetter view (opdater tabellen)
     */
    public void reset() {
        // Hent animals liste i model
        AnimalList animals = model.getAnimalList();
        // Filtre listen hvis der er et filter valgt
        if (currentFilter != null) animals = currentFilter.filterList(animals);
        // Opdater liste
        list.clear();
        Collections.addAll(list, animals.getAllAnimals());
        animalsTable.setItems(list);
    }

    /**
     * Henter roden
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
     * Action til at oprette dyr
     */
    @FXML
    public void createAnimal() {
        viewHandler.openView("ManageAnimal");
    }

    /**
     * Action til at slette dyr
     */
    @FXML
    public void deleteAnimal() {
        Animal selection = animalsTable.getSelectionModel().getSelectedItem();
        // Vis confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Er du sikker på at du vil slette " + selection.getName() + "?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.setGraphic(null);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setTitle("Slet dyr");
        confirmationAlert.showAndWait();

        // Stop hvis bruger har valgt nej
        if (confirmationAlert.getResult() == ButtonType.NO) return;

        // Stop hvis dyret har en fremtidig reservation
        Reservation[] futureReservations = model.getReservationList().getReservationsForAnimal(selection.getAnimalId()).getReservationsInFuture().getAllReservations();
        if (futureReservations.length > 0) {

            Alert successAlert = new Alert(Alert.AlertType.ERROR,
                    selection.getName() + " kunne ikke slettes, da dyret har en reservation fra " + futureReservations[0].getPeriod().getStartDate().toString() + " til " + futureReservations[0].getPeriod().getEndDate().toString() + ".", ButtonType.OK);
            successAlert.setGraphic(null);
            successAlert.setHeaderText(null);
            successAlert.setTitle("Slet dyr");
            successAlert.show();

            return;
        }

        // Slet dyr
        model.getAnimalList().removeById(selection.getAnimalId());

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, selection.getName() + " er slettet", ButtonType.OK);
        successAlert.setGraphic(null);
        successAlert.setHeaderText(null);
        successAlert.setTitle("Slet dyr");
        successAlert.show();

        reset();
    }

    /**
     * Action til at redigere valgt dyr
     */
    @FXML
    public void editAnimal() {
        viewHandler.openView("ManageAnimal", animalsTable.getSelectionModel().getSelectedItem().getAnimalId());
    }

    /**
     * Action til at åbne filter vælgeren
     */
    @FXML
    public void filterAnimals() {
        AnimalsFilteringController.load(model, (filter) -> {
            currentFilter = filter;
            filteringEnabledLabel.setText(currentFilter == null ? "" : "Aktivt filter");
            reset();
        });
    }
}
