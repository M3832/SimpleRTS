/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.display;

import java.awt.Graphics;
import javax.swing.JPanel;
import simplerts.Game;
import simplerts.Handler;

/**
 *
 * @author Markus
 */
public class GamePanel extends JPanel {
    
    private Handler handler;
    private int renderUpdates = 0;
    private long nextSecond = 0;
    
    public GamePanel()
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
            handler.game.controller.getCamera().render(g);

            if(System.currentTimeMillis() > nextSecond)
            {
                if(Game.DEBUG)
                    System.out.println("Renders/second: " + renderUpdates);
                renderUpdates = 0;
                nextSecond = System.currentTimeMillis() + 1000;
            }
            renderUpdates++;
            handler.game.controller.render(g);
            handler.game.controller.scrolling();
            handler.game.mm.render(g);
            g.dispose();            
        }
    }
    
}
