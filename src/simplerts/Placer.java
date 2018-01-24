/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

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
    
    Entity entity;
    private Color color;
    private Color errorColor;
    private int cellX, cellY, cellWidth, cellHeight;
    private Handler handler;
    private BufferedImage image;
    
    public Placer(Handler handler)
    {
        color = new Color(0, 0, 255, 150);
        errorColor = new Color(255, 0, 0, 150);
        entity = null;
        this.handler = handler;
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
        int offsetCellX = (int)handler.getCamera().getOffsetX() / Game.CELLSIZE;
        int offsetCellY = (int)handler.getCamera().getOffsetY() / Game.CELLSIZE;
        
        if(entity instanceof Building)
        {
            image = ((Building)entity).getSprite();
        }
        for(int i = cellX; i < cellWidth + cellX; i++)
        {
            for(int j = cellY; j < cellHeight + cellY; j++)
            {
                if(i >= 0 && j >= 0 && i < handler.map.getCells().length && j < handler.map.getCells()[0].length)
                {
                    g.setColor(handler.map.checkCollision(i, j) ? color : errorColor);
                    g.fillRect(((i - offsetCellX) * Game.CELLSIZE) - (int)handler.getCamera().getOffsetX() % Game.CELLSIZE, (j - offsetCellY) * Game.CELLSIZE - (int)handler.getCamera().getOffsetY() % Game.CELLSIZE, Game.CELLSIZE, Game.CELLSIZE);
                }
            }
        }
        ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g.drawImage(image, ((cellX - offsetCellX) * Game.CELLSIZE) - (int)handler.getCamera().getOffsetX() % Game.CELLSIZE, (cellY - offsetCellY) * Game.CELLSIZE - (int)handler.getCamera().getOffsetY() % Game.CELLSIZE, Game.CELLSIZE * cellWidth, Game.CELLSIZE * cellHeight, null);
//        g.setColor(color);
//        g.fillRect(cellX * Map.CELLSIZE, cellY * Map.CELLSIZE, cellSize * Map.CELLSIZE, cellSize * Map.CELLSIZE);
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
    
    public Entity place()
    {
        boolean placeable = true;
        for(int i = cellX; i < cellWidth + cellX; i++)
        {
            for(int j = cellY; j < cellHeight + cellY; j++)
            {
                if(!handler.map.checkCollision(i, j))
                {
                    placeable = false;
                }
            }
        }
        if(placeable)
        {            
            Entity e = entity.duplicate();
            e.setPosition(cellX * Game.CELLSIZE, cellY * Game.CELLSIZE);
            return e;
        }
        return null;
    }
}
