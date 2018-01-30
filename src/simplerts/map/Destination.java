/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.map;

/**
 *
 * @author Markus
 */
public class Destination {
    
    private int x, y;
    
    public Destination(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public Destination(Destination d)
    {
        this.x = d.getX();
        this.y = d.getY();
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public String toString()
    {
        return "Destination: " + x + ", " + y;
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public boolean isEmpty() {
        return x == -1 || y == -1;
    }
    
    
    
}