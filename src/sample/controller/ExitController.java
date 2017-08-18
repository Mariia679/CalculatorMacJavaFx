package sample.controller;

import javafx.application.Platform;

/**
 * Exit the application
 */
class ExitController {

    /**
     * Instance of {@link ExitController}
     */
    static ExitController instance = new ExitController();

    /**
     * Exit the application
     */
    public void exit() {
        Platform.exit();
    }

    /*
     **********************************************************
     *  Method get()
     **********************************************************
     */

    public static ExitController getInstance() {
        return instance;
    }
}
