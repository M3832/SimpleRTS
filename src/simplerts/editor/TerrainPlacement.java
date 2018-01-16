/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.editor;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Markus
 */
public class TerrainPlacement {
    
    public Integer[] terrainSquare;
    public Integer[] alternatives;
    public BufferedImage[] tiles;
    private Color minimapColor;
    public String name;
    private final boolean walkable;
    
    public TerrainPlacement(Integer[] terrainSquare, Integer[] alternatives, BufferedImage[] tiles, String name, boolean walkable, Color color)
    {
        this.terrainSquare = terrainSquare;
        this.alternatives = alternatives;
        this.tiles = tiles;
        this.name = name;
        this.walkable = walkable;
        minimapColor = color;
    }
    
    public Integer[] getSquare()
    {
        return terrainSquare;
    }
    
    public Integer[] getAlternatives()
    {
        return alternatives;
    }
    
    public int getBaseTileId()
    {
        return terrainSquare[15];
    }
    
    public String getName()
    {
        return name;
    }
    
    public boolean isWalkable()
    {
        return walkable;
    }
    
    public Color getMinimapColor()
    {
        return minimapColor;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
}
