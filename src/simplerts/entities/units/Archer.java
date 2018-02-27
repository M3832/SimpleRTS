/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.units;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.Player;
import simplerts.display.Camera;
import simplerts.entities.Building;
import simplerts.entities.Entity;
import simplerts.entities.projectiles.Projectile;
import simplerts.entities.Unit;
import simplerts.entities.actions.Attack;
import simplerts.entities.interfaces.Attacker;
import simplerts.entities.interfaces.Ranger;
import simplerts.entities.projectiles.Arrow;
import simplerts.gfx.Assets;
import simplerts.map.Destination;
import simplerts.ui.UIAction;
import simplerts.ui.UIActionButton;
import simplerts.utils.TimerTask;

/**
 *
 * @author Markus
 */
public class Archer extends Unit implements Attacker, Ranger {
    
    public static int GOLDCOST = 100;
    
    private int range, attackSpeed;
    private boolean hasAttacked;
    
    private CopyOnWriteArrayList<Projectile> projectiles;
    
    public Archer(int x, int y, Player player)
    {
        super(x, y, player);
        initVariables();
    }
    
    public Archer(Destination d, Player player)
    {
        this(d.getGridX(), d.getGridY(), player);
    }
    
    private void initVariables()
    {
        range = 5;
        hasAttacked = false;
        trainTime = 20 * Game.TICKS_PER_SECOND;
        attackSpeed = 800;
        attackDamage = 5;
        ac = player.getSpriteManager().getArcherAC();
        name = "Archer";
        projectiles = new CopyOnWriteArrayList<>();
        goldCost = GOLDCOST;
        lumberCost = 25;
    }
    
    @Override
    protected void initGraphics()
    {
        icon = getUIIcon(color);        
    }
    
    @Override
    public void setupActions()
    {    
        super.setupActions();
        addActionButton(actionButtons, new UIActionButton(Assets.iconAttack, () -> {player.getController().setAction("attack"); setCancelMenu();}, "Attack", 'a'));
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, Game.HEIGHT + 100f, icon, () -> {grid.getHandler().game.controller.getCamera().centerOnEntity(this);}, 'e'));
    }
    
    @Override
    public void update(){
        super.update();
        projectiles.stream().forEach((Projectile p) -> p.update());
    }
    
    @Override
    public void render(Graphics g, Camera c){
        super.render(g, c);
        projectiles.stream().forEach((Projectile p) -> p.render(g, c));
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
    public void attack(Entity e) {
        if(!hasAttacked)
        {
            hasAttacked = true;
            setDirection(e.getGridX() - getGridX(), e.getGridY() - getGridY());
            projectiles.add(new Arrow(this, e));
            taskManager.addTask(new TimerTask(attackSpeed, () -> {hasAttacked = false;}));
            ac.setAnimation("attack");
            ac.playAnimation();
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

    @Override
    public Entity duplicate() {
        return new Archer(x, y, player);
    }

    @Override
    public void removeProjectile(Projectile p) {
        if(projectiles.contains(p))
            projectiles.remove(p);
    }
    
    public static BufferedImage getUIIcon(Color color)
    {
        return Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Archer/portrait.png"), Assets.loadToCompatibleImage("/Units/Archer/portraittc.png"), color));
    }
    
    public static UIAction getUIAction(Player player, Building building)
    {
        UIAction a = new UIAction(Assets.resizeImage(Archer.getUIIcon(player.getColor()), 55, 55), () -> {
            building.train(new Archer(player.getHandler().map.getAvailableNeighborCell(building), player));
        }, 'a');
        a.setTitle("Archer");
        a.setGoldCost(GOLDCOST + "");
        a.setLumberCost(25 + "");
        return a;
    }
}
