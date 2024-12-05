import controllers.ViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;
import model.*;

public class VIAPetsApplication extends Application {
    
    public static final VIAPetsModelManager model = new VIAPetsModelManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Dummy data
        // Dyr
        model.getAnimalList().add(model.getAnimalList().createNewAnimal("other", "Ninus", 300.0));
        model.getAnimalList().add(model.getAnimalList().createNewAnimal(Animal.CATEGORY_REPTILE, "Ekans", 150.0));
        model.getAnimalList().add(model.getAnimalList().createNewAnimal(Animal.CATEGORY_BIRD, "Pip", 1));

        // Kunder
        model.getCustomerList().add(new Customer("Nikolai Balck", 12345678, "12345@gmail.com", 1));
        model.getCustomerList().add(new Customer("Malthe Laursen", 87654321, "67890@gmail.com", 2));
        model.getCustomerList().add(new Customer("Mazen Sharaf", 24681012, "findeikke@gmail.com", 3));

        // Medarbejdere
        Employee employee = new Employee("Bob", 1);
        employee.setDescription("Ejer hele butikken");
        model.getEmployeeList().add(employee);

        // Reservationer
        DateInterval periode = new DateInterval(new Date(1,1,2024), new Date(31,12,2024));
        model.getReservationList().add(new Reservation(1, 3, periode));

        // Salg
        model.getSalesList().add(new Sale(10.00, 1, 1, 1, new Date(4,12,2024)));

        ViewHandler viewHandler = new ViewHandler(model);
        viewHandler.start(primaryStage);
    }
}
