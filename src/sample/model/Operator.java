package sample.model;

/**
 * Enumeration {@link Operator} contains basic arithmetic operations.
 */
public enum Operator {

    /**
     * Constant for four operators
     */
    ADD(1), SUBTRACT(1), MULTIPLY(2), DIVIDE(2);

    /**
     * Priority of operator
     */
    private final int priority;

    /**
     * Initialize {@link Operator}
     *
     * @param priority the priority of operator
     */
    Operator(int priority) {
        this.priority = priority;
    }

    /*
     **********************************************************
     *  Method get()
     **********************************************************
     */

    public int getPriority() {
        return priority;
    }
}
