/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.actions;

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
    private int waittime = 2000;
    private MoveTo movePath;
    
    public Follow(Unit owner, Entity target) {
        super(owner);
        this.target = target;
    }

    @Override
    public void performAction() {
        if(nextRepathing < System.currentTimeMillis() && (Math.abs(owner.getGridX() - target.getGridX()) > 0 && Math.abs(owner.getGridY() - target.getGridY()) > 0))
        {
            System.out.println("repathing");
            movePath = new MoveTo(owner, owner.getMap().getPathFinder().findPath(owner.getDestination(), getClosestCell(), true));
            nextRepathing = System.currentTimeMillis() + waittime;
        }
        
        if(movePath != null)
            movePath.performAction();
    }
    
    private Destination getClosestCell()
    {
        Integer[][] destinations = new Integer[target.getGridWidth() + 2][target.getGridHeight() + 2];
        
        for(int x = target.getGridX() - 1; x < target.getGridX() + target.getGridWidth() + 1; x++)
        {
            for(int y = target.getGridY() - 1; y < target.getGridY() + target.getGridHeight() + 1; y++)
            {
                destinations[x - target.getGridX() + 1][y - target.getGridY() + 1] = Math.abs(owner.getGridX() - x) + (Math.abs(owner.getGridY() - y));
            }
        }
        int indexX = 0, indexY = 0, score = 100;
        for(int x = 0; x < destinations.length; x++)
        {
            for(int y = 0; y < destinations[0].length; y++)
            {
                System.out.println("X: " + x + " Y: " + y + " Blocked: " + owner.getMap().checkCollision(target.getGridX() - 1 + x, target.getGridY() - 1 + y));
                if(destinations[x][y] < score && !owner.getMap().checkCollision(target.getGridX() - 1 + x, target.getGridY() - 1 + y))
                {
                    score = destinations[x][y];
                    indexX = x;
                    indexY = y;
                    System.out.println("asdojasfobn");
                } else {
                }
            }
        }
        return new Destination(target.getGridX() - 1 + indexX, target.getGridY() - 1 + indexY);
    }
    
}
