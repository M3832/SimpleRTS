/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import simplerts.display.Assets;

/**
 *
 * @author Markus
 */
public class Tower extends Building{
    
    public Tower(int x, int y, int gridSize)
    {
        super(x, y, gridSize);
        color = new Color(255, 50, 50);
        sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/tower.png"),
                                                     Assets.loadToCompatibleImage("/towerteamcolor.png"), color);
    }
    
    
}
