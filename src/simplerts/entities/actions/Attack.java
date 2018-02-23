/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Color;
import java.awt.Graphics;
import simplerts.display.Camera;
import simplerts.entities.interfaces.Attacker;
import simplerts.entities.Entity;
import simplerts.entities.Unit;
import simplerts.map.Destination;

/**
 *
 * @author Markus
 */
public class Attack extends Action {
    
    public static final Color ATTACK_COLOR = new Color(120, 0, 0);
    
    private Entity target;
    private Follow follow;
    
    public Attack(Unit owner, Entity target) {
        super(owner);
        this.target = target;
        follow = new Follow(owner, target);
        System.out.println("Attacking?");
    }

    @Override
    public void performAction() {
        int deltaX = Math.abs(owner.getMap().getClosestCell(owner, target).getGridX() - owner.getGridX());
        int deltaY = Math.abs(owner.getMap().getClosestCell(owner, target).getGridY() - owner.getGridY());
        if(deltaX < ((Attacker)owner).getRange() && deltaY < ((Attacker)owner).getRange() && !target.isDead() && owner.inSquare())
        {
            ((Attacker)owner).attack(target);
        } else {
            if(!target.isDead())
                follow.performAction();
        }
        
        if(target.isDead())
        {
            owner.getPlayer().deselectEntity(target);
            owner.removeAction(this);
        }
    }

    @Override
    public void render(Graphics g, Camera camera) {
        g.setColor(ATTACK_COLOR);
        g.drawRect(target.getX() - (int)camera.getOffsetX(), target.getY() - (int)camera.getOffsetY(), target.getWidth(), target.getHeight());
    }
    
    @Override
    public boolean isMoving()
    {
        return follow.isMoving();
    }
    
}
