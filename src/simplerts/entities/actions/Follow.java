/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Graphics;
import simplerts.entities.Entity;
import simplerts.entities.Unit;

/**
 *
 * @author Markus
 */
public class Follow extends Action {

    private final Entity target;
    private long nextRepathing;
    private final int waittime = 1000;
    private MoveTo movePath;
    
    public Follow(Unit owner, Entity target) {
        super(owner);
        this.target = target;
    }

    @Override
    public void performAction() {
        if(nextRepathing < System.currentTimeMillis() && (Math.abs(owner.getGridX() - target.getGridX()) > 1 || Math.abs(owner.getGridY() - target.getGridY()) > 1))
        {
//            movePath = new MoveTo(owner, owner.getMap().getPathFinder().findPath(owner.getDestination(), owner.getMap().getClosestCell(owner, target), true));
            movePath = new MoveTo(owner, owner.getMap().getPathFinder().findPath(owner.getDestination(), owner.getMap().getClosestCell(owner, target)));
            nextRepathing = System.currentTimeMillis() + waittime;
        }
        
        if(movePath != null)
            movePath.performAction();
    }
    
    @Override
    public void render(Graphics g)
    {
        if(movePath != null)
            movePath.render(g);
        
        g.setColor(owner.getPlayer().getController().getColorFromAllegiance(target));
        g.drawRect(target.getX() - (int)owner.getPlayer().getHandler().getCamera().getOffsetX(), target.getY() - (int)owner.getPlayer().getHandler().getCamera().getOffsetY(), target.getWidth(), target.getHeight());
    }
    
    @Override
    public boolean isMoving()
    {
        if(movePath != null)
            return movePath.isMoving();
        
        return false;
    }
}
