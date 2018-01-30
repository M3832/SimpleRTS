/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.map;

import composite.GraphicsUtil;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.display.Camera;
import simplerts.entities.Entity;
import simplerts.ui.MiniMap;

/**
 *
 * @author Markus
 */
public class FrontEndMap {
    
    private BackEndMap map;
    private Cell[][] cells;
    private CopyOnWriteArrayList<Entity> entities;
    private Camera camera;
    
    public FrontEndMap(BackEndMap map)
    {
        this.map = map;
        this.cells = map.getCells();
        this.entities = map.getEntities();
        camera = new Camera();
    }
    
    public void render(Graphics g, float offsetX, float offsetY)
    {
        int startX = Math.max((int)offsetX / Game.CELLSIZE, 0);
        int endX = Math.min(2 + startX + map.getHandler().game.WIDTH / Game.CELLSIZE, cells.length);
        
        int startY = Math.max((int)offsetY / Game.CELLSIZE, 0);
        int endY = Math.min(1 + startY + map.getHandler().game.WIDTH / Game.CELLSIZE, cells[0].length);

        
        for(int i = startX; i < endX; i++)
        {
            for(int j = startY; j < endY; j++)
            {
                g.drawImage(cells[i][j].getImage(), (int)((i * Game.CELLSIZE) - offsetX), (int)((j * Game.CELLSIZE) - offsetY), Game.CELLSIZE, Game.CELLSIZE, null);
            }
        }
//        renderGrid(g, (int)offsetX, (int)offsetY);
        
        entities.stream()
                .sorted((e1, e2) -> { return Integer.compare(e1.getGridY(), e2.getGridY());})
                .forEach(e -> {
                    if (inView(e, offsetX, offsetY) && e.isVisible()) {
                        e.render(g);
                    }
                });
    }
    
    public boolean inView(Entity e, float offsetX, float offsetY)
    {
        int startX = Math.max((int)offsetX / Game.CELLSIZE, 0);
        int endX = Math.min(2 + startX + map.getHandler().game.WIDTH / Game.CELLSIZE, cells.length);
        
        int startY = Math.max((int)offsetY / Game.CELLSIZE, 0);
        int endY = Math.min(1 + startY + map.getHandler().game.WIDTH / Game.CELLSIZE, cells[0].length);
        
        return e.getGridX() > startX - 5 && e.getGridX() < endX && e.getGridY() > startY - 5 && e.getGridY() < endY;
    }
    
    public void renderMapEditor(Graphics g)
    {
        int startX = 0;
        int endX = cells.length;
        
        int startY = 0;
        int endY = cells[0].length;
        
        for(int i = startX; i < endX; i++)
        {
            for(int j = startY; j < endY; j++)
            {
                g.drawImage(cells[i][j].getImage(), (int)((i * Game.CELLSIZE)), (int)((j * Game.CELLSIZE)), Game.CELLSIZE, Game.CELLSIZE, null);
            }
        }
    }
    
    public void renderGrid(Graphics g, int offsetX, int offsetY)
    {
        g.setColor(Color.YELLOW);
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[0].length; j++)
            {
                g.drawRect(i * Game.CELLSIZE - offsetX, j * Game.CELLSIZE - offsetY, Game.CELLSIZE, Game.CELLSIZE);
            }
        }
    }
    
    public MiniMap getMiniMap(int width, int height)
    {
        float squaresize = Math.min((float)width/cells.length, (float)height/cells[0].length);

        return new MiniMap(getMinimapImage(width, height), squaresize, entities);
    }
    
    public BufferedImage getMinimapImage(int width, int height)
    {
        BufferedImage minimap = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = minimap.createGraphics();
        float tempSquaresize = Math.max((float)1000/cells.length, (float)1000/cells[0].length);
        float squaresize = Math.min((float)width/cells.length, (float)height/cells[0].length);
        
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[0].length; j++)
            {
                g.drawImage(GraphicsUtil.resize((BufferedImage)cells[i][j].getImage(), (int)tempSquaresize, (int)tempSquaresize), i * (int)tempSquaresize, j * (int)tempSquaresize, null);
            }
        }
        return GraphicsUtil.resize(minimap, width, height);
    }

    public BackEndMap getBackEnd() {
        return map;
    }
    
    public Camera getCamera()
    {
        return camera;
    }
    
    public Cell[][] getCells()
    {
        return cells;
    }
    
}
