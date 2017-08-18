package sample.controller;

import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.model.Calculator;
import sample.model.Operator;
import sample.model.exception.DivisionByZeroException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static sample.controller.ButtonData.*;
import static sample.model.Operator.*;

/**
 * A controller is simply checkIfEqualIsPressedBefore class name
 * whose object is created by FXML and used to
 * initialize the UI elements. FXML lets you
 * specify checkIfEqualIsPressedBefore controller on the root element
 * using the fx:controller attribute.
 */
public class Controller implements Initializable {

    /**
     * VBox lays out its children in checkIfEqualIsPressedBefore single vertical column.
     * Need to add key event.
     */
    @FXML
    private VBox calc;

    /**
     * Button that clear memory and display.
     */
    @FXML
    private Button ac;

    /**
     * Button from zero to nine and point to make 1.3.
     */
    @FXML
    private Button zero;
    @FXML
    private Button one;
    @FXML
    private Button two;
    @FXML
    private Button three;
    @FXML
    private Button four;
    @FXML
    private Button five;
    @FXML
    private Button six;
    @FXML
    private Button seven;
    @FXML
    private Button eight;
    @FXML
    private Button nine;
    @FXML
    private Button point;

    /**
     * Button to make multiplication.
     */
    @FXML
    private Button multiplication;

    /**
     * Button to make division.
     */
    @FXML
    private Button division;

    /**
     * Button to make addition.
     */
    @FXML
    private Button addition;

    /**
     * Button to make subtraction.
     */
    @FXML
    private Button subtraction;

    /**
     * Button that count an expression.
     */
    @FXML
    private Button equal;

    /**
     * Button that calculate percent.
     */
    @FXML
    private Button percentage;

    /**
     * Button that make inversion.
     */
    @FXML
    private Button inversion;

    /**
     * A Label for displaying text.
     */
    @FXML
    private Label display;

    /**
     * The number is undefined.
     */
    private final static String UNDEFINED = "Не определено";

    /**
     * The default value is displayed on the screen of the calculator.
     */
    private static final BigDecimal DEFAULT_VALUE = BigDecimal.ZERO;

    /**
     * The ac is pressed
     */
    private final static String AC = "AC";

    /**
     * The ac does not pressed
     */
    private final static String C = "C";

    /**
     * The string representation of comma
     */
    private final static String COMMA = ",";

    /**
     * The string representation of point
     */
    private final static String POINT = ".";

    /**
     * The maximum of exponent value
     */
    private final static Integer MAX_EXPONENT_VALUE = 160;

    /**
     * The minimum of exponent value
     */
    private final static Integer MIN_EXPONENT_VALUE = -95;

    /**
     * Exponent start
     */
    private final static Integer START_EXPONENT_VALUE = 16;

    /**
     * String representation of exponent
     */
    private final static String EXPONENT = "e";

    /**
     * Formatter for number
     */
    private final static DecimalFormat FORMATTER = new DecimalFormat("#.################E0");

    /**
     * Represents the set of symbols (such as the decimal separator,
     * the grouping separator, and so on) needed by <code>DecimalFormat</code>
     * to format numbers.
     */
    private final static DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols();

    /**
     * The maximum font size that can be
     */
    private final static Double MAX_FONT_SIZE = 50.;

    /**
     * The minimum length on display that doesn't change
     */
    private final static Integer MIN_LENGTH_ON_DISPLAY = 7;

    /**
     * The minimum length on display that doesn't change
     */
    private final static Integer MAX_LENGTH_ON_DISPLAY = 55;

    /**
     * The left operand in the expression.
     * First value that user entered and stored in memory.
     */
    private BigDecimal numberFirst;

    /**
     * The right operand in the expression.
     * Second value that user entered and stored in memory.
     */
    private BigDecimal numberSecond;

    /**
     * The additional variable in the expression.
     * Necessary if priority of the last operator is higher than previous.
     */
    private BigDecimal numberThird;

    /**
     * Contains operator from previous operation.
     */
    private Operator previousOperator;

    /**
     * Contains operator from last operation
     */
    private Operator lastOperator;

    /**
     * Flag for operator
     */
    private boolean operatorPressedBefore;

    /**
     * The flag that needs when you pressed button ac
     * one time to clear the display.
     */
    private boolean allClearPressed;

    /**
     * The flag for button mc, m+,m-.
     */
    private boolean memoryPressedBefore;

    /**
     * True if display contains "Не определено"
     */
    private boolean isUndefined;

    /**
     * True if display contains zero
     */
    private boolean isDefaultValue = true;

    /**
     * The model that calculate and processing data.
     */
    private final Calculator calculator = new Calculator();

    /**
     * Mapping buttons and it's data
     */
    private final Map<ButtonData, Button> buttonDataMap = new HashMap<>();

    /**
     * Mapping buttons with it's operator
     */
    private final Map<Button, Operator> operatorMap = new HashMap<>();

    /**
     * This method clear display, memory and so on.
     * If you click one time, the display will clear.
     * if one more click, the memory will clear too.
     */
    @FXML
    public void allClearPressed() {
        reset();
        isUndefined = false;
        calculator.setLastNumber();
        if (!allClearPressed) {
            setAllClearPressed();
        } else {
            setAllClearPressed();
            calculator.setEqualFlag();
            allClearPressed = false;
        }
    }

    /**
     * Inverse number from display,
     * if on the display is "1", inverse to "-1"
     */
    @FXML
    public void inversionPressed() {
        if (isUndefined) {
            return;
        }
        setToDisplay(calculator.getConversion(numberFromDisplay()));
        initializeVariable();
        allClearPressed = false;
        isUndefined = false;
    }

    /**
     * Read operator from the event
     * Push result of the calculation to the screen
     * Checking division by zero.
     *
     * @param event Representing some type of action.
     */
    @FXML
    public void operatorPressed(Event event) {
        if (isUndefined) {
            return;
        }

        if (numberFirst == null || memoryPressedBefore || calculator.isEqualFlag()) {
            reset();
            setNumberFirst();
        }

        if (memoryPressedBefore) { //if button memory is pressed
            memoryPressedBefore = false;
        }

        Button buttonOperator = (Button) event.getSource();

        if (calculator.isEqualFlag()) {
            calculator.setEqualFlag();
        }

        try {

            countIfNumberThirdIsNotNull();

            if (operatorPressedBefore) {
                setOperatorAndCount(buttonOperator);
            } else {
                setOperatorAndCountImpl(buttonOperator);
            }

            operatorPressedBefore = true;
            allClearPressed = false;
            calculator.setLastNumber();

        } catch (DivisionByZeroException ex) {
            setUndefined();
            changeFontSize();
        }

    }

    /**
     * Works when user pressed on the button percentage.
     * Calculate percent from previous value.
     */
    @FXML
    public void percentagePressed() {
        if (isUndefined) {
            return;
        }

        if (calculator.isEqualFlag()) {
            reset();
            setNumberFirst();
            calculator.setEqualFlag();
        }

        if (numberSecond == null) {
            setNumberSecond();
        }

        BigDecimal result;
        if (previousOperator == null) {
            numberFirst = calculator.getPercentWithOneNumber(numberFirst);
            result = numberFirst;
        } else if (numberThird != null) {
            numberThird = calculator.getPercentWithTwoNumbers(numberSecond, numberThird);
            result = numberThird;
        } else {
            numberSecond = calculator.getPercentWithTwoNumbers(numberFirst, numberSecond);
            result = numberSecond;
        }

        setToDisplay(result);
        setFalseToFlag();
    }


    /**
     * Read numbers from the event.
     * Initialize variable to calculate the expression
     * Checking for content COMMA on the display
     *
     * @param event Representing some type of action.
     */
    @FXML
    public void numberPressed(Event event) {
        if (isUndefined || calculator.isEqualFlag() || memoryPressedBefore) {
            setToDisplayDefaultValue();
        }
        if (isDefaultValue) {
            ac.setText(C);
        }

        if (calculator.isEqualFlag()) {
            calculator.setEqualFlag();
        }

        if (memoryPressedBefore) { //if m+,mr,m-,mc is pressed
            memoryPressedBefore = false;
        }

        Button button = ((Button) event.getSource());

        display.setText(processInputData(button));

        initializeVariable();

        setFalseToFlag();
        calculator.setLastNumber();
        isUndefined = false;
        isDefaultValue = false;
        changeFontSize();
    }

    /**
     * Counts what happened as checkIfEqualIsPressedBefore result and writes to the screen,
     * if the operator (+, -, *, /) was last entered,
     * then when equal is pressed second time,
     * the value from the screen and the last operator is taken
     * and counting with the same number,if was the operand (0..9),
     * then when equal is pressed second time, the last operation (+, -, *, /)
     * and the last operand are taken and the counting with value in the memory.
     */
    @FXML
    public void calculateResult() {
        if (isUndefined) {
            return;
        }

        if (numberFirst == null) {
            setNumberFirst();
        }

        try {

            if (previousOperator != null) {
                numberFirst = calculator.calculateResult(numberFirst, numberSecond, numberThird, previousOperator, lastOperator);
                checkIfEqualIsPressedBefore();
            }

            setToDisplay(numberFirst);
            setFalseToFlag();
        } catch (DivisionByZeroException ex) {
            setUndefined();
            changeFontSize();
        }
    }

    /**
     * If equalFlag is pressed before and lastOperator is not a null,
     * set <code>previousOperator = lastOperator</code>.
     * <p>
     * Then check if numberThird is not a null and that numberThird is less than 0.
     * Set null to lastOperator.
     */
    private void checkIfEqualIsPressedBefore() {

        if (calculator.isEqualFlag() && lastOperator != null) {
            previousOperator = lastOperator;
            checkNumberThird();
            setLastOperator();
        }
    }

    /**
     * Then check if numberThird is not a null
     * and that numberThird is less than 0.
     * <p>
     * <code>numberSecond = numberThird</code>
     * <p>
     * Set null to numberThird.
     */
    private void checkNumberThird() {
        if (numberThird != null && numberThird.signum() < 0) {
            numberSecond = numberThird;
            numberThird = null;
        }
    }

    /**
     * Clear memory
     */
    @FXML
    public void memoryClearPressed() {
        calculator.setMemory();
        memoryPressedBefore = true;
    }

    /**
     * Add checkIfEqualIsPressedBefore value on the screen to the value stored in memory,
     * if the memory is empty, writes to it
     */
    @FXML
    public void addToMemoryPressed() {
        if (!isUndefined) {
            calculator.calculateMemoryAdd(numberFromDisplay());
            memoryPressedBefore = true;
        }
    }

    /**
     * Subtracts the value on the screen
     * from the value in memory
     */
    @FXML
    public void subtractFromMemoryPressed() {
        if (!isUndefined) {
            calculator.calculateMemorySubtract(numberFromDisplay());
            memoryPressedBefore = true;
        }
    }

    /**
     * Retrieves the value stored in memory
     * and displays it on the screen
     */
    @FXML
    public void memoryReadPressed() {
        setToDisplay(calculator.getMemory());
        memoryPressedBefore = true;
    }

    /**
     * Close the window, exit the program
     */
    @FXML
    public void closePressed() {
        ExitController.getInstance().exit();
    }

    /**
     * Hides the window in dock.
     *
     * @param event Representing some type of action
     */
    @FXML
    public void minimizedPressed(Event event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Calculate the expression
     * if @param numberThird is not null
     */
    private void countIfNumberThirdIsNotNull() throws DivisionByZeroException {
        if (numberThird != null) {
            numberSecond = calculator.calc(numberSecond, numberThird, lastOperator);
            numberThird = null;
            lastOperator = null;
        }
    }

    /**
     * Write value to variable previousOperator or lastOperator
     * if before operator has been pressed.
     * Count expression.
     *
     * @param buttonOperator Button from event
     */
    private void setOperatorAndCount(Button buttonOperator) throws DivisionByZeroException {
        if (previousOperator != null && lastOperator != null) {
            setLastOperatorAndCount(buttonOperator);
        } else {
            previousOperator = representationOfButton(buttonOperator);
        }
    }

    /**
     * Write value to variable previousOperator and lastOperator.
     * Count expression if previousOperator is not null.
     *
     * @param buttonOperator Button from event
     */
    private void setOperatorAndCountImpl(Button buttonOperator) throws DivisionByZeroException {
        if (previousOperator == null) {
            previousOperator = representationOfButton(buttonOperator);
        } else {
            setLastOperatorAndCount(buttonOperator);
        }
    }

    /**
     * Write value to variable lastOperator.
     * Count expression.
     *
     * @param buttonOperator Button from event
     */
    private void setLastOperatorAndCount(Button buttonOperator) throws DivisionByZeroException {
        lastOperator = representationOfButton(buttonOperator);
        countExpression();
    }

    /**
     * Represents button like enum in the {@link Operator}
     *
     * @param buttonOperator button from event
     * @return operator from this button
     */
    private Operator representationOfButton(Button buttonOperator) {
        return operatorMap.get(buttonOperator);
    }

    /**
     * Calculate the expression if priority first operator
     * is higher then priority second operator.
     * Set to display result of calculation.
     */
    private void countExpression() throws DivisionByZeroException {
        BigDecimal result = numberSecond;
        if (previousOperator.getPriority() >= lastOperator.getPriority()) {
            numberFirst = calculator.calc(numberFirst, numberSecond, previousOperator);
            result = numberFirst;
            previousOperator = lastOperator;
            numberSecond = null;
            lastOperator = null;
        }
        setToDisplay(result);
    }

    /**
     * Processing all data from display
     *
     * @param button button from event
     * @return String representation to display
     */
    private String processInputData(Button button) {
        if (button.equals(point)) {
            return processComma();
        } else {
            return processData(button.getText());
        }
    }

    /**
     * Processing data from display that doesn't contain comma
     *
     * @param text the number from button from event pressed
     * @return String representation to display
     */
    private String processData(String text) {
        if (display.getText().equals(DEFAULT_VALUE.toString())) {
            return text;
        } else {
            return processDataImpl(text);
        }
    }

    /**
     * Processing event with comma
     *
     * @return String representation to display
     */
    private String processComma() {
        String textFromDisplay = display.getText();
        if (operatorPressedBefore) {
            operatorPressedBefore = false;
            return "0" + COMMA;
        } else if (!textFromDisplay.contains(COMMA)) {
            return textFromDisplay + COMMA;
        } else {
            return textFromDisplay;
        }
    }

    /**
     * Processing data from display
     *
     * @param text the number from button from event pressed
     * @return String representation to display
     */
    private String processDataImpl(String text) {
        if (operatorPressedBefore) {
            operatorPressedBefore = false;
            return text;
        } else {
            return display.getText() + text;
        }
    }

    /**
     * Set false to variable operatorPressedBefore,allClearPressed.
     */
    private void setFalseToFlag() {
        operatorPressedBefore = false;
        allClearPressed = false;
    }

    /**
     * Clear display if you click on the button just one time
     */
    private void setAllClearPressed() {
        ac.setText(AC);
        setToDisplay(DEFAULT_VALUE);
        isDefaultValue = true;
        allClearPressed = true;
        operatorPressedBefore = false;
    }

    /**
     * Set to zero to all variable
     * that take part in calculation.
     */
    private void reset() {
        numberFirst = null;
        numberSecond = null;
        numberThird = null;
        previousOperator = null;
        lastOperator = null;
    }

    /**
     * Set undefined to display if division by zero.
     * Reset calculator.
     * Make @param isUndefined true.
     */
    private void setUndefined() {
        display.setText(UNDEFINED);
        reset();
        isUndefined = true;
    }

    /**
     * Set value from display to variables.
     */
    private void initializeVariable() {
        if (previousOperator == null) {
            setNumberFirst();
        } else if (lastOperator == null) {
            setNumberSecond();
        } else {
            setNumberThird();
        }
    }

    /**
     * Set to display @param DEFAULT_VALUE(0)
     * and make @param isDefaultValue true.
     */
    private void setToDisplayDefaultValue() {
        setToDisplay(DEFAULT_VALUE);
        isDefaultValue = true;
        reset();
    }

    /**
     * This method change font size of numbers on the display,
     * when number at the screen is the certain length
     */
    private void changeFontSize() {
        int length = display.getText().length();
        double fontSize = MAX_FONT_SIZE;
        if (length < MIN_LENGTH_ON_DISPLAY) {
            setFontSize(MAX_FONT_SIZE);
        } else if (length > MIN_LENGTH_ON_DISPLAY && length < MAX_LENGTH_ON_DISPLAY) {
            fontSize = MAX_FONT_SIZE / length * MIN_LENGTH_ON_DISPLAY;
            setFontSize(fontSize);
        }

    }

    /**
     * Set font-size depending on the length
     * of the text on the display
     *
     * @param size the desired font size
     */
    private void setFontSize(double size) {
        display.setStyle("-fx-font-size:" + size);
    }


    /**
     * Convert string to number.
     * Make replacement comma to point from display.
     *
     * @return converted string to BigDecimal
     */
    private BigDecimal numberFromDisplay() {
        BigDecimal res;
        String text = display.getText();
        try {
            res = (BigDecimal) FORMATTER.parse(text);
        } catch (ParseException e) {
            //Unparseable data if the beginning of the specified string cannot be parsed.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("CAN NOT PARSE THIS VALUE!");
            alert.initOwner(FXRobotHelper.getStages().get(0).getScene().getWindow());
            alert.showAndWait();
            setToDisplayDefaultValue();
            throw new RuntimeException();
        }


        return res;
    }


    /**
     * Preparation to set to display, without comma and with exponent or without.
     * And then set to display.
     *
     * @param num convert this number
     */
    private void setToDisplay(BigDecimal num) {
        num = num.round(new MathContext(16, RoundingMode.HALF_DOWN));

        String numberFormatter = FORMATTER.format(num);
        int i = numberFormatter.lastIndexOf(EXPONENT);
        int strEnd = Integer.parseInt(numberFormatter.substring(i + 1));

        boolean lessThanMaxExponentValue = strEnd <= MAX_EXPONENT_VALUE;
        boolean moreThanMinExponentValue = strEnd >= MIN_EXPONENT_VALUE;

        String text = null;
        if ((strEnd >= START_EXPONENT_VALUE && lessThanMaxExponentValue) || (moreThanMinExponentValue && strEnd <= -START_EXPONENT_VALUE)) {
            text = numberFormatter;
        } else if (!lessThanMaxExponentValue || !moreThanMinExponentValue) {
            setUndefined();
        } else {
            text = num.stripTrailingZeros().toPlainString().replace(POINT, COMMA);
        }

        if (text != null) {
            display.setText(text);
        }
        changeFontSize();
    }

    /**
     * Called to initialize checkIfEqualIsPressedBefore controller after its root element has been
     * completely processed.
     * Handle event from keyboard.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SYMBOLS.setExponentSeparator("e");
        FORMATTER.setDecimalFormatSymbols(SYMBOLS);
        FORMATTER.setParseBigDecimal(true);

        operatorMap.put(division, DIVIDE);
        operatorMap.put(subtraction, SUBTRACT);
        operatorMap.put(addition, ADD);
        operatorMap.put(multiplication, MULTIPLY);

        buttonDataMap.put(ZERO_BUTTON, zero);
        buttonDataMap.put(ONE_BUTTON, one);
        buttonDataMap.put(TWO_BUTTON, two);
        buttonDataMap.put(THREE_BUTTON, three);
        buttonDataMap.put(FOUR_BUTTON, four);
        buttonDataMap.put(FIVE_BUTTON, five);
        buttonDataMap.put(SIX_BUTTON, six);
        buttonDataMap.put(SEVEN_BUTTON, seven);
        buttonDataMap.put(EIGHT_BUTTON, eight);
        buttonDataMap.put(NINE_BUTTON, nine);
        buttonDataMap.put(POINT_BUTTON, point);

        buttonDataMap.put(DIVISION_BUTTON, division);
        buttonDataMap.put(SUBTRACTION_BUTTON, subtraction);
        buttonDataMap.put(EQUAL_BUTTON, equal);
        buttonDataMap.put(ADDITION_BUTTON, addition);
        buttonDataMap.put(MULTIPLICATION_BUTTON, multiplication);

        buttonDataMap.put(AC_BUTTON, ac);
        buttonDataMap.put(INVERSION_BUTTON, inversion);
        buttonDataMap.put(PERCENTAGE_BUTTON, percentage);

        calc.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            ButtonData buttonData = ButtonData.get(event);
            if (buttonData != null) {
                Button button = buttonDataMap.get(buttonData);
                keyPress(button, buttonData.getStyleButton().getStylePress());
            }
        });

        calc.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            ButtonData buttonData = ButtonData.get(event);
            if (buttonData != null) {
                Button button = buttonDataMap.get(buttonData);
                keyRelease(button, buttonData.getStyleButton().getStyleRelease());
            }
        });
    }

    /**
     * Press the button from keyboard
     * and set style to this button
     *
     * @param button button that should be press
     * @param style  set style to this button when button press
     */

    private void keyPress(Button button, String style) {
        button.fire();
        button.setStyle(style);

    }

    /**
     * Release button after press from keyboard
     * and set style to this button
     *
     * @param button button that should be press
     * @param style  set style to this button when button release
     */
    private void keyRelease(Button button, String style) {
        button.setStyle(style);
    }

    /*
    **********************************************************
    * Method set()
    **********************************************************
    */

    private void setNumberFirst() {
        this.numberFirst = numberFromDisplay();
    }

    private void setNumberSecond() {
        this.numberSecond = numberFromDisplay();
    }

    private void setNumberThird() {
        this.numberThird = numberFromDisplay();
    }

    private void setLastOperator() {
        this.lastOperator = null;
    }

}
