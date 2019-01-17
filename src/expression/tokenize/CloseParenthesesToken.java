package expression.tokenize;

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
