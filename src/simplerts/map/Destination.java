/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.map;

import simplerts.Game;

/**
 *
 * @author Markus
 */
public class Destination {
    
    private int gridX, gridY;
    
    public Destination(int x, int y)
    {
        this.gridX = x;
        this.gridY = y;
    }
    
    public Destination(Destination d)
    {
        this.gridX = d.getGridX();
        this.gridY = d.getGridY();
    }

    /**
     * @return the gridX
     */
    public int getGridX() {
        return gridX;
    }

    /**
     * @param gridX the gridX to set
     */
    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    /**
     * @return the gridY
     */
    public int getGridY() {
        return gridY;
    }

    /**
     * @param gridY the gridY to set
     */
    public void setGridY(int gridY) {
        this.gridY = gridY;
    }
    
    public int getX() {
        return gridX * Game.CELLSIZE;
    }
    
    public int getY() {
        return gridY * Game.CELLSIZE;
    }
    
    @Override
    public String toString()
    {
        return "Destination: " + gridX + ", " + gridY;
    }

    public void add(int x, int y) {
        this.gridX += x;
        this.gridY += y;
    }

    public boolean isEmpty() {
        return gridX == -1 || gridY == -1;
    }
    
    
    
}
