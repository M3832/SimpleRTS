/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.interfaces;

import simplerts.entities.Entity;

/**
 *
 * @author Markus
 */
public interface Attacker {
    
    public void attack(Entity e);
    public int getRange();
    public int getDamage();
    
}
