/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.buildings;

import simplerts.entities.units.Footman;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import simplerts.Game;
import simplerts.Player;
import simplerts.entities.Building;
import simplerts.entities.Entity;
import simplerts.entities.units.Archer;
import simplerts.gfx.Assets;
import simplerts.ui.GUI;
import simplerts.ui.UIAction;

/**
 *
 * @author Markus
 */
public class Barracks extends Building {

    public static int GOLDCOST = 200;
    
    public Barracks(int x, int y, Player player) {
        super(x, y, 3, player, false);
        initVariables();
    }
    
    private void initVariables()
    {
        goldCost = GOLDCOST;
        lumberCost = 100;
        buildTime = 40 * Game.TICKS_PER_SECOND;

    }
    
    @Override
    protected void initGraphics(){
        sprite = Assets.makeTeamColor(Assets.loadAndResizeImage("/Buildings/Barracks/sprite.png", width, height),
                                             Assets.loadAndResizeImage("/Buildings/Barracks/barrackstc.png", width, height), color);
        icon = Assets.makeIcon(color, Assets.resizeImage(sprite.getSubimage(width * (int)(1 * (sprite.getWidth()/width - 1)), 0, width, height), 100, 100));
    }
    
    @Override
    protected void setupActions()
    {
        super.setupActions();
        addActionButton(actionButtons, Footman.getUIAction(player, this));
        addActionButton(actionButtons, Archer.getUIAction(player, this));
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, Game.HEIGHT + 100f, icon, () -> {player.getController().getCamera().centerOnEntity(this);}, 'e'));
    }
    
    public static UIAction getUIAction(Player player)
    {
        UIAction a = new UIAction(Assets.resizeImage(getUIIcon(player.getColor()), 55, 55), () -> {player.getHandler().game.controller.setEntityPlacerEntity(new Barracks(0, 0, player));}, 'b');
        a.setTitle("Barracks");
        a.setGoldCost(GOLDCOST + "");
        a.setLumberCost(100 + "");
        return a;
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        BufferedImage sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/Buildings/Barracks/sprite.png"),
                                                     Assets.loadToCompatibleImage("/Buildings/Barracks/barrackstc.png"), color);
        return Assets.makeIcon(color, sprite);
    }
    
    @Override
    public Entity duplicate() {
        return new Barracks(x, y, player);
    }
    
}
