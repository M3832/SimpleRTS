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
    private int animationSpeed, animationIndex, spriteSize;
    private long timeNextFrame = 0;
    private boolean looping, looped;
    
    public Animation(BufferedImage sheet)
    {
        this(sheet, 75, 64, true);
    }
    
    public Animation(BufferedImage sheet, int animationSpeed, int spriteSize, boolean resize){
        animationSheet = new BufferedImage[sheet.getWidth()/spriteSize][sheet.getHeight()/spriteSize];
        looping = true;
        looped = false;
        this.spriteSize = spriteSize;
        this.animationSpeed = animationSpeed;
        setupAnimation(sheet, resize);
    }
    
    public Animation(BufferedImage sheet, boolean looping)
    {
        this(sheet);
        this.looping = looping;
    }
    
    public Animation(Animation a)
    {
        this.looping = a.isLooping();
        this.animationSpeed = a.animationSpeed;
        animationSheet = new BufferedImage[a.animationSheet.length][a.animationSheet[0].length];
        for(int i = 0; i < a.animationSheet.length; i++)
        {
            System.arraycopy(a.animationSheet[i], 0, animationSheet[i], 0, a.animationSheet[0].length);
        }
    }
    
    private void setupAnimation(BufferedImage sheet, boolean resize)
    {
        for(int i = 0; i < animationSheet.length; i++)
        {
            for(int j = 0; j < animationSheet[0].length; j++)
            {
                BufferedImage tempImage = sheet.getSubimage(i * spriteSize, j * spriteSize, spriteSize, spriteSize);
                animationSheet[i][j] = resize ? Assets.resizeImage(tempImage, Game.CELLSIZE, Game.CELLSIZE) : tempImage;
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
