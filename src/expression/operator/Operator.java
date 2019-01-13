package expression.operator;
/**
 * Class representing operator that can be used in an expression
 */
public abstract class Operator {
    /**
     * The precedence value for the addition operation
     */
    public static final int PRECEDENCE_ADDITION = 500;
    /**
     * The precedence value for the multiplication operation
     */
    public static final int PRECEDENCE_MULTIPLICATION = 1000;
    /**
     * The precedence value for the division operation
     */
    public static final int PRECEDENCE_DIVISION = PRECEDENCE_MULTIPLICATION;
    /**
     * The precedence value for the modulo operation
     */
    public static final int PRECEDENCE_MODULO = PRECEDENCE_DIVISION;
    /**
     * The precedence value for the power operation
     */
    public static final int PRECEDENCE_POWER = 10000;
    /**
     * The precedence value for the unary minus operation
     */
    public static final int PRECEDENCE_UNARY_MINUS = 5000;
    /**
     * The precedence value for the unary plus operation
     */
    public static final int PRECEDENCE_UNARY_PLUS = PRECEDENCE_UNARY_MINUS;

    /**
     * The set of allowed expression operators
     */
    public static final char[] ALLOWED_OPERATOR_CHARS = { '+', '-', '*', '/', '%', '^', '!', '#','§', '$', '&', ';', ':', '~', '<', '>', '|', '='};

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

    /**
     * Create a new expression.expression.operator for use in expressions
     * symbol is the symbol of the expression.expression.operator
     * numberOfOperands is the number of operands the expression.expression.operator takes (1 or 2)
     * leftAssociative is set to true if the expression.expression.operator is left associative, false if it is right associative
     * precedence is the precedence value of the expression.expression.operator
     */
    public Operator(String symbol, int numberOfOperands, boolean leftAssociative,
                    int precedence) {
        super();
        this.numOperands = numberOfOperands;
        this.leftAssociative = leftAssociative;
        this.symbol = symbol;
        this.precedence = precedence;
    }

    /**
     * Check if a character is an allowed expression.expression.operator char
     */
    public static boolean isAllowedOperatorChar(char ch) {
        for (char allowed: ALLOWED_OPERATOR_CHARS) {
            if (ch == allowed) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the expression.expression.operator is left associative
     */
    public boolean isLeftAssociative() {
        return leftAssociative;
    }

    /**
     * Check the precedence value for the expression.expression.operator
     */
    public int getPrecedence() {
        return precedence;
    }

    /**
     * Apply the operation on the given operands
     */
    public abstract double apply(double ... args);

    /**
     * Get the expression.expression.operator symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Get the number of operands
     */
    public int getNumOperands() {
        return numOperands;
    }
}
