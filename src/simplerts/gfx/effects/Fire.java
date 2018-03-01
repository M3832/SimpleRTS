/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.gfx.effects;

import java.awt.image.BufferedImage;
import simplerts.gfx.Animation;
import simplerts.gfx.Assets;
import simplerts.map.Destination;

/**
 *
 * @author Markus
 */
public class Fire extends Effect {

    public Fire(int x, int y){
        super(x, y);
        animation = new Animation(Assets.loadToCompatibleImage("/Other/Effects/fire.png"), 75, 100, false);
        timeAlive = -1;
    }
    
    public Fire(Destination d){
        this(d.getX(), d.getY());
    }
    
    public BufferedImage getFrame(){
        return animation.getCurrentFrame(0);
    }
    
}
