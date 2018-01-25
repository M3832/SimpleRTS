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
import simplerts.Utils;
import simplerts.ui.GUI;
import static simplerts.ui.GUI.HEADER;
import simplerts.ui.UIObject;

/**
 *
 * @author Markus
 */
public abstract class Building extends Entity {
    
    protected int buildTime;
    protected int currentTime;
    protected int armor;
    protected long lastUpdate;
    protected String name;
    protected Builder builder;
    
    public Building(int x, int y, int cellSize, Player player, boolean built)
    {
        super(x * Game.CELLSIZE, y * Game.CELLSIZE, cellSize, player);
        buildTime = 500;
        currentTime = 1;
        armor = 2;
        name = getClass().getSimpleName();
        if(built) setBuilt();
    }
    
    public Building(int x, int y, int gridWidth, int gridHeight){
        this.x = x;
        this.y = y;
        this.width = gridWidth * Game.CELLSIZE;
        this.height = gridHeight * Game.CELLSIZE;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.gridX = x/Game.CELLSIZE;
        this.gridY = y/Game.CELLSIZE;
        buildTime = 500;
        currentTime = 1;
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
    }
    
    @Override
    public void update()
    {
        if(builder != null && currentTime < buildTime)
        {
            currentTime += 1 * Game.GAMESPEED;
        } else if (builder != null && currentTime == buildTime)
        {
            builder.isVisible = true;
            builder.setPosition(player.handler.map.getAvailableNeighborCell(this));
            builder = null;
            setupActions();
        }
    }
    
    @Override
    public void render(Graphics g, float offsetX, float offsetY)
    {
//        super.render(g, offsetX, offsetY);
        
        g.drawImage(sprite.getSubimage(width * (int)((1f * currentTime/buildTime) * (sprite.getWidth()/width - 1)), 0, width, height), (int)(x - offsetX), (int)(y - offsetY), width, height, null);
//        if(currentTime < buildTime)
//        {
//            g.setColor(Color.gray);
//            g.fillRect((int)(x - offsetX), (int)((y + gridHeight * Game.CELLSIZE) - offsetY) + 5, gridWidth * Game.CELLSIZE, (int)(0.25f * Game.CELLSIZE));
//            g.setColor(Color.orange);
//            g.fillRect((int)(x - offsetX), (int)((y + gridHeight * Game.CELLSIZE) - offsetY) + 5, (int)((gridWidth * Game.CELLSIZE) * ((float)currentTime/(float)buildTime)), (int)(0.25f * Game.CELLSIZE));
//
//        }
    }
    
    public Building build(Builder b)
    {
        b.isVisible = false;
        builder = b;
        return this;
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
        Utils.drawWithShadow(g, name, Game.WIDTH/2 - g.getFontMetrics(HEADER).stringWidth(name)/2, 75);
        
        for(UIObject o : uiObjects)
        {
            o.render(g);
        }
        
        if(currentTime < buildTime)
        {
            g.setColor(Color.green);
            g.fillRect(Game.WIDTH/2 - 100, 125, (int)(200 * (1f * currentTime/buildTime)), 25);
            g.setColor(Color.black);
            g.drawRect(Game.WIDTH/2 - 100, 125, (int)(200), 25);
        } else {
            //Render stats
            g.setFont(GUI.BREAD);
            Utils.drawWithShadow(g, "Health: " + health + "/" + maxHealth, 300, 125);
            Utils.drawWithShadow(g, "Armor: " + armor, 300, 150);
        }
        
    }
}