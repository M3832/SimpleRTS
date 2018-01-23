/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import simplerts.entities.Entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import simplerts.Game;

/**
 *
 * @author Markus
 */
public class Building extends Entity {
    
    protected int buildtime;
    protected int currentTime;
    protected long lastUpdate;
    
    public Building(int x, int y, int cellSize)
    {
        super(x * Game.CELLSIZE, y * Game.CELLSIZE, cellSize);
        buildtime = 500;
        currentTime = 1;
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
        buildtime = 500;
        currentTime = 1;
    }
    
    public void setImage(BufferedImage image)
    {
        this.sprite = image;
    }
    
    public void setBuildTime(int buildTime)
    {
        buildtime = buildTime;
    }
    
    @Override
    public void update()
    {
        currentTime += 1 * Game.GAMESPEED;
    }
    
    @Override
    public void render(Graphics g, float offsetX, float offsetY)
    {
//        super.render(g, offsetX, offsetY);
        
        g.drawImage(sprite, (int)(x - offsetX), (int)(y - offsetY), width, height, null);
//        if(currentTime < buildtime)
//        {
//            g.setColor(Color.gray);
//            g.fillRect((int)(x - offsetX), (int)((y + gridHeight * Game.CELLSIZE) - offsetY) + 5, gridWidth * Game.CELLSIZE, (int)(0.25f * Game.CELLSIZE));
//            g.setColor(Color.orange);
//            g.fillRect((int)(x - offsetX), (int)((y + gridHeight * Game.CELLSIZE) - offsetY) + 5, (int)((gridWidth * Game.CELLSIZE) * ((float)currentTime/(float)buildtime)), (int)(0.25f * Game.CELLSIZE));
//
//        }
    }
    
    @Override
    public Entity duplicate()
    {
        return new Building(x, y, gridWidth);
    }
    
    public BufferedImage getSprite()
    {
        return sprite;
    }
}
