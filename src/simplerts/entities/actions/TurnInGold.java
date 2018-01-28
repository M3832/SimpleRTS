/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Graphics;
import simplerts.Timer;
import simplerts.Utils;
import simplerts.entities.Entity;
import simplerts.entities.Unit;
import simplerts.entities.interfaces.GoldProvider;
import simplerts.entities.interfaces.GoldReceiver;
import simplerts.entities.interfaces.Goldminer;
import simplerts.messaging.ErrorMessage;

/**
 *
 * @author Markus
 */
public class TurnInGold extends Action {

    private MoveTo movePath;
    private GoldReceiver gr;
    
    public TurnInGold(Unit owner) {
        super(owner);
    }

    @Override
    public void performAction() {
        if(!(owner instanceof Goldminer))
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
                if(e instanceof GoldReceiver)
                {
                    gr = (GoldReceiver)e;
                    break;
                }
            }
            if(gr != null)
            {
                movePath = new MoveTo(owner, owner.getMap().getPathFinder().findPath(owner.getDestination(), owner.getMap().getClosestCell(owner, (Entity)gr)));
            } else {
                owner.getPlayer().getHandler().game.mm.addMessage(new ErrorMessage("There's nowhere to turn in gold."));
                owner.getActions().remove(this);
                return;
            }
        }
        
        if(gr != null && Utils.isAdjacent(owner, (Entity)gr))
        {
            gr.receiveGold(((Goldminer)owner));
            ((Goldminer)owner).enter();
            new Timer(1000, () -> {
                ((Goldminer)owner).exit(owner.getMap().getClosestCell((Entity)((Goldminer)owner).getLatestMine(), (Entity)gr));
                if(((Goldminer)owner).getLatestMine() != null)
                {
                    owner.addAction(new Gather(owner, ((Goldminer)owner).getLatestMine()));
                }
            }).start();
            owner.clearActions();
        }
    }

    public void render(Graphics g)
    {
        if(movePath != null)
            movePath.render(g);
    }
    
}
