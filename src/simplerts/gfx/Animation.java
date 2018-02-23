/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.gfx;

import composite.GraphicsUtil;
import java.awt.image.BufferedImage;
import simplerts.Game;

/**
 *
 * @author Markus
 */
public class Animation {
    
    public static final int SOUTH = 0;
    public static final int SOUTHWEST = 1;
    public static final int WEST = 2;
    public static final int NORTHWEST = 3;
    public static final int NORTH = 4;
    public static final int SOUTHEAST = 5;
    public static final int EAST = 6;
    public static final int NORTHEAST = 7;
    
    private BufferedImage[][] animationSheet;
    private int animationSpeed = 75, animationIndex = 0;
    private long timeNextFrame = 0;
    private boolean looping, looped;
    
    public Animation(BufferedImage sheet)
    {
        animationSheet = new BufferedImage[sheet.getWidth()/64][sheet.getHeight()/64];
        setupAnimation(sheet);
        looping = true;
        looped = false;
    }
    
    public Animation(BufferedImage sheet, boolean looping)
    {
        this(sheet);
        this.looping = looping;
    }
    
    public Animation(Animation a)
    {
        this.looping = a.isLooping();
        animationSheet = new BufferedImage[a.animationSheet.length][a.animationSheet[0].length];
        for(int i = 0; i < a.animationSheet.length; i++)
        {
            System.arraycopy(a.animationSheet[i], 0, animationSheet[i], 0, a.animationSheet[0].length);
        }
    }
    
    private void setupAnimation(BufferedImage sheet)
    {
        for(int i = 0; i < animationSheet.length; i++)
        {
            for(int j = 0; j < animationSheet[0].length; j++)
            {
                animationSheet[i][j] = GraphicsUtil.resize(sheet.getSubimage(i * 64, j * 64, 64, 64), Game.CELLSIZE, Game.CELLSIZE);
            }
        }
    }
    
    public BufferedImage getCurrentFrame(int direction)
    {
        if(direction < animationSheet[0].length){
            return animationSheet[animationIndex][direction];
        } else {
            return animationSheet[animationIndex][0];
        }
    }
    
    public void update()
    {
        if(System.currentTimeMillis() > timeNextFrame)
        {
            if(animationIndex == animationSheet.length - 1)
            {
                animationIndex = 0;
                if(!looping)
                    looped = true;
            } else if(looping || (!looping && !looped)) {
                animationIndex++;
            }
            
            timeNextFrame = System.currentTimeMillis() + animationSpeed;
        }
    }
    
    public void play()
    {
        animationIndex = 0;
        looped = false;
    }
    
    public boolean isLooping()
    {
        return looping;
    }
    
    
}
