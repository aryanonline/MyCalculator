package expression;

import expression.function.Functions;
import expression.tokenize.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Expression {

    private final Token[] tokens;
    private boolean _DEBUG = true;
    private final Map<String, Double> variables;

    Expression(final Token[] tokens) {
        this.tokens = tokens;
        this.variables = new HashMap<>(1);
    }

    public Expression setVariable(final String name, final double value) {
        if (Functions.getBuiltinFunction(name) != null) {
            throw new IllegalArgumentException("The variable name '" + name + "' is invalid. Since there exists a function with the same name.");
        }
        this.variables.put(name, value);
        return this;
    }

    public double evaluate() {
        final Stack<Double> output = new Stack<>();
        if(_DEBUG) System.out.println("Evaluating: ====");
        for (Token t : tokens) {
            if (t.getType() == Token.TOKEN_NUMBER) {
                output.push(((NumberToken) t).getValue());
            } else if (t.getType() == Token.TOKEN_VARIABLE) {
                final String name = ((VariableToken) t).getName();
                final Double value = this.variables.get(name);
                if (value == null) {
                    throw new IllegalArgumentException("No value has been set for the setVariable '" + name + "'.");
                }
                output.push(value);
            } else if (t.getType() == Token.TOKEN_OPERATOR) {
                OperatorToken op = (OperatorToken) t;
                if (output.size() < op.getOperator().getNumOperands()) {
                    throw new IllegalArgumentException("Invalid number of operands available for '" + op.getOperator().getSymbol() + " operator.");
                }
                if (op.getOperator().getNumOperands() == 2) {
                    /* pop the operands and push the result of the operation */
                    double rightArg = output.pop();
                    double leftArg = output.pop();
                    output.push(op.getOperator().apply(leftArg, rightArg));
                } else if (op.getOperator().getNumOperands() == 1) {
                    /* pop the operand and push the result of the operation */
                    double arg = output.pop();
                    output.push(op.getOperator().apply(arg));
                }
            } else if (t.getType() == Token.TOKEN_FUNCTION) {
                FunctionToken func = (FunctionToken) t;
                final int numArguments = func.getFunction().getNumArguments();
                if (output.size() < numArguments) {
                    throw new IllegalArgumentException("Invalid number of arguments available for '" + func.getFunction().getName() + "' function.");
                }
                /* collect the arguments from the stack */
                double[] args = new double[numArguments];
                for (int j = numArguments - 1; j >= 0; j--) {
                    args[j] = output.pop();
                }
                output.push(func.getFunction().apply(args));
            }

            if(_DEBUG) System.out.println("output: " + getAsStringArray(output));
        }
        if (output.size() > 1) {
            throw new IllegalArgumentException("Invalid number of items in the output queue. Might be caused by an invalid number of arguments for a function.");
        }
        return output.pop();
    }

    private static String getAsStringArray(Stack<Double> tokens){
        StringBuilder stb = new StringBuilder();
        for (double t1 : tokens) {
            stb.append(String.valueOf(t1)).append(", ");
        }

        return stb.toString();
    }
}
