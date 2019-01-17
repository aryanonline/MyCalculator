/*
 * Copyright (C) 2012-2014 Andreas Halle
 *
 * This file is part of jcoolib
 *
 * jcoolib is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jcoolib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public license
 * along with jcoolib. If not, see <http://www.gnu.org/licenses/>.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class representing a visible Cartesian coordinate system.
 * The system contains (in)visible x- and y-axes that range from one double
 * precision number to another.
 * The system can contain objects such as lines, points and polygons.
 */
public class CCSystem extends JPanel {
    /*
     * In this class there are two coordinate systems:
     *
     * 1. A two-dimensional coordinate system for Java2D where x lies in the
     *    interval [0, window width] and y lies in the interval
     *    [0, window height] where the units of both x and y are pixels.
     *
     * 2. An emulated two-dimensional coordinate system where x and y can lie in
     *    any range definable by double precision numbers.
     *
     * Throughout this class, Point is used to represent a point in system 1
     * while Point2D is used to represent a point in system 2.
     *
     * The translate.*(.)-methods are used to translate between the two systems.
     */

    //region Class Variables
    /* Some visual options */
    private Paint axisXPaint;
    private Paint axisYPaint;
    private Paint gridXPaint;
    private Paint gridYPaint;
    private Paint unitXPaint;
    private Paint unitYPaint;

    private Stroke axisXStroke;
    private Stroke axisYStroke;
    private Stroke gridXStroke;
    private Stroke gridYStroke;
    private Stroke unitXStroke;
    private Stroke unitYStroke;

    private boolean niceGraphics;
    /* End of visual options */

    /* The number of grid lines between each unit line */
    private double gridRatio;

    /* Define the range of the visible xy-plane */
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    /* The length of the domain of x and y */
    private double distX;
    private double distY;

    /* The ratio between system 1 and system 2 */
    private double xscale;
    private double yscale;

    /* The distance between each unit line, in pixels */
    private int ulScale;

    /* The size of each unit line, in pixels */
    private int ulSize;

    /*
     * Round this exact value to a value (of the same magnitude) that can be
     * written with very few decimals (or lots of trailing zeroes.)
     *
     * vbu stands for "value between unit lines"
     */
    private BigDecimal vbuX;
    private BigDecimal vbuY;

    /* The origin of system 1 and system 2 */
    private Point2D.Double origin2d;
    private Point origin;

    private final MathContext prec = new MathContext(10);

    private Map<String, JDataSeries> dSource = new HashMap<>();

    //endregion

    public CCSystem(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        axisXPaint = Color.black;
        axisYPaint = Color.black;
        gridXPaint = Color.black;
        gridYPaint = Color.black;
        unitXPaint = Color.black;
        unitYPaint = Color.black;

        axisXStroke = new BasicStroke(1.3f);
        axisYStroke = new BasicStroke(1.3f);
        gridXStroke = new BasicStroke(0.1f);
        gridYStroke = new BasicStroke(0.1f);
        unitXStroke = new BasicStroke(1f);
        unitYStroke = new BasicStroke(1f);

        gridRatio = 5;
        niceGraphics = true;
        ulScale = 65;
        ulSize = 4;

        /* Add some default listeners */
        MouseListener mouseListener = new mouseListener();
        MouseWheelListener mouseWheelListener = new mouseWheelListener();

        addMouseListener(mouseListener);
        addMouseMotionListener((MouseMotionListener) mouseListener);
        addMouseWheelListener(mouseWheelListener);
    }

    /* Move the visible area relevant to the current position. */
    private void drag(double moveX, double moveY) {
        minX += moveX;
        maxX += moveX;
        minY += moveY;
        maxY += moveY;
    }

    /* Draw the axes and unit lines in the best looking way possible for the given x- and y-ranges. */
    private void drawAxes(Graphics2D g2d) {
        //X-Axis
        g2d.setPaint(axisXPaint);
        g2d.setStroke(axisXStroke);
        g2d.drawLine(origin.x, 0, origin.x, getHeight());

        //unit X-Axis
        g2d.setPaint(unitXPaint);
        g2d.setStroke(unitXStroke);
        drawXUnitLines(g2d);

        //Y_Axis
        g2d.setPaint(axisYPaint);
        g2d.setStroke(axisYStroke);
        g2d.drawLine(0, origin.y, getWidth(), origin.y);

        //Unit Y-Axis
        g2d.setPaint(unitYPaint);
        g2d.setStroke(unitYStroke);
        drawYUnitLines(g2d);
    }

    /* Draw a grid for the coordinate system. */
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(0.1f));

        drawXGridLines(g2d);
        drawYGridLines(g2d);
    }

    /* Draw one vertical grid line. */
    private void drawXGridLine(Graphics2D g2d, double val) {
        int x = translateX(val);
        int y1 = translateY(minY);
        int y2 = translateY(maxY);
        g2d.drawLine(x, y1, x, y2);
    }

    /* Draw vertical grid lines. */
    private void drawXGridLines(Graphics2D g2d) {
        drawXGridLines(g2d, gridRatio, gridXStroke, gridXPaint);
    }

    /**
     * Draw vertical grid lines a given amount of times between each unit line.
     * Use the given stroke and paint to draw the grid lines.
     */
    private void drawXGridLines(Graphics2D g2d, double ratio, Stroke stroke, Paint paint) {
        double vbu = this.vbuX.doubleValue() / ratio;

        int idx = (int) Math.ceil(minX / vbu);
        int end = (int) Math.floor(maxX / vbu);

        g2d.setStroke(stroke);
        g2d.setPaint(paint);
        for (int i = idx; i <= end; i++) drawXGridLine(g2d, i*vbu);
    }

    /* Draw one horizontal grid line. */
    private void drawYGridLine(Graphics2D g2d, double val) {
        int y = translateY(val);
        int x1 = translateX(minX);
        int x2 = translateX(maxX);
        g2d.drawLine(x1, y, x2, y);
    }

    /* Draw horizontal grid lines. */
    private void drawYGridLines(Graphics2D g2d) {
        drawYGridLines(g2d, gridRatio, gridYStroke, gridYPaint);
    }

    /**
     * Draw horizontal grid lines a given amount of times between each unit line
     * Use the given stroke and paint to draw the grid lines.
     */
    private void drawYGridLines(Graphics2D g2d, double ratio, Stroke stroke, Paint paint) {
        double vbu = this.vbuY.doubleValue() / ratio;

        int idx = (int) Math.ceil(minY / vbu);
        int end = (int) Math.floor(maxY / vbu);

        g2d.setStroke(stroke);
        g2d.setPaint(paint);
        for (int i = idx; i <= end; i++) drawYGridLine(g2d, i*vbu);
    }

    public void addSeries(JDataSeries ... series){
        for(JDataSeries sr : series) {
            dSource.put(sr.getSeriesName(), sr);
        }
    }

    public void removeSeries(String series){
        dSource.remove(series);
    }

    public void clearAll(){
        dSource.clear();
        repaint();
    }

    public int getDataSourceSize(){
        return dSource.size();
    }

    public void drawPoints(Graphics2D g2d){
        g2d.setStroke(new BasicStroke(1f));
        for(JDataSeries ds: dSource.values()){
            g2d.setPaint(ds.getSeriesColor());
            List<JPoint> pts = new ArrayList(ds.getDataset());
            for (int i=0; i<pts.size()-1; i++){
                int x1 = translateX(pts.get(i).getX());
                int y1 = translateY(pts.get(i).getY());
                int x2 = translateX(pts.get(i+1).getX());
                int y2 = translateY(pts.get(i+1).getY());
                Shape l = new Line2D.Double(x1, y1, x2, y2);
                g2d.draw(l);
            }
        }
    }

    /* Draw a single unit line on the x-axis at a given value. */
    private void drawXUnitLine(Graphics2D g2d, BigDecimal val) {
        /* Don't draw anything at the origin. */
        if (val.doubleValue() == 0.0) return;

        /* val is "small" if -10^7 < val < 10^7. */
        BigDecimal big = BigDecimal.valueOf(10000000);
        boolean small = val.compareTo(big) < 0
                && val.compareTo(big.negate()) > 0;

        /*
         * When val is not "small", BigDecimal's toString does not use
         * scientific notation, so Double's toString is used instead.
         */
        String strval;
        if (small) strval = val.toString();
        else strval = Double.toString(val.doubleValue());

        Point2D.Double p2d = new Point2D.Double(val.doubleValue(), origin2d.y);
        Point p = translate(p2d);

        int strValPixels = 7 * strval.length();
        int offset = (minY >= -translateY(40)) ? -10 : 20;

        g2d.drawLine(p.x, p.y-ulSize, p.x, p.y+ulSize);
        g2d.drawString(strval, p.x - strValPixels/2, p.y + offset);
    }

    /* Draw all the unit lines on the x-axis. */
    private void drawXUnitLines(Graphics2D g2d) {
        /*
         * The value at each unit line will now be defined as i * vbuX. We need
         * to find the value of i such that i * vbuX is the value at the first
         * visible unit line.
         */
        int idx = (int) Math.ceil(minX / vbuX.doubleValue());

        /* Also find the value of the last visible unit line. */
        int end = (int) Math.floor(maxX / vbuX.doubleValue());

        for (int i = idx; i <= end; i++) drawXUnitLine(g2d,
                BigDecimal.valueOf(i).multiply(vbuX));
    }

    /* Draw a single unit line on the y-axis at a given value. */
    private void drawYUnitLine(Graphics2D g2d, BigDecimal val) {
        if (val.doubleValue() == 0.0) return;

        BigDecimal big = BigDecimal.valueOf(10000000);
        boolean small = val.compareTo(big) < 0
                && val.compareTo(big.negate()) > 0;

        String strval;
        if (small) strval = val.toString();
        else strval = Double.toString(val.doubleValue());

        Point2D.Double p2d = new Point2D.Double(origin2d.x, val.doubleValue());
        Point p = translate(p2d);

        int strValPixels = 7 * strval.length() + 7;
        int offset = (minX >= -translateX(strValPixels*2)) ? 5 : -strValPixels;

        g2d.drawLine(p.x-ulSize, p.y, p.x+ulSize, p.y);
        g2d.drawString(strval, p.x+offset, p.y+5);
    }

    /* Draw all the unit lines on the x-axis. */
    private void drawYUnitLines(Graphics2D g2d) {
        int idx = (int) Math.ceil(minY / vbuY.doubleValue());
        int end = (int) Math.floor(maxY / vbuY.doubleValue());


        for (int i = idx; i <= end; i++) drawYUnitLine(g2d,
                BigDecimal.valueOf(i).multiply(vbuY));
    }

    private BigDecimal findScale(double num) {
        int x = (int) Math.floor(Math.log10(num));

        BigDecimal scale;
        try {
            scale = BigDecimal.TEN.pow(x, prec);
        } catch (ArithmeticException e) {
            scale = BigDecimal.valueOf(Double.MAX_VALUE);
        }

        /* Don't need more than double precision here */
        double quot = num / scale.doubleValue();
        if (quot > 5.0) return scale.multiply(BigDecimal.TEN);
        if (quot > 2.0) return scale.multiply(BigDecimal.valueOf(5));
        if (quot > 1.0) return scale.multiply(BigDecimal.valueOf(2));
        else return scale;
    }

    private RenderingHints getNiceGraphics() {
        RenderingHints rh = new RenderingHints(null);
        rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        rh.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        rh.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        rh.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return rh;
    }

    /* Override JPanel Paint */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        updatePosition();

        if (niceGraphics) g2d.addRenderingHints(getNiceGraphics());

        drawPoints(g2d);

        drawGrid(g2d);
        drawAxes(g2d);
    }

    /* Translate a given point from System 2 to System 1. */
    private Point translate(Point2D p2d) { return translate(p2d.getX(), p2d.getY()); }

    /* Translate the point (x, y) from System 2 to System 1. */
    private Point translate(double x, double y) { return new Point(translateX(x), translateY(y)); }

    /* Translate a single x-coordinate from System 2 to System 1. */
    private int translateX(double x) {
        return (int) Math.round((x - minX) / xscale);
    }

    private double translateX(int x) {
        return x * xscale + minX;
    }

    private double translateY(int y) {
        return y * yscale + minY;
    }

    /*
     * Translate a single y-coordinate from System 2 to System 1.
     * Subtract from getHeight() since increasing y goes
     * south in System 1 but north in System 2.
    */
    private int translateY(double y) {
        return getHeight() - (int) Math.round((y - minY) / yscale);
    }

    private void updatePosition() {
        distX = maxX - minX;
        distY = maxY - minY;

        xscale = distX / getWidth();
        yscale = distY / getHeight();

        /* Total number of units on the axis */
        double unitsX = getWidth() / ulScale;
        double unitsY = getHeight() / ulScale;

        /* Exact value between each unit line */
        double udistX = distX / unitsX;
        double udistY = distY / unitsY;

        vbuX = findScale(udistX);
        vbuY = findScale(udistY);

        /* Find origin */
        double ox = 0;
        double oy = 0;

        /*
         * Place origin along the edges of the screen if
         * (0, 0) is not in the visible area.
         */
        if (minX >= 0) ox = minX;
        else if (maxX <= 0) ox = maxX;

        if (minY >= 0) oy = minY;
        else if (maxY <= 0) oy = maxY;

        origin2d = new Point2D.Double(ox, oy);
        origin = translate(origin2d);
    }

    public void zoom(double zoomX, double zoomY){
        minX -= zoomX;
        maxX += zoomX;
        minY -= zoomY;
        maxY += zoomY;
    }



    class mouseWheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int units = e.getUnitsToScroll();

            double zoomx = distX / 100.0 * units;
            double zoomy = distY / 100.0 * units;

            zoom(zoomx, zoomy);

            repaint();
        }
    }

    class mouseListener implements MouseListener, MouseMotionListener {
        private int lastX;
        private int lastY;

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            int dx = lastX - x;
            int dy = lastY - y;

            double moveX = distX / getWidth() * dx;
            double moveY = distY / getHeight() * dy;

            drag(moveX, -moveY);

            repaint();

            lastX = x;
            lastY = y;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }
    }
}