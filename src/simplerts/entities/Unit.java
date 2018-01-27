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
import simplerts.Map;
import simplerts.Player;
import simplerts.Utils;
import simplerts.actions.Action;
import simplerts.actions.Destination;
import simplerts.actions.MoveTo;
import simplerts.display.Assets;
import simplerts.gfx.Animation;
import simplerts.gfx.AnimationController;
import simplerts.ui.GUI;
import static simplerts.ui.GUI.HEADER;
import simplerts.ui.UIObject;

/**
 *
 * @author Markus
 */
public abstract class Unit extends Entity {
    public static int POSITION_BOUNDS = 0;
    private float deltaX;
    private float deltaY;
    
    protected float moveX, moveY;
    protected int tempX, tempY;
    protected int moveSpeed = 5;
    
    protected CopyOnWriteArrayList<Action> actions;
    protected Map map;
    
    protected String name;
    protected int attackDamage;
    
    protected int trainTime;
    
    private boolean collided = false;
    
    protected AnimationController ac;
    
    public Unit()
    {
        super();
        ac = new AnimationController();
        actions = new CopyOnWriteArrayList<>();
        x = 300;
        y = 500;
        trainTime = 100;
        width = Game.CELLSIZE;
        height = Game.CELLSIZE;
        name = "Peasant";
    }
    
    public Unit(int x, int y, Player player)
    {
        this();
        this.x = x;
        this.y = y;
        this.player = player;
        moveSpeed = 2;
        color = player.getColor();
        gridX = (x + width/2)/Game.CELLSIZE;
        gridY = (y + height/2)/Game.CELLSIZE;
    }
    
    public Unit(Destination d, Player player)
    {
        this(d.getX(), d.getY(), player);
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
        
        if(getDeltaX() > 0 + POSITION_BOUNDS/2)
        {
            moveX = x + moveSpeed;
            ac.setDirection(Animation.EAST);
        } else if (getDeltaX() < 0 - POSITION_BOUNDS/2)
        {
            moveX = x - moveSpeed;
            ac.setDirection(Animation.WEST);
        }

        if(getDeltaY() > 0 + POSITION_BOUNDS/2)
        {
            moveY = y + moveSpeed;
            ac.setDirection(Animation.SOUTH);
        }
        if(getDeltaY() < 0 - POSITION_BOUNDS/2)
        {
            moveY = y - moveSpeed;
            ac.setDirection(Animation.NORTH);
        }

        if(Math.abs(getDeltaX()) < moveSpeed)
        {
                moveX = x + getDeltaX();
        }
        if(!map.checkUnitCollision(new Rectangle((int)moveX, y, width, height), this) && !map.checkTerrainCollision(new Rectangle((int)moveX, y, width, height)))
        {
            x = (int)moveX;
        }
        if(Math.abs(getDeltaY()) < moveSpeed)
        {
            moveY = y + getDeltaY();
        }

        if(!map.checkUnitCollision(new Rectangle((int)x, (int)moveY, width, height), this) && !map.checkTerrainCollision(new Rectangle((int)x, (int)moveY, width, height)))
        {
            y = (int)moveY;
        }

        if(getDeltaX() != 0 || getDeltaY() != 0)
        {
            updateCells();

            if(getDeltaX() > 0 + POSITION_BOUNDS/2 && getDeltaY() > 0 + POSITION_BOUNDS/2)
                ac.setDirection(Animation.SOUTHEAST);
            if(getDeltaX() < 0 - POSITION_BOUNDS/2 && getDeltaY() > 0 + POSITION_BOUNDS/2)
                ac.setDirection(Animation.SOUTHWEST);
            if(getDeltaX() < 0 - POSITION_BOUNDS/2 && getDeltaY() < 0 - POSITION_BOUNDS/2)
                ac.setDirection(Animation.NORTHWEST);
            if(getDeltaX() > 0 + POSITION_BOUNDS/2 && getDeltaY() < 0 - POSITION_BOUNDS/2)
                ac.setDirection(Animation.NORTHEAST);
        }
        
        gridX = x/Game.CELLSIZE;
        gridY = y/Game.CELLSIZE;
        if(tempGridX != gridX || tempGridY != gridY)
        {
            map.updateEntityCell(tempGridX, tempGridY, null);
            map.updateEntityCell(gridX, gridY, this);
        }
    }
    
    public void setMap(Map map)
    {
        this.map = map;
    }
    
    public void clearActions()
    {
        actions.clear();
    }
    
    @Override
    public void render(Graphics g, float offsetX, float offsetY)
    {
        g.drawImage(ac.getCurrentFrame(), (int)(x - offsetX), (int)(y - offsetY), null);
        if(!actions.isEmpty() && actions.get(0) instanceof MoveTo)
        {
            ((MoveTo)actions.get(0)).render(g);
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
        
        if(Math.abs(deltaX) > 0 || Math.abs(deltaY) > 0 || (actions.size() > 0 && actions.get(0) instanceof MoveTo))
        {
            ac.playAnimation("walk");
        }
        else 
        {
            ac.playAnimation("stand");
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
        Utils.drawWithShadow(g, name, Game.WIDTH/2 - g.getFontMetrics(HEADER).stringWidth(name)/2, 75);
        
        //Render stats
        g.setFont(GUI.BREAD);
        Utils.drawWithShadow(g, "Health: " + health + "/" + maxHealth, 300, 125);
        Utils.drawWithShadow(g, "Damage: " + attackDamage, 300, 150);
        Utils.drawWithShadow(g, "Movespeed: " + moveSpeed, 300, 175);
        
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
    
    public Map getMap()
    {
        return map;
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
}
