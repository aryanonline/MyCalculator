package expression.tokenize;
/*
Name: Aryan Singh
Date: 18 January 2019
Course Code: ICS4U1-01
To: Mr.Fernandes
Description: This class is used to represent a separator token class
Features: None
Major Skills: Use of inheritance by havig the class extend the abstract class Token
Areas of concern: None
*/

//Represents an argument seperator token
class ArgumentSeparatorToken extends Token {
    //constructor method
    ArgumentSeparatorToken() {
        //passes in Token_Seperator value to parent class
        super(Token.TOKEN_SEPARATOR);
    }

    @Override
    //to string method to return the argument seperator symbol
    public String toString() {
        return ",";
    }
}
