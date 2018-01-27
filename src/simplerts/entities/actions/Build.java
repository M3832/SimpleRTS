/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import simplerts.entities.Builder;
import simplerts.entities.Building;

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
            System.out.println(owner.getPlayer().getGold());
            Building newBuilding = building.build((Builder)owner);
            owner.getMap().addEntity(newBuilding);
            owner.removeAction(this);
            owner.getPlayer().getHandler().game.controller.selectEntity(newBuilding);
        }
    }
    
}
