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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.map.BackEndMap;
import simplerts.Player;
import simplerts.audio.SoundController;
import simplerts.display.Camera;
import simplerts.gfx.Assets;
import simplerts.map.Destination;
import simplerts.messaging.Message;
import simplerts.ui.UIAction;
import simplerts.ui.UIActionButton;
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
    protected UIObject portrait;
    protected ArrayList<UIObject> uiObjects;
    protected ArrayList<UIAction> actionButtons, cancelMenu, currentMenu;
    protected Player player;
    protected boolean isVisible, isDead, remove;
    protected Color color;
    protected BackEndMap grid;
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
        remove = false;
        health = maxHealth;
        soundManager = new SoundController();
        taskManager = new TaskManager();
        actionButtons = new ArrayList<>();
        cancelMenu = new ArrayList<>();
        currentMenu = actionButtons;
        uiObjects = new ArrayList<>();
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
        initGraphics();
        setupActions();
    }
    
    protected void initGraphics(){
        
    }
    
    protected void setupActions(){
        addActionButton(cancelMenu, new UIActionButton(Assets.iconCancel, () -> {cancel();}, "Cancel", 'c'), 2, 2);
    }
    
    protected void addActionButton(ArrayList<UIAction> menu, UIAction button){
        addActionButton(menu, button, (menu.size()/3), (menu.size()%3));
    }
    
    protected void addActionButton(ArrayList<UIAction> menu, UIAction button, int x, int y){
        button.setY(Game.HEIGHT + 20 + (button.getHeight() + 10) * x);
        button.setX(762 + (button.getWidth() + 10) * y);
        button.updateBounds();
        menu.add(button);
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
         int newX = d.getGridX() * Game.CELLSIZE;
         int newY = d.getGridY() * Game.CELLSIZE;
         if(!grid.checkUnitCollision(new Rectangle(newX, newY, width, height), this))
         {
            setPosition(d.getGridX() * Game.CELLSIZE, d.getGridY() * Game.CELLSIZE); 
         } else {
            Entity temp = grid.getEntityFromCell(newX/Game.CELLSIZE, newY/Game.CELLSIZE);
            if(temp != null)
            {
                setPosition(grid.getAvailableNeighborCell(temp));
            } else {
                setPosition(d.getGridX() * Game.CELLSIZE, d.getGridY() * Game.CELLSIZE);
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
        return currentMenu;
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
            die();
        }
    }
    
    protected void die()
    {
        isDead = true;
        player.died(this);
        taskManager.addTask(new TimerTask(30000, () -> { remove = true;}));
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
    
    protected void cancel(){
        setDefaultMenu();
        player.getController().cancel();
    }
    
    public void addTask(TimerTask t)
    {
        taskManager.addTask(t);
    }
    
    public boolean timeToRemove() {
        return remove;
    }

    public void setCancelMenu() {
        currentMenu = cancelMenu;
        updateGUIMenu();
    }

    public void setDefaultMenu() {
        currentMenu = actionButtons;
        updateGUIMenu();
    }
    
    public void setCurrentMenu(ArrayList<UIAction> menu){
        currentMenu = menu;
        updateGUIMenu();
    }
    
    protected void updateGUIMenu() {
        player.getController().changeActionMenu(this, currentMenu);
    }
    
    public BackEndMap getMap(){
        return grid;
    }

    public Color getColor() {
        return color;
    }

    public int getLumberCost() {
        return lumberCost;
    }
}
