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
    private AnimationController healer;
    
    private AnimationController arrow;
    
    public SpriteManager(Color color)
    {
        peasant = new AnimationController();
        peasant.addAnimation("stand", new Animation(Assets.getUnitAnimationSheet("/Units/Peasant/stand.png", color)));
        peasant.addAnimation("standTree", new Animation(Assets.getUnitAnimationSheet("/Units/Peasant/standTree.png", color)));
        peasant.addAnimation("standGold", new Animation(Assets.getUnitAnimationSheet("/Units/Peasant/standGold.png", color)));
        peasant.addAnimation("walk", new Animation(Assets.getUnitAnimationSheet("/Units/Peasant/walk.png", color)));
        peasant.addAnimation("walkTree", new Animation(Assets.getUnitAnimationSheet("/Units/Peasant/walkTree.png", color)));
        peasant.addAnimation("walkGold", new Animation(Assets.getUnitAnimationSheet("/Units/Peasant/walkGold.png", color)));
        peasant.addAnimation("chop", new Animation(Assets.getUnitAnimationSheet("/Units/Peasant/chop.png", color), false));
        peasant.addAnimation("dead", new Animation(Assets.getUnitAnimationSheet("/Units/Peasant/death.png", color), false));
        
        footman = new AnimationController();
        footman.addAnimation("stand", new Animation(Assets.getUnitAnimationSheet("/Units/Footman/stand.png", color)));
        footman.addAnimation("walk", new Animation(Assets.getUnitAnimationSheet("/Units/Footman/walk.png", color)));
        footman.addAnimation("attack", new Animation(Assets.getUnitAnimationSheet("/Units/Footman/attack.png", color), false));
        footman.addAnimation("dead", new Animation(Assets.getUnitAnimationSheet("/Units/Footman/death.png", color), false));
        
        archer = new AnimationController();
        archer.addAnimation("stand", new Animation(Assets.getUnitAnimationSheet("/Units/Archer/stand.png", color)));
        archer.addAnimation("walk", new Animation(Assets.getUnitAnimationSheet("/Units/Archer/walk.png", color)));
        archer.addAnimation("attack", new Animation(Assets.getUnitAnimationSheet("/Units/Archer/attack.png", color), false));
        archer.addAnimation("dead", new Animation(Assets.getUnitAnimationSheet("/Units/Archer/death.png", color), false));
        
        healer = new AnimationController();
        healer.addAnimation("stand", new Animation(Assets.getUnitAnimationSheet("/Units/Healer/stand2.png", color)));
        healer.addAnimation("walk", new Animation(Assets.getUnitAnimationSheet("/Units/Healer/walk.png", color)));
        
        arrow = new AnimationController();
        arrow.addAnimation("default", new Animation(Assets.getUnitAnimationSheet("/Other/Projectiles/arrow.png", color)));
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
    
    public AnimationController getHealerAC()
    {
        return new AnimationController(healer);
    }
    
    public AnimationController getArrowAC() {
        return new AnimationController(arrow);
    }
    
}
