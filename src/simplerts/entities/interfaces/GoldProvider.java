/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.interfaces;

/**
 *
 * @author Markus
 */
public interface GoldProvider {
    public boolean enterGatherer(Goldminer g);
    public void exitGatherer();
}
