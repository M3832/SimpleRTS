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
import simplerts.Handler;
import simplerts.Map;

/**
 *
 * @author Markus
 */
public class GUI {
    
    Map map;
    private MiniMap minimap;
    private int height = 200;
    private Handler handler;
    
    public GUI(Map map, Handler handler)
    {
        this.map = map;
        this.handler = handler;
        minimap = map.getMiniMap(200, 200);
    }
    
    public void render(Graphics g)
    {
        g.setColor(Color.black);
        g.fillRect(0, 0, Game.WIDTH, height);
        g.drawImage(minimap.getMiniMap(), 0, 0, null);
        g.setColor(Color.WHITE);
        g.drawRect((int)((handler.getCamera().getOffsetX() * (minimap.getSquareSize()/Game.CELLSIZE))), (int)(handler.getCamera().getOffsetY() * (minimap.getSquareSize()/Game.CELLSIZE)), (int)(Game.WIDTH * (minimap.getSquareSize()/Game.CELLSIZE)), (int)(Game.HEIGHT * (minimap.getSquareSize()/Game.CELLSIZE)));
    }
    
}
