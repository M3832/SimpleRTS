/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.map.BackEndMap;
import simplerts.Player;
import simplerts.audio.SoundController;
import simplerts.utils.Utils;
import simplerts.entities.actions.Action;
import simplerts.map.Destination;
import simplerts.entities.actions.Follow;
import simplerts.entities.actions.MoveTo;
import simplerts.gfx.Assets;
import simplerts.gfx.Animation;
import simplerts.gfx.AnimationController;
import simplerts.ui.GUI;
import static simplerts.ui.GUI.HEADER;
import simplerts.ui.UIObject;
import simplerts.utils.TimerTask;

/**
 *
 * @author Markus
 */
public abstract class Unit extends Entity {
    private float deltaX;
    private float deltaY;
    
    protected float moveX, moveY;
    protected int tempX, tempY;
    protected int moveSpeed = 5;
    
    protected CopyOnWriteArrayList<Action> actions;
    
    protected String name;
    protected int attackDamage;
    private int foodRequirement;
    
    protected int trainTime;
    
    private boolean collided = false;
    
    private Destination targetDestination;
    
    protected AnimationController ac;
    
    public Unit()
    {
        super();
        initVariables();
    }
    
    public Unit(int x, int y, Player player)
    {
        super(x, y, 1, player);
        initVariables();
    }
    
    public Unit(Destination d, Player player)
    {
        this(d.getX(), d.getY(), player);
    }
    
    private void initVariables()
    {
        ac = new AnimationController();
        actions = new CopyOnWriteArrayList<>();
        moveSpeed = 2;
        trainTime = 100;
        setFoodRequirement(1);
        name = "Peasant";
    }
    
    protected void initGraphics()
    {
        icon = Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/peasantPortrait.png"), Assets.loadToCompatibleImage("/peasantPortraittc.png"), color));        
    }
    
    public void move(Destination d)
    {
        deltaX = d.getX() * Game.CELLSIZE - x;
        deltaY = d.getY() * Game.CELLSIZE - y;
        tempX = x;
        tempY = y;

        int tempGridX = gridX;
        int tempGridY = gridY;
        
        if(getDeltaX() > 0)
        {
            moveX = x + moveSpeed;
        } else if (getDeltaX() < 0)
        {
            moveX = x - moveSpeed;
        }

        if(getDeltaY() > 0)
        {
            moveY = y + moveSpeed;
        }
        if(getDeltaY() < 0)
        {
            moveY = y - moveSpeed;
        }

        if(Math.abs(getDeltaX()) < moveSpeed)
        {
                moveX = x + getDeltaX();
        }
        if(!grid.checkUnitCollision(new Rectangle((int)moveX, y, width, height), this) && !grid.checkTerrainCollision(new Rectangle((int)moveX, y, width, height)))
        {
            x = (int)moveX;
        }
        if(Math.abs(getDeltaY()) < moveSpeed)
        {
            moveY = y + getDeltaY();
        }

        if(!grid.checkUnitCollision(new Rectangle((int)x, (int)moveY, width, height), this) && !grid.checkTerrainCollision(new Rectangle((int)x, (int)moveY, width, height)))
        {
            y = (int)moveY;
        }

        if(getDeltaX() != 0 || getDeltaY() != 0)
        {
            updateCells();
            setDirection((int)getDeltaX(), (int)getDeltaY());

        }
        
        gridX = x/Game.CELLSIZE;
        gridY = y/Game.CELLSIZE;
        if(tempGridX != gridX || tempGridY != gridY)
        {
            grid.updateEntityCell(tempGridX, tempGridY, null);
            grid.updateEntityCell(gridX, gridY, this);
        }
        
        if(grid.checkUnitCollision(new Rectangle((int)x, (int)y, width, height), this))
        {
            grid.setEntityPosition(this, getDestination());
        }
    }
    
    public void setDirection(int x, int y)
    {
        if(x > 0)
            ac.setDirection(Animation.EAST);
        if(x < 0)
            ac.setDirection(Animation.WEST);
        if(y > 0)
            ac.setDirection(Animation.SOUTH);
        if(y < 0)
            ac.setDirection(Animation.NORTH);
        if(x > 0 && y > 0)
            ac.setDirection(Animation.SOUTHEAST);
        if(x < 0 && y > 0)
            ac.setDirection(Animation.SOUTHWEST);
        if(x < 0 && y < 0)
            ac.setDirection(Animation.NORTHWEST);
        if(x > 0 && y < 0)
            ac.setDirection(Animation.NORTHEAST);
    }
    
    public void clearActions()
    {
        actions.clear();
    }
    
    @Override
    public void render(Graphics g)
    {
        super.render(g);
        g.drawImage(ac.getCurrentFrame(), (int)(x - offsetX), (int)(y - offsetY), null);
    }
    
    @Override
    public void renderSelected(Graphics g)
    {
        super.renderSelected(g);
        if(!actions.isEmpty())
        {
            actions.get(0).render(g);
        }        
    }
    
    @Override
    public void update()
    {
        super.update();
        if(actions.size() > 0)
        {
            actions.get(0).performAction();
        }
        
        setAnimation();

    }
    
    protected void setAnimation()
    {
        if(actions.size() > 0 && actions.get(0).isMoving())
        {
            ac.setAnimation("walk");
        }
        else 
        {
            ac.setAnimation("stand");
        }
        ac.update();
    }
    
    public String getName()
    {
        return name;
    }
    
    public CopyOnWriteArrayList<Action> getActions()
    {
        return actions;
    }
    
    public void setActions(CopyOnWriteArrayList<Action> actions)
    {
        this.actions = actions;
    }
    
    @Override
    public void renderGUI(Graphics g)
    {
        g.setColor(new Color(255, 155, 111));
        //Render name
        Utils.drawWithShadow(g, name, Game.WIDTH/2 - g.getFontMetrics(HEADER).stringWidth(name)/2, Game.HEIGHT + 75);
        
        //Render stats
        g.setFont(GUI.BREAD);
        Utils.drawWithShadow(g, "Health: " + health + "/" + maxHealth, 300, Game.HEIGHT + 125);
        Utils.drawWithShadow(g, "Damage: " + attackDamage, 300, Game.HEIGHT + 150);
        Utils.drawWithShadow(g, "Movespeed: " + moveSpeed, 300, Game.HEIGHT + 175);
        
        for(UIObject o : uiObjects)
        {
            o.render(g);
        }
    }

    /**
     * @return the deltaX
     */
    public float getDeltaX() {
        return deltaX;
    }

    /**
     * @return the deltaY
     */
    public float getDeltaY() {
        return deltaY;
    }

    /**
     * @return the collided
     */
    public boolean isCollided() {
        return collided;
    }

    /**
     * @param collided the collided to set
     */
    public void setCollided(boolean collided) {
        this.collided = collided;
    }
    
    public int getTrainTime()
    {
        return trainTime;
    }
    
    public int getTempX()
    {
        return tempX;
    }
    
    public int getTempY()
    {
        return tempY;
    }
    
    public BackEndMap getMap()
    {
        return grid;
    }

    public void addAction(Action action) {
        actions.add(action);
    }
    
    public void addAction(int index, Action action) {
        actions.add(index, action);
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }
    
    public void rightClickAction(Entity e)
    {
        if(e.getPlayer() == player)
        {
            if(e instanceof Unit)
            {
                addAction(new Follow(this, e));
            } else {
                addAction(new MoveTo(this, grid.getPathFinder().findPath(getDestination(), grid.getClosestCell(this, e))));
            }
        } else {
            addAction(new Follow(this, e));
        }
    }

    public int getCost() {
        return goldCost;
    }
    
    public void setTargetDestination (Destination d)
    {
        targetDestination = d;
    }
    
    public Destination getTargetDestination()
    {
        return targetDestination;
    }

    /**
     * @return the foodRequirement
     */
    public int getFoodRequirement() {
        return foodRequirement;
    }

    /**
     * @param foodRequirement the foodRequirement to set
     */
    public void setFoodRequirement(int foodRequirement) {
        this.foodRequirement = foodRequirement;
    }
    
    @Override
    protected void die()
    {
        super.die();
        taskManager.addTask(new TimerTask(30000, () -> {player.getEntities().remove(this); player.getHandler().map.getEntities().remove(this);}));
    }
}
