/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.units;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import simplerts.Game;
import simplerts.Player;
import simplerts.audio.SoundController;
import simplerts.entities.buildings.Barracks;
import simplerts.entities.Building;
import simplerts.entities.Entity;
import simplerts.entities.buildings.Farm;
import simplerts.entities.buildings.Tower;
import simplerts.entities.buildings.TownHall;
import simplerts.entities.Unit;
import simplerts.map.Destination;
import simplerts.entities.actions.*;
import simplerts.entities.interfaces.*;
import simplerts.gfx.Assets;
import simplerts.ui.UIAction;
import simplerts.ui.UIActionButton;

/**
 *
 * @author Markus
 */
public class Builder extends Unit implements Goldminer, Lumberman{
    
    private int gold, lumber, lumberCapacity;
    private GoldProvider latestGoldMine;
    private Destination latestForestDestination;
    private final int goldCapacity = 25;
    private static int GOLDCOST = 0;
    private boolean chopping;
    private ArrayList<UIAction> buildMenu;
    
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
        this(d.getGridX(), d.getGridY(), player);
    }
    
    private void initVariables()
    {
        moveSpeed = 3;
        attackDamage = 5;
        trainTime = 1 * Game.TICKS_PER_SECOND;
        ac = player.getSpriteManager().getPeasantAC();
        gold = 0;
        lumber = 0;
        lumberCapacity = 10;
        chopping = false;
        goldCost = GOLDCOST;
        initSounds();
    }
    
    private void initSounds()
    {
        soundManager.addSound("/Units/Peasant/wake1.wav", SoundController.WAKE);
        soundManager.addSound("/Units/Peasant/wake2.wav", SoundController.WAKE);
        soundManager.addSound("/Units/Peasant/wake3.wav", SoundController.WAKE);
        soundManager.addSound("/Units/Peasant/wake4.wav", SoundController.WAKE);
        soundManager.addSound("/Units/Peasant/confirm1.wav", SoundController.CONFIRM);
        soundManager.addSound("/Units/Peasant/confirm2.wav", SoundController.CONFIRM);
        soundManager.addSound("/Units/Peasant/confirm3.wav", SoundController.CONFIRM);
        soundManager.addSound("/Units/Peasant/confirm4.wav", SoundController.CONFIRM);
    }
    
    @Override
    protected void initGraphics()
    {
        icon = Assets.makeIcon(color, Assets.makeTeamColor(Assets.loadToCompatibleImage("/peasantPortrait.png"), Assets.loadToCompatibleImage("/peasantPortraittc.png"), color));        
    }
    
    @Override
    public void setupActions()
    {
        super.setupActions();
        buildMenu = new ArrayList<>();
        addActionButton(actionButtons, new UIActionButton(Assets.iconGather, () -> {System.out.println("Pressed gather");}, "Gather", 'g'));
        addActionButton(actionButtons, new UIActionButton(Assets.iconBuild, () -> {player.getController().changeActionMenu(buildMenu);}, "Build", 'b'), 2, 0);
        addActionButton(buildMenu, TownHall.getUIAction(player));
        addActionButton(buildMenu, Farm.getUIAction(player));
        addActionButton(buildMenu, Tower.getUIAction(player));        
        addActionButton(buildMenu, Barracks.getUIAction(player));
        addActionButton(buildMenu, new UIActionButton(Assets.iconCancel, () -> {player.getController().changeActionMenu(actionButtons);}, "Cancel", 'c'), 2, 2);
        uiObjects.add(new UIAction(Game.WIDTH/2 + 100f, Game.HEIGHT + 100f, icon, () -> {player.getHandler().game.controller.getCamera().centerOnEntity(this);}, 'e'));
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
        grid.getHandler().game.controller.deselect(this);
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
        }, 'b');
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
    protected void setAnimation()
    {
        if(isDead)
        {
            ac.setAnimation("dead");
        } else { 
            if(chopping)
            {
                ac.setAnimation("chop");
                ac.update();
            } else {
                if(actions.size() > 0 && actions.get(0).isMoving())
                {
                    if(lumber > 0)
                    {
                        ac.setAnimation("walkTree");
                    } else if(gold > 0) {
                        ac.setAnimation("walkGold");
                    } else {
                        ac.setAnimation("walk");
                    }
                }
                else 
                {
                    if(lumber > 0)
                    {
                        ac.setAnimation("standTree");                    
                    } else if(gold > 0) {
                        ac.setAnimation("standGold");
                    } else {
                        ac.setAnimation("stand");
                    }
                }
                ac.update();
            }
        }
    }
    
    @Override
    public void rightClickAction(Entity e)
    {
        if(e instanceof GoldProvider)
        {
            addAction(new Gather(this, (GoldProvider)e));
        } else if(e instanceof GoldReceiver && gold > 0)
        {
            addAction(new TurnInGold(this));
        } else if(e instanceof LumberReceiver && lumber > 0)
        {
            addAction(new TurnInLumber(this));
        } else {
            super.rightClickAction(e);
        }
    }
    
    @Override
    public void update()
    {
        super.update();
        if(!actions.isEmpty())
        {
            if(actions.get(0) instanceof Chop)
                chopping = ((Chop) actions.get(0)).isChopping();
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
    
    @Override
    public int getGoldCapacity()
    {
        return goldCapacity;
    }

    @Override
    public void receiveLumber(int lumber) {
        this.lumber += lumber;
    }

    @Override
    public int takeLumber() {
        int temp = lumber;
        lumber = 0;
        return temp;
    }
    
    @Override
    public int getLumber() {
        return lumber;
    }

    @Override
    public int getChopSpeed() {
        return 1000;
    }

    @Override
    public void chop() {
        ac.setAnimation("chop");
        ac.playAnimation();
    }

    @Override
    public int getLumberCapacity() {
        return lumberCapacity;
    }

    @Override
    public void setLatestForestDestination(Destination d) {
        latestForestDestination = d;
    }

    @Override
    public Destination getLatestForestDestination() {
        return latestForestDestination;
    }
    
    @Override
    public void addAction(Action a)
    {
        super.addAction(a);
        if(!(a instanceof Chop))
            chopping = false;
    }
    
}
