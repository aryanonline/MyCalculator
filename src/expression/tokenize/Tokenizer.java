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
/*
Name: Aryan Singh
Date: 18 January 2019
Course Code: ICS4U1-01
To: Mr.Fernandes
Description: A class that, using a given string tokenize the string into its tokens, and returns a list of the tokens
https://www.baeldung.com/java-stringtokenizer (understanding how to tokenize a given string)
Features: None
Major Skills: Use of switch statements to distinguish from tokens. Use of if and else statements to distigusish
between the similar properties of some tokens. Use of while loop to loop through all tokens within tokenizer.
Use of list to create a list to store tokens
Areas of concern: None
*/

public class Tokenizer {
    private List<Token> tokens;
    private Token lastToken;
    private boolean _DEBUG = false;

    public List<Token> tokenize(String s) throws IOException {
        //stream tokenizer tokenizes a given stream of string into tokens
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(s));
        tokenizer.ordinaryChar('-');  // Don't parse minus as part of numbers.
        tokenizer.ordinaryChar('/');  // Don't treat slash as a comment start.
        tokens = new ArrayList<>();
        String xToken;
        //loops until tokenizer has reached its end
        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            switch (tokenizer.ttype) {
                // if the token is a number token
                case StreamTokenizer.TT_NUMBER:
                    //assign the token as an instance of a number token
                    tokens.add(new NumberToken(tokenizer.nval));
                    //make the last token a number token type
                    lastToken = new NumberToken(tokenizer.nval);
                    if(_DEBUG) System.out.println("Number:" + tokenizer.nval);
                    break;
                    // if the token is a word token
                case StreamTokenizer.TT_WORD:
                    // get the name of the token
                    xToken = String.valueOf(tokenizer.sval);
                    //get the token built in function
                    Function f = Functions.getBuiltinFunction(tokenizer.sval);
                    // if the word has no name
                    if(f==null){
                        // make the token a Variable token type
                        tokens.add(new VariableToken(xToken));
                        //make the last token of type Variable token
                        lastToken = new VariableToken(xToken);
                        if(_DEBUG) System.out.println("Variable:" + tokenizer.sval);
                    }
                    else {
                        //if the token has a built in function assign the token its function token
                        tokens.add(new FunctionToken(f));
                        //make the last token a function token type
                        lastToken = new FunctionToken(f);
                        if(_DEBUG) System.out.println("Function:" + tokenizer.sval);
                    }
                    break;
                default: //if the token does not match any type it is an expression operator
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
                    //if the token is an symbol of an open paratheses
                    else if(xToken.equals("(")){
                        //assign the token as an open parenthesis token
                        tokens.add(new OpenParenthesesToken());
                        // make the last of the same type as token
                        lastToken = new OpenParenthesesToken();
                        if(_DEBUG) System.out.println("Open Parenthesis:" + xToken);
                    }
                    //check if the token has a close parenthesis token
                    else if(xToken.equals(")")){
                        //add a a close parenthesis token with the tokens List
                        tokens.add(new CloseParenthesesToken());
                        //make last token a close parenthesis token type
                        lastToken = new CloseParenthesesToken();
                        if(_DEBUG) System.out.println("Closed Parenthesis:" + xToken);
                    }
                    //check if the token is an argument seperator
                    else if(xToken.equals(",")){
                        //add an argument seperator token within tokens List
                        tokens.add(new ArgumentSeparatorToken());
                        //make the last token an argument seperator token type
                        lastToken=new ArgumentSeparatorToken();
                        if(_DEBUG) System.out.println("Closed Parenthesis:" + xToken);
                    }
                    else {
                        //if none matches throw an illegal argument error
                        throw new IllegalArgumentException(xToken + " is not a valid token.");
                    }
            }

        }
        //return the List of tokens
        return tokens;
    }


}
