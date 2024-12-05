package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    public ManageAnimalController manageAnimalController;
    public ReservationsController reservationsController;
    public EmployeesController employeesController;
    public CustomersController customersController;
    public SalesController salesController;
    public ManageEmployeeController manageEmployeeController;
    public ManageCustomerController manageCustomerController;

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
            case "ManageAnimal" -> loadManageAnimal(-1);
            case "Reservations" -> loadReservations();
            case "Customers" -> loadCustomers();
            case "Employees" -> loadEmployees();
            case "Sales" -> loadSales();
            case "ManageEmployee" -> loadManageEmployee();
            case "ManageCustomer" -> loadManageCustomer();
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
     * Åben et view med data
     * @param id ID til view
     * @param data Data til view
     * @param <T> Typen skal matche hvad viewet forventer
     */
    public <T> void openView(String id, T data) {
        Region root = switch (id) {
            case "ManageAnimal" -> loadManageAnimal((int) data);
            default -> throw new IllegalArgumentException("View: " + id + " does not exist with data! Måske den mangler i openView med data?");
        };

        currentScene.setRoot(root);
        primaryStage.setTitle("VIAPets");
        primaryStage.setScene(currentScene);
        primaryStage.setWidth(root.getPrefWidth());
        primaryStage.setHeight(root.getPrefHeight());
        primaryStage.show();
    }

    /**
     * Luk stage, afslutter programmet
     */
    public void closeView() {
        primaryStage.close();
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
                e.printStackTrace();
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
                e.printStackTrace();
            }
        } else {
            animalsController.reset();
        }
        return animalsController.getRoot();
    }

    /**
     * Load opret dyr (animalId: -1) eller rediger dyr via animalId
     */
    private Region loadManageAnimal(int animalId) {
        if (manageAnimalController == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/ManageAnimalGUI.fxml"));
                Region root = loader.load();
                manageAnimalController = loader.getController();
                manageAnimalController.init(this, model, root, animalId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            manageAnimalController.reset(animalId);
        }
        return manageAnimalController.getRoot();
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
                e.printStackTrace();
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
                e.printStackTrace();
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
                e.printStackTrace();
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
                e.printStackTrace();
            }
        } else {
            salesController.reset();
        }
        return salesController.getRoot();
    }

    public Region loadManageEmployee() {
        if (manageEmployeeController == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/ManageEmployee.fxml"));
                Region root = loader.load();
                manageEmployeeController = loader.getController();
                manageEmployeeController.init(this, model, root);
                System.out.println(manageEmployeeController);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            manageEmployeeController.reset();
        }
        return manageEmployeeController.getRoot();
    }
    public Region loadManageCustomer() {
        if (manageCustomerController == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/ManageCustomer.fxml"));
                Region root = loader.load();
                manageCustomerController = loader.getController();
                manageCustomerController.init(this, model, root);
                System.out.println(manageCustomerController);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            manageCustomerController.reset();
        }
        return manageCustomerController.getRoot();
    }
}