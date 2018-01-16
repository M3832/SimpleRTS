/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.image.BufferedImage;

/**
 *
 * @author Markus
 */
public class Unit extends Entity {
    protected float moveX, moveY;
    protected BufferedImage sprite;
    
    public void setVelocity(float x, float y)
    {
        moveX = x;
        moveY = y;
    }
    
    public void move()
    {
        if(moveX != 0)
        {
            x += moveX;
        }
        
        if(moveY != 0)
        {
            y += moveY;
        }
    }
}
