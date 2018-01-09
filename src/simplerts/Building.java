/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Markus
 */
public class Building extends Entity {
    
    protected int buildtime;
    protected int currentTime;
    protected long lastUpdate;
    protected BufferedImage image;
    
    public Building(int x, int y, int cellSize)
    {
        super(x, y, cellSize);
        buildtime = 500;
        currentTime = 1;
    }
    
    public Building(int x, int y, int cellWidth, int cellHeight){
        super(x, y, cellWidth, cellHeight);
        buildtime = 500;
        currentTime = 1;
    }
    
    public void setImage(BufferedImage image)
    {
        this.image = image;
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
        
        g.drawImage(image, (int)(x - offsetX), (int)(y - offsetY), width, height, null);
        if(currentTime < buildtime)
        {
            g.setColor(Color.gray);
            g.fillRect((int)(x - offsetX), (int)((y + cellHeight * Game.CELLSIZE) - offsetY) + 5, cellWidth * Game.CELLSIZE, (int)(0.25f * Game.CELLSIZE));
            g.setColor(Color.orange);
            g.fillRect((int)(x - offsetX), (int)((y + cellHeight * Game.CELLSIZE) - offsetY) + 5, (int)((cellWidth * Game.CELLSIZE) * ((float)currentTime/(float)buildtime)), (int)(0.25f * Game.CELLSIZE));

        }
    }
    
    @Override
    public Entity duplicate()
    {
        return new Building(x, y, cellWidth);
    }
}
