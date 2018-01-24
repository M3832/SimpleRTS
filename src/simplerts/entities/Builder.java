/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import simplerts.Game;
import simplerts.Placer;
import simplerts.Player;
import simplerts.display.Assets;
import simplerts.gfx.Animation;
import simplerts.ui.UIAction;

/**
 *
 * @author Markus
 */
public class Builder extends Unit{
    
    public Builder()
    {
        super();
        moveSpeed = 3;
        attackDamage = 5;
    }
    
    public Builder(int x, int y, Player player)
    {
        this();
        this.player = player;
        color = player.getColor();
        ac.addAnimation("walk", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/walk.png"),
                                                     Assets.loadToCompatibleImage("/Units/Peasant/walktc.png"), color)));
        ac.addAnimation("stand", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/stand.png"),
                                                     Assets.loadToCompatibleImage("/Units/Peasant/standtc.png"), color)));
        initGraphics();
        setPosition(x * Game.CELLSIZE, y * Game.CELLSIZE);
    }
    
    @Override
    public void setupActions()
    {
        super.setupActions();
        actions.add(new UIAction(777, 24, Assets.loadAndResizeImage("/townhall.png", 55, 55), () -> {player.handler.game.controller.setEntityPlacerEntity(new TownHall(0, 0, 4, player));}));
    }
    
}
