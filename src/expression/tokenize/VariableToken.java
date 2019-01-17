package expression.tokenize;
public class VariableToken extends Token {
    //a variabel to represent the name of the function
    private final String name;
    //constructor method that takes in the anem of the vriable
    public VariableToken(String name) {
        //Passes in the value of TOKEN_VARIABLE to parent class
        super(TOKEN_VARIABLE);
        //sets the name of function
        this.name = name;
    }
    //get the name of the set variable
    public String getName() {
        return name;
    }
    //toString method to return the name of the fucntion
    @Override
    public String toString() {
        return name;
    }
}
