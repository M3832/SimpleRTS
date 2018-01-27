/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import simplerts.entities.Unit;

/**
 *
 * @author Markus
 */
public abstract class Action {
    
    protected Unit owner;
    
    public Action(Unit owner)
    {
        this.owner = owner;
    }
    
    public abstract void performAction();
    
}
