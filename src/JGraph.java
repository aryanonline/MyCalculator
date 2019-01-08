import javax.swing.*;
import java.util.*;

public class JGraph extends JPanel{
    private String graphTitle;
    private double[] xAxis;
    private double[] yAxis;

    private Map<String, JDataSeries> dSource = new HashMap<>();

    public JGraph(String grphTitle, List<JDataSeries> dataSource){
        graphTitle=grphTitle;
        for(JDataSeries s: dataSource){
            if(xAxis.length <= 0){
                calculateAxis()
            }
            dSource.put(s.getSeriesName(), s);
        }
    }

    public JGraph(String grphTitle, JDataSeries series){
        graphTitle=grphTitle;
        dSource.put(series.getSeriesName(), series);
    }

    public JGraph(String grphTitle){
        graphTitle=grphTitle;
    }

    public void addSeries(JDataSeries series){
        dSource.put(series.getSeriesName(), series);
    }

    public void removeSeries(String series){
        dSource.remove(series);
    }

    private Double[] calculateAxis(double lower, double upper, double tick){
        ArrayList<Double> result = new ArrayList<>();
        if(lower == upper){
            lower=lower-10;
            upper=upper+10;
        }

        double range = upper - lower;
        double tempStep = range / tick;
        double mag = Math.floor((Math.log10(tempStep)));
        double magPow = Math.pow(10, mag);
        double magMsd = (int) (tempStep/magPow + 0.5);
        double stepSize = magMsd*magPow;

        double lb = stepSize * Math.floor(lower/stepSize);
        double ub = stepSize * Math.ceil(upper/stepSize);

        double val = lb;
        while (true){
            result.add(val);
            val += stepSize;
            if(val>ub) break;
        }

        result.toArray();
    }
}
