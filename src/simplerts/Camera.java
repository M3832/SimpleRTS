/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Graphics;

/**
 *
 * @author Markus
 */
public class Camera {
    
    private Handler handler;
    private float offsetX, offsetY;
    
    public Camera ()
    {
        offsetX = 0;
        offsetY = 0;
    }
    
    public void addOffset(int x, int y){
        offsetX += x;
        offsetY += y;
        
        if((handler.map.getCells().length * Game.CELLSIZE) > handler.game.WIDTH)
        {
            if(offsetX < 0)
                offsetX = 0;
            if(offsetX > handler.map.getCells().length * Game.CELLSIZE - handler.game.WIDTH + 15)
                offsetX = handler.map.getCells().length * Game.CELLSIZE - handler.game.WIDTH + 15;            
        } else {
            offsetX = -(handler.game.WIDTH / 2 - (handler.map.getCells().length * Game.CELLSIZE) / 2);
        }
        
        if(handler.map.getCells()[0].length * Game.CELLSIZE > handler.game.HEIGHT)
        {
            if(offsetY < 0)
                offsetY = 0;
            if(offsetY > handler.map.getCells()[0].length * Game.CELLSIZE - handler.game.HEIGHT + 15)
            {
                offsetY = handler.map.getCells()[0].length * Game.CELLSIZE - handler.game.HEIGHT + 15;
            }
        } else {
            offsetY = -(handler.game.HEIGHT / 2 - (handler.map.getCells()[0].length * Game.CELLSIZE) / 2);
        }
    }
    
    public void render(Graphics g)
    {
        handler.map.render(g, offsetX, offsetY);
    }
    
    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }
    
    public float getOffsetX()
    {
        return offsetX;
    }
    
    public float getOffsetY()
    {
        return offsetY;
    }
}
