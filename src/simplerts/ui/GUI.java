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
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.entities.Entity;
import simplerts.gfx.Assets;
import simplerts.Game;
import simplerts.Handler;
import simplerts.map.Map;

/**
 *
 * @author Markus
 */
public class GUI {
    
    Map map;
    private MiniMap minimap;
    private int height = 225;
    private Handler handler;
    private int mapOffsetX = 35, mapOffsetY = Game.HEIGHT + 31;
    private ArrayList<Entity> entities;
    public static Font HEADER = new Font("Verdana", Font.PLAIN, 34);
    public static Font BREAD = new Font("Verdana", Font.PLAIN, 16);
    public static Font SMALL = new Font("Verdana", Font.PLAIN, 10);
    private CopyOnWriteArrayList<UIObject> objects;
    
    public GUI(Map map, Handler handler)
    {
        this.map = map;
        this.handler = handler;
        minimap = map.getMiniMap(175, 175);
        entities = new ArrayList<>();
        objects = new CopyOnWriteArrayList<>();
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
        objects.clear();
        if(entities.size() == 1)
        {
            if(!entities.get(0).getUIActions().isEmpty())
            {
                for(UIAction a : entities.get(0).getUIActions())
                {
                    addUIAction(a);
                }
            }
            if(!entities.get(0).getUIObjects().isEmpty())
            {
                for(UIObject a : entities.get(0).getUIObjects())
                {
                    addUIObject(a);
                }
            }            
        }
    }
    
    public void update()
    {
        objects.stream().forEach((UIObject o) -> o.tick());
    }
    
    public void render(Graphics g)
    {
        g.drawImage(Assets.GUI, 0, Game.HEIGHT, null);
        g.drawImage(minimap.getMinimapWithEntities(), 35, Game.HEIGHT + 31, null);
        g.setColor(Color.WHITE);
        g.drawRect((int)(handler.getCamera().getOffsetX() * minimap.getPixelRatio()) + mapOffsetX, (int)(handler.getCamera().getOffsetY() * minimap.getPixelRatio()) + mapOffsetY, (int)(Game.WIDTH * minimap.getPixelRatio()), (int)(Game.HEIGHT * minimap.getPixelRatio()));
        
        if(entities != null && entities.size() == 1)
        {
            g.setColor(Color.WHITE);
            g.setFont(HEADER);
            entities.get(0).renderGUI(g);
        } else if (entities != null && entities.size() > 1 && entities.size() < 6)
        {
            for(int i = 0; i < entities.size(); i++)
            {
                g.drawImage(entities.get(i).getIcon(), 275 + i * 75, Game.HEIGHT + 100, null);
            }
        } else if (entities != null && entities.size() > 5)
        {
            for(int i = 0; i < entities.size(); i++)
            {
                g.drawImage(entities.get(i).getIcon(), (275 + ((i%5) * 75)), Game.HEIGHT + 100 + (75 * (i/5)), null);
            }
        }
        
        objects.stream().forEach((UIObject o) -> {o.render(g);});
    }
    
    public void onMouseMove(MouseEvent e)
    {
        objects.stream().forEach((UIObject o) -> o.onMouseMove(e));
    }
    
    public void onMouseRelease(MouseEvent e)
    {
        objects.stream().forEach((UIObject o) -> o.onMouseRelease(e));
    }
    
    public void addUIAction(UIAction o)
    {
        o.setY(Game.HEIGHT + 24 + (o.getHeight() + 7) * (objects.size()/3));
        o.setX(777 + (o.getWidth() + 8) * (objects.size()%3));
        o.updateBounds();
        objects.add(o);
    }
    
    public void addUIObject(UIObject o)
    {
        objects.add(o);
    }
    
    public void removeObject(UIObject o)
    {
        objects.remove(o);
    }
    
    public void updateMinimap()
    {
        minimap.setImage(map.getMinimapImage(175, 175));
    }
    
}
