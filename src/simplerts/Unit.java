/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.actions.Destination;
import simplerts.display.Assets;
import simplerts.ui.GUI;
import static simplerts.ui.GUI.HEADER;

/**
 *
 * @author Markus
 */
public class Unit extends Entity {
    protected float deltaX, deltaY, moveX, moveY;
    protected BufferedImage sprite;
    protected CopyOnWriteArrayList<Destination> destinations;
    protected int MOVESPEED = 5;
    public static int POSITION_BOUNDS = 1;
    protected Map map;
    
    protected String name;
    protected int attackDamage;
    
    public Unit()
    {
        super();
        sprite = Assets.loadToCompatibleImage("/peasant.png");
        destinations = new CopyOnWriteArrayList<>();
        x = 200;
        y = 500;
        width = Game.CELLSIZE;
        height = Game.CELLSIZE;
        name = "Peasant";
        updateCells();
    }
    
    public Unit(int x, int y)
    {
        this();
        this.x = x;
        this.y = y;
        updateCells();
    }
    
    public void move()
    {
        if(destinations.size() > 0)
        {
            deltaX = destinations.get(0).getX() - x;
            deltaY = destinations.get(0).getY() - y;
            
            if(deltaX > 0 + POSITION_BOUNDS/2)
            {
                moveX = x + MOVESPEED;
            } else if (deltaX < 0 - POSITION_BOUNDS/2)
            {
                moveX = x - MOVESPEED;
            }

            if(deltaY > 0 + POSITION_BOUNDS/2)
            {
                moveY = y + MOVESPEED;
            }
            if(deltaY < 0 - POSITION_BOUNDS/2)
            {
                moveY = y - MOVESPEED;
            }
            
            if((-POSITION_BOUNDS < deltaX && deltaX < POSITION_BOUNDS) && (-POSITION_BOUNDS < deltaY && deltaY < POSITION_BOUNDS))
            {
                destinations.remove(0);
            }
            
            if(deltaX != 0 || deltaY != 0)
            {
                updateCells();
            }
            
            if(!map.checkUnitCollision(new Rectangle((int)moveX, y, width, height), this) && !map.checkTerrainCollision(new Rectangle((int)moveX, y, width, height)))
            {
                x = (int)moveX;
            }
            
            if(!map.checkUnitCollision(new Rectangle((int)x, (int)moveY, width, height), this) && !map.checkTerrainCollision(new Rectangle((int)x, (int)moveY, width, height)))
            {
                y = (int)moveY;
            }
        }
    }
    
    public void setMap(Map map)
    {
        this.map = map;
    }
    
    private void updateCells()
    {
        cellX = x/Game.CELLSIZE;
        cellY = y/Game.CELLSIZE;
    }
    
    public void addDestination(Destination d)
    {
        d.setX(d.getX());
        d.setY(d.getY());
        destinations.add(d);
    }
    
    public void clearDestinations()
    {
        destinations.clear();
    }
    
    @Override
    public void render(Graphics g, float offsetX, float offsetY)
    {
        g.drawImage(sprite, (int)(x - offsetX), (int)(y - offsetY), null);
        
        for(Destination d : destinations)
        {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect((int)(d.getX() - offsetX), (int)(d.getY() - offsetY), Game.CELLSIZE, Game.CELLSIZE);
        }
    }
    
    @Override
    public void update()
    {
        super.update();
        move();
    }
    
    public String getName()
    {
        return name;
    }
    
    @Override
    public void renderGUI(Graphics g)
    {
        g.drawString(name, Game.WIDTH/2 - g.getFontMetrics(HEADER).stringWidth(name)/2, 75);
        g.setFont(GUI.BREAD);
        g.drawString("Health: " + health + "/" + maxHealth, 300, 125);
    }
}
