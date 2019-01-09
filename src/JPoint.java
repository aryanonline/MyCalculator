public class JPoint implements Comparable<JPoint>{
    private double _x;
    private double _y;

    public JPoint(JPoint p){

    }

    public JPoint(double x, double y){
        _x = x;
        _y = y;
    }


    public double getX() {
        return _x;
    }

    public void setX(double _x) {
        this._x = _x;
    }

    public double getY() {
        return _y;
    }

    public void setY(double _y) {
        this._y = _y;
    }

    @Override
    public int compareTo(JPoint o) {
        if(_x == o.getX() && _y == o.getY())
            return 1;
        else
            return -1;
    }
}
