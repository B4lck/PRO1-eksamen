package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.VIAPetsModelManager;

/**
 * Håndter alle views i programmet
 */
public class ViewHandler {
    private final Scene currentScene;
    private Stage primaryStage;

    /**
     * Model
     */
    private final VIAPetsModelManager model;

    /**
     * Controllers
     */
    public MainMenuController mainMenu;
    public AnimalsController animalsController;
    public ReservationsController reservationsController;
    public EmployeesController employeesController;
    public CustomersController customersController;
    public SalesController salesController;

    public ViewHandler(VIAPetsModelManager model) {
        this.model = model;
        this.currentScene = new Scene(new Region());
    }

    /**
     * Åben initial view i stage
     */
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        openView("MainMenu");
    }

    /**
     * Åben et view uden data
     * @param id ID til view
     */
    public void openView(String id) {
        Region root = switch (id) {
            case "MainMenu" -> loadMainMenu();
            case "Animals" -> loadAnimals();
            case "Reservations" -> loadReservations();
            case "Customers" -> loadCustomers();
            case "Employees" -> loadEmployees();
            case "Sales" -> loadSales();
            default -> throw new IllegalArgumentException("View: " + id + " does not exist! Måske den mangler i openView?");
        };

        currentScene.setRoot(root);
        primaryStage.setTitle("VIAPets");
        primaryStage.setScene(currentScene);
        primaryStage.setWidth(root.getPrefWidth());
        primaryStage.setHeight(root.getPrefHeight());
        primaryStage.show();
    }

    /**
     * Load hovedmenu
     */
    private Region loadMainMenu() {
        if (mainMenu == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/MainMenuGUI.fxml"));
                Region root = loader.load();
                mainMenu = loader.getController();
                mainMenu.init(this, model, root);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                errorAlert.setGraphic(null);
                errorAlert.setHeaderText(null);
                errorAlert.setTitle("Fejl");
                errorAlert.showAndWait();
            }
        } else {
            mainMenu.reset();
        }
        return mainMenu.getRoot();
    }

    /**
     * Load dyr liste
     */
    private Region loadAnimals() {
        if (animalsController == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/AnimalsGUI.fxml"));
                Region root = loader.load();
                animalsController = loader.getController();
                animalsController.init(this, model, root);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                errorAlert.setGraphic(null);
                errorAlert.setHeaderText(null);
                errorAlert.setTitle("Fejl");
                errorAlert.showAndWait();
            }
        } else {
            animalsController.reset();
        }
        return animalsController.getRoot();
    }

    /**
     * Load reservationsliste
     */
    private Region loadReservations() {
        if (reservationsController == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/ReservationsGUI.fxml"));
                Region root = loader.load();
                reservationsController = loader.getController();
                reservationsController.init(this, model, root);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                errorAlert.setGraphic(null);
                errorAlert.setHeaderText(null);
                errorAlert.setTitle("Fejl");
                errorAlert.showAndWait();
            }
        } else {
            reservationsController.reset();
        }
        return reservationsController.getRoot();
    }

    /**
     * Load medarbejderliste
     */
    private Region loadEmployees() {
        if (employeesController == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/EmployeesGUI.fxml"));
                Region root = loader.load();
                employeesController = loader.getController();
                employeesController.init(this, model, root);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                errorAlert.setGraphic(null);
                errorAlert.setHeaderText(null);
                errorAlert.setTitle("Fejl");
                errorAlert.showAndWait();
            }
        } else {
            employeesController.reset();
        }
        return employeesController.getRoot();
    }

    private Region loadCustomers() {
        if (customersController == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/CustomersGUI.fxml"));
                Region root = loader.load();
                customersController = loader.getController();
                customersController.init(this, model, root);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                errorAlert.setGraphic(null);
                errorAlert.setHeaderText(null);
                errorAlert.setTitle("Fejl");
                errorAlert.showAndWait();
            }
        } else {
            customersController.reset();
        }
        return customersController.getRoot();
    }

    private Region loadSales() {
        if (salesController == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/SalesGUI.fxml"));
                Region root = loader.load();
                salesController = loader.getController();
                salesController.init(this, model, root);
            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                errorAlert.setGraphic(null);
                errorAlert.setHeaderText(null);
                errorAlert.setTitle("Fejl");
                errorAlert.showAndWait();
            }
        } else {
            salesController.reset();
        }
        return salesController.getRoot();
    }

}