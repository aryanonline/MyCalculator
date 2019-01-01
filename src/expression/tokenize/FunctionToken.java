package expression.tokenize;

import expression.function.*;

/**
 * A class representing a Function token
 */
public class FunctionToken extends Token{
    private final Function function;

    /**
     * Create a new instance
     */
    public FunctionToken(final Function function) {
        super(Token.TOKEN_FUNCTION);
        this.function = function;
    }

    /**
     * Gets the expression.expression.function object
     */
    public Function getFunction() {
        return function;
    }
}
