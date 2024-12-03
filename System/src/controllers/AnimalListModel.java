package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Animal;
import model.VIAPetsModelManager;

public class AnimalListModel {
    private ObservableList<AnimalModel> list;
    private VIAPetsModelManager model;
    
    public AnimalListModel(VIAPetsModelManager model) {
        this.model = model;
        this.list = FXCollections.observableArrayList();
        update();
    }
    
    public void update() {
        list.clear();
        for (Animal animal : model.getAnimalList().getAllAnimals()) {
            this.list.add(new AnimalModel(animal, model));
        }
    }
    
    public ObservableList<AnimalModel> getList() {
        return list;
    }
}
