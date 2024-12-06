package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.Sale;
import model.SalesList;
import model.VIAPetsModelManager;

public class SalesController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    // Elementer
    @FXML
    private Label filteringEnabledLabel;
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
    
    private SalesFilteringController.SalesFilter filter;

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

        SalesList salesList = model.getSalesList();
        if (filter != null) salesList = filter.filterList(salesList);
        
        list.addAll(salesList.getAllSales());

        salesTable.setItems(list);
    }

    public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        viewHandler.openView("MainMenu");
    }

    @FXML
    public void createSale() {
        ManageSaleController.load(model, null);
        reset();
    }

    @FXML
    public void editSale() {
        Sale selectedSale = salesTable.getSelectionModel().getSelectedItem();
        ManageSaleController.load(model, selectedSale);
        reset();
    }

    @FXML
    public void deleteSale() {
        Sale selectedSale = salesTable.getSelectionModel().getSelectedItem();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Er du sikker på du vil slette dette salg?", ButtonType.YES, ButtonType.NO);
        confirmation.setGraphic(null);
        confirmation.setHeaderText(null);
        confirmation.setTitle("Slet salg");
        confirmation.showAndWait();
        if (confirmation.getResult() == ButtonType.NO) return;

        model.getSalesList().remove(selectedSale);
        model.save();
        reset();
    }

    /**
     * Action til at åbne filter vælgeren
     */
    @FXML
    public void filterSales() {
        SalesFilteringController.load(model, (filter) -> {
            this.filter = filter;
            filteringEnabledLabel.setText(filter == null ? "" : "Aktivt filter");
            reset();
        });
    }
}
