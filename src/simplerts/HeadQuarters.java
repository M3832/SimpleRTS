/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Markus
 */
public class HeadQuarters extends Building {
    
    
    public HeadQuarters()
    {
        super(0, 0, 2);
        image = SpriteHolder.loadAndResizeImage("/hus.png", 3 * Game.CELLSIZE, 3 * Game.CELLSIZE);
    }
    
    @Override
    public Entity duplicate()
    {
        return new HeadQuarters();
    }
    
}
