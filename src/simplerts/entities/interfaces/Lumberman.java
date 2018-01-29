/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.interfaces;

import simplerts.map.Destination;

/**
 *
 * @author Markus
 */
public interface Lumberman {
    
    public void receiveLumber(int lumber);
    public int getLumber();
    public int takeLumber();
    public int getLumberCapacity();
    public int getChopSpeed();
    public void enter();
    public void exit(Destination d);
    public void setLatestForestDestination(Destination d);
    public Destination getLatestForestDestination();
    public void chop();
    
}
