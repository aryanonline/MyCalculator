package expression.tokenize;
/*
Name: Aryan Singh
Date: 18 January 2019
Course Code: ICS4U1-01
To: Mr.Fernandes
Description: This class is used to represent a close parentheis token
Features: None
Major Skills: Use of inheritance by having the class extend the abstract class Token
Areas of concern: None
*/
class CloseParenthesesToken extends Token {
    //constructor method
    CloseParenthesesToken() {
        //passes in value for TOKEN_PARENTHESES_CLOSE to parent class
        super(Token.TOKEN_PARENTHESES_CLOSE);
    }

    @Override
    // to String metod to return the symbol of parantheses
    public String toString() {
        return ")";
    }
}
