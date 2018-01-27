/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import simplerts.Player;
import simplerts.gfx.Assets;
import simplerts.ui.UIAction;

/**
 *
 * @author Markus
 */
public class Tower extends Building{
    
    private static int GOLDCOST = 50;
    
    public Tower(int x, int y, int gridSize, Player player)
    {
        super(x, y, gridSize, player, false);
        sprite = Assets.makeTeamColor(Assets.loadAndResizeImage("/tower.png", width, height),
                                                     Assets.loadAndResizeImage("/towerteamcolor.png", width, height), color);
        goldCost = GOLDCOST;
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
    
    public static BufferedImage getUIIcon(Color color)
    {
        BufferedImage sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/tower.png"),
                                                     Assets.loadToCompatibleImage("/towerteamcolor.png"), color);
        return Assets.makeIcon(color, sprite);
    }
    
    public static UIAction getUIAction(Player player)
    {
        UIAction a = new UIAction(Assets.resizeImage(Tower.getUIIcon(player.getColor()), 55, 55), () -> {player.getHandler().game.controller.setEntityPlacerEntity(new Tower(0, 0, 2, player));});
        a.setTitle("Tower");
        a.setGoldCost(GOLDCOST + "");
        a.setLumberCost("25");
        return a;
    }
    
    
}
