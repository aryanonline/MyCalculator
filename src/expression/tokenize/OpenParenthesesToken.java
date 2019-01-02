package expression.tokenize;

/**
 * Represents open parentheses token
 */
class OpenParenthesesToken extends Token{

    /**
     * Create a new instance
     */
    OpenParenthesesToken() {
        super(TOKEN_PARENTHESES_OPEN);
    }

    @Override
    public String toString() {
        return "(";
    }
}
