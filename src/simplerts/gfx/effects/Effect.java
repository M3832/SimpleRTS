/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.gfx.effects;

import java.awt.Graphics;
import simplerts.Game;
import simplerts.display.Camera;
import simplerts.gfx.Animation;
import simplerts.map.Destination;

/**
 *
 * @author Markus
 */
public abstract class Effect {
    
    protected boolean remove;
    protected int x, y;
    protected long timeAlive;
    protected String name;
    protected Animation animation;
    
    public Effect(Destination d){
        x = d.getX();
        y = d.getY();
    }
    
    public Effect(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public void render(Graphics g, Camera c){
        g.drawImage(animation.getCurrentFrame(0), x - (int)c.getOffsetX(), y - (int)c.getOffsetY(), Game.CELLSIZE, Game.CELLSIZE, null);
    }
    
    public void update(){
        animation.update();
        if(timeAlive != -1 && System.currentTimeMillis() > timeAlive){
            remove = true;
        }
    }
    
    public boolean timeToRemove(){
        return remove;
    }
}
