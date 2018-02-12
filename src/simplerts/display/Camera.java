/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.display;

import java.awt.Graphics;
import simplerts.Game;
import simplerts.Handler;
import simplerts.entities.Entity;

/**
 *
 * @author Markus
 */
public class Camera {
    
    private Handler handler;
    private float offsetX, offsetY;
    private int mouseX, mouseY;
    
    public Camera ()
    {
        offsetX = 0;
        offsetY = 0;
    }
    
    public Camera(Handler handler)
    {
        this.handler = handler;
    }
    
    public void addOffset(float x, float y){
        offsetX += x;
        offsetY += y;
        
        if((handler.map.getCells().length * Game.CELLSIZE) > handler.game.WIDTH)
        {
            checkOffsetBounds();           
        } else {
            offsetX = -(handler.game.WIDTH / 2 - (handler.map.getCells().length * Game.CELLSIZE) / 2);
        }
        
        if(handler.map.getCells()[0].length * Game.CELLSIZE > handler.game.HEIGHT)
        {
            checkOffsetBounds();
        } else {
            offsetY = -(handler.game.HEIGHT / 2 - (handler.map.getCells()[0].length * Game.CELLSIZE) / 2);
        }
    }
    
    public void setOffset(int x, int y)
    {
        offsetX = x;
        offsetY = y;
        checkOffsetBounds();
    }
    
    private void checkOffsetBounds()
    {
        if(offsetX < 0)
            offsetX = 0;
        if(offsetX > handler.map.getCells().length * Game.CELLSIZE - Game.WIDTH)
            offsetX = handler.map.getCells().length * Game.CELLSIZE - handler.game.WIDTH;
        if(offsetY < 0)
            offsetY = 0;
        if(offsetY > handler.map.getCells()[0].length * Game.CELLSIZE - handler.game.HEIGHT)
            offsetY = handler.map.getCells()[0].length * Game.CELLSIZE - handler.game.HEIGHT;
    }
    
//    private void repaintGUI()
//    {
//        handler.game.renderGUI();
//    }
    
    public void render(Graphics g)
    {
        handler.renderMap.render(g, this);
    }
    
    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }
    
    public void centerOnEntity(Entity e)
    {
        setOffset(e.getX() - Game.WIDTH/2 + e.getWidth()/2, e.getY() - Game.HEIGHT/2 + e.getHeight()/2);
    }
    
    public float getOffsetX()
    {
        return offsetX;
    }
    
    public float getOffsetY()
    {
        return offsetY;
    }
    
    public void setFinalMouse(int x, int y)
    {
        mouseX = x + (int)offsetX;
        mouseY = y + (int)offsetY;
    }
    
    public int getMouseX() {
        return mouseX;
    }
    
    public int getMouseY() {
        return mouseY;
    }
    
    public int getMouseGridX() {
        return mouseX / Game.CELLSIZE;
    }
    
    public int getMouseGridY() {
        return mouseY / Game.CELLSIZE;
    }
}
