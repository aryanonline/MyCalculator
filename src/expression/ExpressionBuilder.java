package expression;

import expression.function.Functions;
import expression.tokenize.OperatorToken;
import expression.tokenize.Token;
import expression.tokenize.Tokenizer;

import java.io.IOException;
import java.util.*;

/**
 * Factory class for {@link Expression} instances. This class is the main API entrypoint. Users should create new
 * {@link Expression} instances using this factory class.
 */
public class ExpressionBuilder {

    private final String expression;
    private static final boolean _DEBUG = true;
    private final Set<String> variableNames;

    /**
     * Create a new expression.ExpressionBuilder instance and initialize it with a given expression string.
     * @param expression the expression to be parsed
     */
    public ExpressionBuilder(String expression) {
        if (expression == null || expression.trim().length() == 0) {
            throw new IllegalArgumentException("expression.Expression can not be empty");
        }
        this.expression = expression;
        this.variableNames = new HashSet<>(1);
    }

    /**
     * Declare variable names used in the expression
     * @param variableNames the variables used in the expression
     * @return the expression.ExpressionBuilder instance
     */
    public ExpressionBuilder variables(String ... variableNames) {
        Collections.addAll(this.variableNames, variableNames);
        return this;
    }

    /**
     * Declare a variable used in the expression
     * @param var the variable used in the expression
     * @return the expression.ExpressionBuilder instance
     */
    public ExpressionBuilder variable(String var) {
        this.variableNames.add(var);
        return this;
    }

    /**
     * Build the {@link Expression} instance using the custom operator and functions set.
     * @return an {@link Expression} instance which can be used to evaluate the result of the expression
     */
    public Expression build() {
        if (expression.length() == 0) {
            throw new IllegalArgumentException("The expression can not be empty");
        }
        /* Check if there are duplicate vars/functions */
        for (String var : variableNames) {
            if (Functions.getBuiltinFunction(var) != null) {
                throw new IllegalArgumentException("A variable can not have the same name as a expression.function [" + var + "]");
            }
        }
        return new Expression(convertToRPN(this.expression));
    }

    /**
     * Reverse polish notation for expressions.
     * http://www.learn4master.com/data-structures/stack/convert-infix-notation-to-reverse-polish-notation-java
     */
    private static Token[] convertToRPN(final String expression){
        final Stack<Token> stack = new Stack<>();
        final List<Token> output = new ArrayList<>();

        final Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens = null;
        try {
            tokens = tokenizer.tokenize(expression);
            if(_DEBUG) System.out.println("Tokens from Tokenizer: " + tokens);
        }
        catch (IOException | IllegalArgumentException e1){
            System.out.println(e1.toString());
        }

        if(tokens == null){
            throw new IllegalArgumentException("Empty expression cannot be evaluated.");
        }
        //Adding any numericaal values to the output ArrayList and any operators or functions to the stack
        if(_DEBUG) System.out.println("Looping tokens to create RPN: ==== ");
        for(Token token: tokens){
            switch (token.getType()) {
                case Token.TOKEN_NUMBER:
                case Token.TOKEN_VARIABLE:
                    output.add(token);
                    break;
                case Token.TOKEN_FUNCTION:
                    stack.add(token);
                    break;
                    //if a token separator was used keep adding the separator to the output until a open parenthesis is found
                case Token.TOKEN_SEPARATOR:
                    while (!stack.empty() && stack.peek().getType() != Token.TOKEN_PARENTHESES_OPEN) {
                        output.add(stack.pop());
                    }
                    //if the user enters any type of illegal separators
                    if (stack.empty() || stack.peek().getType() != Token.TOKEN_PARENTHESES_OPEN) {
                        throw new IllegalArgumentException("Misplaced expression.function separator ',' or mismatched parentheses");
                    }
                    break;
                    //check if the Token is an opperator
                case Token.TOKEN_OPERATOR:
                    //while the stack is an the peek is an operator
                    while (!stack.empty() && stack.peek().getType() == Token.TOKEN_OPERATOR) {
                        //down cast o1 to an operator token
                        OperatorToken o1 = (OperatorToken) token;
                        //downcast o2 to an operator token
                        OperatorToken o2 = (OperatorToken) stack.peek();
                        //check the number of operands for each operator
                        if (o1.getOperator().getNumOperands() == 1 && o2.getOperator().getNumOperands() == 2) {
                            break;
                            //check which operator has an higher precedence value (ensuring that the expression will be evaluated using the rules of BEDMAS)
                        } else if ((o1.getOperator().isLeftAssociative() && o1.getOperator().getPrecedence() <= o2.getOperator().getPrecedence())
                                || (o1.getOperator().getPrecedence() < o2.getOperator().getPrecedence())) {
                            //if the precedence is greater add the operator intitially within the stack to the output array
                            output.add(stack.pop());
                        }else {
                            break;
                        }
                    }
                    //Push the current operator token into the stack
                    stack.push(token);
                    break;
                    //check if the Token is an open parenthesis
                case Token.TOKEN_PARENTHESES_OPEN:
                    //add the parenthesis into the stack
                    stack.push(token);
                    break;
                    //check if the token is a close parenthesis
                case Token.TOKEN_PARENTHESES_CLOSE:
                    //add all the elements in the stack until the parenthesis open
                    while (stack.peek().getType() != Token.TOKEN_PARENTHESES_OPEN) {
                        output.add(stack.pop());
                    }
                    // remove the parenthesis open
                    stack.pop();
                    //if the stack has a function add the function to the output array
                    if (!stack.isEmpty() && stack.peek().getType() == Token.TOKEN_FUNCTION) {
                        output.add(stack.pop());
                    }
                    break;
                default:
                    //throw an error if nothing can be performed
                    throw new IllegalArgumentException("Unknown Token type encountered. This should not happen");
            }
            //debugging to check if everything is working fine
            if(_DEBUG){
                System.out.println("output: " + getAsStringArray(output));
                System.out.println("stack: " + getAsStringArray(stack));
            }
        }
        //Confirm with Dad on this line of code
        if(_DEBUG) System.out.println("Rearranging output list for evaluation: ==== ");
        //error checking for any invalid inputs
        while (!stack.empty()) {
            Token t = stack.pop();
            if (t.getType() == Token.TOKEN_PARENTHESES_CLOSE || t.getType() == Token.TOKEN_PARENTHESES_OPEN) {
                throw new IllegalArgumentException("Mismatched parentheses detected. Please check the expression");
            } else {
                //add the tokens into the output
                output.add(t);
            }
            //debugging to check if everything is working fine
            if(_DEBUG)
                System.out.println("output: " + getAsStringArray(output));
        }
        return output.toArray(new Token[output.size()]);
    }

    // Method is called when debugging
    private static String getAsStringArray(List<Token> tokens){
        StringBuilder stb = new StringBuilder();
        for (Token t1 : tokens) {
            stb.append(t1.toString()).append(", ");
        }

        return stb.toString();
    }
}
