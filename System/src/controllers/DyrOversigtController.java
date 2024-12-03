package controllers;

import javafx.scene.layout.Region;
import model.VIAPetsModelManager;

public class DyrOversigtController {
    private ViewHandler viewHandler;
    private Region root;
    private VIAPetsModelManager model;

    public DyrOversigtController() {
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
}
