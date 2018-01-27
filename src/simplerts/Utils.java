/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Color;
import java.awt.Graphics;
import simplerts.entities.Entity;
import simplerts.entities.Unit;

/**
 *
 * @author Markus
 */
public class Utils {
    
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
}
