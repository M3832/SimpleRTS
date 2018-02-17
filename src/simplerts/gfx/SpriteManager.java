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
    private AnimationController archer;
    
    private AnimationController arrow;
    
    public SpriteManager(Color color)
    {
        peasant = new AnimationController();
        peasant.addAnimation("walk", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/walk.png"),
                                             Assets.loadToCompatibleImage("/Units/Peasant/walktc.png"), color)));
        peasant.addAnimation("walkTree", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/walkTree.png"),
                                             Assets.loadToCompatibleImage("/Units/Peasant/walkTreetc.png"), color)));
        peasant.addAnimation("walkGold", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/walkGold.png"),
                                             Assets.loadToCompatibleImage("/Units/Peasant/walkGoldtc.png"), color)));
        peasant.addAnimation("stand", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/stand.png"),
                                                     Assets.loadToCompatibleImage("/Units/Peasant/standtc.png"), color)));
        peasant.addAnimation("standTree", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/standTree.png"),
                                                     Assets.loadToCompatibleImage("/Units/Peasant/standTreetc.png"), color)));
        peasant.addAnimation("standGold", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/standGold.png"),
                                                     Assets.loadToCompatibleImage("/Units/Peasant/standGoldtc.png"), color)));
        peasant.addAnimation("chop", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/chop.png"),
                                                     Assets.loadToCompatibleImage("/Units/Peasant/choptc.png"), color), false));
        peasant.addAnimation("dead", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Peasant/dead.png"),
                                                     Assets.loadToCompatibleImage("/Units/Peasant/deadtc.png"), color), false));
        
        footman = new AnimationController();
        footman.addAnimation("walk", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Footman/walk.png"),
                                             Assets.loadToCompatibleImage("/Units/Footman/walktc.png"), color)));
        footman.addAnimation("attack", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Footman/attack.png"),
                                             Assets.loadToCompatibleImage("/Units/Footman/attacktc.png"), color), false));
        footman.addAnimation("stand", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Footman/stand.png"),
                                             Assets.loadToCompatibleImage("/Units/Footman/standtc.png"), color)));
        
        archer = new AnimationController();
        archer.addAnimation("walk", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Archer/walk.png"),
                                             Assets.loadToCompatibleImage("/Units/Archer/walktc.png"), color)));
        archer.addAnimation("stand", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Archer/stand.png"),
                                             Assets.loadToCompatibleImage("/Units/Archer/standtc.png"), color)));
        archer.addAnimation("attack", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Units/Archer/attack.png"),
                                     Assets.loadToCompatibleImage("/Units/Archer/attacktc.png"), color), false));
        
        arrow = new AnimationController();
        arrow.addAnimation("default", new Animation(Assets.makeTeamColor(Assets.loadToCompatibleImage("/Other/Projectiles/arrow.png"),
                                                Assets.loadToCompatibleImage("/Other/Projectiles/arrowtc.png"), color)));
        
    }
    
    public AnimationController getPeasantAC()
    {
        return new AnimationController(peasant);
    }
    
    public AnimationController getFootmanAC()
    {
        return new AnimationController(footman);
    }
    
    public AnimationController getArcherAC()
    {
        return new AnimationController(archer);
    }
    
    public AnimationController getArrowAC() {
        return new AnimationController(arrow);
    }
    
}
