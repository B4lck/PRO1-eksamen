package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import model.VIAPetsModelManager;

/**
 * Controller til hovedmenuen
 */
public class MainMenuController {
    private ViewHandler viewHandler;
    private Region root;

    /**
     * Init
     */
    public void init(ViewHandler viewHandler, VIAPetsModelManager model, Region root) {
        this.viewHandler = viewHandler;
        this.root = root;
    }

    /**
     * Opdater
     */
    public void reset() {
        // Skal være her, da ViewHandler regner med at kunne kalde reset
    }

    /**
     * Returnere roden
     */
    public Region getRoot() {
        return root;
    }

    /**
     * Action der går til den side der står i userdata på elementet
     */
    @FXML
    public void gotoPage(ActionEvent event) {
        Node node = (Node) event.getSource();
        String page = (String) node.getUserData();
        viewHandler.openView(page);
    }
}
