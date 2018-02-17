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
    protected int speed;
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
        speed = 10;
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
        int deltaX = targetDestination.getX() * Game.CELLSIZE - x;
        int deltaY = targetDestination.getY() * Game.CELLSIZE - y;
        
        if(deltaX > 0)
        {
            x += Math.min(speed, Math.abs(deltaX));
        } else if (deltaX < 0)
        {
            x -= Math.min(speed, Math.abs(deltaX));
        }
//        if(deltaY > 0)
//        {
//            y += Math.min(speed, Math.abs(deltaY));
//        }
//        if(deltaY < 0)
//        {
//            y -= Math.min(speed, Math.abs(deltaY));
//        }
        float xTravelPercentage = deltaX / (((float)targetDestination.getX() * Game.CELLSIZE) - ((float)startDestination.getX() * Game.CELLSIZE));
        System.out.println(xTravelPercentage);
        y = (int)((((float)startDestination.getY() * Game.CELLSIZE) - ((float)targetDestination.getY() * Game.CELLSIZE)) * xTravelPercentage) + targetDestination.getY() * Game.CELLSIZE;
        
        if(deltaX == 0 && deltaY == 0)
            arrive();
        
        gridX = x / Game.CELLSIZE;
        gridY = y / Game.CELLSIZE;
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
