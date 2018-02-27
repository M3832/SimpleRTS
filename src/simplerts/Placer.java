/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.map.Destination;
import simplerts.entities.Building;
import simplerts.entities.Entity;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import simplerts.entities.Unit;
import simplerts.entities.actions.Attack;
import simplerts.entities.actions.Build;
import simplerts.entities.actions.Chop;
import simplerts.entities.actions.Gather;
import simplerts.entities.actions.MoveAttack;
import simplerts.entities.actions.MoveTo;
import simplerts.entities.interfaces.GoldProvider;
import simplerts.entities.interfaces.Lumberman;
import simplerts.entities.units.Builder;
import simplerts.gfx.effects.MoveConfirm;

/**
 *
 * @author Markus
 */
public class Placer {
    
    private Entity entity;
    private String action;
    private Controller controller;
    private Color color;
    private Color errorColor;
    private int gridX, gridY, cellWidth, cellHeight, x, y;
    private BufferedImage image;
    
    public Placer(Controller controller)
    {
        color = new Color(0, 0, 255, 150);
        errorColor = new Color(255, 0, 0, 150);
        entity = null;
        action = "none";
        this.controller = controller;
    }
    
    public void setEntity(Entity entity)
    {
        action = "none";
        this.entity = entity;
        gridX = 0;
        gridY = 0;
        cellWidth = entity.getGridWidth();
        cellHeight = entity.getGridHeight();
    }
    
    public void setAction(String action){
        entity = null;
        this.action = action;
    }
    
    public void clear()
    {
        entity = null;
        action = "none";
    }
    
    public void render(Graphics g)
    {
        int offsetCellX = (int)controller.getCamera().getOffsetX() / Game.CELLSIZE;
        int offsetCellY = (int)controller.getCamera().getOffsetY() / Game.CELLSIZE;
        
        if(entity instanceof Building)
        {
            image = ((Building)entity).getFinalSprite();
        }
        for(int i = gridX; i < cellWidth + gridX; i++)
        {
            for(int j = gridY; j < cellHeight + gridY; j++)
            {
                if(i >= 0 && j >= 0 && i < controller.getMap().getCells().length && j < controller.getMap().getCells()[0].length)
                {
                    g.setColor(!controller.getMap().getBackEnd().checkCollision(i, j) ? color : errorColor);
                    g.fillRect(((i - offsetCellX) * Game.CELLSIZE) - (int)controller.getCamera().getOffsetX() % Game.CELLSIZE, (j - offsetCellY) * Game.CELLSIZE - (int)controller.getCamera().getOffsetY() % Game.CELLSIZE, Game.CELLSIZE, Game.CELLSIZE);
                }
            }
        }
        ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g.drawImage(image, ((gridX - offsetCellX) * Game.CELLSIZE) - (int)controller.getCamera().getOffsetX() % Game.CELLSIZE, (gridY - offsetCellY) * Game.CELLSIZE - (int)controller.getCamera().getOffsetY() % Game.CELLSIZE, Game.CELLSIZE * cellWidth, Game.CELLSIZE * cellHeight, null);
//        g.setColor(color);
//        g.fillRect(cellX * BackEndMap.CELLSIZE, cellY * BackEndMap.CELLSIZE, cellSize * BackEndMap.CELLSIZE, cellSize * BackEndMap.CELLSIZE);
    }
    
    public int getWidth(){
        return entity.getWidth();
    }
    
    public int getHeight(){
        return entity.getHeight();
    }
    
    public void setPosition(int x, int y){
        this.gridX = x/Game.CELLSIZE;
        this.gridY = y/Game.CELLSIZE;
        this.x = x;
        this.y = y;
    }
    
    public boolean isPlaceable(Entity excludeThis){
        boolean placeable = true;
        for(int i = gridX; i < cellWidth + gridX; i++)
        {
            for(int j = gridY; j < cellHeight + gridY; j++)
            {
                if(controller.getMap().getBackEnd().checkCollision(i, j, excludeThis))
                {
                    placeable = false;
                }
            }
        }
        return placeable;
    }
    
    public Destination getDestination()
    {
        return new Destination(gridX, gridY);
    }
    
    public Entity getEntity()
    {
        return entity;
    }
    
    public boolean hasEntity()
    {
        return entity != null;
    }

    public void place(Entity owner) {
        Entity e = owner.getMap().getEntityFromCell(gridX, gridY);
        if(!action.equals("none") && owner instanceof Unit){
            controller.getMap().addEffect(new MoveConfirm(controller.getCamera().getMouseX() - Game.CELLSIZE/2, controller.getCamera().getMouseY() - Game.CELLSIZE/2));
            ((Unit)owner).clearActions();
            switch (action){
                case "move":
                    ((Unit)owner).addAction(new MoveTo((Unit)owner, new Destination(gridX, gridY)));
                    break;
                case "attack":
                    if(e != null){
                        ((Unit)owner).addAction(new Attack((Unit)owner, e));
                    } else {
                        ((Unit)owner).addAction(new MoveAttack((Unit)owner, new Destination(gridX, gridY)));
                    }
                    break;
                case "gather":
                    if(e != null && e instanceof GoldProvider){
                        ((Unit)owner).addAction(new Gather((Unit)owner, (GoldProvider)e));
                    } else if (owner.getMap().getCells()[gridX][gridY].isForest() && !owner.getMap().getCells()[gridX][gridY].getForest().isBarren() && owner instanceof Lumberman){
                        ((Unit)owner).addAction(new Chop((Unit)owner, owner.getMap().getCells()[gridX][gridY]));
                    }
                    break;
            }
        }
        
        if(entity != null && owner instanceof Builder){
            Building building = (Building)entity.duplicate();
            building.setPosition(getDestination().getGridX() * Game.CELLSIZE, getDestination().getGridY() * Game.CELLSIZE);
            ((Unit)owner).clearActions();
            ((Unit)owner).addAction(new Build((Builder)owner, building));
        }
        entity = null;
        action = "none";
        owner.setDefaultMenu();
    }

    boolean hasSomething() {
        return !action.equals("none") || entity != null;
    }
}
