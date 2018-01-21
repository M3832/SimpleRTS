/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Markus
 */
public class Utils {
    
    public static void drawWithShadow(Graphics g, String s, int posX, int posY)
    {
        Color c = g.getColor();
        g.setColor(Color.black);
        g.drawString(s, posX +2, posY +2);
        g.setColor(c);
        g.drawString(s, posX, posY);
    }
}
