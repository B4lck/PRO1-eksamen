package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.Date;
import model.Reservation;
import model.ReservationList;
import model.VIAPetsModelManager;

public class ReservationsController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    @FXML
    public Label filteringEnabledLabel;
    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableColumn<Reservation, String> customerNameColumn;
    @FXML
    private TableColumn<Reservation, String> animalCategoryColumn;
    @FXML
    private TableColumn<Reservation, String> animalNameColumn;
    @FXML
    private TableColumn<Reservation, String> startDateColumn;
    @FXML
    private TableColumn<Reservation, String> endDateColumn;
    @FXML
    private TableColumn<Reservation, String> positionColumn;

    private final ObservableList<Reservation> list = FXCollections.observableArrayList();

    private ReservationsFilteringController.ReservationFilter filter;

    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        customerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getCustomerList().getById(cellData.getValue().getCustomerId()).getName()));
        animalCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getAnimalList().getAnimalById(cellData.getValue().getAnimalId()).getCategory()));
        animalNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getAnimalList().getAnimalById(cellData.getValue().getAnimalId()).getName()));
        startDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod().getStartDate().toString()));
        endDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod().getEndDate().toString()));

        reset();
    }

    public void reset() {
        list.clear();
        ReservationList reservations = model.getReservationList();
        if (filter != null) reservations = filter.filterList(reservations);
        list.addAll(reservations.getAllReservations());
        reservationsTable.setItems(list);
    }

    public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        viewHandler.openView("MainMenu");
    }

    @FXML
    public void filterToday() {
        if (filter == null) {
            filter = list -> list.getReservationsForDate(new Date());
            filteringEnabledLabel.setText("Viser i dag");
        }
        else {
            filter = null;
            filteringEnabledLabel.setText("");
        }
        reset();
    }

    @FXML
    public void filterReservations() {
        ReservationsFilteringController.load(model, filter -> {
            this.filter = filter;
            filteringEnabledLabel.setText(filter == null ? "" : "Aktivt filter");
            reset();
        });
        reset();
    }

    @FXML
    public void createReservation() {
        ManageReservationController.load(model, null);
        reset();
    }

    @FXML
    public void editReservation() {
        Reservation selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();
        ManageReservationController.load(model, selectedReservation);
        reset();
    }

    @FXML
    public void deleteReservation() {
        Reservation selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();

        // Vis confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Er du sikker på at du vil slette reservationen?", ButtonType.YES, ButtonType.NO);
        confirmationAlert.setGraphic(null);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setTitle("Slet reservation");
        confirmationAlert.showAndWait();

        // Stop hvis bruger har valgt nej
        if (confirmationAlert.getResult() == ButtonType.NO) return;

        // Stop hvis reservationen stadig ikke er over
        if (new Date().isBefore(selectedReservation.getPeriod().getEndDate())) {
            Alert secondConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Reservationen er ikke overstået endnu, er du sikker på du vil fjerne den?", ButtonType.YES, ButtonType.NO);
            secondConfirmationAlert.setGraphic(null);
            secondConfirmationAlert.setHeaderText(null);
            secondConfirmationAlert.setTitle("Slet reservation");
            secondConfirmationAlert.showAndWait();

            // Stop hvis bruger har valgt nej
            if (secondConfirmationAlert.getResult() == ButtonType.NO) return;
        }

        model.getReservationList().remove(selectedReservation);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Reservationen blev slettet", ButtonType.OK);
        successAlert.setGraphic(null);
        successAlert.setHeaderText(null);
        successAlert.setTitle("Slet dyr");
        successAlert.show();

        reset();
    }
}
