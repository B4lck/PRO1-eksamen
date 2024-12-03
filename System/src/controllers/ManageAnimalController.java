package controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import model.VIAPetsModelManager;

public class ManageAnimalController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;
    
    public ManageAnimalController() {
    }
    
    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.model = model;
        this.root = root;
    }
    
    public void reset() {
        
    }
    
    public Region getRoot() {
        return root;
    }

    @FXML
    public void back() {
        viewHandler.openView("Animals");
    }
}
