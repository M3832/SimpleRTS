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
public class Velocity {
    private double x;
    private double y;
    
    public Velocity(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void normalize()
    {
        x = Math.sqrt(x * x);
        y = Math.sqrt(y * y);
    }
}
