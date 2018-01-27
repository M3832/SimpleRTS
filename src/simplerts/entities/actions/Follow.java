/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.entities.Entity;
import simplerts.entities.Unit;

/**
 *
 * @author Markus
 */
public class Follow extends Action {

    private Entity target;
    private long nextRepathing;
    private int waittime = 500;
    private MoveTo movePath;
    
    public Follow(Unit owner, Entity target) {
        super(owner);
        this.target = target;
    }

    @Override
    public void performAction() {
        if(nextRepathing < System.currentTimeMillis() && (Math.abs(owner.getGridX() - target.getGridX()) > 1 || Math.abs(owner.getGridY() - target.getGridY()) > 1))
        {
            System.out.println("repathing");
            movePath = new MoveTo(owner, owner.getMap().getPathFinder().findPath(owner.getDestination(), owner.getMap().getClosestCell(owner, target), true));
            nextRepathing = System.currentTimeMillis() + waittime;
        }
        
        if(movePath != null)
            movePath.performAction();
    }
}
