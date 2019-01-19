import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
/*
Name: Aryan Singh
Date: 18 January 2019
Description: This class holds a series data from one equation that has all of the information of a graph its coordinates,domain,etc
Features: None
MajorSkills: Use of selection structures to error check the domain and range of the graph. Use of List to store points.
https://www.developer.com/net/vb/article.php/626081/Java-2D-Graphics-The-Point2D-Class.htm#_Methods_of_the
 */

public class JDataSeries {
    //declaration of variables
    private String seriesName;
    private Color seriesColor;
    //List to store in all key points for graph
    private List<Point2D.Double> dataset;
    private double lowerBoundX;
    private double upperBoundX;
    private double lowerBoundY;
    private double upperBoundY;

    public JDataSeries(String series, Color gColor){
        this.setSeriesName(series);
        this.seriesColor=gColor;
        if(getDataset() == null)
            dataset = new ArrayList<>();
    }

    //adding points to the datseroes
    public void add(double x, double y){
        setBounds(x, y);
        getDataset().add(new Point2D.Double(x, y));
    }

    //getters and setters
    public List<Point2D.Double> getDataset() {
        return dataset;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public Color getSeriesColor() {
        return seriesColor;
    }

    public double getLowerBoundX() {
        return lowerBoundX;
    }

    public void setLowerBoundX(double lowerBoundX) {
        this.lowerBoundX = lowerBoundX;
    }

    public double getUpperBoundX() {
        return upperBoundX;
    }

    public void setUpperBoundX(double upperBoundX) {
        this.upperBoundX = upperBoundX;
    }

    public double getLowerBoundY() {
        return lowerBoundY;
    }

    public void setLowerBoundY(double lowerBoundY) {
        this.lowerBoundY = lowerBoundY;
    }

    public double getUpperBoundY() {
        return upperBoundY;
    }

    public void setUpperBoundY(double upperBoundY) {
        this.upperBoundY = upperBoundY;
    }

    private void setBounds(double x, double y){
        if(getLowerBoundX() > x) setLowerBoundX(x);
        if(getLowerBoundY() > y) setLowerBoundY(y);
        if(getUpperBoundX() < x) setUpperBoundX(x);
        if(getUpperBoundY() < y) setUpperBoundY(y);
    }
}
