/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.entities.Entity;
import simplerts.Game;

/**
 *
 * @author Markus
 */
public class MiniMap {
    
    private BufferedImage minimap;
    private float squaresize;
    private float pixelRatio;
    private CopyOnWriteArrayList<Entity> entities;
    
    public MiniMap(BufferedImage minimap, float squaresize, CopyOnWriteArrayList<Entity> entities)
    {
        this.minimap = minimap;
        this.squaresize = squaresize;
        this.pixelRatio = squaresize/Game.CELLSIZE;
        this.entities = entities;
    }
    
    public BufferedImage getMiniMap()
    {
        return minimap;
    }
    
    public BufferedImage getMinimapWithEntities()
    {
        BufferedImage i = new BufferedImage(minimap.getWidth(), minimap.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D)i.getGraphics();
        g.drawImage(minimap, 0, 0, null);
        for(Entity e: entities)
        {
            g.fillRect((int)(e.getGridX() * squaresize), (int)(e.getGridY() * squaresize), (int)(squaresize * e.getGridWidth()), (int)(squaresize * e.getGridHeight()));
        }
        return i;
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
