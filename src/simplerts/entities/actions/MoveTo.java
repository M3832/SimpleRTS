/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Color;
import simplerts.Destination;
import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.entities.Entity;
import simplerts.entities.Unit;

/**
 *
 * @author Markus
 */
public class MoveTo extends Action {
    
    public static final int CHECK_AHEAD_DISTANCE = 1;
    public static final int COLLISION_CHECK_BUFFER = 2;
    
    protected CopyOnWriteArrayList<Destination> destinations;
    protected int collisions = 0;

    public MoveTo(Unit owner, CopyOnWriteArrayList<Destination> destinations) {
        super(owner);
        this.destinations = destinations;
    }

    @Override
    public void performAction() {
        if(destinations.isEmpty())
        {
            if(owner.getActions().contains(this))
                owner.getActions().remove(this);
            return;
        }
        if(destinations.size() > 0)
        {
            owner.move(destinations.get(0));
        }
        
        if(owner.getDeltaX() == 0 && owner.getDeltaY() == 0)
        {
            destinations.remove(0);
        }
        
        if(isCollisionAhead())
        {
            collisions++;
            System.out.println(collisions);
        } else {
            collisions = 0;
        }
        
        if(collisions == COLLISION_CHECK_BUFFER)
            avoid();
    }
    
    @Override
    public void render(Graphics g)
    {
        destinations.forEach((d) -> {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect((int)(d.getX() * Game.CELLSIZE - owner.getMap().getHandler().getCamera().getOffsetX()), (int)(d.getY() * Game.CELLSIZE - owner.getMap().getHandler().getCamera().getOffsetY()), Game.CELLSIZE, Game.CELLSIZE);
        });
    }

    private boolean isCollisionAhead() {
        int deltaX = (int)owner.getDeltaX();
        int deltaY = (int)owner.getDeltaY();
        int directionX = deltaX > 0 ? 1 : -1;
        int directionY = deltaY > 0 ? 1 : -1;
        if(deltaX == 0) directionX = 0;
        if(deltaY == 0) directionY = 0;
        for(int i = 0; i < Math.min(CHECK_AHEAD_DISTANCE, destinations.size()); i++)
        {
            Entity e = owner.getMap().getEntityFromCell(destinations.get(i).getX(), destinations.get(i).getY());
            if(e != null && e instanceof Unit)
            {
                int deltaX1 = (int)((Unit)e).getDeltaX();
                int deltaY1 = (int)((Unit)e).getDeltaY();
                int directionX1 = deltaX1 > 0 ? 1 : -1;
                int directionY1 = deltaY1 > 0 ? 1 : -1;
                if(directionX * -1 == directionX1 || directionY * -1 == directionY1 || (deltaX1 == 0 && deltaY1 == 0))
                    return true;
            }
        }
        return false;
    }

    private void avoid() {
        int deltaX = (int)owner.getDeltaX();
        int deltaY = (int)owner.getDeltaY();
        if(deltaX != 0)
        {
            int direction = deltaX > 0 ? 1 : -1;
            if(destinations.size() > 1)
                direction = destinations.get(1).getY() > owner.getGridY() ? 1 : -1;
            for(int i = 0; i < Math.min(CHECK_AHEAD_DISTANCE + 1, destinations.size() - 1); i++)
            {
                if(!owner.getMap().checkCollision(destinations.get(i).getX(), destinations.get(i).getY() + 1 * direction))
                {
                    destinations.get(i).add(0, 1 * direction);
                } else if (!owner.getMap().checkCollision(destinations.get(i).getX(), destinations.get(i).getY() + 1 * -direction))
                {
                    destinations.get(i).add(0, 1 * -direction);
                }
            }
            
            if(destinations.size() == 1)
            {
                destinations.add(0, new Destination(owner.getGridX(), owner.getGridY() + 1 * direction));
            }
            
        } else if (deltaY != 0)
        {
            int direction = deltaY > 0 ? 1 : -1;
            if(destinations.size() > 1)
                direction = destinations.get(1).getX() > owner.getGridX() ? 1 : -1;
            for(int i = 0; i < Math.min(CHECK_AHEAD_DISTANCE + 1, destinations.size() - 1); i++)
            {
                if(!owner.getMap().checkCollision(destinations.get(i).getX() + 1 * direction, destinations.get(i).getY()))
                {
                    destinations.get(i).add(1 * direction, 0);
                } else if (!owner.getMap().checkCollision(destinations.get(i).getX() + 1 * -direction, destinations.get(i).getY()))
                {
                    destinations.get(i).add(1 * -direction, 0);
                }
            }
            if(destinations.size() == 1)
            {
                destinations.add(0, new Destination(owner.getGridX() + 1 * direction, owner.getGridY()));
            }
        }
    }
    
}
