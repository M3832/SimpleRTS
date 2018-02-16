/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Color;
import java.awt.Graphics;
import simplerts.display.Camera;
import simplerts.utils.Utilities;
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
            movePath = new MoveTo(owner, (Entity)mine);
        }
        
        if(Utilities.isAdjacent(owner, (Entity)mine) && !movePath.isMoving())
        {
            if(mine.enterGatherer((Goldminer)owner))
                owner.getActions().clear();
        }
    }
    
    @Override
    public void render(Graphics g, Camera camera)
    {
        if(movePath != null)
            movePath.render(g, camera);
        
        g.setColor(Color.yellow);
        g.drawRect(((Entity)mine).getX() - (int)camera.getOffsetX(),
                   ((Entity)mine).getY() - (int)camera.getOffsetY(),
                   ((Entity)mine).getWidth(), ((Entity)mine).getHeight());
    }
    
    @Override
    public boolean isMoving()
    {
        return movePath.isMoving();
    }
    
}
