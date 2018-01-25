/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import java.util.ArrayList;
import simplerts.Game;
import simplerts.Player;
import simplerts.display.Assets;
import simplerts.ui.UIAction;

/**
 *
 * @author Markus
 */
public class TownHall extends Building {
    
    public TownHall(int x, int y, int gridSize, Player player) {
        super(x, y, gridSize, player, false);
        sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/Buildings/TownHall/sprite.png"),
                                                     Assets.loadToCompatibleImage("/Buildings/TownHall/teamcolor.png"), color);
        icon = Assets.makeIcon(color, Assets.resizeImage(sprite.getSubimage(width * (int)(1 * (sprite.getWidth()/width - 1)), 0, width, height), 100, 100));
        uiObjects = new ArrayList<>();
        uiActions = new ArrayList<>();
    }
    
    public TownHall(int x, int y, int gridSize, Player player, boolean built)
    {
        this(x, y, gridSize, player);
        if(built) setBuilt();
        setupActions();
    }
    
    @Override
    public void setupActions()
    {
        super.setupActions();
        uiActions.add(new UIAction(Assets.resizeImage(Builder.getUIIcon(color), 55, 55), () -> {train((new Builder(player.handler.map.getAvailableNeighborCell(this), player)));}));
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, 100f, icon, () -> {player.handler.camera.centerOnEntity(this);}));
    }

    @Override
    public TownHall duplicate() {
        return new TownHall(gridX, gridY, gridWidth, player);
    }
    
}
