import java.util.ArrayList;
import java.util.List;

public class Point implements Comparable<Point>{
    public int x;
    public int y;
    public double costTo;
    public int highCost;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.costTo = Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int compareTo(Point o) {
        return Double.compare(this.costTo, o.costTo);
    }

    @Override
    public boolean equals(Object o) {
        Point p = (Point) o;
        if (this.x == p.x && this.y == p.y)
            return true;
        return false;
    }

    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}
