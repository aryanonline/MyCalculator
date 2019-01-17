package expression.function;
/*
Name: Aryan Singh
Date: 18 January 2019
To: Mr.Fernandes
Accomplishments:
Challenges:
Concerns:
 */
public abstract class Function {

    private final String name;
    private final int numArguments;

    //Contructor method
    public Function(String name, int numArguments) {
        //checks if the passesd on number of arguments is valid
        if (numArguments < 0) {
            //throws illegal Argument Exception error message
            throw new IllegalArgumentException("The number of expression.expression.function arguments can not be less than 0 for '" +
                    name + "'");
        }
        //setting the name
        this.name = name;
        //setting the number of Arguments
        this.numArguments = numArguments;
    }

    //Function method that tkes in a name and has only one single argument
    public Function(String name) {
        this(name, 1);
    }

   //getter method for the name of the fuction
    public String getName() {
        return name;
    }

    //getter method for the number of arguments for the fuction
    public int getNumArguments() {
        return numArguments;
    }

    //abstract method that will be defined to perform the calculation for each type of function
    public abstract double apply(double... args);
}
