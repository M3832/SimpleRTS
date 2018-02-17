/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.utils;

import java.awt.Color;
import java.awt.Graphics;
import simplerts.entities.Entity;
import simplerts.entities.Unit;
import simplerts.gfx.Animation;
import simplerts.map.Destination;

/**
 *
 * @author Markus
 */
public class Utilities {
    
    public static void drawWithShadow(Graphics g, String s, int posX, int posY)
    {
        Color c = g.getColor();
        g.setColor(Color.black);
        g.drawString(s, posX +1, posY +1);
        g.setColor(c);
        g.drawString(s, posX, posY);
    }
    
    public static boolean isAdjacent(Unit e1, Entity e2)
    {
        for(int x = e2.getGridX(); x < e2.getGridX() + e2.getGridWidth(); x++)
        {
            for(int y = e2.getGridY(); y < e2.getGridY() + e2.getGridWidth(); y++)
            {
                int deltaX = Math.abs(e1.getGridX() - x);
                int deltaY = Math.abs(e1.getGridY() - y);
                if(deltaX < 2 && deltaY < 2)
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isAdjacent(Destination d1, Destination d2)
    {
        int deltaX = Math.abs(d1.getX() - d2.getX());
        int deltaY = Math.abs(d1.getY() - d2.getY());
        if(deltaX < 2 && deltaY < 2)
        {
            return true;
        }
        return false;
    }
    
    public static Color getRandomColor()
    {
        return new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
    }
    
    public static int getMovementDirection(int x, int x1)
    {
        if(x - x1 < 0)
            return -1;
        if(x - x1 > 0)
            return 1;
        
        return 0;
    }
    
    public static int getFacingDirection(int x, int y) {
        if(x > 0)
            return Animation.EAST;
        if(x < 0)
            return Animation.WEST;
        if(y > 0)
            return Animation.SOUTH;
        if(y < 0)
            return Animation.NORTH;
        if(x > 0 && y > 0)
            return Animation.SOUTHEAST;
        if(x < 0 && y > 0)
            return Animation.SOUTHWEST;
        if(x < 0 && y < 0)
            return Animation.NORTHWEST;
        if(x > 0 && y < 0)
            return Animation.NORTHEAST;
        
        return Animation.SOUTH;
    }
}
