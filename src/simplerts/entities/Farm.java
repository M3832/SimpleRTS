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
import simplerts.entities.interfaces.FoodProvider;
import simplerts.gfx.Assets;
import simplerts.ui.GUI;
import simplerts.ui.UIAction;

/**
 *
 * @author Markus
 */
public class Farm extends Building implements FoodProvider {
    
    public static int GOLDCOST = 100;

    public Farm(int x, int y, int cellSize, Player player) {
        super(x, y, cellSize, player, false);
        
        sprite = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = sprite.getGraphics();
        g.setColor(color);
        g.setFont(GUI.BREAD);
        g.drawString("FARM", sprite.getWidth()/2 - g.getFontMetrics().stringWidth("FARM")/2, sprite.getHeight()/2 + g.getFontMetrics().getHeight()/4);
        g.setColor(Color.BLACK);
        g.drawRect(1, 1, width-2, height-2);
        
        initVariables();
    }
    
    @Override
    public void render(Graphics g)
    {
        super.render(g);
//        g.setColor(color);
//        g.fillRect(x - (int)player.getHandler().camera.getOffsetX(), y - (int)player.getHandler().camera.getOffsetY(), width, height);
    }
    
    private void initVariables()
    {
        goldCost = GOLDCOST;
        buildTime = 1 * Game.TICKS_PER_SECOND;
    }
    
    public static UIAction getUIAction(Player player)
    {
        UIAction a = new UIAction(Assets.resizeImage(getUIIcon(player.getColor()), 55, 55), () -> {player.getHandler().game.controller.setEntityPlacerEntity(new Farm(0, 0, 2, player));});
        a.setTitle("Farm");
        a.setGoldCost(GOLDCOST + "");
        return a;
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        BufferedImage sprite = new BufferedImage(55, 55, BufferedImage.TYPE_INT_ARGB);
        Graphics g = sprite.getGraphics();
        g.setColor(color);
        g.setFont(GUI.SMALL);
        g.drawString("FARM", sprite.getWidth()/2 - g.getFontMetrics().stringWidth("FARM")/2, sprite.getHeight()/2 + g.getFontMetrics().getHeight()/4);
        return Assets.makeIcon(color, sprite);
    }

    @Override
    public Entity duplicate() {
        return new Farm(x, y, gridWidth, player);
    }

    @Override
    public int getFoodProduced() {
        return 5;
    }
    
}
