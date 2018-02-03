/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.gfx;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Markus
 */
public class AnimationController {
    private HashMap<String, Animation> animations;
    private Animation currentAnimation;
    private int direction = 0;
    
    public AnimationController()
    {
        animations = new HashMap<>();
    }
    
    public AnimationController(AnimationController a)
    {
        this();
        for(Entry<String, Animation> entry : a.animations.entrySet())
        {
            animations.put(entry.getKey(), new Animation(entry.getValue()));
        }
    }
    
    public BufferedImage getCurrentFrame()
    {
        if(currentAnimation == null)
            currentAnimation = animations.get("stand");
        return currentAnimation.getCurrentFrame(direction);
    }
    
    public void addAnimation(String name, Animation animation)
    {
        animations.put(name, animation);
    }
    
    public void update()
    {
        animations.values().stream().forEach((a) -> {
            a.update();
        });
    }
    
    public void setAnimation(String name)
    {
        currentAnimation = animations.get(name);
    }
    
    public void playAnimation()
    {
        currentAnimation.play();
    }
    
    public void setDirection(int direction)
    {
        this.direction = direction;
    }
    
}
