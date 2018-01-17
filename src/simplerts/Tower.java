/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.display.Assets;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Markus
 */
public class Tower extends Building {
    
    public Tower()
    {
        super(0, 0, 2, 2);
        try {
            image = ImageIO.read(Image.class.getResource("/tower.png"));
            BufferedImage teamColorImage = Assets.coverImageWithColor(new Color(255, 0, 0), ImageIO.read(Image.class.getResource("/towerteamcolor.png")));
            image = Assets.combineImages(image, teamColorImage);
        } catch (IOException ex) {
        }
    }
    
    @Override
    public Entity duplicate()
    {
        return new Tower();
    }
}
