package expression.tokenize;
/**
 * represents a setVariable used in an expression
 */
public class VariableToken extends Token {
    private final String name;

    /**
     * Create a new instance
     */
    public VariableToken(String name) {
        super(TOKEN_VARIABLE);
        this.name = name;
    }

    /**
     * Get the name of the setVariable
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
