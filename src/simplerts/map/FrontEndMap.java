/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.map;

import composite.GraphicsUtil;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import simplerts.Controller;
import simplerts.Game;
import simplerts.Player;
import simplerts.display.Camera;
import simplerts.entities.Entity;
import simplerts.gfx.effects.Effect;
import simplerts.ui.MiniMap;

/**
 *
 * @author Markus
 */
public class FrontEndMap {
    
    private final BackEndMap map;
    private final Cell[][] cells;
    private final CopyOnWriteArrayList<Entity> entities;
    private final CopyOnWriteArrayList<Effect> effects;
    private final boolean[][] mapVisibility;
    private Controller controller;
    
    public FrontEndMap(BackEndMap map)
    {
        this.map = map;
        this.cells = map.getCells();
        this.entities = map.getEntities();
        mapVisibility = new boolean[cells.length][cells[0].length];
        effects = new CopyOnWriteArrayList<>();
    }
    
    public void setController(Controller controller)
    {
        this.controller = controller;
    }
    
    public void update()
    {
        if(controller != null)
        {
            for(Entity e : entities)
            {
                if(e.getPlayer() == controller.getPlayer())
                {
                    int tempX = e.getGridX() - e.getViewRange();
                    int tempWidth = e.getGridX() + e.getGridWidth() + e.getViewRange();
                    int tempY = e.getGridY() - e.getViewRange();
                    int tempHeight = e.getGridY() + e.getGridHeight() + e.getViewRange();
                    
                    for(int x = tempX; x < tempWidth; x++)
                    {
                        for(int y = tempY; y < tempHeight; y++)
                        {
                            if(map.isInBounds(x, y) && !mapVisibility[x][y])
                            {
                                if((x != tempX || y != tempY) && (x != tempWidth - 1 || y != tempHeight - 1) && (x != tempWidth - 1 || y != tempY) && (x != tempX || y != tempHeight - 1))
                                    mapVisibility[x][y] = true;
                            }
                        }
                    }
                }
            }            
        }
        
        for(int i = effects.size() - 1; i >= 0; i--){
            effects.get(i).update();
            if(effects.get(i).timeToRemove()){
                effects.remove(effects.get(i));
            }
        }
        
    }
    
    public void render(Graphics g, Camera camera)
    {
        int startX = Math.max((int)camera.getOffsetX() / Game.CELLSIZE, 0);
        int endX = Math.min(2 + startX + map.getHandler().game.WIDTH / Game.CELLSIZE, cells.length);
        
        int startY = Math.max((int)camera.getOffsetY() / Game.CELLSIZE, 0);
        int endY = Math.min(1 + startY + map.getHandler().game.WIDTH / Game.CELLSIZE, cells[0].length);

        
        for(int i = startX; i < endX; i++)
        {
            for(int j = startY; j < endY; j++)
            {
                    g.drawImage(cells[i][j].getImage(), (int)((i * Game.CELLSIZE) - camera.getOffsetX()), (int)((j * Game.CELLSIZE) - camera.getOffsetY()), Game.CELLSIZE, Game.CELLSIZE, null);
            }
        }
//        renderGrid(g, (int)offsetX, (int)offsetY);
        
        entities.stream()
                .sorted((e1, e2) -> { return Integer.compare(e1.getGridY(), e2.getGridY());})
                .sorted((e1, e2) -> {return Boolean.compare(e2.isDead(), e1.isDead());})
                .forEach(e -> {
                    if (inView(e, camera.getOffsetX(), camera.getOffsetY()) && e.isVisible()) {
                        e.render(g, camera);
                    }
                });
        
        for(int i = startX; i < endX; i++)
        {
            for(int j = startY; j < endY; j++)
            {
                if(!mapVisibility[i][j])
                {
                    boolean neighborVisible = false;
                    for(int x = i-1; x < i + 2; x++)
                    {
                        for(int y = j-1; y < j + 2; y++)
                        {
                            if(map.isInBounds(x, y))
                            {
                                if(mapVisibility[x][y])
                                {
                                    neighborVisible = true;
                                }
                            }
                        }
                    }
                    g.setColor(neighborVisible ? new Color(0, 0, 0, 100) : Color.black);
                    g.fillRect((int)((i * Game.CELLSIZE) - camera.getOffsetX()), (int)((j * Game.CELLSIZE) - camera.getOffsetY()), Game.CELLSIZE, Game.CELLSIZE);
                }
            }
        }
        effects.stream().forEach((Effect e) -> e.render(g, camera));
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

        return new MiniMap(getMinimapImage(width, height, false), squaresize, this);
    }
    
    public BufferedImage getMinimapImage(int width, int height, boolean editor)
    {
        BufferedImage minimap = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = minimap.createGraphics();
        float tempSquaresize = Math.max((float)1000/cells.length, (float)1000/cells[0].length);
        float squaresize = Math.min((float)width/cells.length, (float)height/cells[0].length);
        
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[0].length; j++)
            {
                if(mapVisibility[i][j] || editor)
                {
                    g.drawImage(GraphicsUtil.resize((BufferedImage)cells[i][j].getImage(), (int)tempSquaresize, (int)tempSquaresize), i * (int)tempSquaresize, j * (int)tempSquaresize, null);
                } else {
                    g.setColor(Color.black);
                    g.fillRect(i * (int)tempSquaresize, j * (int)tempSquaresize, (int)tempSquaresize, (int)tempSquaresize);
                }
            }
        }
        return GraphicsUtil.resize(minimap, width, height);
    }

    public BackEndMap getBackEnd() {
        return map;
    }
    
    public Cell[][] getCells()
    {
        return cells;
    }

    public boolean isNotMasked(int x, int y) {
        if(map.isInBounds(x, y))
            return mapVisibility[x][y];
        return false;
    }

    public boolean isNotMasked(Entity e) {
        for(int x = e.getGridX(); x < e.getGridX() + e.getGridWidth(); x++)
        {
            for(int y = e.getGridY(); y < e.getGridY() + e.getGridHeight(); y++)
            {
                if(map.isInBounds(x, y) && mapVisibility[x][y])
                    return true;
            }
        }
        return false;
    }

    public Player getControllingPlayer() {
        return controller.getPlayer();
    }

    public void showAll() {
        for (boolean[] mapVisibility1 : mapVisibility) {
            for (int j = 0; j < mapVisibility[0].length; j++) {
                mapVisibility1[j] = true;
            }
        }
    }
    
    public void addEffect(Effect e){
        effects.add(e);
    }
    
}
