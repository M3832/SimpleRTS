/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.gfx.effects;

import simplerts.gfx.Animation;
import simplerts.gfx.Assets;
import simplerts.map.Destination;

/**
 *
 * @author Markus
 */
public class MoveConfirm extends Effect{
       
    public MoveConfirm(int x, int y){
        super(x, y);
        animation = new Animation(Assets.loadToCompatibleImage("/Other/Effects/confirmMove.png"), 25);
        timeAlive = System.currentTimeMillis() + 300;
    }
    
    public MoveConfirm(Destination d){
        this(d.getX(), d.getY());
    }
}
