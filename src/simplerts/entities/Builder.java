/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import simplerts.Game;
import simplerts.Placer;
import simplerts.Player;
import simplerts.actions.Destination;
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
        setupActions();
        setPosition(x * Game.CELLSIZE, y * Game.CELLSIZE);
    }
    
    public Builder(Destination d, Player player)
    {
        this(d.getX(), d.getY(), player);
    }
    
    @Override
    public void setupActions()
    {
        super.setupActions();
        uiActions.add(new UIAction(Assets.loadAndResizeImage("/townhall.png", 55, 55), () -> {player.handler.game.controller.setEntityPlacerEntity(new TownHall(0, 0, 4, player));}));
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, 100f, icon, () -> {player.handler.camera.centerOnEntity(this);}));
    }

    @Override
    public Entity duplicate() {
        return new Builder(gridX, gridY, player);
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        return Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/peasantPortrait.png"), Assets.loadToCompatibleImage("/peasantPortraittc.png"), color));
    }
    
}
