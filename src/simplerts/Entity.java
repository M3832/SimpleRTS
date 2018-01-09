/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Markus
 */
public class Entity {
    
    protected int x, y, cellX, cellY, cellWidth, cellHeight, width, height;
    public Color color;
    public Map grid;
    private Point oldPos;
    
    public Entity()
    {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
        cellX = 0;
        cellY = 0;
        color = new Color(0, 255, 0, 125);
        cellWidth = 1;
        cellHeight = 1;
    }
    
    public Entity(int x, int y, int size)
    {
        this();
        this.x = x;
        this.y = y;
        this.cellWidth = size;
        this.cellHeight = size;
        this.cellX = x/Game.CELLSIZE;
        this.cellY = y/Game.CELLSIZE;
        width = size * Game.CELLSIZE;
        height = size * Game.CELLSIZE;
        this.grid = grid;
    }
    
    public Entity(int x, int y, int cellWidth, int cellHeight)
    {
        this();
        this.x = x;
        this.y = y;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.cellX = x/Game.CELLSIZE;
        this.cellY = y/Game.CELLSIZE;
        width = cellWidth * Game.CELLSIZE;
        height = cellHeight * Game.CELLSIZE;
    }
    
    public void render(Graphics g, float offsetX, float offsetY)
    {
        g.setColor(color);
        g.fillRect((int)((cellX * Game.CELLSIZE) - offsetX), (int)((cellY * Game.CELLSIZE) - offsetY), cellWidth * Game.CELLSIZE, cellHeight * Game.CELLSIZE);
    }
    
    public void setPosition(int x, int y)
    {
        oldPos = new Point(this.x, this.y);
        this.x = x;
        this.y = y;
        this.cellX = x/Game.CELLSIZE;
        this.cellY = y/Game.CELLSIZE;
    }
    
    public void update()
    {
        
    }
    
    public void setColor(Color color)
    {
        this.color = color;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public int getCellX(){
        return cellX;
    }
    
    public int getCellY(){
        return cellY;
    }
    
    public int getCellWidth(){
        return cellWidth;
    }
    
    public int getCellHeight(){
        return cellHeight;
    }
    
    public Entity duplicate()
    {
        return new Entity(x, y, cellWidth);
    }
    
}
