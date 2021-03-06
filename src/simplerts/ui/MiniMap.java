/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.entities.Entity;
import simplerts.Game;
import simplerts.map.FrontEndMap;

/**
 *
 * @author Markus
 */
public class MiniMap {
    
    private BufferedImage minimap;
    private float squaresize;
    private float pixelRatio;
    private FrontEndMap renderMap;
    
    public MiniMap(BufferedImage minimap, float squaresize, FrontEndMap renderMap)
    {
        this.minimap = minimap;
        this.squaresize = squaresize;
        this.pixelRatio = squaresize/Game.CELLSIZE;
        this.renderMap = renderMap;
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
        for(Entity e: renderMap.getBackEnd().getEntities())
        {
            g.setColor(e.getPlayer() == renderMap.getControllingPlayer() ? Color.WHITE : e.getColor() );
            if(renderMap.isNotMasked(e) && !e.isDead() && e.isVisible())
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

    public void setImage(BufferedImage minimapImage) {
        minimap = minimapImage;
    }
}
