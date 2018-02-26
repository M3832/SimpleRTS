/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Color;
import simplerts.map.Destination;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.display.Camera;
import simplerts.entities.Entity;
import simplerts.entities.Unit;
import simplerts.utils.TaskManager;
import simplerts.utils.TimerTask;
import simplerts.utils.Utilities;

/**
 *
 * @author Markus
 */
public class MoveTo extends Action {
    
    public static final int CHECK_AHEAD_DISTANCE = 1;
    public static final int COLLISION_CHECK_BUFFER = 50;
    public static final long STUCK_TIME = 50;
    
    protected TaskManager tm;
    protected CopyOnWriteArrayList<Destination> destinations;
    protected int collisions;
    protected boolean stuckWarning, stuck, lastOccupied, arrive, finalLastDestination;
    protected long stuckTimer;
    private Destination lastDestination;
    private final Entity targetEntity;

    public MoveTo(Unit owner, Destination d) {
        super(owner);
        lastDestination = d;
        targetEntity = null;
        finalLastDestination = false;
        initialize();
    }
    
    public MoveTo(Unit owner, Destination d, boolean finalLastDestination)
    {
        this(owner, d);
        this.finalLastDestination = finalLastDestination;
    }
    
    public MoveTo(Unit owner, Entity e)
    {
        super(owner);
        targetEntity = e;
        lastDestination = owner.getMap().getClosestCell(owner, e);
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
        if(targetEntity != null) {
            destinations = owner.findPath(owner.getMap().getClosestCell(owner, targetEntity));
            tm.addTask(new TimerTask(1500, () -> {calcPath();}));
            if(!destinations.isEmpty())
                lastDestination = destinations.get(destinations.size()-1);
        } else {
            destinations = owner.findPath(lastDestination);
        }
    }

    @Override
    public void performAction() {
        int x, y;
        tm.update();
        if(owner.getX() == lastDestination.getX() && owner.getY() == lastDestination.getY())
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
        
        if(!destinations.isEmpty() && owner.getX() == destinations.get(0).getX() && owner.getY() == destinations.get(0).getY())
        {
            destinations.remove(0);
            if(destinations.isEmpty())
                moving = false;
        }
        
        if( isCollisionAhead())
        {
            collisions++;
        } else {
            collisions = 0;
        }
        
        if(collisions == (int)(COLLISION_CHECK_BUFFER/owner.getMoveSpeed()))
        {
            avoid();
            collisions = 0;
        }
        
        if(stuck)
        {
            if(targetEntity != null) {
                destinations = owner.findPath(owner.getMap().getClosestCell(owner, targetEntity));
            } else if (lastDestination != null) {
                destinations = owner.findPath(owner.getMap().getClosestCell(owner, lastDestination));
            }
            stuck = false;
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
        
        if(lastDestination != null && !finalLastDestination && destinations.size() < 3 && destinations.size() > 0 && owner.getMap().checkCollision(destinations.get(destinations.size()-1).getGridX(), destinations.get(destinations.size()-1).getGridY(), owner))
        {
            if(targetEntity != null){
                destinations = owner.findPathIncludeUnits(owner.getMap().getClosestCell(owner, targetEntity));
            } else {
                destinations = owner.findPathIncludeUnits(owner.getMap().getClosestCell(owner, owner.getMap().getEntityFromCell(lastDestination.getGridX(), lastDestination.getGridY())));
            }
            if(destinations.size() > 0)
                lastDestination = destinations.get(destinations.size()-1);
        }
                
        if(destinations.isEmpty() && !owner.inSquare())
        {
            destinations.add(owner.getDestination());
        }
        

        
        if(destinations.isEmpty() && (owner.getX() != lastDestination.getX() || owner.getY() != lastDestination.getY()))
        {
            moving = false;
        }
    }
    
    @Override
    public void render(Graphics g, Camera camera)
    {
        if(Game.DEBUG)
        {
            destinations.forEach((d) -> {
                g.setColor(new Color(255, 255, 255, 50));
                g.fillRect((int)(d.getGridX() * Game.CELLSIZE - camera.getOffsetX()), (int)(d.getGridY() * Game.CELLSIZE - camera.getOffsetY()), Game.CELLSIZE, Game.CELLSIZE);
            });
        }
    }

    private boolean isCollisionAhead() {
        for(int i = 0; i < Math.min(CHECK_AHEAD_DISTANCE, destinations.size()); i++)
        {
            if(owner.getMap().checkCollision(destinations.get(i).getGridX(), destinations.get(i).getGridY(), owner))
            {
                Entity e = owner.getMap().getEntityFromCell(destinations.get(i).getGridX(), destinations.get(i).getGridY());
                if(e != null && e instanceof Unit && movingInSameDirection(owner, (Unit)e))
                {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private void avoid() {
        for(int i = 0; i < destinations.size(); i++)
        {
            if(!owner.getMap().checkCollision(destinations.get(i).getGridX(), destinations.get(i).getGridY(), owner))
            {
                destinations = owner.findPathIncludeUnits(destinations.get(destinations.size()-1));
                return;
            }
        }
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
        } else return Utilities.getMovementDirection(moveOther, 0) == Utilities.getMovementDirection(moveOwner, 0);
    }
    
}
