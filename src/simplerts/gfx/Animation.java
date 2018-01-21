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
    private int animationSpeed = 100, animationIndex = 0;
    private long timeNextFrame = 0;
    
    public Animation(BufferedImage sheet)
    {
        animationSheet = new BufferedImage[sheet.getWidth()/64][sheet.getHeight()/64];
        setupAnimation(sheet);
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
        return animationSheet[animationIndex][direction];
    }
    
    public void update()
    {
        if(System.currentTimeMillis() > timeNextFrame)
        {
            if(animationIndex == animationSheet.length - 1)
            {
                animationIndex = 0;
            } else {
                animationIndex++;
            }
            
            timeNextFrame = System.currentTimeMillis() + animationSpeed;
        }
    }
    
    
}
