/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.entities.Building;
import simplerts.entities.Unit;
import simplerts.entities.Entity;
import simplerts.display.Assets;
import composite.GraphicsUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import simplerts.ui.MiniMap;

/**
 *
 * @author Markus
 */
public class Map {
    private final CopyOnWriteArrayList<Entity> entities;
    private final Cell[][] cells;
    private Handler handler;
    private PathFinder pathFinder;
    private Rectangle selectBox;
    
    public Map(int colSize, int rowSize)
    {
        entities = new CopyOnWriteArrayList();
        cells = new Cell[colSize][rowSize];

        for (Cell[] cell : cells) {
            for (int j = 0; j < cells[0].length; j++) {
                cell[j] = new Cell(Assets.grass);
            }
        }
        pathFinder = new PathFinder(this);
    }
    
    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }
    
    public void addEntity(Entity e)
    {
        entities.add(e);
        if(e instanceof Building)
        {
            for(int i = e.getGridX(); i < e.getGridX() + e.getGridWidth(); i++)
            {
                for(int j = e.getGridY(); j < e.getGridY() + e.getGridHeight(); j++)
                {
                    cells[i][j].available = false;
                }
            }
        } else if (e instanceof Unit)
        {
            ((Unit)e).setMap(this);
        }
    }
    
    public Cell[][] getCells()
    {
        return cells;
    }
    
    protected void updateOccupiedCells()
    {   
//        for(Entity e : entities)
//        {
//            for(int i = e.getCellX(); i < e.getCellWidth() + e.getCellX(); i++)
//            {
//                for(int j = e.getCellY(); j < e.getCellHeight() + e.getCellY(); j++)
//                {
//                    cells[i][j].available = false;
//                }
//            }
//        }
        for(Entity e : entities)
        {
            e.updateCells();
        }
    }
    
    public void render(Graphics g, float offsetX, float offsetY)
    {
        int startX = Math.max((int)offsetX / Game.CELLSIZE, 0);
        int endX = Math.min(2 + startX + handler.game.WIDTH / Game.CELLSIZE, cells.length);
        
        int startY = Math.max((int)offsetY / Game.CELLSIZE, 0);
        int endY = Math.min(1 + startY + handler.game.WIDTH / Game.CELLSIZE, cells[0].length);

        
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
                    if (inView(e, offsetX, offsetY)) {
                    e.render(g, offsetX, offsetY);
                    }
                });
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
    
    private boolean inView(Entity e, float offsetX, float offsetY)
    {
        int startX = Math.max((int)offsetX / Game.CELLSIZE, 0);
        int endX = Math.min(2 + startX + handler.game.WIDTH / Game.CELLSIZE, cells.length);
        
        int startY = Math.max((int)offsetY / Game.CELLSIZE, 0);
        int endY = Math.min(1 + startY + handler.game.WIDTH / Game.CELLSIZE, cells[0].length);
        
        return e.getGridX() > startX - 5 && e.getGridX() < endX && e.getGridY() > startY - 5 && e.getGridY() < endY;
    }
    
    public void update()
    {
        entities.stream().forEach(e -> e.update());
    }
    
    public boolean checkCollision(int cellX, int cellY)
    {
        if(cellX >= 0 && cellX < cells.length && cellY >= 0 && cellY < cells[0].length)
            return cells[cellX][cellY].available;
        return false;
    }
    
    public Dimension getSize()
    {
        return new Dimension(cells.length * Game.CELLSIZE, cells[0].length * Game.CELLSIZE);
    }
    
    public List<Entity> getEntitiesInSelection(Rectangle r)
    {
        ArrayList<Entity> list = new ArrayList<>();
        entities.stream().forEach(e -> {
            if(rectangleIntersectsEntity(r, e))
            {
                list.add(e);
            }
        });
        if(list.stream().anyMatch(e -> e instanceof Unit))
        {
            return list.stream().filter(e -> e instanceof Unit).collect(Collectors.toList());
        }
        
        return list;
    }
    
    public boolean checkUnitCollision(Rectangle r, Entity entity)
    {
       return entities.stream().anyMatch(e -> {if(e == entity){return false;} return rectangleIntersectsEntity(r, e);});
    }
    
    public CopyOnWriteArrayList<Entity> getEntities()
    {
        return entities;
    }
    
    private boolean rectangleIntersectsEntity(Rectangle r, Entity e)
    {
        return r.intersects(new Rectangle(e.getX() + 1, e.getY() + 1, e.getWidth() - 2, e.getHeight() - 2));
    }
    
    public MiniMap getMiniMap(int width, int height)
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
        return new MiniMap(GraphicsUtil.resize(minimap, width, height), squaresize, entities);
    }

    public boolean checkTerrainCollision(Rectangle rectangle) {
        for(int i = (int)rectangle.getX()/Game.CELLSIZE; i  <(int)rectangle.getX()/Game.CELLSIZE + 2; i++)
        {
            for(int j = (int)rectangle.getY()/Game.CELLSIZE; j < (int)rectangle.getY()/Game.CELLSIZE + 2; j++)
            {
                if(i < cells.length && j < cells[0].length && !cells[i][j].available)
                {
                    if(rectangle.intersects(new Rectangle(i * Game.CELLSIZE, j * Game.CELLSIZE, Game.CELLSIZE, Game.CELLSIZE)))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public PathFinder getPathFinder()
    {
        return pathFinder;
    }

    void setSelectBox(Rectangle selectBox) {
        this.selectBox = selectBox;
    }
}
