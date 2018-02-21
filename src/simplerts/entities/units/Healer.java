/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.units;

import java.awt.Color;
import java.awt.image.BufferedImage;
import simplerts.Game;
import simplerts.Player;
import simplerts.entities.Building;
import simplerts.entities.Entity;
import simplerts.entities.Unit;
import simplerts.gfx.Assets;
import simplerts.map.Destination;
import simplerts.ui.UIAction;

/**
 *
 * @author marku
 */
public class Healer extends Unit {
    
    public static final int GOLDCOST = 100;
    
    public Healer(int x, int y, Player player)
    {
        super(x, y, player);
        initVariables();
    }
    
    public Healer(Destination d, Player player)
    {
        this(d.getGridX(), d.getGridY(), player);
    }
    
    private void initVariables()
    {
        attackDamage = 5;
        ac = player.getSpriteManager().getHealerAC();
        name = "Healer";
        initGraphics();
        setupActions();
    }
    
    @Override
    protected void initGraphics()
    {
        icon = getUIIcon(color);        
    }
    
    public void setupActions()
    {     
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, Game.HEIGHT + 100f, icon, () -> {player.getController().getCamera().centerOnEntity(this);}, 'e'));
    }

    @Override
    public Entity duplicate() {
        return new Healer(getDestination(), player);
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        return Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Healer/portrait.png"), Assets.loadToCompatibleImage("/Units/Healer/portraittc.png"), color));
    }
    
    public static UIAction getUIAction(Player player, Building building)
    {
        UIAction a = new UIAction(Assets.resizeImage(Healer.getUIIcon(player.getColor()), 55, 55), () -> {
            building.train(new Healer(player.getHandler().map.getAvailableNeighborCell(building), player));
        }, 'h');
        a.setTitle("Healer");
        a.setGoldCost(GOLDCOST + "");
        return a;
    }
    
}
