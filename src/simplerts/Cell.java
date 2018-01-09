/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Arrays;
import simplerts.editor.TerrainPlacement;

/**
 *
 * @author Markus
 */
public class Cell {
    
    public Image backgroundimage;
    public boolean available;
    private int tileId;
    private TerrainPlacement terrain;
    
    public Cell()
    {
        available = true;
        backgroundimage = SpriteHolder.GRASS;
    }
    
    public Cell(TerrainPlacement terrain)
    {
        available = true;
        setTerrain(terrain);
    }
    
    public Image getImage()
    {
        return backgroundimage;
    }
    
    public int getTileId()
    {
        return tileId;
    }
    
    public void setTile(int tileId)
    {
        this.tileId = tileId;
        setImage(SpriteHolder.getTile(tileId));
    }
    
    public void setImage(Image image)
    {
        this.backgroundimage = image;
    }
    
    public boolean isAvailable()
    {
        return available;
    }
    
    public void variate()
    {
        System.out.println(tileId);
        if(Arrays.asList(terrain.getAlternatives()).contains(tileId))
        {
        System.out.println("Variation in progress");
            double d = Math.random();
            if(d < .8)
            {
                setTile(terrain.getAlternatives()[0]);
            } else {
                setTile(terrain.getAlternatives()[(int)(Math.random() * terrain.getAlternatives().length)]);
            }
            
        }
    }
    
    public TerrainPlacement getTerrain()
    {
        return terrain;
    }
    
    public void setTerrain(TerrainPlacement terrain)
    {
        this.terrain = terrain;
        this.tileId = terrain.getBaseTileId();
        backgroundimage = SpriteHolder.getTile(terrain.getBaseTileId());
    }
}
