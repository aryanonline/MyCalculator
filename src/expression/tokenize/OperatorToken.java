package expression.tokenize;

import expression.operator.Operator;

/**
 * Represents an expression.expression.operator used in expressions
 */
public class OperatorToken extends Token{
    private final Operator operator;

    /**
     * Create a new instance
     */
    public OperatorToken(Operator op) {
        super(Token.TOKEN_OPERATOR);
        if (op == null) {
            throw new IllegalArgumentException("Operator is unknown for token.");
        }
        this.operator = op;
    }

    /**
     * Get the expression.expression.operator for that token
     */
    public Operator getOperator() {
        return operator;
    }
}
