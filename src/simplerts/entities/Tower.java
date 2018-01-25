/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import simplerts.Player;
import simplerts.display.Assets;

/**
 *
 * @author Markus
 */
public class Tower extends Building{
    
    public Tower(int x, int y, int gridSize, Player player)
    {
        super(x, y, gridSize, player, false);
        color = player.getColor();
        sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/tower.png"),
                                                     Assets.loadToCompatibleImage("/towerteamcolor.png"), color);
        this.player = player;
        setupActions();
    }
    
    public Tower(int x, int y, int gridSize, Player player, boolean built)
    {
        this(x, y, gridSize, player);
        if(built) setBuilt();
    }

    @Override
    public Entity duplicate() {
        return new Tower(gridX, gridY, gridWidth, player);
    }
    
    @Override
    public void setupActions()
    {
        super.setupActions();
    }
    
    
}
