/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Markus
 */
public class Map {
    private final CopyOnWriteArrayList<Entity> entities;
    private final Cell[][] cells;
    private Handler handler;
    
    public Map(int colSize, int rowSize)
    {
        entities = new CopyOnWriteArrayList();
        cells = new Cell[colSize][rowSize];

        for (Cell[] cell : cells) {
            for (int j = 0; j < cells[0].length; j++) {
                cell[j] = new Cell(SpriteHolder.grass);
            }
        }
    }
    
    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }
    
    public void addEntity(Entity e)
    {
        entities.add(e);
        updateOccupiedCells();
    }
    
    public Cell[][] getCells()
    {
        return cells;
    }
    
    protected void updateOccupiedCells()
    {
        for(Cell[] cell : cells)
        {
            for(Cell c : cell)
            {
                c.available = true;
            }
        }
        
        for(Entity e : entities)
        {
            for(int i = e.getCellX(); i < e.getCellWidth() + e.getCellX(); i++)
            {
                for(int j = e.getCellY(); j < e.getCellHeight() + e.getCellY(); j++)
                {
                    cells[i][j].available = false;
                }
            }
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
        
        entities.stream()
                .sorted((e1, e2) -> { return Integer.compare(e1.getCellY(), e2.getCellY());})
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
    
    public void renderGrid(Graphics g)
    {
        g.setColor(Color.YELLOW);
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[0].length; j++)
            {
                g.drawRect(i * Game.CELLSIZE, j * Game.CELLSIZE, Game.CELLSIZE, Game.CELLSIZE);
            }
        }
    }
    
    private boolean inView(Entity e, float offsetX, float offsetY)
    {
        int startX = Math.max((int)offsetX / Game.CELLSIZE, 0);
        int endX = Math.min(2 + startX + handler.game.WIDTH / Game.CELLSIZE, cells.length);
        
        int startY = Math.max((int)offsetY / Game.CELLSIZE, 0);
        int endY = Math.min(1 + startY + handler.game.WIDTH / Game.CELLSIZE, cells[0].length);
        
        return e.getCellX() > startX - 5 && e.getCellX() < endX && e.getCellY() > startY - 5 && e.getCellY() < endY;
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
    
    public static Map loadMap(String url)
    {
        return loadMap(new File(url));
    }
    
    public static Map loadMap(File file)
    {
        Scanner in;
        ArrayList<String> mapData = new ArrayList<>();
        int width = 0, height = 0;
        Map map = new Map(1, 1);
        try {
            in = new Scanner(file);
            while(in.hasNextLine())
            {
                mapData.add(in.nextLine());
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        
        
        int tileIndex = 0;
        boolean tiles = false;
        for(int i = 0; i < mapData.size(); i++)
        {
            String[] tokens = mapData.get(i).split(",");
            if(i == 0)
            {
                width = Integer.parseInt(tokens[0]);
                height = Integer.parseInt(tokens[1]);
                map = new Map(width, height);
            }
            
            if(tiles)
            {
                for(int j = 0; j < tokens.length; j++)
                {
                    map.getCells()[j][tileIndex].setImage(SpriteHolder.getTile(Integer.parseInt(tokens[j])));
                }
                tileIndex++;
            }
            
            if(tokens[0].equalsIgnoreCase("tiles"))
            {
                System.out.println("tokens is true");
                tiles = true;
            }
        }
        
        return map;
    }
    
    public static void saveMap(File file, Map map) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getAbsolutePath()), "utf-8"))) {
            writer.write(map.getCells().length + "," + map.getCells()[0].length + System.getProperty("line.separator"));
            writer.write("tiles," + System.getProperty("line.separator"));
            for(int i = 0; i < map.getCells().length; i++)
            {
                for(int j = 0; j < map.getCells()[0].length; j++)
                {
                    writer.write(map.getCells()[j][i].getTileId() + ",");
                }
                writer.write(System.getProperty("line.separator"));
            }
        } catch (Exception e) {

        }
    }
    
    public Dimension getSize()
    {
        return new Dimension(cells.length * Game.CELLSIZE, cells[0].length * Game.CELLSIZE);
    }
}
