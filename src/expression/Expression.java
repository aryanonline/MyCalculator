package expression;
/*
Name: Aryan Singh
Date: 18 January 2019
Course Code: ICS4U1-01
To: Mr.Fernandes
Description: A class that represents an expression that can have variables and contains a method to evaluate the expression
Features: None
Major Skills: Use of map to use a key-value pair for variables. Use of hash map to disallow the usage of duplicate
variables. Use of if and else statements to check if a function exists with a variable name. Use of for loop to loop through
all the array of tokens. Use of stack to store numbers and store final result. Use of if and else statements to distinguish
number tokens from operator tokens and add the number tokens into the stack, for evaluation. Use of downcasting techniques to cast
a generalized token into a specific token type.
Areas of concern: None
*/

import expression.function.Functions;
import expression.tokenize.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Expression {
    //declaration of variables
    private final Token[] tokens;
    private boolean _DEBUG = true;
    //Since every variable is key-value pair type
    private final Map<String, Double> variables;

    //constructor method
    Expression(final Token[] tokens) {
        this.tokens = tokens;
        this.variables = new HashMap<>(1);
    }
    //setter method for variables given a key(name) and value
    public Expression setVariable(final String name, final double value) {
        //if a function exists with a variable name
        if (Functions.getBuiltinFunction(name) != null) {
            throw new IllegalArgumentException("The variable name '" + name + "' is invalid. Since there exists a function with the same name.");
        }
        //puts the name of the variable and value within the Map as a key-value pair
        this.variables.put(name, value);
        return this;
    }
    // evaluation of the tokens within the output array
    public double evaluate() {
        final Stack<Double> output = new Stack<>();
        if(_DEBUG) System.out.println("Evaluating: ====");
        for (Token t : tokens) {
            //if the token is a number push into the output stack
            if (t.getType() == Token.TOKEN_NUMBER) {
                //downcasting the Token to a NumberToken and getting value and pushing it into the stack
                output.push(((NumberToken) t).getValue());
                //check if the current token has the same type as a Token_VARIABLE
            } else if (t.getType() == Token.TOKEN_VARIABLE) {
                //downcast to Variable Token and get the String value of the Token
                final String name = ((VariableToken) t).getName();
                //gets the value of the given key
                final Double value = this.variables.get(name);
                //Throw an Error message if no value has been set for the variables
                if (value == null) {
                    throw new IllegalArgumentException("No value has been set for the setVariable '" + name + "'.");
                }
                //push the value of the variable into the output stack
                output.push(value);
                //check if the token is an operator
            } else if (t.getType() == Token.TOKEN_OPERATOR) {
                //downcasting token t to operator token
                OperatorToken op = (OperatorToken) t;
                //if the post fix is incorrect
                if (output.size() < op.getOperator().getNumOperands()) {
                    throw new IllegalArgumentException("Invalid number of operands available for '" + op.getOperator().getSymbol() + " operator.");
                }
                if (op.getOperator().getNumOperands() == 2) {
                    // pop the operands and push the result of the operation
                    double rightArg = output.pop();
                    double leftArg = output.pop();
                    output.push(op.getOperator().apply(leftArg, rightArg));
                } else if (op.getOperator().getNumOperands() == 1) {
                    // pop the operand and push the result of the operation
                    double arg = output.pop();
                    output.push(op.getOperator().apply(arg));
                }
                //if the token is a function token
            } else if (t.getType() == Token.TOKEN_FUNCTION) {
                //downcast the token to a function token
                FunctionToken func = (FunctionToken) t;
                //get the number of arguments for the function
                final int numArguments = func.getFunction().getNumArguments();
                if (output.size() < numArguments) {
                    throw new IllegalArgumentException("Invalid number of arguments available for '" + func.getFunction().getName() + "' function.");
                }
                // collect the arguments from the stack
                double[] args = new double[numArguments];
                for (int j = numArguments - 1; j >= 0; j--) {
                    args[j] = output.pop();
                }
                //apply the function to the argument and store the result in the output stack
                output.push(func.getFunction().apply(args));
            }

            if(_DEBUG) System.out.println("output: " + getAsStringArray(output));
        }
        // if the output is not possible
        if (output.size() > 1) {
            throw new IllegalArgumentException("Invalid number of items in the output queue. Might be caused by an invalid number of arguments for a function.");
        }
        //return the calculated output
        return output.pop();
    }
/*

 */
//used to distinguish the tokens as seperate pieces when printing on console for debugging
private static String getAsStringArray(Stack<Double> tokens){
    StringBuilder stb = new StringBuilder();
    for (double t1 : tokens) {
        stb.append(String.valueOf(t1)).append(", ");
    }

    return stb.toString();
}
}
