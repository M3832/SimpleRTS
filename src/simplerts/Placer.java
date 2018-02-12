/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.map.Destination;
import simplerts.entities.Building;
import simplerts.entities.Entity;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Markus
 */
public class Placer {
    
    private Entity entity;
    private Controller controller;
    private Color color;
    private Color errorColor;
    private int cellX, cellY, cellWidth, cellHeight;
    private BufferedImage image;
    
    public Placer(Controller controller)
    {
        color = new Color(0, 0, 255, 150);
        errorColor = new Color(255, 0, 0, 150);
        entity = null;
        this.controller = controller;
    }
    
    public void setEntity(Entity entity)
    {
        this.entity = entity;
        cellX = 0;
        cellY = 0;
        cellWidth = entity.getGridWidth();
        cellHeight = entity.getGridHeight();
    }
    
    public void clear()
    {
        entity = null;
    }
    
    public void render(Graphics g)
    {
        int offsetCellX = (int)controller.getCamera().getOffsetX() / Game.CELLSIZE;
        int offsetCellY = (int)controller.getCamera().getOffsetY() / Game.CELLSIZE;
        
        if(entity instanceof Building)
        {
            image = ((Building)entity).getFinalSprite();
        }
        for(int i = cellX; i < cellWidth + cellX; i++)
        {
            for(int j = cellY; j < cellHeight + cellY; j++)
            {
                if(i >= 0 && j >= 0 && i < controller.getMap().getCells().length && j < controller.getMap().getCells()[0].length)
                {
                    g.setColor(!controller.getMap().getBackEnd().checkCollision(i, j) ? color : errorColor);
                    g.fillRect(((i - offsetCellX) * Game.CELLSIZE) - (int)controller.getCamera().getOffsetX() % Game.CELLSIZE, (j - offsetCellY) * Game.CELLSIZE - (int)controller.getCamera().getOffsetY() % Game.CELLSIZE, Game.CELLSIZE, Game.CELLSIZE);
                }
            }
        }
        ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g.drawImage(image, ((cellX - offsetCellX) * Game.CELLSIZE) - (int)controller.getCamera().getOffsetX() % Game.CELLSIZE, (cellY - offsetCellY) * Game.CELLSIZE - (int)controller.getCamera().getOffsetY() % Game.CELLSIZE, Game.CELLSIZE * cellWidth, Game.CELLSIZE * cellHeight, null);
//        g.setColor(color);
//        g.fillRect(cellX * BackEndMap.CELLSIZE, cellY * BackEndMap.CELLSIZE, cellSize * BackEndMap.CELLSIZE, cellSize * BackEndMap.CELLSIZE);
    }
    
    public int getWidth()
    {
        return entity.getWidth();
    }
    
    public int getHeight()
    {
        return entity.getHeight();
    }
    
    public void setPosition(int x, int y)
    {
        this.cellX = x/Game.CELLSIZE;
        this.cellY = y/Game.CELLSIZE;
    }
    
    public boolean isPlaceable(Entity excludeThis)
    {
        boolean placeable = true;
        for(int i = cellX; i < cellWidth + cellX; i++)
        {
            for(int j = cellY; j < cellHeight + cellY; j++)
            {
                if(controller.getMap().getBackEnd().checkCollision(i, j, excludeThis))
                {
                    placeable = false;
                }
            }
        }
        return placeable;
    }
    
    public Destination getDestination()
    {
        return new Destination(cellX, cellY);
    }
    
    public Entity getEntity()
    {
        return entity;
    }
    
    public boolean hasEntity()
    {
        return entity != null;
    }
}
