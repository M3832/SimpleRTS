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
        ac = player.getSpriteManager().getPeasantAC();
        initGraphics();
        setupActions();
        setPosition(x * Game.CELLSIZE, y * Game.CELLSIZE);
        trainTime = 1 * Game.TICKS_PER_SECOND;
    }
    
    public Builder(Destination d, Player player)
    {
        this(d.getX(), d.getY(), player);
    }
    
    @Override
    public void setupActions()
    {
        super.setupActions();
        uiActions.add(TownHall.getUIAction(player));
        uiActions.add(Tower.getUIAction(player));
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, 100f, icon, () -> {player.getHandler().camera.centerOnEntity(this);}));
    }

    @Override
    public Entity duplicate() {
        return new Builder(gridX, gridY, player);
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        return Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/peasantPortrait.png"), Assets.loadToCompatibleImage("/peasantPortraittc.png"), color));
    }
    
    public void setInvisible()
    {
        isVisible = false;
        map.getCells()[gridX][gridY].setEntity(null);
    }
    
}
