/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Graphics;
import simplerts.audio.SoundController;
import simplerts.display.Camera;
import simplerts.entities.units.Builder;
import simplerts.entities.Building;
import simplerts.map.Destination;
import simplerts.messaging.ErrorMessage;

/**
 *
 * @author Markus
 */
public class Build extends Action {
    
    private final Building building;
    private final MoveTo moveTo;

    public Build(Builder owner, Building building) {
        super(owner);
        this.building = building;
        owner.playSound(SoundController.CONFIRM);
        moveTo = new MoveTo(owner, new Destination(building.getGridX() + building.getGridWidth()/2, building.getGridY() + building.getGridHeight()/2));
    }

    @Override
    public void performAction() {
        if(moveTo.arrived())
        {
            if(owner.getPlayer().hasGoldFor(building))
            {
                owner.getPlayer().pay(building.getGoldCost());
                Building newBuilding = building.build((Builder)owner);
                owner.getMap().addEntity(newBuilding);
                owner.removeAction(this);
                owner.getPlayer().getHandler().game.controller.clearSelection();
            } else {
                owner.getPlayer().getHandler().game.mm.addMessage(new ErrorMessage("Not enough gold!"));
                owner.getActions().remove(this);
            }            
        } else {
            moveTo.performAction();
        }
    }
    
    @Override
    public void render(Graphics g, Camera camera){}
    
    @Override
    public boolean isMoving(){
        return moveTo.isMoving();
    }
}
