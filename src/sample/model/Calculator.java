package sample.model;

import sample.model.exception.DivisionByZeroException;

import java.math.BigDecimal;

import static sample.model.Operator.*;


/**
 * The main model of calculator.
 * Calculates an expression in the priority of operators.
 */
public class Calculator {

    /**
     * The flag for equal.
     */
    private boolean equalFlag;

    /**
     * Last number.
     */
    private BigDecimal lastNumber;

    /**
     * The calculator memory.
     */
    private BigDecimal memory = BigDecimal.ZERO;

    /**
     * The instance of {@link ArithmeticOperations} need to calculate
     */
    private final ArithmeticOperations operations = new ArithmeticOperations();

    /**
     * Make a calculation with two numbers.
     *
     * @param numberOne first number in the expression
     * @param numberTwo second number in the expression
     * @param operator  operator in the expression
     * @return result of calculation
     * @throws ArithmeticException divide by zero
     */
    public BigDecimal calc(BigDecimal numberOne, BigDecimal numberTwo, Operator operator) throws DivisionByZeroException {
        setOperationsVariables(numberOne, numberTwo);

        if (operator == ADD) {
            return operations.getAddition(); //make addition
        } else if (operator == SUBTRACT) {
            return operations.getSubtraction(); //make subtraction
        } else if (operator == MULTIPLY)
            return operations.getMultiplication(); //make multiplication
        else {
            return operations.getDivision(); //make division
        }
    }


    /**
     * Calculates and return result.
     *
     * @param numberFirst    first number in the expression
     * @param numberSecond   second number in the expression
     * @param numberThird    third number in the expression
     * @param operatorFirst  first operator in the expression
     * @param operatorSecond second operator in the expression
     * @return result of calculating
     * @throws ArithmeticException division by zero
     */
    public BigDecimal calculateResult(BigDecimal numberFirst, BigDecimal numberSecond, BigDecimal numberThird, Operator operatorFirst, Operator operatorSecond) throws DivisionByZeroException {
        if (lastNumber == null) {
            lastNumber = numberFirst;
        }

        equalFlag = true;

        if (operatorSecond == null && numberSecond == null) {
            numberSecond = lastNumber;
        } else if (operatorSecond != null && numberThird == null) {
            numberSecond = calc(numberFirst, numberSecond, operatorSecond);
        } else if (numberThird != null) {
            numberSecond = calc(numberSecond, numberThird, operatorSecond);
        }
        return calc(numberFirst, numberSecond, operatorFirst);

    }

    /**
     * Calculate the percent from number
     *
     * @param number The number from which to calculate the percentage
     * @return calculation the percent from number
     */
    public BigDecimal getPercentWithOneNumber(BigDecimal number) {
        operations.setNumberOne(number);
        return operations.getPercentWithOneNumber();
    }

    /**
     * Calculate the numberSecond percent of the numberFirst
     *
     * @param numberOne first number in the expression
     * @param numberTwo second number in the expression
     * @return result of calculation
     */
    public BigDecimal getPercentWithTwoNumbers(BigDecimal numberOne, BigDecimal numberTwo) {
        setOperationsVariables(numberOne, numberTwo);
        return operations.getPercentWithTwoNumbers();
    }

    /**
     * Converse number 1 or another to -1 or another
     *
     * @param number the text from display
     */
    public BigDecimal getConversion(BigDecimal number) {
        operations.setNumberOne(number);
        return operations.getConversion();
    }

    /**
     * Add value to memory
     *
     * @param number number in the expression
     */
    public void calculateMemoryAdd(BigDecimal number) {
        setOperationsVariables(memory, number);
        memory = operations.getAddition();
    }

    /**
     * Subtract value to memory
     *
     * @param number number in the expression
     */
    public void calculateMemorySubtract(BigDecimal number) {
        setOperationsVariables(memory, number);
        memory = operations.getSubtraction();
    }

    /**
     * Set value to variables in instance {@link ArithmeticOperations}
     *
     * @param numberOne first number in the expression
     * @param numberTwo second number in the expression
     */
    private void setOperationsVariables(BigDecimal numberOne, BigDecimal numberTwo) {
        operations.setNumberOne(numberOne);
        operations.setNumberTwo(numberTwo);
    }

   /*
    **********************************************************
    * Method get() and set()
    **********************************************************
    */

    public boolean isEqualFlag() {
        return equalFlag;
    }

    public void setEqualFlag() {
        this.equalFlag = false;
    }

    public void setLastNumber() {
        this.lastNumber = null;
    }

    public BigDecimal getMemory() {
        return memory;
    }

    public void setMemory() {
        this.memory = BigDecimal.ZERO;
    }
}
