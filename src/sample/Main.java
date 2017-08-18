package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.view.CalculatorApp;

/**
 * The main application class.
 * The application starts from this class.
 * Application class from which JavaFX applications extend.
 */
public class Main extends Application {
    /**
     * Launch the application
     *
     * @param args the supplied command-line arguments as an array of String objects
     */
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        CalculatorApp app = new CalculatorApp();
        app.start(primaryStage);
    }
}
