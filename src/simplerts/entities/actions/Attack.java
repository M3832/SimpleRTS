/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Color;
import java.awt.Graphics;
import simplerts.entities.interfaces.Attacker;
import simplerts.entities.Entity;
import simplerts.entities.Unit;

/**
 *
 * @author Markus
 */
public class Attack extends Action {
    
    private Entity target;
    private Follow follow;
    
    public Attack(Unit owner, Entity target) {
        super(owner);
        this.target = target;
        follow = new Follow(owner, target);
    }

    @Override
    public void performAction() {
        int deltaX = Math.abs(target.getGridX() - owner.getGridX());
        int deltaY = Math.abs(target.getGridY() - owner.getGridY());
        if(deltaX <= ((Attacker)owner).getRange() && deltaY <= ((Attacker)owner).getRange() && !follow.isMoving() && !target.isDead())
        {
            ((Attacker)owner).attack(target);
        } else {
            follow.performAction();
        }
        
        if(target.isDead())
        {
            owner.getPlayer().deselectEntity(target);
            owner.removeAction(this);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(target.getX() - (int)owner.getPlayer().getController().getCamera().getOffsetX(), target.getY() - (int)owner.getPlayer().getController().getCamera().getOffsetY(), target.getWidth(), target.getHeight());
    }
    
    @Override
    public boolean isMoving()
    {
        return follow.isMoving();
    }
    
}
