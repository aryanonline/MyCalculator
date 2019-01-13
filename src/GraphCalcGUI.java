import expression.Expression;
import expression.ExpressionBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/*
Consult about this class with dad
 */
public class GraphCalcGUI extends JFrame {
    private HashSet<String> workingGraphList = new HashSet<String>();
    private HashSet<String> plottedGraphList = new HashSet<String>();

    private JPanel jPnlSetPlotter = new JPanel(new BorderLayout());
    private ChartPanel jPnlChartPanel;
    JFreeChart chart;

    private JButton jBtnZoomIn = new JButton("");
    private JButton jBtnZoomOut = new JButton("");
    private JButton jBtnSave = new JButton("");
    private JButton jBtnPlot = new JButton("");
    private JButton jBtnDelete = new JButton("X");
    private JButton jBtnClear = new JButton("");
    private JButton jBtnIntersect = new JButton("");


    private JComboBox jCbFunctionTypes = new JComboBox();
    private JComboBox jCbSavedFunctions = new JComboBox();
    private JTextField jTxtA = new JTextField("");
    private JTextField jTxtB = new JTextField("");
    private JTextField jTxtC = new JTextField("");
    private JTextField jTxtLowerBound = new JTextField("");
    private JTextField jTxtUpperBound = new JTextField("");
    private JTextField jTxtPoints = new JTextField("");
    private final List<XYSeries> equations = new ArrayList<>();
    private final XYSeriesCollection chartData = new XYSeriesCollection();

    public GraphCalcGUI(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        setSize(800, 500);
        this.setResizable(false);

        readSavedGraphs();
        setUI();
        getContentPane().add(jPnlChartPanel, BorderLayout.CENTER);
        getContentPane().add(jPnlSetPlotter, BorderLayout.WEST);

        jCbFunctionTypes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //Only interested in selection change event
                if(e.getStateChange()==ItemEvent.SELECTED) {
                    clearSettings();
                    loadSavedList(jCbFunctionTypes.getSelectedIndex());
                    jCbSavedFunctions.setSelectedIndex(0);
                }
            }
        });

        jCbSavedFunctions.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //Only interested in selection change event
                if(e.getStateChange()==ItemEvent.SELECTED) {
                    if(jCbSavedFunctions.getSelectedIndex()!=0) {
                       loadSavedItem(jCbSavedFunctions.getItemAt(jCbSavedFunctions.getSelectedIndex()).toString());
                    }
                }
                else{
                    clearSettings();
                }
            }
        });

        jBtnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isValid = true;
                if(!(CalcUtils.isNumeric(jTxtA.getText())
                        && CalcUtils.isNumeric(jTxtB.getText())
                        && CalcUtils.isNumeric(jTxtB.getText())
                        && CalcUtils.isNumeric(jTxtLowerBound.getText())
                        && CalcUtils.isNumeric(jTxtUpperBound.getText())
                        && CalcUtils.isNumeric(jTxtPoints.getText())
                )) isValid=false;

                if(!isValid) {
                    showErrorMessage("All parameter values should be numeric.");
                    return;
                }

                saveNewGraph();
            }
        });

        //Plot graph Button Listener
        jBtnPlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isValid = true;
                if(!(CalcUtils.isNumeric(jTxtA.getText())
                        && CalcUtils.isNumeric(jTxtB.getText())
                        && CalcUtils.isNumeric(jTxtB.getText())
                        && CalcUtils.isNumeric(jTxtLowerBound.getText())
                        && CalcUtils.isNumeric(jTxtUpperBound.getText())
                        && CalcUtils.isNumeric(jTxtPoints.getText())
                )) isValid=false;

                if(!isValid) {
                    showErrorMessage("All parameter values should be numeric.");
                    return;
                }

                plotGraph(jCbFunctionTypes.getSelectedIndex());
            }
        });

        jBtnZoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomChartAxis(jPnlChartPanel, true);
            }
        });

        jBtnZoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomChartAxis(jPnlChartPanel, false);
            }
        });

        jBtnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSettings();
                plottedGraphList.clear();
                chartData.removeAllSeries();
                equations.clear();
                revalidate();
                jCbFunctionTypes.setSelectedIndex(0);
            }
        });

        jBtnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(jCbSavedFunctions.getSelectedIndex()>0){
                    String grp = jCbSavedFunctions.getItemAt(jCbSavedFunctions.getSelectedIndex()).toString();
                    boolean isRemoved = workingGraphList.remove(grp);
                    jCbSavedFunctions.setSelectedIndex(0);
                    saveGraph();
                    loadSavedList(jCbFunctionTypes.getSelectedIndex());
                    jCbSavedFunctions.setSelectedIndex(0);
                }
            }
        });

        //hack to read saved items
        jCbFunctionTypes.setSelectedIndex(1);
        jCbFunctionTypes.setSelectedIndex(0);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void plotGraph(int index) {
        double valA = Double.parseDouble(jTxtA.getText());
        double valB = Double.parseDouble(jTxtB.getText());
        double valC = Double.parseDouble(jTxtC.getText());

        StringBuilder stringExpression = new StringBuilder();
        List<Double> list;

        switch (index){
            case 1: //Quadratic - AX^2+BX+C
                stringExpression.append("(").append(valA).append("*(X^2))")
                        .append("+ (").append(valB).append("*X)")
                        .append("+ ").append(valC);
                break;
            case 2: //POWER - AB^X + C
                stringExpression.append("(").append(valA).append("*").append(valB).append("^X)")
                        .append("+").append(valC);
                break;
            case 3: //EXP - AX^B + C
                stringExpression.append("(").append(valA).append("*X^").append(valB).append(")")
                        .append("+").append(valC);
                break;
            case 4: //EXP-S - AX^B + C
                stringExpression.append(valA).append("*Sin(").append(valB).append("*X+").append(valC).append(")");
                break;
            case 5: //EXP-C - AX^B + C
                stringExpression.append(valA).append("*Cos(").append(valB).append("*X+").append(valC).append(")");
                break;
            case 6: //EXP-T - AX^B + C
                stringExpression.append(valA).append("*Tan(").append(valB).append("*X+").append(valC).append(")");
                break;
            default:
                stringExpression.append(valA).append("*X+").append(valB);
                break;
        }

        if(plottedGraphList.contains(stringExpression.toString()))
            return;

        Expression exp = new ExpressionBuilder(stringExpression.toString())
                .variable("X")
                .build();

        list = createDataset(exp);

        int i = 0;
        int j = equations.size();
        XYSeries eq = new XYSeries("E"+j);
        for (Double d : list) {
            eq.add(i++, d);
        }

        equations.add(eq);

        chartData.removeAllSeries();
        for(XYSeries s: equations){
            chartData.addSeries(s);
        }

        plottedGraphList.add(stringExpression.toString());

        revalidate();
    }

    private List<Double> createDataset(Expression exp) {
        //Upper domain of the equation
        double valLower = Double.parseDouble(jTxtLowerBound.getText());
        //lower domain of the equation
        double valUpper = Double.parseDouble(jTxtUpperBound.getText());
        //The number of points that the graph will consist of
        double valPoints = Double.parseDouble(jTxtPoints.getText());

        //declaration of array list to dtore the evaluated points for each X-Value on the graph
        List<Double> list = new ArrayList<Double>();

        //calculating the the seperation of between the points
        double spread = valUpper - valLower;
        double increments = spread/valPoints;

        for(double i=valLower; i<=valUpper; i += increments){
            //invoking the evaluate method from expression class to evaluate for each X-value
            double result = exp.setVariable("X", i).evaluate();
            //Used to convert any value to a two decimal format
            DecimalFormat formatter = new DecimalFormat("####.##");
            //Converting the result into two decimals and storing it within the variable fmtNum
            double fmtNum = Double.parseDouble(formatter.format(result));
            //adding converted result within the list
            list.add(fmtNum);
        }

        return list;
    }

    private void setUI(){
        //Setting components for the GraphGUI
        JLabel jLblFxType = new JLabel("Select F(x) Type: ");
        JLabel jLblFx = new JLabel("Select from saved functions: ");
        JLabel jLblA = new JLabel("A");
        JLabel jLblB = new JLabel("B");
        JLabel jLblC = new JLabel("C");
        JLabel jLblXRange = new JLabel("Domain Range: ");
        JLabel jLblBounds = new JLabel("<=  X  <=");
        JLabel jLblPoints = new JLabel("Number of Points: ");


        setLabelDisplay(jLblFxType, SwingConstants.LEFT);
        setLabelDisplay(jLblFx, SwingConstants.LEFT);
        setLabelDisplay(jLblA, SwingConstants.CENTER);
        setLabelDisplay(jLblB, SwingConstants.CENTER);
        setLabelDisplay(jLblC, SwingConstants.CENTER);
        setLabelDisplay(jLblXRange, SwingConstants.LEFT);
        setLabelDisplay(jLblBounds, SwingConstants.CENTER);
        setLabelDisplay(jLblPoints, SwingConstants.LEFT);


        //Chart Panel Settings
        jPnlChartPanel = createChartPanel();
        jPnlChartPanel.setOpaque(true);
        jPnlChartPanel.setPreferredSize(new Dimension(550, 500));


        //Plotter Panel Settings
        jPnlSetPlotter.setOpaque(true);
        jPnlSetPlotter.setBackground(new Color(51,51,51));
        jPnlSetPlotter.setPreferredSize(new Dimension(250, 500));
        jPnlSetPlotter.setBorder(BorderFactory.createEmptyBorder(0, 10 ,0, 10));

        //Plotter Center Panel
        JPanel p1 = new JPanel(new GridBagLayout());
        p1.setOpaque(true);
        p1.setBackground(new Color(51,51,51));

        //Plotter South Panel
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setOpaque(true);
        p2.setBackground(new Color(51,51,51));

        //Button Icons
        ImageIcon ic1 = CalcUtils.getTransparentIcon("plot-2.png", 24, 24);
        jBtnPlot.setIcon(ic1);

        ImageIcon ic2 = CalcUtils.getTransparentIcon("zoom-in.png", 24, 24);
        jBtnZoomIn.setIcon(ic2);

        ImageIcon ic3 = CalcUtils.getTransparentIcon("zoom-out.png", 24, 24);
        jBtnZoomOut.setIcon(ic3);

        ImageIcon ic5 = CalcUtils.getTransparentIcon("save-icon-2.png", 24, 24);
        jBtnSave.setIcon(ic5);

        ImageIcon ic6 = CalcUtils.getTransparentIcon("intersection.png", 24, 24);
        jBtnIntersect.setIcon(ic6);

        ImageIcon ic7 = CalcUtils.getTransparentIcon("eraser-2.png", 24, 24);
        jBtnClear.setIcon(ic7);

        //Init Function Types
        jCbFunctionTypes.addItem("Linear: f(x): AX + B");
        jCbFunctionTypes.addItem("Quadratic: f(x): AX^2 + BX + C");
        jCbFunctionTypes.addItem("Power: f(x): AB^X + C");
        jCbFunctionTypes.addItem("Exponential: f(x): AB^X + C");
        jCbFunctionTypes.addItem("Sinusoidal: f(x): A*SIN(BX + C)");
        jCbFunctionTypes.addItem("Cosine: f(x): A*COS(BX + C)");
        jCbFunctionTypes.addItem("Tangent: f(x): A*TAN(BX + C)");
        jCbFunctionTypes.setSelectedIndex(0);

        //Add Note to Saved Function Combo box
        jCbSavedFunctions.addItem("Available saved functions...");

        //Function Types
        CalcUtils.addToGridBagLayout(p1, jLblFxType, 0, 0,3,1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p1, jCbFunctionTypes, 0, 1, 3, 1, GridBagConstraints.HORIZONTAL);

        //Spacer
        CalcUtils.addToGridBagLayout(p1, jLblFx, 0, 2, 3, 1, GridBagConstraints.HORIZONTAL);

        //Functions
        CalcUtils.addToGridBagLayout(p1, jCbSavedFunctions, 0,3,2,1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p1, jBtnDelete, 2, 3,1,1, GridBagConstraints.HORIZONTAL);

        // A-B-C
        CalcUtils.addToGridBagLayout(p1, jLblA, 0, 4, 1, 1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p1, jLblB, 1, 4, 1, 1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p1, jLblC, 2, 4, 1, 1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p1, jTxtA, 0, 5, 1, 1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p1, jTxtB, 1, 5, 1, 1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p1, jTxtC, 2, 5, 1, 1, GridBagConstraints.HORIZONTAL);

        //Range
        CalcUtils.addToGridBagLayout(p1, jLblXRange, 0, 6,3,1, GridBagConstraints.BOTH);
        CalcUtils.addToGridBagLayout(p1, jTxtLowerBound, 0, 7, 1,1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p1, jLblBounds, 1,7,1,1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p1, jTxtUpperBound, 2, 7, 1,1, GridBagConstraints.HORIZONTAL);

        //#Points
        CalcUtils.addToGridBagLayout(p1, jLblPoints, 0, 8,2,1, GridBagConstraints.BOTH);
        CalcUtils.addToGridBagLayout(p1, jTxtPoints, 2, 8, 1,1, GridBagConstraints.HORIZONTAL);

        //Buttons
        CalcUtils.addToGridBagLayout(p2, jBtnPlot, 0, 0,1,1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p2, jBtnSave,1,0,1,1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p2, jBtnClear, 2, 0, 1,1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p2, jBtnZoomIn,3,0,1,1, GridBagConstraints.HORIZONTAL);
        CalcUtils.addToGridBagLayout(p2, jBtnZoomOut,4,0,1,1, GridBagConstraints.HORIZONTAL);
        //CalcUtils.addToGridBagLayout(p2, jBtnIntersect,5,0,1,1, GridBagConstraints.HORIZONTAL);

        jPnlSetPlotter.add(p1, BorderLayout.CENTER);
        jPnlSetPlotter.add(p2, BorderLayout.SOUTH);
    }

    private ChartPanel createChartPanel(){
        //Chart Panel Settings
        chart = ChartFactory.createXYLineChart(
                "Equation Chart", "X", "F(x)", chartData);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setRangePannable(true);
        xyPlot.setDomainPannable(true);
        return new ChartPanel(chart);
    }

    private void clearSettings(){
        jTxtA.setText("");
        jTxtB.setText("");
        jTxtC.setText("");
        jTxtLowerBound.setText("");
        jTxtUpperBound.setText("");
        jTxtPoints.setText("");
    }

    //zooming in within graph
    private void zoomChartAxis(ChartPanel cp, boolean increase){
        int width = cp.getMaximumDrawWidth() - cp.getMinimumDrawWidth();
        int height = cp.getMaximumDrawHeight() - cp.getMinimumDrawWidth();
        if(increase){
            cp.zoomInBoth(width/5, height/5);
        }else{
            cp.zoomOutBoth(width/5, height/5);
        }

    }

    //load saved equations into combo box that match function type
    private void loadSavedList(int idx){
        List<String> savedTypeList = new ArrayList<String>();
        String letter;
        switch (idx){
            case 1: letter="Q"; break;
            case 2: letter="P"; break;
            case 3: letter="E"; break;
            case 4: letter="S"; break;
            case 5: letter="C"; break;
            case 6: letter="T"; break;
            default: letter="L";
        }

        for(String s : workingGraphList){
            if(s.substring(0, 1).toUpperCase().equals(letter)){
                savedTypeList.add(s);
            }
        }

        jCbSavedFunctions.removeAllItems();
        jCbSavedFunctions.addItem("Available saved functions...");
        for(String s: savedTypeList){
            jCbSavedFunctions.addItem(s);
        }
    }

    //load saved ite to plot
    private void loadSavedItem(String item){
        String[] itemParts = item.split(";");

        jTxtA.setText(itemParts[1]);
        jTxtB.setText(itemParts[2]);
        jTxtC.setText(itemParts[3]);
        jTxtLowerBound.setText(itemParts[4]);
        jTxtUpperBound.setText(itemParts[5]);
        jTxtPoints.setText(itemParts[6]);
    }

    //read data from the user cardio file
    private void readSavedGraphs(){
        ArrayList<String[]> itemRows = new ArrayList<>();

        String[] row = new String[6];
        try {
            File file = new File("my-graphs.txt");
            if(!file.exists()) return;
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\r\n");
            while (sc.hasNextLine()) {
                String rec = sc.next();
                workingGraphList.add(rec);
            }

            sc.close();// closing the scanner stream
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //read data from the user cardio file
    private void saveNewGraph(){
        StringBuilder sb = new StringBuilder();

        sb.append(jCbFunctionTypes.getSelectedItem().toString().substring(0,1).toUpperCase());
        sb.append(";").append(jTxtA.getText()).append(";").append(jTxtB.getText()).append(";").append(jTxtC.getText());
        sb.append(";").append(jTxtLowerBound.getText()).append(";").append(jTxtUpperBound.getText());
        sb.append(";").append(jTxtPoints.getText());

        workingGraphList.add(sb.toString());
        saveGraph();
    }

    //read data from the user cardio file
    private void saveGraph(){
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for(String s : workingGraphList){
            if(i==0)
                sb.append(s);
            else
                sb.append("\r\n").append(s);

            i++;
        }

        //now write all content at once into the file
        try{
            FileWriter fw = new FileWriter("my-graphs.txt", false);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //set Label Fonts and Color
    private void setLabelDisplay(JLabel lbl, int align){
        lbl.setOpaque(true);
        lbl.setBackground(new Color(51,51,51));
        lbl.setForeground(Color.WHITE);
        lbl.setHorizontalAlignment(align);
        lbl.setFont(new Font("Tahoma", Font.PLAIN, 16));
    }

    //display error message
    private void showErrorMessage(String message){
        JOptionPane.showMessageDialog(this, message, "Incorrect Age", JOptionPane.ERROR_MESSAGE);
    }
}
