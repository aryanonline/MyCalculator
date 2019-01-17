package expression.tokenize;

public final class NumberToken extends Token {
    //varaible to represent the value of the Number Token
    private final double value;
    //constructor method that takes in a value foe the Number
    public NumberToken(double value) {
        //passes value of TOKEN_NUMBER to parent class
        super(TOKEN_NUMBER);
        //sets value variable
        this.value = value;
    }
    //get the value of the number
    public double getValue() {
        return value;
    }
    //to string method to display the value of the number
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
