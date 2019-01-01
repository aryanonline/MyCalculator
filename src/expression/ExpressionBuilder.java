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
        }
        catch (IOException | IllegalArgumentException e1){
            System.out.println(e1.toString());
        }

        if(tokens == null){
            throw new IllegalArgumentException("Empty expression cannot be evaluated.");
        }

        for(Token token: tokens){
            switch (token.getType()) {
                case Token.TOKEN_NUMBER:
                case Token.TOKEN_VARIABLE:
                    output.add(token);
                    break;
                case Token.TOKEN_FUNCTION:
                    stack.add(token);
                    break;
                case Token.TOKEN_SEPARATOR:
                    while (!stack.empty() && stack.peek().getType() != Token.TOKEN_PARENTHESES_OPEN) {
                        output.add(stack.pop());
                    }
                    if (stack.empty() || stack.peek().getType() != Token.TOKEN_PARENTHESES_OPEN) {
                        throw new IllegalArgumentException("Misplaced expression.function separator ',' or mismatched parentheses");
                    }
                    break;
                case Token.TOKEN_OPERATOR:
                    while (!stack.empty() && stack.peek().getType() == Token.TOKEN_OPERATOR) {
                        OperatorToken o1 = (OperatorToken) token;
                        OperatorToken o2 = (OperatorToken) stack.peek();
                        if (o1.getOperator().getNumOperands() == 1 && o2.getOperator().getNumOperands() == 2) {
                            break;
                        } else if ((o1.getOperator().isLeftAssociative() && o1.getOperator().getPrecedence() <= o2.getOperator().getPrecedence())
                                || (o1.getOperator().getPrecedence() < o2.getOperator().getPrecedence())) {
                            output.add(stack.pop());
                        }else {
                            break;
                        }
                    }
                    stack.push(token);
                    break;
                case Token.TOKEN_PARENTHESES_OPEN:
                    stack.push(token);
                    break;
                case Token.TOKEN_PARENTHESES_CLOSE:
                    while (stack.peek().getType() != Token.TOKEN_PARENTHESES_OPEN) {
                        output.add(stack.pop());
                    }
                    stack.pop();
                    if (!stack.isEmpty() && stack.peek().getType() == Token.TOKEN_FUNCTION) {
                        output.add(stack.pop());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Token type encountered. This should not happen");
            }
        }


        while (!stack.empty()) {
            Token t = stack.pop();
            if (t.getType() == Token.TOKEN_PARENTHESES_CLOSE || t.getType() == Token.TOKEN_PARENTHESES_OPEN) {
                throw new IllegalArgumentException("Mismatched parentheses detected. Please check the expression");
            } else {
                output.add(t);
            }
        }
        return output.toArray(new Token[output.size()]);
    }
}
