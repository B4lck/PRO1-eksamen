import controllers.ViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;
import model.VIAPetsModelManager;

public class VIAPetsApplication extends Application {
    
    public static VIAPetsModelManager model = new VIAPetsModelManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ViewHandler viewHandler = new ViewHandler(model);
        viewHandler.start(primaryStage);
    }
}
