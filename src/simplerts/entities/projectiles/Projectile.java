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
    protected int x, y;
    protected Entity owner, target;
    protected Destination targetDestination, startDestination;
    
    public Projectile(Attacker owner, Entity target)
    {
        this.owner = (Entity)owner;
        this.target = target;
        this.targetDestination = target.getDestination();
        this.startDestination = this.owner.getDestination();
        this.x = this.owner.getX();
        this.y = this.owner.getY();
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
        x = (int)((((float)targetDestination.getGridX() * Game.CELLSIZE) - ((float)startDestination.getGridX() * Game.CELLSIZE)) * ((float)currentUpdate/tempSpeed)) + startDestination.getGridX() * Game.CELLSIZE;
        y = (int)((((float)targetDestination.getGridY() * Game.CELLSIZE) - ((float)startDestination.getGridY() * Game.CELLSIZE)) * ((float)currentUpdate/tempSpeed)) + startDestination.getGridY() * Game.CELLSIZE;
        
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
