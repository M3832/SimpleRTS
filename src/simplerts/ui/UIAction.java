/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Markus
 */
public class UIAction extends UIObject {

    private ClickListener clicker;
    private BufferedImage icon;
    
    public UIAction(float x, float y, BufferedImage icon, ClickListener clicker)
    {
        super(x, y, 55, 55);
        this.icon = icon;
        this.clicker = clicker;
    }
    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(icon, (int)x, (int)y, null);
    }

    @Override
    public void onClick() {
        clicker.onClick();
    }
    
}
