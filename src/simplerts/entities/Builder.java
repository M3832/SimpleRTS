/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.awt.Color;
import simplerts.Game;
import simplerts.display.Assets;
import simplerts.gfx.Animation;

/**
 *
 * @author Markus
 */
public class Builder extends Unit{
    
    public Builder()
    {
        super();
        color = new Color(255, 50, 50);
        ac.addAnimation("walk", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/walk.png"),
                                                     Assets.loadToCompatibleImage("/Units/Peasant/walktc.png"), color)));
        ac.addAnimation("stand", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/stand.png"),
                                                     Assets.loadToCompatibleImage("/Units/Peasant/standtc.png"), color)));
        moveSpeed = 3;
        attackDamage = 5;
        initGraphics();
    }
    
    public Builder(int x, int y)
    {
        this();
        setPosition(x * Game.CELLSIZE, y * Game.CELLSIZE);
    }
    
}
