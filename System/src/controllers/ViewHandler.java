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
    public DyrOversigtController dyrOversigt;
    public ManageAnimalController manageAnimalController;

    public ViewHandler(VIAPetsModelManager model) {
        this.model = model;
        this.currentScene = new Scene(new Region());
    }
    
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        openView("ManageAnimal");
    }

    public void openView(String id) {
        Region root = switch (id) {
            case "MainMenu" -> loadMainMenu();
            case "DyrOversigt" -> loadDyrOversigt();
            case "ManageAnimal" -> loadManageAnimal();
            default -> throw new IllegalArgumentException("View: " + id + " does not exist!");
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

    private Region loadDyrOversigt() {
        if (dyrOversigt == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/DyrOversigtGUI.fxml"));
                Region root = loader.load();
                dyrOversigt = loader.getController();
                dyrOversigt.init(this, model, root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            dyrOversigt.reset();
        }
        return dyrOversigt.getRoot();
    }

    private Region loadManageAnimal() {
        if (manageAnimalController == null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/ManageAnimalGUI.fxml"));
                Region root = loader.load();
                manageAnimalController = loader.getController();
                manageAnimalController.init(this, model, root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            manageAnimalController.reset();
        }
        return manageAnimalController.getRoot();
    }
}