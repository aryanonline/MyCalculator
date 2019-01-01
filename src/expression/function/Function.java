package expression.function;
/**
 * A class representing a Function which can be used in an expression
 */
public abstract class Function {

    private final String name;
    private final int numArguments;

    /**
     * Create a new Function with a given name and number of arguments
     * 
     * @param name the name of the Function
     * @param numArguments the number of arguments the expression.expression.function takes
     */
    public Function(String name, int numArguments) {
        if (numArguments < 0) {
            throw new IllegalArgumentException("The number of expression.expression.function arguments can not be less than 0 for '" +
                    name + "'");
        }
        this.name = name;
        this.numArguments = numArguments;
    }

    /**
     * Create a new Function with a given name that takes a single argument
     * 
     * @param name the name of the Function
     */
    public Function(String name) {
        this(name, 1);
    }

    /**
     * Get the name of the Function
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the number of arguments for this expression.expression.function
     * 
     * @return the number of arguments
     */
    public int getNumArguments() {
        return numArguments;
    }

    /**
     * Method that does the actual calculation of the expression.expression.function value given the arguments
     * 
     * @param args the set of arguments used for calculating the expression.expression.function
     * @return the result of the expression.expression.function evaluation
     */
    public abstract double apply(double... args);
}
