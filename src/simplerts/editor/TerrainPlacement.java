/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.editor;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Markus
 */
public class TerrainPlacement {
    
    public Integer[] terrainSquare;
    public Integer[] alternatives;
    
    public TerrainPlacement(Integer[] terrainSquare, Integer[] alternatives)
    {
        this.terrainSquare = terrainSquare;
        this.alternatives = alternatives;
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
}
