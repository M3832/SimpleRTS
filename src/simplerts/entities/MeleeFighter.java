/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

/**
 *
 * @author Markus
 */
public class MeleeFighter extends Unit {

    @Override
    public Entity duplicate() {
        return new MeleeFighter();
    }
    
}
