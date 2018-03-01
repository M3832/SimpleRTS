/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.projectiles;

import java.awt.Graphics;
import simplerts.Game;
import simplerts.display.Camera;
import simplerts.entities.Entity;
import simplerts.entities.interfaces.Attacker;
import simplerts.entities.interfaces.Ranger;
import simplerts.gfx.AnimationController;
import simplerts.map.Destination;
import simplerts.utils.Utilities;

/**
 *
 * @author Markus
 */
public abstract class Projectile {
    
    protected AnimationController ac;
    protected int speed, currentUpdate;
    protected int damage;
    protected int x, y, targetX, targetY, startX, startY;
    protected Entity owner, target;
    public Projectile(Attacker owner, Entity target)
    {
        this.owner = (Entity)owner;
        this.target = target;
        this.x = this.owner.getX();
        this.y = this.owner.getY();
        startX = this.owner.getX();
        startY = this.owner.getY();
        targetX = target.getX() + target.getWidth()/2 - Game.CELLSIZE/2;
        targetY = target.getY() + target.getHeight()/2 - Game.CELLSIZE/2;
        speed = 1000;
        currentUpdate = 3;
        damage = owner.getDamage();
    }
    
    public void render(Graphics g, Camera c)
    {
        g.drawImage(ac.getCurrentFrame(), x - (int)c.getOffsetX(), y - (int)c.getOffsetY(), null);
    }
    
    public void update()
    {
        float tempSpeed = speed*Math.max(Math.abs(owner.getGridX() - target.getGridX()), Math.abs(owner.getGridY() - target.getGridY()));
        x = (int)((((float)targetX) - ((float)startX)) * ((float)currentUpdate/tempSpeed)) + startX;
        y = (int)((((float)targetY) - ((float)startY)) * ((float)currentUpdate/tempSpeed)) + startY;
        
        if(currentUpdate == tempSpeed)
            arrive();
        
        currentUpdate++;
    }
    
    protected void arrive()
    {
        if(!target.isDead())
        {
            target.hit(damage);
        }
        ((Ranger)owner).removeProjectile(this);
    }
    
    
    
}
