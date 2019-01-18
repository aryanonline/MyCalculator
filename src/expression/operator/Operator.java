package expression.operator;
/*
Name: Aryan Singh
Date: 18 January 2019
Course Code: ICS4U1-01
To: Mr.Fernandes
Description: An abstract class representing the different type of operators along with their precedence. Can be used by
sub classes to set the get an operators symbol and its properties defined within the abstract class.
Features: None
Major Skills: Use an array that contains all of the defined operators. Use of a while loop to loop through the array, and
use of if statement to check if a specified operator is valid. Use of an abstract to be inherited by other sub classes that
are of type operator. Use of getters and setters to get and set the properties of an operator. Use of abstract method to
to be overidden by other sub classes that are also an operator type
Areas of concern: None
*/
public abstract class Operator {

    //Assigning each operator their precedence to be used during calculation
    //precedence value for addition operator
    public static final int PRECEDENCE_ADDITION = 500;
    //precedence value for multipication operator
    public static final int PRECEDENCE_MULTIPLICATION = 1000;
    //precedence value for division operator
    public static final int PRECEDENCE_DIVISION = PRECEDENCE_MULTIPLICATION;
    //precedence value for modulus operator
    public static final int PRECEDENCE_MODULO = PRECEDENCE_DIVISION;
    //precedence value for Power operator
    public static final int PRECEDENCE_POWER = 10000;
    //precedence value for unary minus operator
    public static final int PRECEDENCE_UNARY_MINUS = 5000;
    //precedence value for modulus unary plus operator
    public static final int PRECEDENCE_UNARY_PLUS = PRECEDENCE_UNARY_MINUS;

    //Array that contains a set of allowed expression operators
    public static final char[] ALLOWED_OPERATOR_CHARS = { '+', '-', '*', '/', '%', '^', '!', '<', '>', '|', '='};

    protected final int numOperands;
    protected final boolean leftAssociative;
    protected final String symbol;
    protected final int precedence;

    /*
       checking if an operator is valid
     */
    public static boolean contains(char c){
        boolean found=false;
        for(char q: ALLOWED_OPERATOR_CHARS){
            if(c == q)
                found= true;
        }
        return found;
    }

   /*
   Constructor method for Operator class
   Symbol represents the symbol of the expression operator
   numOfOperands represent the the number of operands that the operator takes (1 or 2)
   leftAssociative is set to true if the operator is left associative and false if it is right associative
   Precedence is the precedence of operators (A way of ranking different operators)
    */
    public Operator(String symbol, int numberOfOperands, boolean leftAssociative,
                    int precedence) {
        super();
        this.numOperands = numberOfOperands;
        this.leftAssociative = leftAssociative;
        this.symbol = symbol;
        this.precedence = precedence;
    }
    //return true or false depending on if the operator is left associative of not
    public boolean isLeftAssociative() {
        return leftAssociative;
    }
    //return the precendence value for the operator
    public int getPrecedence() {
        return precedence;
    }
    //A method to apply the operation on the given operands
    public abstract double apply(double ... args);
    //return the expression sumbol of the operator
    public String getSymbol() {
        return symbol;
    }
    //returns the number of operands for the operator
    public int getNumOperands() {
        return numOperands;
    }
}
