package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.VIAPetsModelManager;

import java.io.IOException;

public class AnimalsFilteringController {
    private Region root;
    private VIAPetsModelManager model;
    
    public AnimalsFilteringController() {
    }
    
    public void loadSelf(VIAPetsModelManager model) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/AnimalsFilteringGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Vælg filtering og sortering");
            stage.setScene(new Scene(root, 400, 360));
            stage.show();
            this.model = model;
            this.root = root;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Region getRoot() {
        return root;
    }
}
