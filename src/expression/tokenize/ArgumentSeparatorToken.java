package expression.tokenize;

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
