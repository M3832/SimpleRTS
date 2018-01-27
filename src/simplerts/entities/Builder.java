/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import simplerts.Game;
import simplerts.Player;
import simplerts.Destination;
import simplerts.entities.actions.Gather;
import simplerts.gfx.Assets;
import static simplerts.entities.TownHall.GOLDCOST;
import simplerts.entities.interfaces.GoldProvider;
import simplerts.entities.interfaces.Goldminer;
import simplerts.ui.UIAction;

/**
 *
 * @author Markus
 */
public class Builder extends Unit implements Goldminer{
    
    private int gold;
    private GoldProvider latestGoldMine;
    private static int GOLDCOST = 50;
    
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
        gold = 0;
        goldCost = GOLDCOST;
        initGraphics();
        setupActions();
    }
    
    public void setupActions()
    {
        uiActions.add(TownHall.getUIAction(player));
        uiActions.add(Tower.getUIAction(player));
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, Game.HEIGHT + 100f, icon, () -> {player.getHandler().camera.centerOnEntity(this);}));
    }

    @Override
    public Entity duplicate() {
        return new Builder(gridX, gridY, player);
    }
    
    @Override
    public void enter()
    {
        isVisible = false;
        grid.getCells()[gridX][gridY].setEntity(null);
        grid.getHandler().game.controller.removeFromSelection(this);
    }
    
    @Override
    public void exit(Destination d)
    {
        isVisible = true;
        setPosition(d);
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

    @Override
    public void setGold(int amount) {
        gold = amount;
    }

    @Override
    public int takeGold() {
        int temp = gold;
        gold = 0;
        return temp;
    }
    
    @Override
    public void rightClickAction(Entity e)
    {
        if(e instanceof GoldProvider)
        {
            addAction(new Gather(this, (GoldProvider)e));
        } else {
            super.rightClickAction(e);
        }
    }
    
    @Override
    public void render(Graphics g)
    {
        super.render(g);
        if(gold > 0)
        {
            g.setColor(Color.WHITE);
            g.drawString("Gold", x - offsetX, y - offsetY);
        }
    }

    @Override
    public void setLatestMine(GoldProvider g) {
        latestGoldMine = g;
    }

    @Override
    public GoldProvider getLatestMine() {
        return latestGoldMine;
    }
    
    
    
}
