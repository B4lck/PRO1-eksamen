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
import model.VIAPetsModel;

public class ReservationsController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModel model;

    // Elementer
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

    // Liste til tabel
    private final ObservableList<Reservation> list = FXCollections.observableArrayList();

    // Nuværende filter
    private ReservationsFilteringController.ReservationFilter filter;

    /**
     * Init viewet
     */
    public void init(ViewHandler viewHandler, VIAPetsModel model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;

        customerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getCustomerList().getById(cellData.getValue().getCustomerId()) != null ? model.getCustomerList().getById(cellData.getValue().getCustomerId()).getName() : "Findes ikke..."));
        animalCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getAnimalList().getAnimalById(cellData.getValue().getAnimalId()) != null ? model.getAnimalList().getAnimalById(cellData.getValue().getAnimalId()).getCategory() : "Findes ikke..."));
        animalNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(model.getAnimalList().getAnimalById(cellData.getValue().getAnimalId()) != null ? model.getAnimalList().getAnimalById(cellData.getValue().getAnimalId()).getName() : "Findes ikke..."));
        startDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod().getStartDate().toString()));
        endDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod().getEndDate().toString()));
        positionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPositionId() == -1 ? "ingen position" : Integer.toString(cellData.getValue().getPositionId())));

        reset();
    }

    /**
     * Reset viewet / Opdater tabellen
     */
    public void reset() {
        list.clear();
        ReservationList reservations = model.getReservationList();
        if (filter != null) reservations = filter.filterList(reservations);
        list.addAll(reservations.getAllReservations());
        reservationsTable.setItems(list);
    }

    /**
     * Returner roden
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
     * Action til at filtre for i dag
     */
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

    /**
     * Action til at åbne filter vælgeren
     */
    @FXML
    public void filterReservations() {
        ReservationsFilteringController.load(model, filter -> {
            this.filter = filter;
            filteringEnabledLabel.setText(filter == null ? "" : "Aktivt filter");
            reset();
        });
        reset();
    }

    /**
     * Action til at oprette en ny reservation
     */
    @FXML
    public void createReservation() {
        ManageReservationController.load(model, null);
        reset();
    }

    /**
     * Action til at redigere en reservation
     */
    @FXML
    public void editReservation() {
        Reservation selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();
        ManageReservationController.load(model, selectedReservation);
        reset();
    }

    /**
     * Action til at slette en reservation
     */
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
