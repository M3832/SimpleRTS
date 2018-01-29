/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.entities.Entity;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.entities.interfaces.FoodProvider;
import simplerts.entities.Unit;
import simplerts.entities.interfaces.GoldReceiver;
import simplerts.gfx.SpriteManager;

/**
 *
 * @author Markus
 */
public class Player {
    
    private int gold, lumber, maxFood, currentFood;
    private Color teamColor;
    private CopyOnWriteArrayList<Entity> entities;
    private SpriteManager spritemanager;
    private Controller controller;

    private Handler handler;
    
    public Player(Handler handler)
    {
        this.handler = handler;
        entities = new CopyOnWriteArrayList<>();
        gold = 400;
        lumber = 0;
        maxFood = 0;
        currentFood = 0;
        teamColor = new Color(255, 0, 0);
        this.spritemanager = new SpriteManager(teamColor);
    }
    
    public SpriteManager getSpriteManager()
    {
        return spritemanager;
    }
    
    public Color getColor()
    {
        return teamColor;
    }

    /**
     * @return the lumber
     */
    public int getLumber() {
        return lumber;
    }

    /**
     * @param lumber the lumber to set
     */
    public void setLumber(int lumber) {
        this.lumber = lumber;
    }

    /**
     * @return the maxFood
     */
    public int getMaxFood() {
        return maxFood;
    }

    /**
     * @param maxFood the maxFood to set
     */
    public void setMaxFood(int maxFood) {
        this.maxFood = maxFood;
    }

    /**
     * @return the handler
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    
    public void setGold(int gold)
    {
        this.gold = gold;
    }
    
    public void addGold(int gold)
    {
        this.gold += gold;
    }
    
    public int getGold()
    {
        return gold;
    }
    
    public void addEntity(Entity e)
    {
        entities.add(e);
        if(e instanceof FoodProvider)
        {
            maxFood += ((FoodProvider)e).getFoodProduced();
        } else if (e instanceof Unit)
        {
            currentFood++;
        }
    }

    public int getCurrentFood() {
        return currentFood;
    }
    
    public boolean hasRoomFor(Unit u)
    {
        return currentFood + u.getFoodRequirement() <= maxFood;
    }
    
    public CopyOnWriteArrayList<Entity> getEntities()
    {
        return entities;
    }

    public boolean hasGoldFor(Entity e) {
        return gold - e.getGoldCost() >= 0;
    }

    public void pay(int cost) {
        this.gold -= cost;
    }

    /**
     * @return the controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void addLumber(int lumber) {
        this.lumber += lumber;
    }
    
    
    
}
