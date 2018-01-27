/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.interfaces;

import simplerts.Destination;

/**
 *
 * @author Markus
 */
public interface Goldminer {
    
    public void setGold(int amount);
    public int takeGold();
    public void enter();
    public void exit(Destination d);
    public void setLatestMine(GoldProvider g);
    public GoldProvider getLatestMine();
    
}
