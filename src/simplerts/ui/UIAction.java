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
    private boolean visible;
    
    public UIAction(BufferedImage icon, ClickListener clicker)
    {
        super(0, 0, icon.getWidth(), icon.getHeight());
        this.icon = icon;
        this.clicker = clicker;
        visible = true;
    }
    
    public UIAction(float x, float y, BufferedImage icon, ClickListener clicker)
    {
        super(x, y, icon.getWidth(), icon.getHeight());
        this.icon = icon;
        this.clicker = clicker;
        visible = true;
    }
    
    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics g) {
        if(visible)
            g.drawImage(icon, (int)x, (int)y, null);
    }

    @Override
    public void onClick() {
        clicker.onClick();
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
}
