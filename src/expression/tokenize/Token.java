package expression.tokenize;
/*
Name: Aryan Singh
Date: 18 January 2019
Course Code: ICS4U1-01
To: Mr.Fernandes
Description: An abstract class that defines a general token and its properties.
Features: None
Major Skills: Creation of abstract class can be used to create sub classes for inheritance.
Areas of concern: None
*/
public abstract class Token {
    //Defines the value for each type of token
    public static final short TOKEN_NUMBER = 1;
    public static final short TOKEN_OPERATOR = 2;
    public static final short TOKEN_FUNCTION = 3;
    public static final short TOKEN_PARENTHESES_OPEN = 4;
    public static final short TOKEN_PARENTHESES_CLOSE = 5;
    public static final short TOKEN_VARIABLE = 6;
    public static final short TOKEN_SEPARATOR = 7;

    protected final int type;
    //Sets the token type
    Token(int type) {
        this.type = type;
    }
    //gets the token type
    public int getType() {
        return type;
    }

}