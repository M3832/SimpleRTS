/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Graphics;
import simplerts.display.Camera;
import simplerts.entities.Entity;
import simplerts.entities.Unit;
import simplerts.entities.interfaces.LumberReceiver;
import simplerts.entities.interfaces.Lumberman;
import simplerts.messaging.ErrorMessage;
import simplerts.utils.TimerTask;
import simplerts.utils.Utils;

/**
 *
 * @author Markus
 */
public class TurnInLumber extends Action {

    private MoveTo movePath;
    private LumberReceiver lr;
    
    public TurnInLumber(Unit owner) {
        super(owner);
    }

    @Override
    public void performAction() {
        if(!(owner instanceof Lumberman))
        {
            owner.getActions().remove(this);
            return;
        }
        
        
        if(movePath != null)
        {
            movePath.performAction();
        } else {
            for(Entity e : owner.getPlayer().getEntities())
            {
                if(e instanceof LumberReceiver)
                {
                    lr = (LumberReceiver)e;
                    break;
                }
            }
            if(lr != null)
            {
                movePath = new MoveTo(owner, (Entity)lr);
            } else {
                owner.getPlayer().getHandler().game.mm.addMessage(new ErrorMessage("There's nowhere to turn in lumber."));
                owner.getActions().remove(this);
                return;
            }
        }
        
        if(lr != null && !movePath.isMoving() && Utils.isAdjacent(owner, (Entity)lr))
        {
            lr.receiveLumber(((Lumberman)owner).takeLumber());
            ((Lumberman)owner).enter();
            owner.addTask(new TimerTask(1000, () -> {
                ((Lumberman)owner).exit(owner.getDestination());
                if(((Lumberman)owner).getLatestForestDestination() != null)
                {
                    owner.addAction(new Chop(owner, owner.getMap().findLumberCloseTo(((Lumberman)owner).getLatestForestDestination(), 1)));
                }
            }));
            owner.clearActions();
        }
    }

    @Override
    public void render(Graphics g, Camera camera)
    {
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
    
}
