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
import simplerts.gfx.SpriteManager;
import simplerts.messaging.ErrorMessage;
import simplerts.utils.TaskManager;
import simplerts.utils.TimerTask;

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
    private TaskManager tm;

    private Handler handler;
    
    public Player(Handler handler, Color color)
    {
        this.handler = handler;
        entities = new CopyOnWriteArrayList<>();
        gold = 400;
        lumber = 0;
        maxFood = 0;
        currentFood = 0;
        teamColor = color;
        tm = new TaskManager();
        this.spritemanager = new SpriteManager(teamColor);
    }
    
    public void update()
    {
        tm.update();
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
        for(int i = 0; i < gold; i++){
            tm.addTask(new TimerTask(i+1, () -> {this.gold += 1;}));
        }
    }
    
    public int getGold()
    {
        return gold;
    }
    
    public void addEntity(Entity e)
    {
        entities.add(e);
        if (e instanceof Unit)
        {
            currentFood++;
        }
    }
    
    public void removeEntity(Entity e){
        entities.remove(e);
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
    
    public boolean hasLumberFor(Entity e) {
        return lumber - e.getLumberCost() >= 0;
    }

    public void pay(int cost, int lumberCost) {
        this.gold -= cost;
        this.lumber -= lumberCost;
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
        for(int i = 0; i < lumber; i++){
            tm.addTask(new TimerTask(i+1, () -> {this.lumber += 1;}));
        }
    }
    
    public void addFoodCapacity(int sum){
        maxFood += sum;
    }

    public void deselectEntity(Entity e) {
        if(controller != null)
        {
            controller.deselect(e);
        }
    }

    public void died(Entity e) {
        if(e instanceof FoodProvider)
        {
            maxFood -= ((FoodProvider)e).getFoodProduced();
        } else if (e instanceof Unit)
        {
            currentFood--;
        }
    }

    public void sendErrorMessage(String message) {
        if(controller != null){
            controller.sendMessage(new ErrorMessage(message));
        }
    }
    
    
    
}
