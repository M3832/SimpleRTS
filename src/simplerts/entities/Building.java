/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import simplerts.entities.buildings.Tower;
import simplerts.entities.units.Builder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import simplerts.Game;
import simplerts.Player;
import simplerts.display.Camera;
import simplerts.utils.Utilities;
import simplerts.gfx.Assets;
import simplerts.messaging.ErrorMessage;
import simplerts.messaging.Message;
import simplerts.ui.GUI;
import static simplerts.ui.GUI.HEADER;
import simplerts.ui.UIAction;
import simplerts.ui.UIObject;

/**
 *
 * @author Markus
 */
public abstract class Building extends Entity {
    
    protected int buildTime;
    protected int currentTime;
    protected int trainTime, currentTrainTime;
    protected long lastUpdate;
    protected String name;
    protected Builder builder;
    protected Unit unitTraining;
    protected boolean training, building;
    
    public Building(int x, int y, int cellSize, Player player, boolean built)
    {
        super(x, y, cellSize, player);
        initVariables(built);
    }
    
    private void initVariables(boolean built)
    {
        buildTime = 500;
        currentTime = 1;
        armor = 2;
        name = getClass().getSimpleName();
        if(built) setBuilt();
        trainTime = 0;
        currentTrainTime = 0;
        training = false;
        building = !built;
    }
    
    public void setImage(BufferedImage image)
    {
        this.sprite = image;
    }
    
    public void setBuildTime(int buildTime)
    {
        this.buildTime = buildTime;
    }
    
    public void setBuilt()
    {
        currentTime = buildTime;
        setupActions();
    }
    
    @Override
    public void update()
    {
        super.update();
        if(building)
            building();
        
        if(training)
            training();
    }
    
    private void building()
    {
        if(builder != null && currentTime < buildTime)
        {
            currentTime += 1 * Game.GAMESPEED;
        } else if (builder != null && currentTime == buildTime)
        {
            builder.exit(grid.getAvailableNeighborCell(this));
            builder = null;
            building = false;
            setupActions();
        }
    }
    
    private void training()
    {
        if(unitTraining != null && currentTrainTime < trainTime)
        {
            currentTrainTime += 1 * Game.GAMESPEED;
        } else if (unitTraining != null && currentTrainTime == trainTime)
        {
            unitTraining.setPosition(player.getHandler().map.getAvailableNeighborCell(this));
            player.getHandler().map.addEntity(unitTraining);
            unitTraining = null;
            uiObjects.stream().forEach((object) -> {if(!((UIAction)object).isVisible()){((UIAction)object).setVisible(true);}});
            training = false;
        }
    }
    
    @Override
    public void render(Graphics g, Camera camera)
    {
        super.render(g, camera);
        g.drawImage(sprite.getSubimage(width * (int)((1f * currentTime/buildTime) * (sprite.getWidth()/width - 1)), 0, width, height), (int)(x - offsetX), (int)(y - offsetY), width, height, null);
    }
    
    public Building build(Builder b)
    {
        b.enter();
        builder = b;
        return this;
    }
    
    public void train(Unit u)
    {
        if(player.hasRoomFor(u))
        {
            if(player.hasGoldFor(u))
            {
                uiObjects.stream().forEach((object) -> {if(((UIAction)object).isVisible()){((UIAction)object).setVisible(false);}});
                training = true;
                player.pay(u.getCost());
                unitTraining = u;
                currentTrainTime = 0;
                trainTime = u.getTrainTime();
            } else {
                player.getHandler().game.mm.addMessage(new ErrorMessage("Not enough gold."));
            }
        } else {
            player.getHandler().game.mm.addMessage(new ErrorMessage("Not enough food. Create more farms."));
        }
    }
    
    public BufferedImage getSprite()
    {
        return sprite;
    }
    
        @Override
    public void renderGUI(Graphics g)
    {
        g.setColor(new Color(255, 155, 111));
        //Render name
        Utilities.drawWithShadow(g, name, Game.WIDTH/2 - g.getFontMetrics(HEADER).stringWidth(name)/2, Game.HEIGHT + 75);
        
        if(currentTime < buildTime)
        {
            renderBuilding(g);
        } else if (currentTrainTime < trainTime) 
        {
            renderTraining(g);
        } else {
            
            for(UIObject o : uiObjects)
            {
                o.render(g);
            }
                    
            //Render stats
            g.setFont(GUI.BREAD);
            Utilities.drawWithShadow(g, "Health: " + health + "/" + maxHealth, 300, Game.HEIGHT + 125);
            Utilities.drawWithShadow(g, "Armor: " + armor, 300, Game.HEIGHT + 150);
        }
        
    }
    
    public void renderBuilding(Graphics g)
    {
        g.setColor(Color.green);
        g.fillRect(Game.WIDTH/2 - 100, Game.HEIGHT + 125, (int)(200 * (1f * currentTime/buildTime)), 25);
        g.setColor(Color.black);
        g.drawRect(Game.WIDTH/2 - 100, Game.HEIGHT + 125, (int)(200), 25);        
    }
    
    public void renderTraining(Graphics g)
    {
        g.setColor(Color.green);
        g.fillRect(Game.WIDTH/2 - 100, Game.HEIGHT + 125, (int)(200 * (1f * currentTrainTime/trainTime)), 25);
        g.setColor(Color.black);
        g.drawRect(Game.WIDTH/2 - 100, Game.HEIGHT + 125, (int)(200), 25);        
    }
    
    public static UIAction getUIAction(Player player){
        UIAction a = new UIAction(Assets.resizeImage(Tower.getUIIcon(player.getColor()), 55, 55), () -> {player.getHandler().game.controller.setEntityPlacerEntity(new Tower(0, 0, 2, player));}, 't');
        a.setTitle("PLACEHOLDER");
        return a;
    }
}