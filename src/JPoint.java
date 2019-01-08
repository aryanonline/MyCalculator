public class JPoint {
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
}
