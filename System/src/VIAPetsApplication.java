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
        model.fileManager.loadAnimals();
        
        ViewHandler viewHandler = new ViewHandler(model);
        viewHandler.start(primaryStage);
    }
}
