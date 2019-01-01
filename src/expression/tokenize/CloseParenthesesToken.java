package expression.tokenize;

/**
 * Represents closed parentheses token
 */
class CloseParenthesesToken extends Token {

    /**
     * Create a new instance
     */
    CloseParenthesesToken() {
        super(Token.TOKEN_PARENTHESES_CLOSE);
    }
}
