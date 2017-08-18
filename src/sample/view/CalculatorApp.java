package sample.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.net.URL;

public class CalculatorApp {
    /**
     * The horizontal location of this {@code Stage} on the screen.
     */
    private double xOffset;
    /**
     * The vertical location of this {@code Stage} on the screen.
     */
    private double yOffset;

    /**
     * Parent class for all nodes that have children in the scene graph.
     * Initialize root of the application
     *
     * @return root of the application
     * @throws Exception from FXMLLoader.load(getClass().getResource("resources/sample.fxml"));
     */
    private Parent getRoot() throws Exception {
        //Loads an object hierarchy from an XML document.
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.setStyle("-fx-background-color: null;");
        Rectangle rect = new Rectangle(233.0, 380.0);
        rect.setArcHeight(12.0);
        rect.setArcWidth(12.0);
        root.setClip(rect);
        return root;
    }

    /**
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(getRoot());
        //Class is the container for all content in a scene graph.
        scene.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
        scene.setFill(Color.TRANSPARENT);//A fully transparent color with an ARGB value of #00000000.

        primaryStage.initStyle(StageStyle.TRANSPARENT);//Style with a transparent background and no decorations.
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        URL iconURL = CalculatorApp.class.getResource("resources/image/icons.png");
        java.awt.Image image = new ImageIcon(iconURL).getImage();
        com.apple.eawt.Application.getApplication().setDockIconImage(image);

        primaryStage.show();
    }
}
