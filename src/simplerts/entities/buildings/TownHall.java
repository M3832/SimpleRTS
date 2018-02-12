/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.buildings;

import simplerts.entities.units.Builder;
import simplerts.entities.interfaces.FoodProvider;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import simplerts.Game;
import simplerts.Player;
import simplerts.entities.Building;
import simplerts.gfx.Assets;
import simplerts.entities.interfaces.GoldReceiver;
import simplerts.entities.interfaces.Goldminer;
import simplerts.entities.interfaces.LumberReceiver;
import simplerts.ui.UIAction;

/**
 *
 * @author Markus
 */
public class TownHall extends Building implements FoodProvider, GoldReceiver, LumberReceiver {
    public static int GOLDCOST = 400;
    
    public TownHall(int x, int y, int gridSize, Player player) {
        super(x, y, gridSize, player, false);
        sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/Buildings/TownHall/sprite.png"),
                                                     Assets.loadToCompatibleImage("/Buildings/TownHall/teamcolor.png"), color);
        icon = Assets.makeIcon(color, Assets.resizeImage(sprite.getSubimage(width * (int)(1 * (sprite.getWidth()/width - 1)), 0, width, height), 100, 100));
        uiObjects = new ArrayList<>();
        uiActions = new ArrayList<>();
        buildTime = 1 * Game.TICKS_PER_SECOND;
        goldCost = GOLDCOST;
        viewRange = 5;
    }
    
    public TownHall(int x, int y, int gridSize, Player player, boolean built)
    {
        this(x, y, gridSize, player);
        if(built) setBuilt();
    }
    
    @Override
    protected void setupActions()
    {
        uiActions.add(Builder.getUIAction(player, this));
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, Game.HEIGHT + 100f, icon, () -> {player.getHandler().game.controller.getCamera().centerOnEntity(this);}));
    }

    @Override
    public TownHall duplicate() {
        return new TownHall(gridX, gridY, gridWidth, player);
    }

    @Override
    public int getFoodProduced() {
        return 25;
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        BufferedImage sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/Buildings/TownHall/sprite.png"),
                                                     Assets.loadToCompatibleImage("/Buildings/TownHall/teamcolor.png"), color);
        return Assets.makeIcon(color, Assets.resizeImage(sprite.getSubimage(400, 0, 200, 200), 100, 100));
    }
    
    public static UIAction getUIAction(Player player)
    {
        UIAction a = new UIAction(Assets.resizeImage(TownHall.getUIIcon(player.getColor()), 55, 55), () -> {player.getHandler().game.controller.setEntityPlacerEntity(new TownHall(0, 0, 4, player));});
        a.setTitle("Town Hall");
        a.setGoldCost(GOLDCOST + "");
        return a;
    }

    @Override
    public void receiveGold(Goldminer g) {
        player.addGold(g.takeGold());
    }

    @Override
    public void receiveLumber(int lumber) {
        player.addLumber(lumber);
    }
    
}
