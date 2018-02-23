/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Graphics;
import simplerts.display.Camera;
import simplerts.entities.Unit;
import simplerts.map.Destination;

/**
 *
 * @author marku
 */
public class MoveAttack extends Action {
    
    private final MoveTo moveTo;

    public MoveAttack(Unit owner, Destination destination) {
        super(owner);
        moveTo = new MoveTo(owner, destination);
    }

    @Override
    public void performAction() {
        if(moveTo != null){
            moveTo.performAction();
        }
    }

    @Override
    public void render(Graphics g, Camera camera) {
        moveTo.render(g, camera);
    }
    
    @Override
    public boolean isMoving(){
        return moveTo.isMoving();
    }
    
}
