package sample.controller;

import javafx.scene.input.KeyCode;

class DataFromButton {

    private final KeyCode keycode;

    private final String buttonId;

    public DataFromButton(KeyCode keycode, String buttonId) {
        this.keycode = keycode;
        this.buttonId = buttonId;
    }

    public KeyCode getKeycode() {
        return keycode;
    }

    public String getButtonId() {
        return buttonId;
    }
}
