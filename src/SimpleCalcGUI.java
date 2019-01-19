import expression.Expression;
import expression.ExpressionBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class SimpleCalcGUI extends JFrame {
    private JButton[] CalcButtons;
    private JLabel jtaResult;
    private String textExpression="";
    private String lastToken="";
    Stack<String> tokenArray = new Stack<String>();
    Stack<String> memTokenArray = new Stack<String>();
    boolean is2ndFActive = false;

    String[] ButtonText = {"AC", "2ndF", "M", "MR", "MC", "+/-", "%", "/",
                           "", "Pow", "Exp", "x!", "7", "8", "9", "X",
                            "Sin", "Cos", "Tan", "Log", "4", "5", "6", "-",
                            "Sinh", "Cosh", "Tanh", "Ln", "1", "2", "3", "+",
                            "(", ")", "Pi", ",", "Del", "0", ".", "="};

    public SimpleCalcGUI(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        setSize(680, 500);
        this.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        CalcButtons = new JButton[40];


        ImageIcon ic = CalcUtils.getTransparentIcon("graph-icon2.png", 32, 32);

        //Create Common Action Listener
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JButton) {
                    String text = ((JButton) e.getSource()).getText();
                    HandleExpression(e);
                }
            }
        };

        for(int i=0; i<5; i++){
            for(int j=0; j<8; j++){
                int itemNum = (i*8)+j;

                CalcButtons[itemNum] = new JButton(ButtonText[itemNum]);
                CalcButtons[itemNum].setPreferredSize(new Dimension(24, 24));
                CalcButtons[itemNum].putClientProperty("ItemIndex", itemNum);
                CalcButtons[itemNum].setForeground(Color.WHITE);
                CalcButtons[itemNum].setFont(new Font("Tahoma", Font.PLAIN, 16));

                //Set ActionListener
                CalcButtons[itemNum].addActionListener(listener);

                if(itemNum==8) CalcButtons[itemNum].setIcon(ic);

                //Set Button Color
                if(itemNum==0)
                    CalcButtons[itemNum].setBackground(new Color(232, 91, 47));
                else if(itemNum==1)
                    CalcButtons[itemNum].setBackground(new Color(0, 90, 177));
                else if(itemNum==8)
                    CalcButtons[itemNum].setBackground(new Color(250, 250, 180));
                else if(itemNum==7 || itemNum==15 || itemNum==23 || itemNum==31 || itemNum==36 || itemNum==39)
                    CalcButtons[itemNum].setBackground(new Color(246, 135, 16));
                else if((itemNum>=2 && itemNum<=6) || (itemNum>=12 && itemNum<=14) || (itemNum>=20 && itemNum<=22) || (itemNum>=28 && itemNum<=30) || (itemNum>=36 && itemNum<=38))
                    CalcButtons[itemNum].setBackground(new Color(100, 100, 100));
                else
                    CalcButtons[itemNum].setBackground(new Color(51, 51, 51));

                //Add to GridBagLayout
                CalcUtils.addToGridBagLayout(panel, CalcButtons[itemNum], j, i,1,1, GridBagConstraints.BOTH);
            }
        }


        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(jtaResult = new JLabel(), BorderLayout.CENTER);
        jtaResult.setFont(new Font("Tahoma", Font.PLAIN, 24));
        jtaResult.setOpaque(true);
        jtaResult.setBackground(Color.BLACK);
        jtaResult.setForeground(Color.WHITE);
        jtaResult.setHorizontalAlignment(JLabel.RIGHT);
        jtaResult.setVerticalAlignment(JLabel.CENTER);
        jtaResult.setPreferredSize(new Dimension(800, 80));

        this.getContentPane().add(p2, BorderLayout.NORTH);
        this.getContentPane().add(panel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void toggle2NdF() {
        if(is2ndFActive){
            CalcButtons[1].setBackground(new Color(246, 135, 16));
            CalcButtons[1].setFont(new Font("Tahoma", Font.PLAIN, 16));

            CalcButtons[16].setText("Sin");
            CalcButtons[17].setText("Cos");
            CalcButtons[18].setText("Tan");
            CalcButtons[24].setText("Sinh");
            CalcButtons[25].setText("Cosh");
            CalcButtons[26].setText("Tanh");

            //change sin to sin inverse
            is2ndFActive=false;
        }
        else
        {
            CalcButtons[1].setBackground(new Color(0, 110, 0));
            CalcButtons[1].setFont(new Font("Tahoma", Font.ITALIC, 16));

            CalcButtons[16].setText("ASin");
            CalcButtons[17].setText("ACos");
            CalcButtons[18].setText("ATan");
            CalcButtons[24].setText("ASinh");
            CalcButtons[25].setText("ACosh");
            CalcButtons[26].setText("ATanh");

            is2ndFActive=true;
        }
    }

    //Creating buttons within calculator
    private void HandleExpression(ActionEvent e){
        JButton btn = (JButton) e.getSource();
        int ItemIndex = (int) btn.getClientProperty("ItemIndex");
        switch (ItemIndex){
            case 0: // AC
                allClear();
                break;
            case 1: // 2ndF
                toggle2NdF();
                lastToken="";
                break;
            case 2: // M
                memorize();
                lastToken="";
                break;
            case 3: // MR
                memRecall();
                lastToken="";
                break;
            case 4: //MC
                memClear();
                lastToken="";
                break;
            case 5: // +/-
                if(CalcUtils.isNumeric(lastToken)) {
                    tokenArray.pop();
                    if(lastToken.substring(0,1).equals("-"))
                        lastToken = lastToken.substring(1);
                    else
                        lastToken = "-" + lastToken;
                }
                else
                    lastToken="";
                break;
            case 6: // %
                if(CalcUtils.isNumeric(lastToken)) {
                    double d;
                    try {
                        d = Double.parseDouble(lastToken);
                        tokenArray.pop();
                        lastToken = String.valueOf(d/100);
                    } catch (NumberFormatException | NullPointerException nfe) {
                        System.out.println(e.toString());
                    }
                }
                else
                    lastToken="";
                break;
            case 7: // /
                lastToken="/";
                break;
            case 8: // GRAPH
                lastToken="";
                GraphCalcGUI g = new GraphCalcGUI();
                break;
            case 9: // POW
                lastToken = "POW(";
                break;
            case 10: // EXP
                lastToken = "EXP(";
                break;
            case 11: // FACTORIAL
                lastToken="FACT(";
                break;
            case 12: // NUM 7
                lastToken="7";
                break;
            case 13: // NUM 8
                lastToken="8";
                break;
            case 14: // NUM 9
                lastToken="9";
                break;
            case 15: // MULT
                lastToken="*";
                break;
            case 16: // ASIN
                if(is2ndFActive)
                    lastToken="ASin(";
                else
                    lastToken="Sin(";
                break;
            case 17: // ACOS
                if(is2ndFActive)
                    lastToken="ACos(";
                else
                    lastToken="Cos(";
                break;
            case 18: // ATAN
                if(is2ndFActive)
                    lastToken="ATan(";
                else
                    lastToken="Tan(";
                break;
            case 19: // LOG10
                lastToken="Log10(";
                break;
            case 20: // NUM 4
                lastToken="4";
                break;
            case 21: // NUM 5
                lastToken="5";
                break;
            case 22: // NUM 6
                lastToken="6";
                break;
            case 23: // MINUS
                lastToken="-";
                break;
            case 24: // ASINH
                if(is2ndFActive)
                    lastToken="ASinh(";
                else
                    lastToken="Sinh(";
                break;
            case 25: // ACOSH
                if(is2ndFActive)
                    lastToken="ACosh(";
                else
                    lastToken="Cosh(";
                break;
            case 26: // ATANH
                if(is2ndFActive)
                    lastToken="ATanh(";
                else
                    lastToken="Tanh(";
                break;
            case 27: // LN
                lastToken="Log(";
                break;
            case 28: // NUM 1
                lastToken="1";
                break;
            case 29: // NUM 2
                lastToken="2";
                break;
            case 30: // NUM 3
                lastToken="3";
                break;
            case 31: // ADD
                lastToken="+";
                break;
            case 32: // LPAREN
                lastToken="(";
                break;
            case 33: // RPAREN
                lastToken=")";
                break;
            case 34: // Pi
                lastToken=String.valueOf(Math.PI);
                break;
            case 35: // g
                lastToken=",";
                break;
            case 36: // DEL
                tokenArray.pop();
                lastToken = "";
                break;
            case 37: // 0
                lastToken = "0";
                break;
            case 38: // .
                lastToken = ".";
                break;
            case 39: // =
                lastToken = "=";
                evaluateExpression();
                break;
        }

        //Once the equal button is pressed create the given expression into a text Expression
        if(!lastToken.equals("=")) {
            if (!lastToken.equals(""))
                tokenArray.push(lastToken);
            createTextExpression();
        }
    }

    //Method to create text expression
    public void createTextExpression(){
        //initializing text expression
        textExpression = "";
        //looping through the token array
        for(int i=0; i<tokenArray.size();i++)
            //setting each text expression as a text expression
            textExpression += tokenArray.get(i);

        //setting the text expression within a jtaResult
        jtaResult.setText(textExpression);
    }

    //adding expression from token array memTokenArray stack. Allows for the expression to be memorised and stored to be used in further evaluations
    public void memorize(){
        memTokenArray.clear();

        for(int i=tokenArray.size()-1; i>=0;i--)
            memTokenArray.push(tokenArray.get(i));
    }
    //Recalling the expression that has initially been entered
    public void memRecall(){
        tokenArray.clear();
        textExpression="";

        for(int i=memTokenArray.size()-1; i>=0;i--) {
            tokenArray.push(memTokenArray.get(i));
            textExpression+=memTokenArray.get(i);
        }

        jtaResult.setText(textExpression);
    }
    //clearing the memTokenArray to clear the stored memory
    public void memClear(){
        memTokenArray.clear();
    }
    //Clear everything in calculator
    public void allClear(){
        memTokenArray.clear();
        tokenArray.clear();
        textExpression="";
        lastToken="";
        if(is2ndFActive) toggle2NdF();
        jtaResult.setText(textExpression);
    }
    //evaluating the result of the expression
    public void evaluateExpression(){
        try{
            //text expression is converted into RPN notation and stored within variable exp of type Expression
            Expression exp = new ExpressionBuilder(textExpression).build();
            double d = exp.evaluate();
            jtaResult.setText(String.valueOf(d));
            tokenArray.clear();
        }
        catch (IllegalArgumentException ex){
            jtaResult.setText(ex.getMessage());
        }
    }
}
