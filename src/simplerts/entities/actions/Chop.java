/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Graphics;
import simplerts.entities.Forest;
import simplerts.entities.Unit;
import simplerts.entities.interfaces.Goldminer;
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
    
    public Chop(Unit owner, Cell cell) {
        super(owner);
        this.cell = cell;
    }

    @Override
    public void performAction() {
        if(!(owner instanceof Goldminer))
        {
            owner.getActions().remove(this);
            return;
        }
        
        if(cell.getForest().isBarren())
        {
            Cell tempCell = owner.getMap().findLumberCloseTo(new Destination(cell.getX(), cell.getY()));
            if(cell == tempCell)
            {
                owner.removeAction(this);
            } else {
                cell = tempCell;
            }
        }
        
        if(movePath != null && !movePath.stuck)
        {
            movePath.performAction();
        } else {
            movePath = new MoveTo(owner, owner.getMap().getPathFinder().findPath(owner.getDestination(), new Destination(cell.getX(), cell.getY())));
        }
        
        if(Utils.isAdjacent(owner.getDestination(), new Destination(cell.getX(), cell.getY())) && !movePath.moving)
        {
            if(nextChop < System.currentTimeMillis())
            {
                Lumberman l = (Lumberman)owner;
                Forest f = cell.getForest();
                
                if(!f.isBarren())
                {
                    l.chop();
                    l.receiveLumber(f.chop());                    
                } else {
//                    movePath = new MoveTo(owner, owner.getMap().)
                }
                nextChop = System.currentTimeMillis() + l.getChopSpeed();
                if(l.getLumber() == l.getLumberCapacity())
                {
                    owner.addAction(new TurnInLumber(owner));
                    ((Lumberman)owner).setLatestForestDestination(new Destination(cell.getX(), cell.getY()));
                    owner.removeAction(this);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if(movePath != null)
            movePath.render(g);
    }
    
}
