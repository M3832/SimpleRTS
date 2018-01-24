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
public class TownHall extends Building {
    
    public TownHall(int x, int y, int gridSize) {
        super(x, y, gridSize);
        color = new Color(100, 100, 255);
        sprite = Assets.makeTeamColor(Assets.loadToCompatibleImage("/townhall.png"),
                                                     Assets.loadToCompatibleImage("/townhalltc.png"), color);
    }
    
}
