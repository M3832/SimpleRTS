/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.input.KeyManager;
import simplerts.input.MouseInput;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Markus
 */
public class Player {
    
    private int gold;
    private CopyOnWriteArrayList<Entity> selectedEntities;
    private EntityPlacement entityplacer;
    private Map grid;
    public Handler handler;
    
    private ArrayList<Entity> availableEntities;
    
    private float boundary;
    
    private MouseInput ml;
    private MouseInput guiml;
    private KeyManager km;
    
    public Player(Handler handler)
    {
        ml = new MouseInput();
        guiml = new MouseInput();
        km = new KeyManager();
        selectedEntities = new CopyOnWriteArrayList<>();
        availableEntities = new ArrayList<>();
        this.grid = handler.map;
        this.handler = handler;
        handler.getDisplay().getGamePanel().addMouseListener(ml);
        handler.getDisplay().getGamePanel().addMouseMotionListener(ml);
        handler.getDisplay().window.addKeyListener(km);
        handler.getDisplay().getGUIPanel().addMouseListener(guiml);
        handler.getDisplay().getGUIPanel().addMouseMotionListener(guiml);
        entityplacer = new EntityPlacement(handler);
        gold = 300;
        boundary = 50;
        initiateEntities();
    }
    
    private void initiateEntities()
    {
        availableEntities.add(new HeadQuarters());
        availableEntities.add(new Bank());
        availableEntities.add(new Tower());
    }
    
    public void input()
    {
        if(km.isPressedOnce(KeyEvent.VK_L))
        {
            if(entityplacer.entity == null)
            {
                entityplacer.setEntity(availableEntities.get(0));
            } else {
                int index = availableEntities.indexOf(entityplacer.entity);
                if(index == availableEntities.size() - 1)
                {
                    entityplacer.clear();
                } else {
                    entityplacer.setEntity(availableEntities.get(++index));
                }
            }
        }
        if(entityplacer.entity != null)
        {
            entityplacer.setPosition((int)(handler.getCamera().getOffsetX() + ml.posX - (entityplacer.getWidth() / 2)),(int)(handler.getCamera().getOffsetY() + ml.posY - (entityplacer.getHeight() / 2)));
        }
        
        if(ml.isMouseClicked())
        {
            if(entityplacer.entity != null)
            {
                Entity e = entityplacer.place();
                if(e != null)
                {
                    grid.addEntity(e);
                }
            }
        }
        
        if(guiml.isMouseDown)
        {
            handler.game.gui.onClick(guiml.posX, guiml.posY);
        }
        
        
    }
    
    public void render(Graphics g)
    {
        if(entityplacer != null && entityplacer.entity != null)
        {
            entityplacer.render(g);
        }
        
        int scrollSpeed = 2;
        
        if(ml.posX > handler.game.WIDTH - boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(scrollSpeed, 0);
        }
        
        if(ml.posX < 0 + boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(-scrollSpeed, 0);
        }
        
        if(ml.posY > handler.game.HEIGHT - boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(0, scrollSpeed);
        }
        
        if(ml.posY < 0 + boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(0, -scrollSpeed);
        }
    }
    
    public void setEntityToPlace(EntityPlacement entity)
    {
        entityplacer = entity;
    }
    
    
}
