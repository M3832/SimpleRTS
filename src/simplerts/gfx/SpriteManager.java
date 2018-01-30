/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.gfx;

import java.awt.Color;

/**
 *
 * @author Markus
 */
public class SpriteManager {
    
    private AnimationController peasant;
    private AnimationController footman;
    
    public SpriteManager(Color color)
    {
        peasant = new AnimationController();
        peasant.addAnimation("walk", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/walk.png"),
                                             Assets.loadToCompatibleImage("/Units/Peasant/walktc.png"), color)));
        peasant.addAnimation("stand", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/stand.png"),
                                                     Assets.loadToCompatibleImage("/Units/Peasant/standtc.png"), color)));
        
        footman = new AnimationController();
        footman.addAnimation("walk", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Footman/walk.png"),
                                             Assets.loadToCompatibleImage("/Units/Peasant/walktc.png"), color)));
        footman.addAnimation("stand", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Footman/stand.png"),
                                             Assets.loadToCompatibleImage("/Units/Peasant/standtc.png"), color)));
    }
    
    public AnimationController getPeasantAC()
    {
        return new AnimationController(peasant);
    }
    
    public AnimationController getFootmanAC()
    {
        return new AnimationController(footman);
    }
    
}
