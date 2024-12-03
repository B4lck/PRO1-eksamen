package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import model.VIAPetsModelManager;

public class MainMenuController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;
    
    public MainMenuController() {
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
    public void gotoPage(ActionEvent event) {
        Node node = (Node) event.getSource();
        String page = (String) node.getUserData();
        viewHandler.openView(page);
    }
}
