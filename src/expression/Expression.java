package expression;

import expression.function.Functions;
import expression.tokenize.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Expression {
    //decleration of variables
    private final Token[] tokens;
    private boolean _DEBUG = true;
    private final Map<String, Double> variables;

    //contructor method
    Expression(final Token[] tokens) {
        this.tokens = tokens;
        this.variables = new HashMap<>(1);
    }
    // if the user has entered any type of illegal functions
    public Expression setVariable(final String name, final double value) {
        if (Functions.getBuiltinFunction(name) != null) {
            throw new IllegalArgumentException("The variable name '" + name + "' is invalid. Since there exists a function with the same name.");
        }
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
                //downcasting the Token to a NumberToken and getting value
                output.push(((NumberToken) t).getValue());
                //check if the current token has the same type as a Token_VARIABLE
            } else if (t.getType() == Token.TOKEN_VARIABLE) {
                //downcast to Variable Token and get the String value of the Token
                final String name = ((VariableToken) t).getName();
                //I am not Sure on what does this line do
                final Double value = this.variables.get(name);
                //Throw an Error message if no value has been set for the variables
                if (value == null) {
                    throw new IllegalArgumentException("No value has been set for the setVariable '" + name + "'.");
                }
                //push the value of the variable into the output stack
                output.push(value);
                //check if the token is an operator
            } else if (t.getType() == Token.TOKEN_OPERATOR) {
                // if there are too many operators
                OperatorToken op = (OperatorToken) t;
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
                //if the argument is with a function
            } else if (t.getType() == Token.TOKEN_FUNCTION) {
                FunctionToken func = (FunctionToken) t;
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
