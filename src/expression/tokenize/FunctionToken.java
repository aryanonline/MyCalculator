package expression.tokenize;

import expression.function.*;

/*
A class representing a function token
 */
public class FunctionToken extends Token{
    //fucntion varaible that contains all variables defines in Function class
    private final Function function;
    //constructor method that takes in a function variable of type FUnction
    public FunctionToken(final Function function) {
        //passes in value of TOKEN_FUNCTION to parent class
        super(Token.TOKEN_FUNCTION);
        //sets the variable
        this.function = function;
    }
    //gets the expression function object
    public Function getFunction() {
        return function;
    }
    //to string method to return function name
    @Override
    public String toString() {
        return function.getName();
    }
}
