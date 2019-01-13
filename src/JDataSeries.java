import java.awt.*;
import java.util.*;

public class JDataSeries {
    private String seriesName;
    private Color seriesColor;
    private Set<JPoint> dataset;
    private double lowerBoundX;
    private double upperBoundX;
    private double lowerBoundY;
    private double upperBoundY;

    public JDataSeries(String series, Color gColor){
        this.setSeriesName(series);
        this.seriesColor=gColor;
        if(getDataset() == null)
            dataset = new HashSet<>();
    }

    public void add(double x, double y){
        setBounds(x, y);
        getDataset().add(new JPoint(x, y));
    }

    public void add(JPoint ... points){
        for(JPoint p: points) {
            setBounds(p.getX(), p.getY());
            getDataset().add(new JPoint(p.getX(), p.getY()));
        }
    }

    public Set<JPoint> getDataset() {
        return dataset;
    }

    public void setDataset(Set<JPoint> dataset) {
        this.dataset = dataset;
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
