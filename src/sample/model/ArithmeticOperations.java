package sample.model;

import sample.model.exception.DivisionByZeroException;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * This class implements calculation of mathematical operations.
 */
final class ArithmeticOperations {

    /**
     * First value in operation
     */
    private BigDecimal numberOne;

    /**
     * Second value in operation
     */
    private BigDecimal numberTwo;

    /**
     * This method does conversion with value which we get from user.
     * If we get 2 it will convert to -2, and vice versa.
     *
     * @return We return the inverted number
     */
    BigDecimal getConversion() {
        return numberOne.negate(MathContext.DECIMAL64).stripTrailingZeros();
    }

    /**
     * This method find some percent from expected value
     *
     * @return <code> numberTwo % </code> from @param numberOne
     */
    BigDecimal getPercentWithTwoNumbers() {
        if (numberTwo.equals(BigDecimal.ZERO)) {
            return numberTwo.stripTrailingZeros();
        }
        return numberOne.multiply(BigDecimal.valueOf(0.01).multiply(numberTwo), MathContext.DECIMAL64).stripTrailingZeros();
    }

    /**
     * Returns a percent from expected value.
     *
     * @return 1%(percent) from common number
     */
    BigDecimal getPercentWithOneNumber() {
        if (numberOne.equals(BigDecimal.ZERO)) {
            return numberOne.stripTrailingZeros();
        }
        return numberOne.multiply(BigDecimal.valueOf(0.01), MathContext.DECIMAL64).stripTrailingZeros();
    }

    /**
     * Returns a {@link BigDecimal} whose value is
     * <code> this / divisor </code>,
     * with rounding according to the context settings.
     *
     * @return the result of division numberOne by numberTwo, rounded as necessary.
     * @throws DivisionByZeroException could be thrown if  ("Division by zero");
     */
    BigDecimal getDivision() throws DivisionByZeroException {
        if (numberTwo.compareTo(BigDecimal.ZERO) == 0) {
            throw new DivisionByZeroException();
        }
        return numberOne.divide(numberTwo, MathContext.DECIMAL128).stripTrailingZeros();
    }

    /**
     * Returns a {@link BigDecimal} whose value is
     * <code> numberOne * numberTwo </code>,
     * with rounding according to the context settings.
     *
     * @return {@code numberOne * numberTwo}, rounded as necessary.
     */
    BigDecimal getMultiplication() {
        return numberOne.multiply(numberTwo, MathContext.DECIMAL128).stripTrailingZeros();
    }

    /**
     * Returns a {@link BigDecimal} whose value is
     * <code> numberOne - numberTwo </code>
     *
     * @return {@code numberOne - numberTwo}
     */
    BigDecimal getSubtraction() {
        return numberOne.subtract(numberTwo).stripTrailingZeros();
    }

    /**
     * Returns a {@link BigDecimal} whose value is {@code (numberOne +
     * numberTwo)},
     *
     * @return {@code numberOne + numberTwo}
     */
    BigDecimal getAddition() {
        return numberOne.add(numberTwo).stripTrailingZeros();
    }

    /*
     **********************************************************
     * Method set()
     **********************************************************
     */

    void setNumberOne(BigDecimal numberOne) {
        this.numberOne = numberOne;
    }

    void setNumberTwo(BigDecimal numberTwo) {
        this.numberTwo = numberTwo;
    }
}
