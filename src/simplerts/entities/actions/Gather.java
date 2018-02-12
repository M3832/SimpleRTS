/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Color;
import java.awt.Graphics;
import simplerts.utils.Utils;
import simplerts.entities.Entity;
import simplerts.entities.Unit;
import simplerts.entities.interfaces.GoldProvider;
import simplerts.entities.interfaces.Goldminer;

/**
 *
 * @author Markus
 */
public class Gather extends Action {

    private GoldProvider mine;
    private MoveTo movePath;
    
    public Gather(Unit owner, GoldProvider mine) {
        super(owner);
        this.mine = mine;
    }

    @Override
    public void performAction() {
        if(!(owner instanceof Goldminer))
        {
            owner.getActions().remove(this);
            return;
        }
        
        if(movePath != null && !movePath.stuck)
        {
            movePath.performAction();
        } else {
            movePath = new MoveTo(owner, owner.getMap().getPathFinder().findPath(owner.getDestination(), owner.getMap().getClosestCell(owner, (Entity)mine)));
        }
        
        if(Utils.isAdjacent(owner, (Entity)mine))
        {
            if(mine.enterGatherer((Goldminer)owner))
                owner.getActions().clear();
        }
    }
    
    public void render(Graphics g)
    {
        if(movePath != null)
            movePath.render(g);
        
        g.setColor(Color.yellow);
        g.drawRect(((Entity)mine).getX() - (int)owner.getPlayer().getController().getCamera().getOffsetX(),
                   ((Entity)mine).getY() - (int)owner.getPlayer().getController().getCamera().getOffsetY(),
                   ((Entity)mine).getWidth(), ((Entity)mine).getHeight());
    }
    
    @Override
    public boolean isMoving()
    {
        return movePath.isMoving();
    }
    
}
