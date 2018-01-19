/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import simplerts.Entity;
import simplerts.display.Assets;
import simplerts.Game;
import simplerts.Handler;
import simplerts.Map;
import simplerts.Unit;

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
    private ArrayList<Entity> entities;
    public static Font HEADER = new Font(Font.MONOSPACED, Font.BOLD, 34);
    public static Font BREAD = new Font(Font.MONOSPACED, Font.BOLD, 20);
    
    public GUI(Map map, Handler handler)
    {
        this.map = map;
        this.handler = handler;
        minimap = map.getMiniMap(175, 175);
        entities = new ArrayList<>();
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
    
    public void setSelectedEntities(ArrayList<Entity> elist)
    {
        entities = elist;
    }
    
    public void render(Graphics g)
    {
        g.drawImage(Assets.GUI, 0, 0, null);
        g.drawImage(minimap.getMinimapWithEntities(), 35, 31, null);
        g.setColor(Color.WHITE);
        g.drawRect((int)(handler.getCamera().getOffsetX() * minimap.getPixelRatio()) + mapOffsetX, (int)(handler.getCamera().getOffsetY() * minimap.getPixelRatio()) + mapOffsetY, (int)(Game.WIDTH * minimap.getPixelRatio()), (int)(Game.HEIGHT * minimap.getPixelRatio()));
        
        if(entities != null && entities.size() == 1)
        {
            g.setColor(Color.WHITE);
            g.setFont(HEADER);
            entities.get(0).renderGUI(g);
        }
    }
    
}
