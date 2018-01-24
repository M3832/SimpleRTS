/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import simplerts.Player;
import simplerts.display.Assets;
import simplerts.ui.UIAction;

/**
 *
 * @author Markus
 */
public class TownHall extends Building {
    
    public TownHall(int x, int y, int gridSize, Player player) {
        super(x, y, gridSize, player);
        sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/townhall.png"),
                                                     Assets.loadToCompatibleImage("/townhalltc.png"), color);
    }
    
    @Override
    public void setupActions()
    {
        super.setupActions();
        actions.add(new UIAction(777, 24, Assets.loadAndResizeImage("/townhall.png", 55, 55), () -> {player.handler.map.addEntity(new Builder(gridX, gridY, player));}));
    }
    
}
