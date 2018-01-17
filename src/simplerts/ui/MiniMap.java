/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import simplerts.Game;
import simplerts.input.Clicker;

/**
 *
 * @author Markus
 */
public class MiniMap {
    
    private BufferedImage minimap;
    private float squaresize;
    private float pixelRatio;
    
    public MiniMap(BufferedImage minimap, float squaresize)
    {
        this.minimap = minimap;
        this.squaresize = squaresize;
        this.pixelRatio = squaresize/Game.CELLSIZE;
        System.out.println("Minimap bounds: " + minimap.getWidth() + " " + minimap.getHeight());
    }
    
    public BufferedImage getMiniMap()
    {
        return minimap;
    }
    
    public float getSquareSize()
    {
        return squaresize;
    }
    
    public float getPixelRatio()
    {
        return pixelRatio;
    }
}
