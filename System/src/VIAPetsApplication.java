import controllers.ViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Animal;
import model.VIAPetsModelManager;

public class VIAPetsApplication extends Application {
    
    public static VIAPetsModelManager model = new VIAPetsModelManager();

    public static void main(String[] args) {
        model.getAnimalList().add(model.getAnimalList().createNewAnimal("other", "Mazen", 69.0));
        model.getAnimalList().add(model.getAnimalList().createNewAnimal("other", "Mazen", 69.0));
        model.getAnimalList().add(model.getAnimalList().createNewAnimal("other", "Mazen", 69.0));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ViewHandler viewHandler = new ViewHandler(model);
        viewHandler.start(primaryStage);
    }
}
