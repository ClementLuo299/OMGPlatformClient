import gui.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;
import networking.App;

/**
 * Launches the Program!
 */
public class BoardGamePlatformApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            //Startup backend
            App.getInstance();

            // Initialize the ScreenManager with the primary stage
            ScreenManager screenManager = ScreenManager.getInstance();
            screenManager.initialize(primaryStage);
            
            // Navigate to the opening screen (initial screen)
            screenManager.navigateTo(ScreenManager.LOGIN_SCREEN, ScreenManager.LOGIN_CSS);
            
            // Start preloading common screens in background for faster navigation
            new Thread(() -> {
                screenManager.preloadCommonScreens();
            }).start();
            
            primaryStage.setTitle("OMG Platform");
            primaryStage.setWidth(1500);
            primaryStage.setHeight(800);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error starting Application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void stop() {
        try {
            App.db().saveDBData();
        } catch (Exception e) {
            System.err.println("Error saving database data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
