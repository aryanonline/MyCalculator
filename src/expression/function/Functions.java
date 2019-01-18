package expression.function;
/*
Name: Aryan Singh
Date: 18 January 2019
Course Code: ICS4U1-01
To: Mr.Fernandes
Description: Class representing the builtin functions available for use in expressions
Features: Each function has been defined using a set of of values so that each functiion can be stored using an array
of the abstract function class. I am creating an instance of the function class for each index in the array
Major Skills: Creation of an array of abstract class Function to store each builtin function, rather than intializing
everytime. Use of a static block to assign each function with their own array index and overriding their abstract method.
Use of switch statment to return the builtin fuction given the name of the function
Areas of concern: None
*/
public class Functions {
    //Defined set of functions within scientific calculator
    private static final int INDEX_SIN = 0;
    private static final int INDEX_COS = 1;
    private static final int INDEX_TAN = 2;
    private static final int INDEX_COT = 3;
    private static final int INDEX_LOG = 4;
    private static final int INDEX_LOG1P = 5;
    private static final int INDEX_ABS = 6;
    private static final int INDEX_ACOS = 7;
    private static final int INDEX_ASIN = 8;
    private static final int INDEX_ATAN = 9;
    private static final int INDEX_CBRT = 10;
    private static final int INDEX_CEIL = 11;
    private static final int INDEX_FLOOR = 12;
    private static final int INDEX_SINH = 13;
    private static final int INDEX_SQRT = 14;
    private static final int INDEX_TANH = 15;
    private static final int INDEX_COSH = 16;
    private static final int INDEX_POW = 17;
    private static final int INDEX_EXP = 18;
    private static final int INDEX_EXPM1 = 19;
    private static final int INDEX_LOG10 = 20;
    private static final int INDEX_LOG2 = 21;
    private static final int INDEX_SGN = 22;
    private static final int INDEX_ASINH = 23;
    private static final int INDEX_ACOSH = 24;
    private static final int INDEX_ATANH = 25;
    private static final int INDEX_FACT = 26;

    //array of abstract to classes to be later on used to assign set of defined functions with their values
    private static final Function[] builtinFunctions = new Function[27];

    //Assigning each index as type of Function (parent class) with their function name and defining their apply method
    static {
        //sin function
        builtinFunctions[INDEX_SIN] = new Function("sin") {
            @Override
            public double apply(double... args) {
                return Math.sin(Math.toRadians(args[0]));
            }
        };
        //cos function
        builtinFunctions[INDEX_COS] = new Function("cos") {
            @Override
            public double apply(double... args) {
                return Math.cos(Math.toRadians(args[0]));
            }
        };
        //tan function
        builtinFunctions[INDEX_TAN] = new Function("tan") {
            @Override
            public double apply(double... args) {
                return Math.tan(Math.toRadians(args[0]));
            }
        };
        //cot function
        builtinFunctions[INDEX_COT] = new Function("cot") {
            @Override
            public double apply(double... args) {
                double tan = Math.tan(Math.toRadians(args[0]));
                if (tan == 0d) {
                    throw new ArithmeticException("Division by zero in cotangent!");
                }
                return 1d/Math.tan(Math.toRadians(args[0]));
            }
        };
        //Log function
        builtinFunctions[INDEX_LOG] = new Function("log") {
            @Override
            public double apply(double... args) {
                return Math.log(args[0]);
            }
        };
        //Log2 function
        builtinFunctions[INDEX_LOG2] = new Function("log2") {
            @Override
            public double apply(double... args) {
                return Math.log(args[0]) / Math.log(2d);
            }
        };
        //Log10 function
        builtinFunctions[INDEX_LOG10] = new Function("log10") {
            @Override
            public double apply(double... args) {
                return Math.log10(args[0]);
            }
        };
        //Log1P function
        builtinFunctions[INDEX_LOG1P] = new Function("log1p") {
            @Override
            public double apply(double... args) {
                return Math.log1p(args[0]);
            }
        };
        //Abs function
        builtinFunctions[INDEX_ABS] = new Function("abs") {
            @Override
            public double apply(double... args) {
                return Math.abs(args[0]);
            }
        };
        //ACos function
        builtinFunctions[INDEX_ACOS] = new Function("acos") {
            @Override
            public double apply(double... args) {
                return Math.toDegrees(Math.acos(args[0]));
            }
        };
        //Asin function
        builtinFunctions[INDEX_ASIN] = new Function("asin") {
            @Override
            public double apply(double... args) {
                return Math.toDegrees(Math.asin(args[0]));
            }
        };
        //Atan function
        builtinFunctions[INDEX_ATAN] = new Function("atan") {
            @Override
            public double apply(double... args) {
                return Math.toDegrees(Math.atan(args[0]));
            }
        };
        //floor function
        builtinFunctions[INDEX_FLOOR] = new Function("floor") {
            @Override
            public double apply(double... args) {
                return Math.floor(args[0]);
            }
        };
        //SinH function
        builtinFunctions[INDEX_SINH] = new Function("sinh") {
            @Override
            public double apply(double... args) {
                return Math.sinh(args[0]);
            }
        };
        //Sqrt function
        builtinFunctions[INDEX_SQRT] = new Function("sqrt") {
            @Override
            public double apply(double... args) {
                return Math.sqrt(args[0]);
            }
        };
        //Tanh function
        builtinFunctions[INDEX_TANH] = new Function("tanh") {
            @Override
            public double apply(double... args) {
                return Math.tanh(args[0]);
            }
        };
        //cosh function
        builtinFunctions[INDEX_COSH] = new Function("cosh") {
            @Override
            public double apply(double... args) {
                return Math.cosh(args[0]);
            }
        };
        //ceil function
        builtinFunctions[INDEX_CEIL] = new Function("ceil") {
            @Override
            public double apply(double... args) {
                return Math.ceil(args[0]);
            }
        };
        //pow function
        builtinFunctions[INDEX_POW] = new Function("pow", 2) {
            @Override
            public double apply(double... args) {
                return Math.pow(args[0], args[1]);
            }
        };
        //Exp function
        builtinFunctions[INDEX_EXP] = new Function("exp", 1) {
            @Override
            public double apply(double... args) {
                return Math.exp(args[0]);
            }
        };
        //Expm1 function
        builtinFunctions[INDEX_EXPM1] = new Function("expm1", 1) {
            @Override
            public double apply(double... args) {
                return Math.expm1(args[0]);
            }
        };
        //SGN function
        builtinFunctions[INDEX_SGN] = new Function("signum", 1) {
            @Override
            public double apply(double... args) {
                if (args[0] > 0) {
                    return 1;
                } else if (args[0] < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
        //ASinH function
        builtinFunctions[INDEX_ASINH] = new Function("asinh", 1) {
            @Override
            public double apply(double... args) {
                return Math.log(args[0] + Math.sqrt(args[0] * args[0] + 1));
            }
        };
        //Acosh function
        builtinFunctions[INDEX_ACOSH] = new Function("acosh", 1) {
            @Override
            public double apply(double... args) {
                return Math.log(args[0] + Math.sqrt(args[0] * args[0] - 1));
            }
        };
        //Atanh function
        builtinFunctions[INDEX_ATANH] = new Function("atanh", 1) {
            @Override
            public double apply(double... args) {
                return Math.log((1 / args[0] + 1) / (1 / args[0] - 1))  / 2;
            }
        };
        //Fact function
        builtinFunctions[INDEX_FACT] = new Function("fact", 1) {
            @Override
            public double apply(double... args) {
                long x = Math.round(args[0]);
                long fact = 1;
                if(x<0) return 0;
                while(x>=1){
                    if(x<=1 && fact==0)
                        fact=1;
                    else
                        fact = fact * x;

                    x--;
                }
                return fact;
            }
        };
    }

    /*
     * Get the built in function depending on its name and return the correct set of methods assigned for that function
     */
    public static Function getBuiltinFunction(final String name) {

        switch (name.toLowerCase()) {
            // returning builtin function given name of function
            case "sin": //return built in fuction for sin
                return builtinFunctions[INDEX_SIN];
            case "cos"://return built in fuction for cos
                return builtinFunctions[INDEX_COS];
            case "tan"://return built in fuction for tan
                return builtinFunctions[INDEX_TAN];
            case "cot"://return built in fuction for cot
                return builtinFunctions[INDEX_COT];
            case "asin"://return built in fuction for asin
                return builtinFunctions[INDEX_ASIN];
            case "acos"://return built in fuction for acos
                return builtinFunctions[INDEX_ACOS];
            case "atan"://return built in fuction for atan
                return builtinFunctions[INDEX_ATAN];
            case "sinh"://return built in fuction for sinh
                return builtinFunctions[INDEX_SINH];
            case "cosh"://return built in fuction for cosh
                return builtinFunctions[INDEX_COSH];
            case "tanh"://return built in fuction for tanh
                return builtinFunctions[INDEX_TANH];
            case "abs"://return built in fuction for abs
                return builtinFunctions[INDEX_ABS];
            case "log"://return built in fuction for log
                return builtinFunctions[INDEX_LOG];
            case "log10"://return built in fuction for log10
                return builtinFunctions[INDEX_LOG10];
            case "log2"://return built in fuction for log2
                return builtinFunctions[INDEX_LOG2];
            case "log1p"://return built in fuction for log1p
                return builtinFunctions[INDEX_LOG1P];
            case "ceil"://return built in fuction for ceil
                return builtinFunctions[INDEX_CEIL];
            case "floor"://return built in fuction for floor
                return builtinFunctions[INDEX_FLOOR];
            case "sqrt"://return built in fuction for sqrt
                return builtinFunctions[INDEX_SQRT];
            case "cbrt"://return built in fuction for cbrt
                return builtinFunctions[INDEX_CBRT];
            case "pow"://return built in fuction for pow
                return builtinFunctions[INDEX_POW];
            case "exp"://return built in fuction for exp
                return builtinFunctions[INDEX_EXP];
            case "expm1"://return built in fuction for expm1
                return builtinFunctions[INDEX_EXPM1];
            case "signum"://return built in fuction for signum
                return builtinFunctions[INDEX_SGN];
            case "asinh"://return built in fuction for asinh
                return builtinFunctions[INDEX_ASINH];
            case "acosh"://return built in fuction for acosh
                return builtinFunctions[INDEX_ACOSH];
            case "atanh"://return built in fuction for atanh
                return builtinFunctions[INDEX_ATANH];
            case "fact"://return built in fuction for fact
                return builtinFunctions[INDEX_FACT];
            default://return null if name does not match
                return null;
        }
    }

}
