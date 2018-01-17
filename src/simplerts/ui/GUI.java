/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import simplerts.display.Assets;
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
    private int height = 225;
    private Handler handler;
    private int mapOffsetX = 35, mapOffsetY = 31;
    
    public GUI(Map map, Handler handler)
    {
        this.map = map;
        this.handler = handler;
        minimap = map.getMiniMap(175, 175);
    }
    
    public void onClick(int posX, int posY)
    {
        if(posX > mapOffsetX && posX < mapOffsetX + minimap.getMiniMap().getWidth() && posY > mapOffsetY && posY < mapOffsetY + minimap.getMiniMap().getHeight())
        {
            int x1 = (int)((posX - mapOffsetX) / minimap.getPixelRatio()) - Game.WIDTH / 2;
            int y1 = (int)((posY - mapOffsetY) / minimap.getPixelRatio()) - Game.HEIGHT / 2;
            handler.getCamera().setOffset(x1, y1);
        }
    }
    
    public void render(Graphics g)
    {
        g.drawImage(Assets.GUI, 0, 0, null);
        g.drawImage(minimap.getMiniMap(), 35, 31, null);
        g.setColor(Color.WHITE);
        g.drawRect((int)(handler.getCamera().getOffsetX() * minimap.getPixelRatio()) + mapOffsetX, (int)(handler.getCamera().getOffsetY() * minimap.getPixelRatio()) + mapOffsetY, (int)(Game.WIDTH * minimap.getPixelRatio()), (int)(Game.HEIGHT * minimap.getPixelRatio()));
    }
    
}
