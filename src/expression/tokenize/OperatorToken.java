package expression.tokenize;

import expression.operator.Operator;

public class OperatorToken extends Token{
    //operator variable of type operator object
    private final Operator operator;
    //constructor method that takes in an operator object
    public OperatorToken(Operator op) {
        //passes on value of TOKEN_OPERATOR to parent class
        super(Token.TOKEN_OPERATOR);
        //if the operator is given
        if (op == null) {
            //throws error message a operator is not given
            throw new IllegalArgumentException("Operator is unknown for token.");
        }
        //sets the operator
        this.operator = op;
    }
    //get the expression operator for that token
    public Operator getOperator() {
        return operator;
    }
    //to string to return the symbol of the operator
    @Override
    public String toString() {
        return operator.getSymbol();
    }
}
