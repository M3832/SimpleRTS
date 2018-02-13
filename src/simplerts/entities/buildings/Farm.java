/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.buildings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import simplerts.Game;
import simplerts.Player;
import simplerts.display.Camera;
import simplerts.entities.Building;
import simplerts.entities.Entity;
import simplerts.entities.interfaces.FoodProvider;
import simplerts.gfx.Assets;
import simplerts.ui.GUI;
import simplerts.ui.UIAction;

/**
 *
 * @author Markus
 */
public class Farm extends Building implements FoodProvider {
    
    public static String SPRITE_URL = "/Buildings/Farm/sprite.png";
    public static String SPRITE_TEAMCOLOR_URL = "/Buildings/Farm/sprite.png";
    public static int GOLDCOST = 100;

    public Farm(int x, int y, int cellSize, Player player) {
        super(x, y, cellSize, player, false);
        sprite = Assets.makeTeamColor(Assets.loadAndResizeImage("/Buildings/Farm/sprite.png", width, height),
                                                     Assets.loadAndResizeImage("/Buildings/Farm/teamcolor.png", width, height), color);
        initVariables();
    }
    
    @Override
    public void render(Graphics g, Camera camera)
    {
        super.render(g, camera);
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
        BufferedImage sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/Buildings/Farm/sprite.png"),
                                                     Assets.loadToCompatibleImage("/Buildings/Farm/teamcolor.png"), color);
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
