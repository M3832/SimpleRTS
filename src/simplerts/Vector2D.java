/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

/**
 *
 * @author Markus
 */
public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void normalize()
    {
        setX(Math.sqrt(getX() * getX()));
        setY(Math.sqrt(getY() * getY()));
    }

    public void scaleBy(double scale)
    {
        setX(getX() * scale);
        setY(getY() * scale);
    }

    public void add(Vector2D v)
    {
        x += v.getX();
        y += v.getY();
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }
}
