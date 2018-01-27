/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import simplerts.Game;
import simplerts.Map;
import simplerts.Player;
import simplerts.actions.Destination;
import simplerts.ui.UIAction;
import simplerts.ui.UIObject;

/**
 *
 * @author Markus
 */
public abstract class Entity {
    
    protected int x, y, width, height, gridX, gridY, gridWidth, gridHeight;
    protected int goldCost, lumberCost;
    protected int health, maxHealth;
    protected BufferedImage sprite, icon;
    protected ArrayList<UIObject> uiObjects;
    protected ArrayList<UIAction> uiActions;
    protected Player player;
    protected boolean isVisible, isDead;
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
        goldCost = 0;
        lumberCost = 0;
        color = new Color(50, 200, 50);
        gridWidth = 1;
        gridHeight = 1;
        maxHealth = 50;
        isVisible = true;
        isDead = false;
        health = maxHealth;
    }
    
    public Entity(int x, int y, int size, Player player)
    {
        this();
        this.x = x * Game.CELLSIZE;
        this.y = y * Game.CELLSIZE;
        this.width = size * Game.CELLSIZE;
        this.height = size * Game.CELLSIZE;
        this.gridX = x;
        this.gridY = y;
        this.gridWidth = size;
        this.gridHeight = size;
        this.player = player;
        color = player.getColor();
        grid = player.getHandler().map;
    }
    
    public void setupActions()
    {
        uiObjects = new ArrayList<>();
        uiActions = new ArrayList<>();
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
    
     public void setPosition(Destination d)
     {
         setPosition(d.getX() * Game.CELLSIZE, d.getY() * Game.CELLSIZE);
         updateCells();
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
    
    public abstract Entity duplicate();
    
    public int getHealth()
    {
        return health;
    }
    
    public int getMaxHealth()
    {
        return maxHealth;
    }
    
    public ArrayList<UIObject> getUIObjects()
    {
        return uiObjects;
    }
    
    public ArrayList<UIAction> getUIActions()
    {
        return uiActions;
    }
    
    public BufferedImage getIcon()
    {
        return icon;
    }
    
    public void renderGUI(Graphics g)
    {
        
    }
    
    public Destination getDestination()
    {
        return new Destination(gridX, gridY);
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public boolean isVisible()
    {
        return isVisible;
    }

    public void setGridPosition(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.x = gridX * Game.CELLSIZE;
        this.y = gridY * Game.CELLSIZE;
    }
    
    public BufferedImage getFinalSprite()
    {
        return sprite.getSubimage(width * (int)(1 * (sprite.getWidth()/width - 1)), 0, width, height);
    }
    
}
