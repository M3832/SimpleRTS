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
import simplerts.entities.actions.Attack;
import simplerts.gfx.Assets;
import simplerts.map.Destination;
import simplerts.ui.UIAction;
import simplerts.utils.Timer;

/**
 *
 * @author Markus
 */
public class Footman extends Unit implements Attacker {
    
    private static int GOLDCOST = 100;
    private int range;
    private boolean hasAttacked, renderAttack;
    private int attackSpeed, attackRenderSpeed;

    public Footman(int x, int y, Player player)
    {
        super(x, y, player);
        initVariables();
    }
    
    public Footman(Destination d, Player player)
    {
        this(d.getX(), d.getY(), player);
    }
    
    private void initVariables()
    {
        range = 1;
        hasAttacked = false;
        attackSpeed = 800;
        attackRenderSpeed = 300;
        attackDamage = 5;
        ac = player.getSpriteManager().getFootmanAC();
        name = "Footman";
        initGraphics();
        setupActions();
    }
    
    public void setupActions()
    {     
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, Game.HEIGHT + 100f, icon, () -> {player.getHandler().camera.centerOnEntity(this);}));
    }
    
    @Override
    public Entity duplicate() {
        return new Footman(x, y, player);
    }

    @Override
    public void attack(Entity e) {
        if(!hasAttacked)
        {
            hasAttacked = true;
            renderAttack = true;
            setDirection(e.getGridX() - getGridX(), e.getGridY() - getGridY());
            e.hit(Math.max(attackDamage - e.getArmor(), 0));
            new Timer(attackSpeed, () -> {hasAttacked = false;}).start();
            new Timer(attackRenderSpeed, () -> {renderAttack = false;}).start();
        }
    }

    @Override
    public int getRange() {
        return range;
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        return Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/peasantPortrait.png"), Assets.loadToCompatibleImage("/peasantPortraittc.png"), color));
    }
    
    public static UIAction getUIAction(Player player, Building building)
    {
        UIAction a = new UIAction(Assets.resizeImage(Footman.getUIIcon(player.getColor()), 55, 55), () -> {
            building.train(new Footman(player.getHandler().map.getAvailableNeighborCell(building), player));
        });
        a.setTitle("Footman");
        a.setGoldCost(GOLDCOST + "");
        return a;
    }
    
    @Override
    public void render(Graphics g)
    {
        super.render(g);
        g.setColor(Color.WHITE);
        if(renderAttack)
            g.drawString("HIT", x - offsetX, y - offsetY);
    }
    
    @Override
    public void rightClickAction(Entity e)
    {
        if(e.getPlayer() != player)
        {
            addAction(new Attack(this, e));
        } else {
            super.rightClickAction(e);
        }
    }
    
}