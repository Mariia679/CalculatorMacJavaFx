package sample.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCombination.*;
import static sample.controller.ButtonData.StyleButton.*;

/**
 * Data from button.
 * Contains all buttons with its {@link KeyCodeCombination} and its style.
 * Set of key codes for {@link KeyEvent} objects.
 */
public enum ButtonData {
    //Constants for the number and point key.
    ZERO_BUTTON(DIGIT0, META_ANY, NUMBER_STYLE),
    ONE_BUTTON(DIGIT1, META_ANY, NUMBER_STYLE),
    TWO_BUTTON(DIGIT2, META_ANY, NUMBER_STYLE),
    THREE_BUTTON(DIGIT3, META_ANY, NUMBER_STYLE),
    FOUR_BUTTON(DIGIT4, META_ANY, NUMBER_STYLE),
    FIVE_BUTTON(DIGIT5, META_ANY, NUMBER_STYLE),
    SIX_BUTTON(DIGIT6, META_ANY, NUMBER_STYLE),
    SEVEN_BUTTON(DIGIT7, META_ANY, NUMBER_STYLE),
    EIGHT_BUTTON(DIGIT8, META_ANY, NUMBER_STYLE),
    NINE_BUTTON(DIGIT9, META_ANY, NUMBER_STYLE),
    POINT_BUTTON(PERIOD, META_ANY, NUMBER_STYLE),


    // Constant for the operator key.
    DIVISION_BUTTON(SLASH, SHIFT_ANY, OPERATOR_STYLE),
    SUBTRACTION_BUTTON(MINUS, SHIFT_ANY, OPERATOR_STYLE),
    ADDITION_BUTTON(EQUALS, SHIFT_ANY, OPERATOR_STYLE),
    EQUAL_BUTTON(ENTER, META_ANY, OPERATOR_STYLE),
    MULTIPLICATION_BUTTON(DIGIT8, SHIFT_DOWN, OPERATOR_STYLE),

    // Constant for the operator AC % +/- key.
    AC_BUTTON(ESCAPE, META_ANY, AC_INVERSION_PERCENTAGE_BUTTON),
    INVERSION_BUTTON(MINUS, ALT_DOWN, AC_INVERSION_PERCENTAGE_BUTTON),
    PERCENTAGE_BUTTON(DIGIT5, SHIFT_DOWN, AC_INVERSION_PERCENTAGE_BUTTON);

    /**
     * Such key combination is independent on the keyboard
     * functional layout configured by the user at the time of key combination
     * matching.
     */
    private final KeyCodeCombination keyCodeCombination;

    /**
     * Style for different button
     */
    private final StyleButton styleButton;


    /**
     * Initialize {@link ButtonData)
     *
     * @param keyCodeCombination keyboard combination
     * @param styleButton        button style
     */
    ButtonData(KeyCode keyCode, KeyCodeCombination.Modifier combination, StyleButton styleButton) {
        this.keyCodeCombination = new KeyCodeCombination(keyCode, combination);
        this.styleButton = styleButton;
    }

    /**
     * Style for different button
     */
    enum StyleButton {
        /**
         * Constant for style to number button.
         */
        NUMBER_STYLE("-fx-background-color: rgb(163, 163, 163);-fx-text-fill: #000;", " -fx-background-color: rgb(217, 217, 217);"),

        /**
         * Constant for style to operator button.
         */
        OPERATOR_STYLE(" -fx-text-fill: rgb(85, 85, 85); -fx-background-color: rgb(182, 96, 29);", "-fx-text-fill: #fff;-fx-background-color: rgb(242, 128, 38)"),

        /**
         * Constant for style to ac, inversion and percentage button.
         */
        AC_INVERSION_PERCENTAGE_BUTTON("-fx-background-color: rgb(153, 153, 153);", "-fx-background-color: rgb(204, 204, 204);");

        /**
         * Button style press
         */
        private final String stylePress;

        /**
         * Button style release
         */
        private final String styleRelease;

        /**
         * Initialize the {@link StyleButton}
         *
         * @param stylePress   button style press
         * @param styleRelease button style release
         */
        StyleButton(String stylePress, String styleRelease) {
            this.stylePress = stylePress;
            this.styleRelease = styleRelease;
        }

        /*
         **********************************************************
         * Method get()
         **********************************************************
         */

        public String getStylePress() {
            return stylePress;
        }

        public String getStyleRelease() {
            return styleRelease;
        }
    }

    /**
     * Get instance of {@link ButtonData} by key event
     *
     * @param event event from keyboard
     * @return instance of {@link ButtonData}
     */
    public static ButtonData get(KeyEvent event) {
        ButtonData buttonData = null;
        for (ButtonData name : ButtonData.values()) {
            if (name.getKeyCodeCombination().match(event)) {
                buttonData = name;
                break;
            }
        }
        return buttonData;
    }

    /*
     **********************************************************
     * Method get()
     **********************************************************
     */

    public StyleButton getStyleButton() {
        return styleButton;
    }

    private KeyCodeCombination getKeyCodeCombination() {
        return keyCodeCombination;
    }
}
