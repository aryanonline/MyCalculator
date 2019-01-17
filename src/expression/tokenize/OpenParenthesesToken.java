package expression.tokenize;

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
