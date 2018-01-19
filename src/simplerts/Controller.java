/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import simplerts.actions.Destination;
import simplerts.input.KeyManager;
import simplerts.input.MouseInput;

/**
 *
 * @author Markus
 */
public class Controller {
    
    private Player player;
    private Handler handler;
    
    private EntityPlacement entityplacer;
    
    private MouseInput ml;
    private MouseInput guiml;
    private KeyManager km;
    
    private float boundary;
    
    private ArrayList<Entity> selected;
    
    private Rectangle selectBox;
    private double startDragX, startDragY;
    private double currentDragX, currentDragY;
    
    public Controller(Handler handler, Player player)
    {
        this.handler = handler;
        this.player = player;
        
        ml = new MouseInput();
        guiml = new MouseInput();
        km = new KeyManager();
        
        selected = new ArrayList<>();
        
        handler.getDisplay().getGamePanel().addMouseListener(ml);
        handler.getDisplay().getGamePanel().addMouseMotionListener(ml);
        handler.getDisplay().getGUIPanel().addMouseListener(guiml);
        handler.getDisplay().getGUIPanel().addMouseMotionListener(guiml);
        handler.getDisplay().window.addKeyListener(km);
        
        boundary = 50;
    }
    
    public void update()
    {
        input();
    }
    
    public void render(Graphics g)
    {
        if(selectBox != null)
        {
            g.setColor(Color.GREEN);
            g.drawRect((int)(selectBox.getX() - handler.getCamera().getOffsetX()), (int)(selectBox.getY() - handler.getCamera().getOffsetY()), (int)selectBox.getWidth(), (int)selectBox.getHeight());
        }
        if(selected.size() > 0)
        {
            for(Entity e : selected)
            {
                g.setColor(Color.GREEN);
                g.drawRect(e.x - (int)handler.camera.getOffsetX(), e.y - (int)handler.camera.getOffsetY(), e.width, e.height);
            }
        }
        
    }
    
    public void scrolling()
    {
        int scrollSpeed = 1000;
        float frameScroll = scrollSpeed * ((float)Game.millisSinceLastRender/1000);
        
        if(ml.posX > handler.game.WIDTH - boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(frameScroll, 0);
        }
        
        if(ml.posX < 0 + boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(-frameScroll, 0);
        }
        
        if(ml.posY > handler.game.HEIGHT - boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(0, frameScroll);
        }
        
        if(ml.posY < 0 + boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(0, -frameScroll);
        }
    }
    
    private void input()
    {       
        if(guiml.isMouseDown)
        {
            handler.game.gui.onClick(guiml.posX, guiml.posY);
        }
        
        if(ml.isMouseDown)
        {
            if(selectBox == null)
            {
                startDragX = ml.posX + (int)handler.getCamera().getOffsetX();
                startDragY = ml.posY + (int)handler.getCamera().getOffsetY();
                selectBox = new Rectangle(ml.posX + (int)handler.getCamera().getOffsetX(), ml.posY + (int)handler.getCamera().getOffsetY(), 0, 0);
            } else {
                currentDragX = ml.posX + (int)handler.getCamera().getOffsetX();
                currentDragY = ml.posY + (int)handler.getCamera().getOffsetY();
                selectBox.setBounds((int)Math.min(startDragX, currentDragX), (int)Math.min(startDragY, currentDragY), (int)Math.abs(startDragX - currentDragX), (int)Math.abs(startDragY - currentDragY));
            }
        } else {
            if(selectBox != null)
            {
                selected = (ArrayList<Entity>)handler.map.getEntitiesInSelection(selectBox);
                handler.game.gui.setSelectedEntities(selected);
                selectBox = null;
            }
        }
        
        if(ml.isRightMouseClicked())
        {
            for(Entity e : selected)
            {
                if(e instanceof Unit)
                {
                    ((Unit)e).clearDestinations();
                    ((Unit)e).destinations = new PathFinder(handler.map).findPath(new Destination(((Unit) e).x, ((Unit) e).y), new Destination(((int)(ml.posX + handler.camera.getOffsetX())/Game.CELLSIZE) * Game.CELLSIZE, ((int)(ml.posY + handler.getCamera().getOffsetY()) / Game.CELLSIZE) * Game.CELLSIZE));
                }
            }
        }
    }
    
}
