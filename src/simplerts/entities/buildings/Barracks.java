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
        sprite = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
        icon = Assets.makeIcon(color, Assets.resizeImage(sprite.getSubimage(width * (int)(1 * (sprite.getWidth()/width - 1)), 0, width, height), 100, 100));
        Graphics g = sprite.getGraphics();
        g.setColor(color);
        g.setFont(GUI.BREAD);
        g.drawString("BARRACKS", sprite.getWidth()/2 - g.getFontMetrics().stringWidth("BARRACKS")/2, sprite.getHeight()/2 + g.getFontMetrics().getHeight()/4);
        g.setColor(Color.BLACK);
        g.drawRect(1, 1, width-2, height-2);
        uiObjects = new ArrayList<>();
        uiActions = new ArrayList<>();
        initVariables();
    }
    
    private void initVariables()
    {
        goldCost = 0;
        buildTime = 1;
    }
    
    @Override
    protected void setupActions()
    {
        uiActions.add(Footman.getUIAction(player, this));
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, Game.HEIGHT + 100f, icon, () -> {player.getHandler().camera.centerOnEntity(this);}));
    }
    
    public static UIAction getUIAction(Player player)
    {
        UIAction a = new UIAction(Assets.resizeImage(getUIIcon(player.getColor()), 55, 55), () -> {player.getHandler().game.controller.setEntityPlacerEntity(new Barracks(0, 0, player));});
        a.setTitle("Barracks");
        a.setGoldCost(GOLDCOST + "");
        return a;
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        BufferedImage sprite = new BufferedImage(55, 55, BufferedImage.TYPE_INT_ARGB);
        Graphics g = sprite.getGraphics();
        g.setColor(color);
        g.setFont(GUI.SMALL);
        g.drawString("BARRACKS", sprite.getWidth()/2 - g.getFontMetrics().stringWidth("BARRACKS")/2, sprite.getHeight()/2 + g.getFontMetrics().getHeight()/4);
        return Assets.makeIcon(color, sprite);
    }
    
    @Override
    public Entity duplicate() {
        return new Barracks(x, y, player);
    }
    
}