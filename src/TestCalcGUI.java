/*
Overall Program
Name: Aryan Singh
Date: 18 January 2019
Project Title: The online graping calculator

Description: This project is known as the scientific calculator that allows an user to be able to perform simple
calculations and secondary calculations, while also being able to generate graphs for a defined set of
equations. This calculator is an online calculator to help th user with being able to understand their math properly
by being able to look and visually understand their equation from the graphs.

Features: Allows use to be able to use secondary function within calculator. Allows user to be able to perform simple
calculations within simple calculator. User is able to select a general form of a defined equation from graph. After
selecting a general equation the user is able to selected from his saved equations for that equation. If the user
has added an equation accidentally, user is able to delete their equation. User is able to specify a domain and
range for the equation. Domain and range is validated to see if any domain is inapplicable. User is able to generate
a graph of the equation. User is able to zoom in and out of graph for mouse and by using the zoom in and zoom out
buttons. User is able save and save a graph. User is able drag their chart around. Calculator is able to perform
primary and secondary functions.

Major Skills: Use of inheritance to create a generalized functions abstract class and a operators class to then create
their sub types. Use of tokenizer to tokenize a given string expression to be later on be processed using the
shunting yard algorithm to covert infix to post fix so that a expression can be calculated using the rules of BEDMAS.
Use of selection structures like if and else statements, switch statements.
Use of different types of repetition structures like for loops, while loops, and enhanced for loops,.
Use of different data types like final,char,int,double,etc
Use of different class types like final classes, public classes, and private classes
Use of Data structures such as Maps and hashMaps
Use ... operator to create an array that is dynamic. The ... operator creates an array for a given set of of inputs
Use of static blocks that will be initialized first as soon as the class is initialized

Areas of concern:
Graph is not continuous.
Only a defined set of equations are able to be graphed
When graph has been made user has to drag around their chart to find their graph rather than being directed straight to the graph
The user has not been provided with a recently deleted button to be able to recover deleted equations
 */

public class TestCalcGUI {
    public static void main(String[] args){
        new SimpleCalcGUI();
    }
}
