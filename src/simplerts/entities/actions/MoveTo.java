/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import simplerts.Destination;
import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.entities.Unit;

/**
 *
 * @author Markus
 */
public class MoveTo extends Action {
    
    protected CopyOnWriteArrayList<Destination> destinations;
    protected int repaths = 0;
    protected long nextRePathing = 0;
    protected int repathingCooldown = 3000;

    public MoveTo(Unit owner, CopyOnWriteArrayList<Destination> destinations) {
        super(owner);
        this.destinations = destinations;
        nextRePathing = System.currentTimeMillis() + repathingCooldown;
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
            owner.setCollided(false);
        }
        
        if(owner.getTempX() == owner.getX() && owner.getTempY() == owner.getY())
        {
            findNewPath();
        }
        
    }
    
    private void findNewPath()
    {
        if(repaths > 0)
        {
            destinations = owner.getMap().getPathFinder().findPath(new Destination(owner.getGridX(), owner.getGridY()), destinations.get(destinations.size() - 1), true);
            repaths = 0;
            return;
        }
        
        if(System.currentTimeMillis() > nextRePathing && destinations.size() > 1)
        {
//            destinations = map.getPathFinder().findPath(new Destination(gridX, gridY), destinations.get(destinations.size() - 1), (++repaths > 3));
            destinations.get(0).setX(destinations.get(0).getX() + (1 * (owner.getDeltaY() > 0 ? 1 : -1)) * (owner.getDeltaY() == 0 ? 0 : 1));
            destinations.get(0).setY(destinations.get(0).getY() + (1 * (owner.getDeltaX() > 0 ? 1 : -1)) * (owner.getDeltaX() == 0 ? 0 : 1));
            nextRePathing = System.currentTimeMillis() + repathingCooldown;
            repaths++;
        } else if (System.currentTimeMillis() > nextRePathing && destinations.size() == 1)
        {
            int x = destinations.get(0).getX() + 1 * (owner.getDeltaY() > 0 ? 1 : -1) * (owner.getDeltaY() == 0 ? 0 : 1);
            int y = destinations.get(0).getY() + 1 * (owner.getDeltaX() > 0 ? 1 : -1) * (owner.getDeltaX() == 0 ? 0 : 1);
            destinations.add(0, new Destination(x, y));
            nextRePathing = System.currentTimeMillis() + repathingCooldown;
            repaths++;
        }
    }
    
    public void render(Graphics g)
    {
        destinations.forEach((d) -> {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect((int)(d.getX() * Game.CELLSIZE - owner.getMap().getHandler().getCamera().getOffsetX()), (int)(d.getY() * Game.CELLSIZE - owner.getMap().getHandler().getCamera().getOffsetY()), Game.CELLSIZE, Game.CELLSIZE);
        });
    }
    
}
