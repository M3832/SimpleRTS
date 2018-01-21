/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import simplerts.entities.Entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.Map;
import simplerts.Utils;
import simplerts.actions.Destination;
import simplerts.display.Assets;
import simplerts.ui.GUI;
import static simplerts.ui.GUI.HEADER;

/**
 *
 * @author Markus
 */
public class Unit extends Entity {
    public static int POSITION_BOUNDS = 1;
    
    protected float deltaX, deltaY, moveX, moveY;
    protected int moveSpeed = 5;
    
    protected CopyOnWriteArrayList<Destination> destinations;
    protected Map map;
    
    protected String name;
    protected int attackDamage;
    
    protected boolean collidedX = false, collidedY = false;
    
    public Unit()
    {
        super();
        destinations = new CopyOnWriteArrayList<>();
        x = 200;
        y = 500;
        width = Game.CELLSIZE;
        height = Game.CELLSIZE;
        name = "Empty";
        initGraphics();
        updateCells();
    }
    
    public Unit(int x, int y)
    {
        this();
        this.x = x;
        this.y = y;
        moveSpeed = 2;
        color = new Color(255, 50, 50);
        initGraphics();
        updateCells();
    }
    
    private void initGraphics()
    {
        sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/peasant.png"), Assets.loadToCompatibleImage("/peasanttc.png"), color);
        icon = Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/peasantPortrait.png"), Assets.loadToCompatibleImage("/peasantPortraittc.png"), color));        
    }
    
    public void move()
    {
        if(destinations.size() > 0)
        {
            deltaX = destinations.get(0).getX() - x;
            deltaY = destinations.get(0).getY() - y;
            
            if(deltaX > 0 + POSITION_BOUNDS/2)
            {
                moveX = x + moveSpeed;
            } else if (deltaX < 0 - POSITION_BOUNDS/2)
            {
                moveX = x - moveSpeed;
            }

            if(deltaY > 0 + POSITION_BOUNDS/2)
            {
                moveY = y + moveSpeed;
            }
            if(deltaY < 0 - POSITION_BOUNDS/2)
            {
                moveY = y - moveSpeed;
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
        
//        destinations.forEach((d) -> {
//            g.setColor(new Color(255, 255, 255, 50));
//            g.fillRect((int)(d.getX() - offsetX), (int)(d.getY() - offsetY), Game.CELLSIZE, Game.CELLSIZE);
//        });
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
    
    public CopyOnWriteArrayList<Destination> getDestinations()
    {
        return destinations;
    }
    
    public void setDestinations(CopyOnWriteArrayList<Destination> destinations)
    {
        this.destinations = destinations;
    }
    
    @Override
    public void renderGUI(Graphics g)
    {
        g.setColor(new Color(255, 155, 111));
        //Render name
        Utils.drawWithShadow(g, name, Game.WIDTH/2 - g.getFontMetrics(HEADER).stringWidth(name)/2, 75);
        
        //Render stats
        g.setFont(GUI.BREAD);
        Utils.drawWithShadow(g, "Health: " + health + "/" + maxHealth, 300, 125);
        Utils.drawWithShadow(g, "Damage: " + attackDamage, 300, 150);
        Utils.drawWithShadow(g, "Movespeed: " + moveSpeed, 300, 175);
        
        //Render icon
        g.drawImage(icon, Game.WIDTH/2 + 100, 110, null);
    }
}
