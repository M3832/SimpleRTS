/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Graphics;
import simplerts.entities.Unit;

/**
 *
 * @author Markus
 */
public abstract class Action {
    
    protected Unit owner;
    protected boolean moving;
    
    public Action(Unit owner)
    {
        this.owner = owner;
        moving = false;
    }
    
    public abstract void performAction();
    public abstract void render(Graphics g);
    public boolean isMoving()
    {
        return moving;
    }
    
}
