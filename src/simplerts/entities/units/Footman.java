/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.units;

import simplerts.entities.interfaces.Attacker;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import simplerts.Game;
import simplerts.Player;
import simplerts.display.Camera;
import simplerts.entities.Building;
import simplerts.entities.Entity;
import simplerts.entities.Unit;
import simplerts.entities.actions.Attack;
import simplerts.gfx.Assets;
import simplerts.map.Destination;
import simplerts.ui.UIAction;
import simplerts.ui.UIActionButton;
import simplerts.utils.TimerTask;

/**
 *
 * @author Markus
 */
public class Footman extends Unit implements Attacker {
    
    private static int GOLDCOST = 100;
    private int range;
    private boolean hasAttacked, renderAttack;
    private int attackSpeed;

    public Footman(int x, int y, Player player)
    {
        super(x, y, player);
        initVariables();
    }
    
    public Footman(Destination d, Player player)
    {
        this(d.getGridX(), d.getGridY(), player);
    }
    
    private void initVariables()
    {
        range = 1;
        hasAttacked = false;
        attackSpeed = 800;
        attackDamage = 5;
        ac = player.getSpriteManager().getFootmanAC();
        name = "Footman";
    }
    
    @Override
    protected void initGraphics()
    {
        icon = Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Footman/portrait.png"), Assets.loadToCompatibleImage("/Units/Footman/portraittc.png"), color));        
    }
    
    @Override
    public void setupActions()
    {     
        super.setupActions();
        addActionButton(actionButtons, new UIActionButton(Assets.iconAttack, () -> {player.getController().setAction("attack"); setCancelMenu();}, "Attack", 'a'));
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, Game.HEIGHT + 100f, icon, () -> {grid.getHandler().game.controller.getCamera().centerOnEntity(this);}, 'e'));
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
            setDirection(e.getGridX() - getGridX(), e.getGridY() - getGridY());
            e.hit(Math.max(attackDamage - e.getArmor(), 0));
            taskManager.addTask(new TimerTask(attackSpeed, () -> {hasAttacked = false;}));
            ac.setAnimation("attack");
            ac.playAnimation();
        }
    }
    
    @Override
    public void setAnimation()
    {
        if(hasAttacked)
        {
            ac.update();
        } else {
            super.setAnimation();
        }
    }

    @Override
    public int getRange() {
        return range;
    }
    
    @Override
    public int getDamage() {
        return attackDamage;
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        return Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Footman/portrait.png"), Assets.loadToCompatibleImage("/Units/Footman/portraittc.png"), color));
    }
    
    public static UIAction getUIAction(Player player, Building building)
    {
        UIAction a = new UIAction(Assets.resizeImage(Footman.getUIIcon(player.getColor()), 55, 55), () -> {
            building.train(new Footman(player.getHandler().map.getAvailableNeighborCell(building), player));
        }, 'f');
        a.setTitle("Footman");
        a.setGoldCost(GOLDCOST + "");
        return a;
    }
    
    @Override
    public void render(Graphics g, Camera camera)
    {
        super.render(g, camera);
        g.setColor(Color.WHITE);
        if(renderAttack)
            g.drawString("HIT", x - offsetX, y - offsetY);
    }
    
    @Override
    public void rightClickAction(Entity e)
    {
        if(e.getPlayer() != player && !e.isDead())
        {
            addAction(new Attack(this, e));
        } else {
            super.rightClickAction(e);
        }
    }
    
}
