package expression.tokenize;

/**
 * Represents a number in the expression
 */
public final class NumberToken extends Token {
    private final double value;

    /**
     * Create a new instance
     */
    public NumberToken(double value) {
        super(TOKEN_NUMBER);
        this.value = value;
    }

    NumberToken(final char[] expression, final int offset, final int len) {
        this(Double.parseDouble(String.valueOf(expression, offset, len)));
    }

    /**
     * Get the value of the number
     */
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
