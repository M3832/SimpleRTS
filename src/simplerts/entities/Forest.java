/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import simplerts.map.Cell;
import simplerts.map.Wood;

/**
 *
 * @author Markus
 */
public class Forest extends Entity implements Wood {
    private int lumber;
    private boolean barren;
    private Cell cell;
    
    public Forest(Cell cell)
    {
        lumber = 10;
        barren = false;
        this.cell = cell;
    }
    
    @Override
    public Entity duplicate() {
        return new Forest(cell);
    }

    @Override
    public int chop() {
        System.out.println("Forest has " + lumber);
        if(!barren)
        {
            if(--lumber == 0)
            {
                barren = true;
                cell.depleteForest();
            }
            return 1;
        }
        return 0;
    }

    @Override
    public boolean isBarren() {
        return barren;
    }
    
}
