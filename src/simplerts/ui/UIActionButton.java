/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import simplerts.utils.Utilities;

/**
 *
 * @author Markus
 */
public class UIActionButton extends UIAction{

    public UIActionButton(BufferedImage icon, ClickListener clicker, String title, char hotkey) {
        super(icon, clicker, hotkey);
        this.title = title;
    }
    
    @Override
    protected void renderToolTip(Graphics g) {
            g.setColor(new Color(25, 25, 25, 245));
            g.fillRect((int)x, (int)y - 25, 50, 20);
            g.setColor(Color.black);
            g.drawRect((int)x, (int)y - 25, 50, 20);
            g.setColor(Color.WHITE);
            g.setFont(GUI.SMALL);
            Utilities.drawWithShadow(g, title, (int)x + 25 - g.getFontMetrics(GUI.SMALL).stringWidth(title)/2, (int)y - 25 + g.getFontMetrics(GUI.SMALL).getHeight());
    }
}
