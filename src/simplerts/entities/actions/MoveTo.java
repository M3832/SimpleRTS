/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Color;
import simplerts.map.Destination;
import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.display.Camera;
import simplerts.entities.Entity;
import simplerts.entities.Unit;
import simplerts.utils.TaskManager;
import simplerts.utils.TimerTask;

/**
 *
 * @author Markus
 */
public class MoveTo extends Action {
    
    public static final int CHECK_AHEAD_DISTANCE = 1;
    public static final int COLLISION_CHECK_BUFFER = 5;
    public static final long STUCK_TIME = 50;
    
    protected TaskManager tm;
    protected CopyOnWriteArrayList<Destination> destinations;
    protected int collisions;
    protected boolean stuckWarning, stuck, lastOccupied;
    protected long stuckTimer;
    private final Destination lastDestination;
    private final Entity targetEntity;

    public MoveTo(Unit owner, Destination d) {
        super(owner);
        lastDestination = d;
        targetEntity = null;
        initialize();
    }
    
    public MoveTo(Unit owner, Entity e)
    {
        super(owner);
        lastDestination = null;
        targetEntity = e;
        initialize();
    }
    
    private void initialize()
    {
        this.destinations = new CopyOnWriteArrayList<>();
        tm = new TaskManager();
        collisions = 0;
        stuckWarning = false;
        stuck = false;
        moving = true;
        lastOccupied = false;
        stuckTimer = 0;
        calcPath();
    }
    
    private void calcPath()
    {
        if(lastDestination != null)
        {
            destinations = owner.getMap().getPathFinder().findPath(owner, lastDestination);
            if(!destinations.isEmpty() && lastDestination.getX() != owner.getGridX() && lastDestination.getY() != owner.getGridY())
                tm.addTask(new TimerTask(1500, () -> {calcPath();}));
        } 
        
        if(targetEntity != null) {
            destinations = owner.getMap().getPathFinder().findPath(owner, owner.getMap().getClosestCell(owner, targetEntity));
            tm.addTask(new TimerTask(1500, () -> {calcPath();}));
        }
    }

    @Override
    public void performAction() {
        int x, y;
        tm.update();
        if(destinations.isEmpty())
        {
            if(owner.getActions().contains(this))
                owner.getActions().remove(this);
            
            moving = false;
            return;
        }
        if(destinations.size() > 0)
        {
            x = owner.getX();
            y = owner.getY();
            owner.move(destinations.get(0));
            if(owner.getX() == x && owner.getY() == y)
            {
                stuckWarning = true;
            } else {
                stuckWarning = false;
                stuck = false;
                moving = true;
                stuckTimer = 0;
            }
        }
        
        if(!destinations.isEmpty() && owner.getX() == destinations.get(0).getX() * Game.CELLSIZE && owner.getY() == destinations.get(0).getY() * Game.CELLSIZE)
        {
            destinations.remove(0);
            System.out.println("removing");
        }
        
        if(isCollisionAhead())
        {
            collisions++;
        } else {
            collisions = 0;
        }
        
        if(collisions == COLLISION_CHECK_BUFFER)
        {
            avoid();
            collisions = 0;
        }
        
        if(stuckWarning && !destinations.isEmpty())
        {
            stuckTimer++;
            if(stuckTimer >= STUCK_TIME)
            {
                stuck = true; moving = false;
                stuckTimer = 0;
            }
        }
        
//        if(stuck)
//        {
//            System.out.println("stuck");
//            destinations = owner.getMap().getPathFinder().findPath(owner, lastDestination);
//            stuck = false;
//            moving = true;
//        }
    }
    
    @Override
    public void render(Graphics g, Camera camera)
    {
        destinations.forEach((d) -> {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect((int)(d.getX() * Game.CELLSIZE - camera.getOffsetX()), (int)(d.getY() * Game.CELLSIZE - camera.getOffsetY()), Game.CELLSIZE, Game.CELLSIZE);
        });
    }

    private boolean isCollisionAhead() {
        for(int i = 0; i < Math.min(CHECK_AHEAD_DISTANCE, destinations.size()); i++)
        {
            if(owner.getMap().checkCollision(destinations.get(i).getX(), destinations.get(i).getY(), owner))
            {
                Unit u = (Unit)owner.getMap().getEntityFromCell(destinations.get(i).getX(), destinations.get(i).getY());
                if(u != null && movingInSameDirection(owner, u))
                {
                    return false;
                } else {
                    System.out.println("Not moving in same direction");
                    return true;
                }
            }
        }
        return false;
    }

    private void avoid() {
        for(int i = 0; i < Math.max(0, destinations.size() - 1); i++)
        {
            if(owner.getMap().checkCollision(destinations.get(i).getX(), destinations.get(i).getY(), owner))
            {
                System.out.println("recalculating?");
                destinations.set(i, owner.getMap().getAvailableNeighborCell(owner.getMap().getEntityFromCell(destinations.get(i).getX(), destinations.get(i).getY())));
            }
        }
        collisions = 0;
    }

    public boolean arrived() {
        return destinations.isEmpty();
    }
    
    public boolean isTargetOccupied()
    {
        return lastOccupied;
    }

    private boolean movingInSameDirection(Unit owner, Unit u) {
        int moveOwner = (int)(owner.getDeltaX() + owner.getDeltaY());
        int moveOther = (int)(u.getDeltaX() + u.getDeltaY());
        if (moveOwner == 0 || moveOther == 0) {
            return false;
        } else return (Integer.MIN_VALUE & moveOther) == (moveOwner & Integer.MIN_VALUE);
    }
    
}
