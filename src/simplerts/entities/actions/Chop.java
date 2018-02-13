/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Graphics;
import simplerts.display.Camera;
import simplerts.entities.resources.Forest;
import simplerts.entities.Unit;
import simplerts.entities.interfaces.Lumberman;
import simplerts.map.Cell;
import simplerts.map.Destination;
import simplerts.utils.Utils;

/**
 *
 * @author Markus
 */
public class Chop extends Action {

    private Cell cell;
    private MoveTo movePath;
    private long nextChop;
    private boolean chopping;
    
    public Chop(Unit owner, Cell cell) {
        super(owner);
        this.cell = cell;
    }

    @Override
    public void performAction() {
        if(!(owner instanceof Lumberman) || cell.getForest() == null)
        {
            owner.getActions().remove(this);
            return;
        }
        
        if(movePath != null)
        {
            movePath.performAction();
            chopping = false;
        } else {
            movePath = new MoveTo(owner, owner.getMap().getClosestCell(owner, new Destination(cell.getX(), cell.getY())));
        }
        
        if(Utils.isAdjacent(owner.getDestination(), new Destination(cell.getX(), cell.getY())) && !movePath.moving)
        {
            chopping = true;
            if(nextChop < System.currentTimeMillis())
            {
                Lumberman l = (Lumberman)owner;
                Forest f = cell.getForest();
                owner.setDirection(cell.getX() - owner.getGridX(), cell.getY() - owner.getGridY());
                
                if(!f.isBarren())
                {
                    l.chop();
                    l.receiveLumber(f.chop());                    
                } else {
                    owner.addAction(new Chop(owner, owner.getMap().findLumberCloseTo(owner.getDestination(), 1)));
                    owner.removeAction(this);
                }
                nextChop = System.currentTimeMillis() + l.getChopSpeed();
                if(l.getLumber() >= l.getLumberCapacity())
                {
                    owner.addAction(new TurnInLumber(owner));
                    ((Lumberman)owner).setLatestForestDestination(new Destination(owner.getGridX(), owner.getGridY()));
                    owner.removeAction(this);
                }
            }
        }
        if(movePath.stuck)
        {
            cell = owner.getMap().findLumberCloseTo(owner.getDestination(), 1);
            movePath = new MoveTo(owner, new Destination(cell.getX(), cell.getY()));
        }
    }

    @Override
    public void render(Graphics g, Camera camera) {
        if(movePath != null)
            movePath.render(g, camera);
    }
    
    @Override
    public boolean isMoving()
    {
        if(movePath != null)
        {
            return movePath.isMoving();
        }
        return false;
    }
    
    public boolean isChopping()
    {
        return chopping;
    }
    
}
