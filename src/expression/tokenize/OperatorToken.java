package expression.tokenize;
import expression.operator.Operator;
/*
Name: Aryan Singh
Date: 18 January 2019
Course Code: ICS4U1-01
To: Mr.Fernandes
Description: This class is used to represent an Operator Token
Features: None
Major Skills: Use of inheritance by having the class extend the abstract class Token. Use of if statment to check
if an operator is valid. Use to throw statement to shrpw error message for operator not being valid
Areas of concern: None
*/

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
