package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import model.Sale;
import model.VIAPetsModelManager;

public class SalesController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    @FXML
    private TableView<Sale> salesTable;
    @FXML
    private TableColumn<Sale, String> customerNameColumn;
    @FXML
    private TableColumn<Sale, String> animalCategoryColumn;
    @FXML
    private TableColumn<Sale, String> animalNameColumn;
    @FXML
    private TableColumn<Sale, String> dateColumn;
    @FXML
    private TableColumn<Sale, String> employeeColumn;
    @FXML
    private TableColumn<Sale, String> priceColumn;

    private final ObservableList<Sale> list = FXCollections.observableArrayList();

    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        customerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getCustomerList().getById(cellData.getValue().getCustomerId()).getName()));
        animalCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getAnimalList().getAnimalById(cellData.getValue().getAnimalId()).getCategory()));
        animalNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getAnimalList().getAnimalById(cellData.getValue().getAnimalId()).getName()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOfSale().toString()));
        employeeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getEmployeeList().getById(cellData.getValue().getEmployeeId()).getName()));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Double.toString(cellData.getValue().getFinalPrice())));

        reset();
    }

    public void reset() {
        list.clear();
        list.addAll(model.getSalesList().getList());

        salesTable.setItems(list);
    }

    public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        // TODO
        viewHandler.openView("MainMenu");
    }

    @FXML
    public void createSale() {
        // TODO
    }

    @FXML
    public void editSale() {
        // TODO
    }

    @FXML
    public void deleteSale() {
        // TODO
    }
}
