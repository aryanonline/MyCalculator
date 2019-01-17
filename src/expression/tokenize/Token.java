package expression.tokenize;

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