package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Animal;
import model.AnimalList;
import model.VIAPetsModelManager;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AnimalsFilteringController {
    private Region root;
    private VIAPetsModelManager model;

    private FilteringOptionsCallback callback;

    private static final String[] categoryIds = {"Any", Animal.CATEGORY_DEFAULT, Animal.CATEGORY_BIRD, Animal.CATEGORY_FISH, Animal.CATEGORY_REPTILE};
    private static final String[] categoryDisplay = {"Alle", "(andet)", "Fugl", "Fisk", "Reptil"};
    public static final Map<String, String> categoryIdsToDisplay = new HashMap<>();
    public static final Map<String, String> categoryDisplayToIds = new HashMap<>();

    static {
        for (int i = 0; i < categoryIds.length; i++) {
            categoryIdsToDisplay.put(categoryIds[i], categoryDisplay[i]);
            categoryDisplayToIds.put(categoryDisplay[i], categoryIds[i]);
        }
    }

    private static final String[] saleOrPensionIds = {"Any", "Sale", "Pension"};
    private static final String[] saleOrPensionDisplay = {"Alle", "Til salg", "Til pasning"};
    public static final Map<String, String> saleOrPensionIdsToDisplay = new HashMap<>();
    public static final Map<String, String> saleOrPensionDisplayToIds = new HashMap<>();

    static {
        for (int i = 0; i < saleOrPensionIds.length; i++) {
            saleOrPensionIdsToDisplay.put(saleOrPensionIds[i], saleOrPensionDisplay[i]);
            saleOrPensionDisplayToIds.put(saleOrPensionDisplay[i], saleOrPensionIds[i]);
        }
    }

    @FunctionalInterface
    public interface AnimalFilter {
        AnimalList filterList(AnimalList animalList);
    }

    @FunctionalInterface
    public interface FilteringOptionsCallback {
        void callback(AnimalFilter filter);
    }

    @FXML
    public ChoiceBox categorySelector;
    @FXML
    public ChoiceBox saleOrPensionSelector;
    @FXML
    public TextField minimumPriceSelector;
    @FXML
    public TextField maximumPriceSelector;
    @FXML
    public TextField animalNameSelector;
    @FXML
    public TextField ownerNameSelector;

    public static void load(VIAPetsModelManager model, FilteringOptionsCallback callback) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AnimalsFilteringController.class.getResource("/views/AnimalsFilteringGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Vælg filtering og sortering");
            stage.setScene(new Scene(root, 400, 360));
            stage.show();
            ((AnimalsFilteringController) loader.getController()).init(root, model, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(Region root, VIAPetsModelManager model, FilteringOptionsCallback callback) {
        this.root = root;
        this.model = model;
        this.callback = callback;
        // Initialiser kategori/art selector
        categorySelector.setItems(FXCollections.observableArrayList(AnimalsFilteringController.categoryIdsToDisplay.values().toArray()));
        categorySelector.setValue(categoryIdsToDisplay.get("Any"));
        // Til salg eller pension
        saleOrPensionSelector.setItems(FXCollections.observableArrayList(AnimalsFilteringController.saleOrPensionIdsToDisplay.values().toArray()));
        saleOrPensionSelector.setValue(saleOrPensionIdsToDisplay.get("Any"));
        // Pris
        minimumPriceSelector.setText("0,00");
        maximumPriceSelector.setText("99999,00");
        // Navne
        animalNameSelector.setText("");
        ownerNameSelector.setText("");
    }

    @FXML
    public void confirm() {
        this.callback.callback((animalList) -> {
            // Filtre kategorier
            String categoryChoice = categoryDisplayToIds.get(categorySelector.getValue());
            if (!Objects.equals(categoryChoice, "Any")) {
                animalList = animalList.getAnimalsFromCategory(categoryChoice);
            }

            // Filtre salg eller pension
            String saleOfPensionChoice = saleOrPensionDisplayToIds.get(saleOrPensionSelector.getValue());
            if (!Objects.equals(saleOfPensionChoice, "Any")) {
                if (saleOfPensionChoice.equals("Sale")) animalList = animalList.getAnimalsForSale();
                else animalList = animalList.getAnimalsForPension();
            }

            // Filtre penge
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            double minimum = 0;
            double maximum = 99999;
            try {
                minimum = format.parse(minimumPriceSelector.getText()).doubleValue();
                maximum = format.parse(maximumPriceSelector.getText()).doubleValue();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            animalList = animalList.getAnimalsByPrice(minimum, maximum);

            // Filtre efter navne
            System.out.println(animalNameSelector.getText());
            if (!animalNameSelector.getText().isEmpty())
                animalList = animalList.getAnimalsByName(animalNameSelector.getText());
            if (!ownerNameSelector.getText().isEmpty())
                animalList = animalList.getAnimalsByOwnerName(ownerNameSelector.getText(), model.getCustomerList());

            return animalList;
        });

        ((Stage) root.getScene().getWindow()).close();
    }
}
