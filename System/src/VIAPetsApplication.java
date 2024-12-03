import controllers.ViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Animal;
import model.Customer;
import model.VIAPetsModelManager;

public class VIAPetsApplication extends Application {
    
    public static VIAPetsModelManager model = new VIAPetsModelManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        model.getAnimalList().add(model.getAnimalList().createNewAnimal("other", "Mazen", 69.0));
        model.getAnimalList().add(model.getAnimalList().createNewAnimal(Animal.CATEGORY_REPTILE, "Mazen Sharaf", 69.0));
        model.getAnimalList().add(model.getAnimalList().createNewAnimal(Animal.CATEGORY_BIRD, "Mazen Sharaf", 1));

        model.getCustomerList().addCustomer(new Customer("Nikolai Balck", 12345678, "12345@gmail.com", 1));

        ViewHandler viewHandler = new ViewHandler(model);
        viewHandler.start(primaryStage);
    }
}
