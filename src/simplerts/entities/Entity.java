/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.map.BackEndMap;
import simplerts.Player;
import simplerts.audio.SoundController;
import simplerts.display.Camera;
import simplerts.map.Destination;
import simplerts.messaging.Message;
import simplerts.ui.UIAction;
import simplerts.ui.UIObject;
import simplerts.utils.TaskManager;
import simplerts.utils.TimerTask;

/**
 *
 * @author Markus
 */
public abstract class Entity {
    
    protected int x, y, width, height, gridX, gridY, gridWidth, gridHeight;
    protected int goldCost, lumberCost;
    protected int health, maxHealth, armor;
    protected int offsetX, offsetY;
    protected int viewRange;
    protected BufferedImage sprite, icon;
    protected ArrayList<UIObject> uiObjects;
    protected ArrayList<UIAction> uiActions;
    protected Player player;
    protected boolean isVisible, isDead;
    public Color color;
    public BackEndMap grid;
    protected SoundController soundManager;
    protected TaskManager taskManager;
    
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
        viewRange = 2;
        isVisible = true;
        isDead = false;
        health = maxHealth;
        soundManager = new SoundController();
        taskManager = new TaskManager();
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
        uiActions = new ArrayList<>();
        uiObjects = new ArrayList<>();
    }
    
    public void render(Graphics g, Camera camera)
    {
        this.offsetX = (int)camera.getOffsetX();
        this.offsetY = (int)camera.getOffsetY();
    }
    
    public void renderSelected(Graphics g, Camera camera)
    {
        
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
         int newX = d.getX() * Game.CELLSIZE;
         int newY = d.getY() * Game.CELLSIZE;
         if(!grid.checkUnitCollision(new Rectangle(newX, newY, width, height), this))
         {
            setPosition(d.getX() * Game.CELLSIZE, d.getY() * Game.CELLSIZE); 
         } else {
            Entity temp = grid.getEntityFromCell(newX/Game.CELLSIZE, newY/Game.CELLSIZE);
            if(temp != null)
            {
                setPosition(grid.getAvailableNeighborCell(temp));
            } else {
                setPosition(d.getX() * Game.CELLSIZE, d.getY() * Game.CELLSIZE);
            }
         }
         updateCells();
     }
    
    public void update()
    {
        taskManager.update();
    }
    
    public void updateCells()
    {
        gridX = Math.round((float)x/Game.CELLSIZE);
        gridY = Math.round((float)y/Game.CELLSIZE);
        grid.updateEntityCell(gridX, gridY, this);
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
    
    public int getGoldCost()
    {
        return goldCost;
    }

    public int getViewRange() {
        return viewRange;
    }
    
    public int getArmor()
    {
        return armor;
    }

    public void hit(int damage) {
        health -= damage;
        if(health <= 0)
        {
            player.getHandler().game.mm.addMessage(new Message("Died"));
            die();
        }
    }
    
    protected void die()
    {
        isDead = true;
        taskManager.addTask(new TimerTask(30000, () -> {player.getEntities().remove(this); player.getHandler().map.getEntities().remove(this);}));
    }

    public boolean isDead() {
        return isDead;
    }
    
    public void playSound(int type)
    {
        if(soundManager != null)
        {
            soundManager.playSound(type);
        }
    }
    
    public void addTask(TimerTask t)
    {
        taskManager.addTask(t);
    }
}
