/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.ui;

import java.awt.image.BufferedImage;

/**
 *
 * @author Markus
 */
public class MiniMap {
    
    private BufferedImage minimap;
    private float squaresize;
    
    public MiniMap(BufferedImage minimap, float squaresize)
    {
        this.minimap = minimap;
        this.squaresize = squaresize;
    }
    
    public BufferedImage getMiniMap()
    {
        return minimap;
    }
    
    public float getSquareSize()
    {
        return squaresize;
    }
}
