package expression;

import expression.function.Functions;
import expression.tokenize.OperatorToken;
import expression.tokenize.Token;
import expression.tokenize.Tokenizer;

import java.io.IOException;
import java.util.*;
/*
Name: Aryan Singh
Date: 18 January 2019
To: Mr.Fernandes
Accomplishments:
Challenges:
Concerns:
 */
public class ExpressionBuilder {

    private final String expression;
    private static final boolean _DEBUG = true;
    private final Set<String> variableNames;

    //  //Create a new Expression Builder instance and initialize it with a given expression string
    public ExpressionBuilder(String expression) {
        //checks if the expression is empty
        if (expression == null || expression.trim().length() == 0){
            //displays error message
            throw new IllegalArgumentException("expression.Expression can not be empty");
        }
        //sets the expression
        this.expression = expression;
        //creates a new Hash set for vriable names variable with a capacity of 1
        this.variableNames = new HashSet<>(1);
    }
    //Declare the variables names used in the expression
    public ExpressionBuilder variables(String ... variableNames) {
        Collections.addAll(this.variableNames, variableNames);
        return this;
    }
    //declare a variable used in the expression
    public ExpressionBuilder variable(String var) {
        this.variableNames.add(var);
        return this;
    }
    //Build the instance using the custon operator and function set
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
        //return a RPN notation expression
        return new Expression(convertToRPN(this.expression));
    }
    //Converts expression into reversed polish notation
    private static Token[] convertToRPN(final String expression){
        //Stack to store operators
        final Stack<Token> stack = new Stack<>();
        //output to store numbers and RPN array
        final List<Token> output = new ArrayList<>();
        //creates a new instance of Tokenizer class
        final Tokenizer tokenizer = new Tokenizer();
        //Creates and empty list of type Token
        List<Token> tokens = null;
        try {
            //tokenizes the given expression
            tokens = tokenizer.tokenize(expression);
            if(_DEBUG) System.out.println("Tokens from Tokenizer: " + tokens);
        }
        //illegal Argument exception
        catch (IOException | IllegalArgumentException e1){
            System.out.println(e1.toString());
        }
        //if the tokens List is empty
        if(tokens == null){
            //throw error message
            throw new IllegalArgumentException("Empty expression cannot be evaluated.");
        }
        //Adding any numericaal values to the output ArrayList and any operators or functions to the stack
        if(_DEBUG) System.out.println("Looping tokens to create RPN: ==== ");
        //loops through the list
        for(Token token: tokens){
            switch (token.getType()) {
                //if the token is a number
                case Token.TOKEN_NUMBER:
                    //if the token is a variable
                case Token.TOKEN_VARIABLE:
                    //add the token to the output array list
                    output.add(token);
                    break;
                    //if the token is a function token
                case Token.TOKEN_FUNCTION:
                    //add the token to the stack
                    stack.add(token);
                    break;
                    //if a token separator was used
                case Token.TOKEN_SEPARATOR:
                    //keep adding all the into the output array list until a open tarentheses token is found
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
                    //while the stack is not empty and the peek is an operator
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
                    //throw an error message if nothing can be performed
                    throw new IllegalArgumentException("Unknown Token type encountered. This should not happen");
            }
            //debugging to check if everything is working fine
            if(_DEBUG){
                System.out.println("output: " + getAsStringArray(output));
                System.out.println("stack: " + getAsStringArray(stack));
            }
        }
        //Debugging
        if(_DEBUG) System.out.println("Rearranging output list for evaluation: ==== ");
        //loops while the stack is not empty
        while (!stack.empty()) {
            //creates each element i the stack a token
            Token t = stack.pop();
            //if a misplace parenthesis is detected
            if (t.getType() == Token.TOKEN_PARENTHESES_CLOSE || t.getType() == Token.TOKEN_PARENTHESES_OPEN) {
                //throw error message
                throw new IllegalArgumentException("Mismatched parentheses detected. Please check the expression");
            } else {
                //add the tokens into the output
                output.add(t);
            }
            //debugging to check if everything is working fine
            if(_DEBUG)
                System.out.println("output: " + getAsStringArray(output));
        }
        //returns the uotput array for debug checking
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
