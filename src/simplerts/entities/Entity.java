/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import simplerts.Game;
import simplerts.Map;

/**
 *
 * @author Markus
 */
public class Entity {
    
    protected int x, y, width, height, gridX, gridY, gridWidth, gridHeight;
    protected int health, maxHealth;
    protected BufferedImage sprite, icon;
    public Color color;
    public Map grid;
    
    public Entity()
    {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
        gridX = 0;
        gridY = 0;
        color = new Color(50, 200, 50);
        gridWidth = 1;
        gridHeight = 1;
        maxHealth = 50;
        health = maxHealth;
    }
    
    public Entity(int x, int y, int size)
    {
        this();
        this.x = x;
        this.y = y;
        this.width = size * Game.CELLSIZE;
        this.height = size * Game.CELLSIZE;
        this.gridX = x/Game.CELLSIZE;
        this.gridY = y/Game.CELLSIZE;
        this.gridWidth = size;
        this.gridHeight = size;
    }
    
    public void render(Graphics g, float offsetX, float offsetY)
    {
        g.setColor(color);
        g.fillRect((int)((gridX * Game.CELLSIZE) - offsetX), (int)((gridY * Game.CELLSIZE) - offsetY), gridWidth * Game.CELLSIZE, gridHeight * Game.CELLSIZE);
    }
    
    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.gridX = x/Game.CELLSIZE;
        this.gridY = y/Game.CELLSIZE;
    }
    
    public void update()
    {
        
    }
    
    public void updateCells()
    {
        gridX = (x + width/2)/Game.CELLSIZE;
        gridY = (y + height/2)/Game.CELLSIZE;
    }
    
    public void setColor(Color color)
    {
        this.color = color;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public int getGridX(){
        return gridX;
    }
    
    public int getGridY(){
        return gridY;
    }
    
    public int getGridWidth(){
        return gridWidth;
    }
    
    public int getGridHeight(){
        return gridHeight;
    }
    
    public Entity duplicate()
    {
        return new Entity(x, y, gridWidth);
    }
    
    public int getHealth()
    {
        return health;
    }
    
    public int getMaxHealth()
    {
        return maxHealth;
    }
    
    public BufferedImage getIcon()
    {
        return icon;
    }
    
    public void renderGUI(Graphics g)
    {
        
    }
    
}
