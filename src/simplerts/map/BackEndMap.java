/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.map;

import simplerts.entities.Unit;
import simplerts.entities.Entity;
import simplerts.gfx.Assets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import simplerts.Game;
import simplerts.Handler;
import simplerts.Player;
import simplerts.editor.LoadedObject;
import simplerts.entities.projectiles.Projectile;
import simplerts.entities.buildings.TownHall;
import simplerts.entities.resources.Goldmine;
import simplerts.entities.units.Archer;
import simplerts.entities.units.Builder;
import simplerts.entities.units.Footman;
import simplerts.entities.units.Healer;

/**
 *
 * @author Markus
 */
public class BackEndMap {
    
    private final CopyOnWriteArrayList<Entity> entities;
    private CopyOnWriteArrayList<Player> players;
    private final Cell[][] cells;
    private final ArrayList<LoadedObject> loadObjects;
    private final ArrayList<Destination> startLocations; 
    private final PathFinder pathFinder;
    private Handler handler;
    private Player neutral;
    
    public BackEndMap(int colSize, int rowSize)
    {
        entities = new CopyOnWriteArrayList();
        cells = new Cell[colSize][rowSize];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new Cell(i, j);
                cells[i][j].setTerrain(Assets.grass);
            }
        }
        pathFinder = new PathFinder(this);
        loadObjects = new ArrayList<>();
        startLocations = new ArrayList<>();
        players = new CopyOnWriteArrayList<>();
    }
    
    public void setHandler(Handler handler)
    {
        this.handler = handler;
        setNeutral(new Player(handler, Color.LIGHT_GRAY));
    }
    
    public void addEntity(Entity e)
    {
        entities.add(e);
        updateEntityCell(e.getGridX(), e.getGridY(), e);
        e.getPlayer().addEntity(e);
    }
    
    public void start(CopyOnWriteArrayList<Player> players, int startGold, int startLumber, int startWorkers)
    {
        this.players = players;
        players.stream().map((p) -> {
            p.setGold(startGold);
            p.setLumber(startLumber);
            return p;
        }).filter((p) -> (!startLocations.isEmpty())).forEach((p) -> {
            Destination startLocation = startLocations.get((int)(Math.random() * (startLocations.size())));
            TownHall th = new TownHall(startLocation.getGridX(), startLocation.getGridY(), 4, p, true);
            addEntity(th);
            for(int i = 0; i < startWorkers; i++)
            {
                Destination target = getAvailableNeighborCell(th);
                Builder b = new Builder(target.getGridX(), target.getGridY(), p);
                addEntity(b);
            }
            startLocations.remove(startLocation);
        });
    }
    
    public Cell[][] getCells()
    {
        return cells;
    }
    
    public void update()
    {
        entities.stream().forEach(e -> e.update());
        players.stream().forEach((Player p) -> p.update());
        for(int i = entities.size() - 1; i >= 0; i--)
        {
            if(entities.get(i).timeToRemove())
            {
                entities.get(i).getPlayer().removeEntity(entities.get(i));
                for(int x = entities.get(i).getGridX(); x < entities.get(i).getGridX() + entities.get(i).getGridWidth(); x++)
                {
                    for(int y = entities.get(i).getGridY(); y < entities.get(i).getGridY() + entities.get(i).getGridHeight(); y++)
                    {
                        cells[x][y].setEntity(null);
                    }
                }
                entities.remove(entities.get(i));
            }
        }
    }
    
    public boolean checkCollision(int cellX, int cellY)
    {
        if(isInBounds(cellX, cellY))
        {
            return isCellBlocked(cellX, cellY);
        }
        return false;
    }
    
    public boolean checkCollision(int cellX, int cellY, Entity excludeThis)
    {
        if(cellX >= 0 && cellX < cells.length && cellY >= 0 && cellY < cells[0].length)
        {
            return isCellBlocked(cellX, cellY, excludeThis);
        }
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
            if(e.isVisible() && !e.isDead() && rectangleIntersectsEntity(r, e))
            {
                list.add(e);
            }
        });

        
        return filterList(list);
    }
    
    public List<Entity> filterList(List<Entity> list)
    {
        if(list.stream().anyMatch(e -> e instanceof Unit))
        {
            list = list.stream().filter(e -> e instanceof Unit).collect(Collectors.toList());
        }
        
        return list;
    }
    
    public boolean checkUnitCollision(Rectangle r, Entity entity)
    {
       return entities.stream().anyMatch(e -> {if(e == entity || !e.isVisible() || e.isDead()){return false;} return rectangleIntersectsEntity(r, e);});
    }
    
    public CopyOnWriteArrayList<Entity> getEntities()
    {
        return entities;
    }
    
    private boolean rectangleIntersectsEntity(Rectangle r, Entity e)
    {
        return r.intersects(new Rectangle(e.getX() + 1, e.getY() + 1, e.getWidth() - 2, e.getHeight() - 2));
    }

    public boolean checkTerrainCollision(Rectangle rectangle) {
        for(int i = (int)rectangle.getX()/Game.CELLSIZE; i  <(int)rectangle.getX()/Game.CELLSIZE + 2; i++)
        {
            for(int j = (int)rectangle.getY()/Game.CELLSIZE; j < (int)rectangle.getY()/Game.CELLSIZE + 2; j++)
            {
                if(isInBounds(i, j) && !cells[i][j].available)
                {
                    if(rectangle.intersects(new Rectangle(i * Game.CELLSIZE, j * Game.CELLSIZE, Game.CELLSIZE, Game.CELLSIZE)) || !isInBounds(i, j))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public Destination getAvailableNeighborCell(Entity e)
    {
        Destination d = new Destination(-1, -1);
        if(e != null)
        {
            for(int x = e.getGridX() - 1; x <= e.getGridX() + e.getGridWidth(); x++)
            {
                for(int y = e.getGridY() - 1; y <= e.getGridY() + e.getGridHeight(); y++)
                {
                    if(isInBounds(x, y) && !isCellBlocked(x, y))
                    {
                        if(!checkUnitCollision(new Rectangle(x * Game.CELLSIZE, y * Game.CELLSIZE, Game.CELLSIZE, Game.CELLSIZE), e))
                            return new Destination(x, y);
                    }
                }            
            }            
        }
        
        if(d.isEmpty())
        {
            if( e != null && isInBounds(e.getGridX() - 1, e.getGridY() - 1) && getAvailableNeighborCell(cells[e.getGridX()-1][e.getGridY()-1].getEntity()) != null)
            {
                d = getAvailableNeighborCell(cells[e.getGridX()-1][e.getGridY()-1].getEntity());
            } else if (e != null) {
                d = e.getDestination();
            }
        }

        
        return d;
    }
    
    private boolean isCellBlocked(int x, int y)
    {
        return cells[x][y].getEntity() != null || !cells[x][y].available;
    }
    
    private boolean isCellBlocked(int x, int y, Entity excludeEntity)
    {
        return (cells[x][y].getEntity() != null && cells[x][y].getEntity() != excludeEntity && !cells[x][y].getEntity().isDead()) || !cells[x][y].available;
    }
    
    public void updateEntityCell(int x, int y, Entity e)
    {
        if(e != null)
        {
            for(int x1 = e.getGridX(); x1 < e.getGridX()+e.getGridWidth(); x1++)
            {
                for(int y1 = e.getGridY(); y1 < e.getGridY()+e.getGridHeight(); y1++)
                {
                    if(isInBounds(x1, y1))
                        cells[x1][y1].setEntity(e);
                }
            }
        } else {
            cells[x][y].setEntity(e);
        }
    }
    
    public PathFinder getPathFinder()
    {
        return pathFinder;
    }
    
    public Handler getHandler()
    {
        return handler;
    }

    public void setEntityPosition(Entity entity, Destination destination) {
        entity.setPosition(destination);
        updateEntityCell(entity.getGridX(), entity.getGridY(), entity);
    }

    /**
     * @return the neutral
     */
    public Player getNeutral() {
        return neutral;
    }

    /**
     * @param neutral the neutral to set
     */
    public void setNeutral(Player neutral) {
        this.neutral = neutral;
    }
    
    public void placeLoadedObjects()
    {
        loadObjects.stream().filter((o) -> (o.name.equals("Goldmine"))).forEach((o) -> {
            addEntity(new Goldmine(o.gridX, o.gridY, getNeutral()));
        });
    }
    
    public Destination getClosestCell(Entity owner, Entity target)
    {
        Integer[][] destinations = new Integer[target.getGridWidth() + 2][target.getGridHeight() + 2];
        
        for(int x = target.getGridX() - 1; x < target.getGridX() + target.getGridWidth() + 1; x++)
        {
            for(int y = target.getGridY() - 1; y < target.getGridY() + target.getGridHeight() + 1; y++)
            {
                destinations[x - target.getGridX() + 1][y - target.getGridY() + 1] = Math.abs(owner.getGridX() - x) + (Math.abs(owner.getGridY() - y));
            }
        }
        int indexX = 0, indexY = 0, score = 100;
        for(int x = 0; x < destinations.length; x++)
        {
            for(int y = 0; y < destinations[0].length; y++)
            {
                if(destinations[x][y] < score && !checkCollision(target.getGridX() - 1 + x, target.getGridY() - 1 + y, owner))
                {
                    score = destinations[x][y];
                    indexX = x;
                    indexY = y;
                } else {
                }
            }
        }
        return new Destination(target.getGridX() - 1 + indexX, target.getGridY() - 1 + indexY);
    }
    
    public Destination getClosestCell(Entity e, Destination d)
    {
        return getClosestCell(e, new Builder(d.getGridX(), d.getGridY(), neutral));
    }
    
    public Cell findLumberCloseTo(Destination d, int squaresAround)
    {
        int maxSquaresAround = 3;
        
        Rectangle area = getArea(d, squaresAround);
        
        for(int x = (int)area.getX() - squaresAround; x < (int)area.getX() + (int)area.getWidth(); x++){
            for(int y = (int)area.getY() - squaresAround; y < (int)area.getY() + (int)area.getHeight(); y++)
            {
                if(isInBounds(x, y) && cells[x][y].isForest() && !cells[x][y].getForest().isBarren())
                {
                    return cells[x][y];
                }
            }
        }
        
        if(squaresAround > maxSquaresAround)
        {
            return cells[d.getGridX()][d.getGridY()];
        } else {
            return findLumberCloseTo(d, ++squaresAround);
        }
    }

    public Entity getEntityFromCell(int gridX, int gridY) {
        if(isInBounds(gridX, gridY))
        {
            return cells[gridX][gridY].getEntity();
        }
        return null;
    }
    
    public Rectangle getArea(Destination d, int squaresAround)
    {
        int x = d.getGridX();
        int y = d.getGridY();
        int width = 1 + 2 * squaresAround;
        int height = 1 + 2 * squaresAround;
        return new Rectangle(x, y, width, height);
    }
    
    public boolean isInBounds(int gridX, int gridY)
    {
        return gridX >= 0 && gridX < cells.length && gridY >= 0 && gridY < cells[0].length;
    }

    public void addLoadedObject(String name, int gridX, int gridY) {
        if(name.equals("Goldmine"))
            loadObjects.add(new LoadedObject("Goldmine", gridX, gridY));
        
        if(name.equals("StartingLocation"))
            startLocations.add(new Destination(gridX, gridY));
    }

    public void lateUpdate() {
        entities.stream().filter((e) -> (e instanceof Unit)).forEach((e) -> {
            ((Unit)e).lateUpdate();
        });
    }
}
