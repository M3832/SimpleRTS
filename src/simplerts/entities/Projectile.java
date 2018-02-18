/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Graphics;
import simplerts.Game;
import simplerts.display.Camera;
import simplerts.entities.interfaces.Attacker;
import simplerts.gfx.AnimationController;
import simplerts.map.Destination;
import simplerts.utils.Utilities;

/**
 *
 * @author Markus
 */
public abstract class Projectile extends Entity {
    
    protected AnimationController ac;
    protected int speed, currentUpdate;
    protected int damage;
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
        this.gridX = this.owner.getGridX();
        this.gridY = this.owner.getGridY();
        player = this.owner.getPlayer();
        speed = 1000;
        currentUpdate = 3;
        damage = owner.getDamage();
    }
    
    @Override
    public void render(Graphics g, Camera c)
    {
        g.drawImage(ac.getCurrentFrame(), x - (int)c.getOffsetX(), y - (int)c.getOffsetY(), null);
    }
    
    @Override
    public void update()
    {
        float tempSpeed = speed*Math.max(Math.abs(owner.getGridX() - target.getGridX()), Math.abs(owner.getGridY() - target.getGridY()));
        x = (int)((((float)targetDestination.getGridX() * Game.CELLSIZE) - ((float)startDestination.getGridX() * Game.CELLSIZE)) * ((float)currentUpdate/tempSpeed)) + startDestination.getGridX() * Game.CELLSIZE;
        y = (int)((((float)targetDestination.getGridY() * Game.CELLSIZE) - ((float)startDestination.getGridY() * Game.CELLSIZE)) * ((float)currentUpdate/tempSpeed)) + startDestination.getGridY() * Game.CELLSIZE;
        
        if(currentUpdate == tempSpeed)
            arrive();
        
        gridX = x / Game.CELLSIZE;
        gridY = y / Game.CELLSIZE;
        currentUpdate++;
    }
    
    protected void arrive()
    {
        if(!target.isDead())
        {
            target.hit(damage);
        }
        remove = true;
    }
    
    
    
}
