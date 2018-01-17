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
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        if(handler != null)
        {
            handler.camera.render(g);

            if(System.currentTimeMillis() > nextSecond)
            {
                System.out.println(renderUpdates);
                renderUpdates = 0;
                nextSecond = System.currentTimeMillis() + 1000;
            }
            renderUpdates++;
            handler.game.players.stream().forEach(player -> player.render(g));
            g.dispose();            
        }
    }
    
}
