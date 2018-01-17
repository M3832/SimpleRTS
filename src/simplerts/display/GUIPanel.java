/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.display;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import simplerts.Handler;
import static simplerts.Game.HEIGHT;
import static simplerts.Game.WIDTH;

/**
 *
 * @author Markus
 */
public class GUIPanel extends JPanel {
    
    private Handler handler;
    
    public GUIPanel()
    {
        super();
    }
    
    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        if(handler != null)
        {
            handler.game.gui.render(g);           
        }
    }
    
}
