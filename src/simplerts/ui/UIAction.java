/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import simplerts.Game;
import simplerts.utils.Utilities;

/**
 *
 * @author Markus
 */
public class UIAction extends UIObject {

    private ClickListener clicker;
    private BufferedImage icon;
    private boolean visible;
    private boolean action;
    private String title, goldCost, lumberCost;
    
    public UIAction(BufferedImage icon, ClickListener clicker)
    {
        super(0, 0, icon.getWidth(), icon.getHeight());
        this.icon = icon;
        this.clicker = clicker;
        visible = true;
        action = true;
    }
    
    public UIAction(float x, float y, BufferedImage icon, ClickListener clicker)
    {
        super(x, y, icon.getWidth(), icon.getHeight());
        this.icon = icon;
        this.clicker = clicker;
        visible = true;
        action = false;
    }
    
    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics g) {
        if(visible)
        {
            g.drawImage(icon, (int)x, (int)y, null);
            if(hovering && action)
            {
                renderToolTip(g);
                g.setColor(new Color(20, 20, 20, 175));
                g.fillRect((int)x, (int)y, width, height);
            }
        }
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

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the goldCost
     */
    public String getGoldCost() {
        return goldCost;
    }

    /**
     * @param goldCost the goldCost to set
     */
    public void setGoldCost(String goldCost) {
        this.goldCost = goldCost;
    }

    /**
     * @return the lumberCost
     */
    public String getLumberCost() {
        return lumberCost;
    }

    /**
     * @param lumberCost the lumberCost to set
     */
    public void setLumberCost(String lumberCost) {
        this.lumberCost = lumberCost;
    }

    private void renderToolTip(Graphics g) {
                g.setColor(new Color(25, 25, 25, 245));
                g.fillRect((int)x - 25, (int)y - 100, 100, 100);
                g.setColor(Color.black);
                g.drawRect((int)x - 25, (int)y - 100, 100, 100);
                g.setColor(Color.WHITE);
                g.setFont(GUI.SMALL);
                if(title != null)
                    Utilities.drawWithShadow(g, title, (int)x + 25 - g.getFontMetrics(GUI.SMALL).stringWidth(title)/2, (int)y - 100 + g.getFontMetrics(GUI.SMALL).getHeight());
                if(goldCost != null)
                    Utilities.drawWithShadow(g, "Gold: " + goldCost, (int)x - 20, (int)y - 100 + 20 + g.getFontMetrics(GUI.SMALL).getHeight());
                if(lumberCost != null)
                    Utilities.drawWithShadow(g, "Wood: " + lumberCost, (int)x - 20, (int)y - 100 + 35 + g.getFontMetrics(GUI.SMALL).getHeight());
    }
    
    
    
}
