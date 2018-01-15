/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Markus
 */
public class Bank extends Building {
    
    public Bank()
    {
        super(0, 0, 3);
        try {
            image = ImageIO.read(Image.class.getResource("/mineon.png"));
        } catch (IOException ex) {
        }
    }
    
    @Override
    public Entity duplicate()
    {
        return new Bank();
    }
}
