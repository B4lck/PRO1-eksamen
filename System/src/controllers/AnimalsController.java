package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import model.*;

public class AnimalsController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

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

    private ObservableList<Animal> list;

    public AnimalsController() {
    }

    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ownerColumn.setCellValueFactory(cellData -> {
            Animal animal = cellData.getValue();
            if (animal.isForSale()) return new SimpleStringProperty("-");
            Customer customer = model.getCustomerList().getById(animal.getOwnerId());
            if (customer == null) return new SimpleStringProperty("Kunde findes ikke");
            return new SimpleStringProperty(customer.getName());
        });
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        foodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFood()));
        forSaleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isForSale() ? "Til salg" : "Til pasning"));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isForSale() ? String.format("%.2f kr.", cellData.getValue().getPrice()) : "-"));
        waterColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue() instanceof AnimalFish ? (((AnimalFish) cellData.getValue()).isFreshWater() ? "Ferskvand" : "Saltvand") : "-"));
        venomousColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue() instanceof AnimalReptile ? (((AnimalReptile) cellData.getValue()).isVenomous() ? "Giftig" : "Ikke giftig") : "-"));
        tameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue() instanceof AnimalBird ? (((AnimalBird) cellData.getValue()).isTamed() ? "Tæmmet" : "Ikke tæmmet") : "-"));
        commentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComment()));

        reset();

        animalsTable.setItems(list);
    }

    public void reset() {
        list = FXCollections.observableArrayList(model.getAnimalList().getList());
    }

    public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        viewHandler.openView("MainMenu");
    }

    @FXML
    public void createAnimal() {
        viewHandler.openView("ManageAnimal");
    }

    @FXML
    public void editAnimal() {
        viewHandler.openView("ManageAnimal");
    }
}
