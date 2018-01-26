/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.actions;

import simplerts.entities.Builder;
import simplerts.entities.Building;
import simplerts.entities.Entity;
import simplerts.entities.Unit;

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
        System.out.println("Building?");
        Building newBuilding = building.build((Builder)owner);
        owner.getMap().addEntity(newBuilding);
        owner.removeAction(this);
        owner.getPlayer().handler.game.controller.selectEntity(newBuilding);
    }
    
}
