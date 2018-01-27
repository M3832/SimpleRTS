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
import static simplerts.entities.TownHall.GOLDCOST;
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
        initVariables();
    }
    
    public Builder(int x, int y, Player player)
    {
        super(x, y, player);
        initVariables();
    }
    
    public Builder(Destination d, Player player)
    {
        this(d.getX(), d.getY(), player);
    }
    
    private void initVariables()
    {
        moveSpeed = 3;
        attackDamage = 5;
        trainTime = 1 * Game.TICKS_PER_SECOND;
        ac = player.getSpriteManager().getPeasantAC();
        initGraphics();
        setupActions();
    }
    
    @Override
    public void setupActions()
    {
        super.setupActions();
        uiActions.add(TownHall.getUIAction(player));
        uiActions.add(Tower.getUIAction(player));
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, Game.HEIGHT + 100f, icon, () -> {player.getHandler().camera.centerOnEntity(this);}));
    }

    @Override
    public Entity duplicate() {
        return new Builder(gridX, gridY, player);
    }
    
    public void setInvisible()
    {
        isVisible = false;
        grid.getCells()[gridX][gridY].setEntity(null);
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        return Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/peasantPortrait.png"), Assets.loadToCompatibleImage("/peasantPortraittc.png"), color));
    }
    
    public static UIAction getUIAction(Player player, Building building)
    {
        UIAction a = new UIAction(Assets.resizeImage(Builder.getUIIcon(player.getColor()), 55, 55), () -> {
            building.train(new Builder(player.getHandler().map.getAvailableNeighborCell(building), player));
        });
        a.setTitle("Peasant");
        a.setGoldCost(GOLDCOST + "");
        return a;
    }
    
}
