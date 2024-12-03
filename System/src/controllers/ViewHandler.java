package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.VIAPetsModelManager;

public class ViewHandler {
    private Scene currentScene;
    private Stage primaryStage;

    private VIAPetsModelManager model;

    public MainMenuController mainMenu;
    public AnimalsController animalsController;
    public ManageAnimalController manageAnimalController;
    public ReservationsController reservationsController;

    public ViewHandler(VIAPetsModelManager model) {
        this.model = model;
        this.currentScene = new Scene(new Region());
    }
    
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        openView("MainMenu");
    }

    public void openView(String id) {
        Region root = switch (id) {
            case "MainMenu" -> loadMainMenu();
            case "Animals" -> loadAnimals();
            case "ManageAnimal" -> loadManageAnimal(-1);
            case "Reservations" -> loadReservations();
            default -> throw new IllegalArgumentException("View: " + id + " does not exist!");
        };

        currentScene.setRoot(root);
        primaryStage.setTitle("VIAPets");
        primaryStage.setScene(currentScene);
        primaryStage.setWidth(root.getPrefWidth());
        primaryStage.setHeight(root.getPrefHeight());
        primaryStage.show();
    }

    public <T> void openView(String id, T data) {
        Region root = switch (id) {
            case "ManageAnimal" -> loadManageAnimal((int) data);
            default -> throw new IllegalArgumentException("View: " + id + " does not exist with data!");
        };

        currentScene.setRoot(root);
        primaryStage.setTitle("VIAPets");
        primaryStage.setScene(currentScene);
        primaryStage.setWidth(root.getPrefWidth());
        primaryStage.setHeight(root.getPrefHeight());
        primaryStage.show();
    }

    public void closeView() {
        primaryStage.close();
    }

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
            manageAnimalController.reset();
        }
        return manageAnimalController.getRoot();
    }

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
}