package expression.tokenize;

import expression.function.Function;
import expression.function.Functions;
import expression.operator.Operator;
import expression.operator.Operators;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private List<Token> tokens;
    private Token lastToken;
    private boolean _DEBUG = false;

    public List<Token> tokenize(String s) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(s));
        tokenizer.ordinaryChar('-');  // Don't parse minus as part of numbers.
        tokenizer.ordinaryChar('/');  // Don't treat slash as a comment start.
        tokens = new ArrayList<>();
        String xToken;
        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            switch (tokenizer.ttype) {
                case StreamTokenizer.TT_NUMBER:
                    tokens.add(new NumberToken(tokenizer.nval));
                    lastToken = new NumberToken(tokenizer.nval);
                    if(_DEBUG) System.out.println("Number:" + tokenizer.nval);
                    break;
                case StreamTokenizer.TT_WORD:
                    xToken = String.valueOf(tokenizer.sval);
                    Function f = Functions.getBuiltinFunction(tokenizer.sval);
                    if(f==null){
                        tokens.add(new VariableToken(xToken));
                        lastToken = new VariableToken(xToken);
                        if(_DEBUG) System.out.println("Variable:" + tokenizer.sval);
                    }
                    else {
                        tokens.add(new FunctionToken(f));
                        lastToken = new FunctionToken(f);
                        if(_DEBUG) System.out.println("Function:" + tokenizer.sval);
                    }
                    break;
                default:  // expression.operator
                    xToken = String.valueOf((char) tokenizer.ttype);
                    if( Operator.contains(xToken.charAt(0))){
                        if(lastToken.getType()==2 || lastToken.getType()==4 || lastToken.getType()==7){
                            tokens.add(new OperatorToken(Operators.getBuiltinOperator(xToken.charAt(0), 1)));
                            lastToken = new OperatorToken(Operators.getBuiltinOperator(xToken.charAt(0), 1));
                            if(_DEBUG) System.out.println("Unary Operator:" + xToken);
                        }
                        else{
                            tokens.add( new OperatorToken(Operators.getBuiltinOperator(xToken.charAt(0), 0)));
                            lastToken = new OperatorToken(Operators.getBuiltinOperator(xToken.charAt(0), 0));
                            if(_DEBUG) System.out.println("Normal Operator:" + xToken);
                        }
                    }
                    else if(xToken.equals("(")){
                        tokens.add(new OpenParenthesesToken());
                        lastToken = new OpenParenthesesToken();
                        if(_DEBUG) System.out.println("Open Parenthesis:" + xToken);
                    }
                    else if(xToken.equals(")")){
                        tokens.add(new CloseParenthesesToken());
                        lastToken = new CloseParenthesesToken();
                        if(_DEBUG) System.out.println("Closed Parenthesis:" + xToken);
                    }
                    else if(xToken.equals(",")){
                        tokens.add(new ArgumentSeparatorToken());
                        lastToken=new ArgumentSeparatorToken();
                        if(_DEBUG) System.out.println("Closed Parenthesis:" + xToken);
                    }
                    else {
                        throw new IllegalArgumentException(xToken + " is not a valid token.");
                    }
            }

        }

        return tokens;
    }


}
