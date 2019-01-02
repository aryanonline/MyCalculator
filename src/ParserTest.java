import expression.Expression;
import expression.ExpressionBuilder;

public class ParserTest {

    public static void main(String[] args) {
        System.out.println("==== General Equation ====");
        generalEquation();
        //System.out.println("==== Quadratic Equation ====");
        //testQuadratic();
        //System.out.println("====Power====");
        //Power();
    }

    private static void generalEquation() {
        Expression e = new ExpressionBuilder("2+6*(8-3)")
                .variables("x", "y")
                .build()
                .setVariable("x", 6)
                .setVariable("y", 7);

        double result = e.evaluate();
        System.out.println(result);
    }

    private static void testQuadratic() {
        Expression e = new ExpressionBuilder("(A*(x^2))+(B*x)+C")
                .variables("A", "B", "C", "x")
                .build()
                .setVariable("A", 8)
                .setVariable("B", 2)
                .setVariable("C", 11)
                .setVariable("x", 12);

        double result = e.evaluate();
        System.out.println(result);

    }

    private static void Power() {
        Expression e = new ExpressionBuilder("(M*(Y^N))+Q")
                .variables("M", "x", "N", "Q")
                .build()
                .setVariable("M", 1)
                .setVariable("Y", 2)
                .setVariable("N", 2)
                .setVariable("Q", 4);

        double result = e.evaluate();
        System.out.println(result);

    }
}
