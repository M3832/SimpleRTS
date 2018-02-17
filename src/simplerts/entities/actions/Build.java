/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Graphics;
import simplerts.display.Camera;
import simplerts.entities.units.Builder;
import simplerts.entities.Building;
import simplerts.messaging.ErrorMessage;

/**
 *
 * @author Markus
 */
public class Build extends Action {
    
    private Building building;

    public Build(Builder owner, Building building) {
        super(owner);
        this.building = building;
    }

    @Override
    public void performAction() {
        if(owner.getPlayer().hasGoldFor(building))
        {
            owner.getPlayer().pay(building.getGoldCost());
            Building newBuilding = building.build((Builder)owner);
            owner.getMap().addEntity(newBuilding);
            owner.removeAction(this);
            owner.getPlayer().getHandler().game.controller.selectEntity(newBuilding);
        } else {
            owner.getPlayer().getHandler().game.mm.addMessage(new ErrorMessage("Not enough gold!"));
            owner.getActions().remove(this);
        }
    }
    
    public void render(Graphics g, Camera camera)
    {
        
    }
    
}
