package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * View til valg af filtre for dyr oversigten
 */
public class AnimalsFilteringController {
    private Region root;
    private VIAPetsModel model;

    // Nuværende callback
    private FilteringCallback callback;

    // 2 maps, der hjælper med at oversætte fra engelske termer til dansk display tekst
    private static final String[] categoryIds = {"Any", Animal.CATEGORY_DEFAULT, Animal.CATEGORY_BIRD, Animal.CATEGORY_FISH, Animal.CATEGORY_REPTILE};
    private static final String[] categoryDisplay = {"Alle", "(andet)", "Fugl", "Fisk", "Reptil"};
    public static final Map<String, String> categoryIdsToDisplay = new HashMap<>();
    public static final Map<String, String> categoryDisplayToIds = new HashMap<>();

    static {
        // Udfylder de 2 maps ovenover
        for (int i = 0; i < categoryIds.length; i++) {
            categoryIdsToDisplay.put(categoryIds[i], categoryDisplay[i]);
            categoryDisplayToIds.put(categoryDisplay[i], categoryIds[i]);
        }
    }

    // 2 maps, der hjælper med at oversætte fra engelske termer til dansk display tekst
    private static final String[] saleOrPensionIds = {"Any", "Sale", "Pension"};
    private static final String[] saleOrPensionDisplay = {"Alle", "Til salg", "Til pasning"};
    public static final Map<String, String> saleOrPensionIdsToDisplay = new HashMap<>();
    public static final Map<String, String> saleOrPensionDisplayToIds = new HashMap<>();

    static {
        // Udfylder de 2 maps ovenover
        for (int i = 0; i < saleOrPensionIds.length; i++) {
            saleOrPensionIdsToDisplay.put(saleOrPensionIds[i], saleOrPensionDisplay[i]);
            saleOrPensionDisplayToIds.put(saleOrPensionDisplay[i], saleOrPensionIds[i]);
        }
    }

    /**
     * Lambda funktion der skal filtre en AnimalList
     */
    @FunctionalInterface
    public interface AnimalFilter {
        AnimalList filterList(AnimalList animalList);
    }

    /**
     * Lambda funktion der skal modtage en AnimalFilter
     * Dette filter er resultatet af brugerens valg
     */
    @FunctionalInterface
    public interface FilteringCallback {
        void callback(AnimalFilter filter);
    }

    /**
     * De forskellige inputs fra FXML
     */
    @FXML
    public ChoiceBox<String> categorySelector;
    @FXML
    public ChoiceBox<String> saleOrPensionSelector;
    @FXML
    public TextField minimumPriceSelector;
    @FXML
    public TextField maximumPriceSelector;
    @FXML
    public TextField animalNameSelector;
    @FXML
    public TextField ownerNameSelector;
    @FXML
    public DatePicker pensionStart;
    @FXML
    public DatePicker pensionEnd;
    @FXML
    public CheckBox inPension;

    /**
     * Indlæser og åbner AnimalsFilteringGUI
     *
     * @param model    Modellen
     * @param callback Filteret returneres via et callback når brugeren har valgt muligheder og trykket OK
     */
    public static void load(VIAPetsModel model, FilteringCallback callback, Boolean forceSaleOrPension) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AnimalsFilteringController.class.getResource("/views/AnimalsFilteringGUI.fxml"));
            Region root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vælg filtering og sortering");
            stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            ((AnimalsFilteringController) loader.getController()).init(root, model, callback, forceSaleOrPension);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Init viewet, reset behøves ikke, da der åbnes et nyt view hver gang
     *
     * @param root     FXML roden
     * @param model    Model
     * @param callback Tilbagekald med filter
     */
    public void init(Region root, VIAPetsModel model, FilteringCallback callback, Boolean forceSaleOrPension) {
        this.root = root;
        this.model = model;
        this.callback = callback;

        // Initialiser kategori/art selector
        categorySelector.setItems(FXCollections.observableArrayList(AnimalsFilteringController.categoryIdsToDisplay.values()));
        categorySelector.setValue(categoryIdsToDisplay.get("Any"));
        // Til salg eller pension
        saleOrPensionSelector.setItems(FXCollections.observableArrayList(AnimalsFilteringController.saleOrPensionIdsToDisplay.values()));
        saleOrPensionSelector.setValue(saleOrPensionIdsToDisplay.get("Any"));

        if (forceSaleOrPension != null) {
            saleOrPensionSelector.setValue(saleOrPensionIdsToDisplay.get(forceSaleOrPension ? "Sale" : "Pension"));
            saleOrPensionSelector.setDisable(true);
        }
        // Pris
        minimumPriceSelector.setText("0,00");
        maximumPriceSelector.setText("99999,00");
        // Navne
        animalNameSelector.setText("");
        ownerNameSelector.setText("");
        // Periode
        inPension.setSelected(false);
        pensionStart.setValue(LocalDate.now());
        pensionStart.setDisable(true);
        pensionEnd.setValue(LocalDate.now());
        pensionEnd.setDisable(true);
        inPension.selectedProperty().addListener((observable, oldValue, newValue) -> {
            pensionStart.setDisable(!newValue);
            pensionEnd.setDisable(!newValue);
        });
    }

    /**
     * Nulstil
     */
    @FXML
    public void clear() {
        this.callback.callback(null);
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Luk
     */
    @FXML
    public void close() {
        ((Stage) root.getScene().getWindow()).close();
    }

    /**
     * Action der opretter filteret og returnere det via callback
     */
    @FXML
    public void confirm() {
        this.callback.callback((animalList) -> {
            // Filtrer kategorier
            String categoryChoice = categoryDisplayToIds.get(categorySelector.getValue());
            if (!Objects.equals(categoryChoice, "Any")) {
                animalList = animalList.getAnimalsFromCategory(categoryChoice);
            }

            // Filtrer salg eller pension
            String saleOfPensionChoice = saleOrPensionDisplayToIds.get(saleOrPensionSelector.getValue());
            if (!Objects.equals(saleOfPensionChoice, "Any")) {
                if (saleOfPensionChoice.equals("Sale")) animalList = animalList.getAnimalsForSale();
                else animalList = animalList.getAnimalsForPension();
            }

            // Filtrer penge
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

            // Filtrer efter navne
            if (!animalNameSelector.getText().isEmpty())
                animalList = animalList.getAnimalsByName(animalNameSelector.getText());
            if (!ownerNameSelector.getText().isEmpty())
                animalList = animalList.getAnimalsByOwnerName(ownerNameSelector.getText(), model.getCustomerList());

            // Filtrer efter periode
            if (inPension.isSelected())
                animalList = animalList.getAnimalsWithReservation(
                        new DateInterval(new Date(pensionStart.getValue()), new Date(pensionEnd.getValue())),
                        model.getReservationList()
                );

            return animalList;
        });

        ((Stage) root.getScene().getWindow()).close();
    }
}
