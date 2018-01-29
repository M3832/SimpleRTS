/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.map;

import simplerts.gfx.Assets;
import java.awt.Image;
import java.util.Arrays;
import simplerts.entities.Entity;
import simplerts.entities.Forest;

/**
 *
 * @author Markus
 */
public class Cell {
    
    public Image backgroundimage;
    public boolean available;
    private int tileId;
    private Terrain terrain;
    private Entity entity;
    private Forest forest;
    private int x, y;
    
    public Cell(int x, int y)
    {
        available = true;
        this.x = x;
        this.y = y;
    }
    
    public Cell(Terrain terrain)
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
        setImage(terrain.tiles[tileId]);
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
    
    public Terrain getTerrain()
    {
        return terrain;
    }
    
    public void setTerrain(Terrain terrain)
    {
        this.terrain = terrain;
        this.tileId = terrain.getBaseTileId();
        this.available = terrain.isWalkable();
        backgroundimage = terrain.tiles[terrain.getBaseTileId()];
        if(terrain.name.equals("trees"))
        {
            forest = new Forest(this);
        }
    }
    
    public void setEntity(Entity e)
    {
        entity = e;
    }
    
    public Entity getEntity()
    {
        return entity;
    }
    
    public boolean isForest()
    {
        return forest != null;
    }
    
    public void depleteForest()
    {
        backgroundimage = Assets.barrenTree;
        available = true;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }
    
    public Forest getForest()
    {
        return forest;
    }
}
