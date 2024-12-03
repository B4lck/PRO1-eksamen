package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import model.AnimalList;
import model.VIAPetsModelManager;

public class AnimalsController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    @FXML
    private TableView<AnimalModel> animalsTable;

    @FXML
    private TableColumn<AnimalModel, String> ownerColumn;
    @FXML
    private TableColumn<AnimalModel, String> categoryColumn;
    @FXML
    private TableColumn<AnimalModel, String> nameColumn;
    @FXML
    private TableColumn<AnimalModel, String> foodColumn;
    @FXML
    private TableColumn<AnimalModel, String> forSaleColumn;
    @FXML
    private TableColumn<AnimalModel, String> priceColumn;
    @FXML
    private TableColumn<AnimalModel, String> waterColumn;
    @FXML
    private TableColumn<AnimalModel, String> venomousColumn;
    @FXML
    private TableColumn<AnimalModel, String> tameColumn;
    @FXML
    private TableColumn<AnimalModel, String> commentColumn;

    private AnimalListModel animalListModel;

    public AnimalsController() {
    }

    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;
        animalListModel = new AnimalListModel(model);

        nameColumn.setCellValueFactory(celldata -> celldata.getValue().getName());
        ownerColumn.setCellValueFactory(celldata -> celldata.getValue().getOwner());
        categoryColumn.setCellValueFactory(celldata -> celldata.getValue().getCategory());
        foodColumn.setCellValueFactory(celldata -> celldata.getValue().getFood());
        forSaleColumn.setCellValueFactory(celldata -> celldata.getValue().getForSale());
        priceColumn.setCellValueFactory(celldata -> celldata.getValue().getPrice());
        waterColumn.setCellValueFactory(celldata -> celldata.getValue().getWater());
        venomousColumn.setCellValueFactory(celldata -> celldata.getValue().getVenomous());
        tameColumn.setCellValueFactory(celldata -> celldata.getValue().getTamed());


        animalsTable.setItems(animalListModel.getList());
        animalListModel.update();
    }

    public void reset() {
        animalListModel.update();
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
