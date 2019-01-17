package expression.tokenize;
/*
Name: Aryan Singh
Date: 18 January 2019
Course Code: ICS4U1-01
To: Mr.Fernandes
Description: This class is used to represent a open parenthesis token
Features: None
Major Skills: Use of inheritance by having the class extend the abstract class Token
Areas of concern: None
*/

class OpenParenthesesToken extends Token{
    //constructor method
    OpenParenthesesToken() {
        //passes in value of PARENTHESES_OPEN to parent class
        super(TOKEN_PARENTHESES_OPEN);
    }
    //to string to return the symbol of the open parenthese token
    @Override
    public String toString() {
        return "(";
    }
}
