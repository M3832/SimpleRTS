/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import simplerts.editor.TerrainPlacement;

/**
 *
 * @author Markus
 */
public class SpriteHolder {

    public static BufferedImage GRASS;
    private static BufferedImage[] tiles;
    public static TerrainPlacement dirt;    
    public static TerrainPlacement grass;

    public static void setup() {
        GRASS = loadAndResizeImage("/grass.bmp", Game.CELLSIZE, Game.CELLSIZE);
        loadTiles(64);
        dirt = new TerrainPlacement(new Integer[] {0, 23, 21, 16, 5, 17, 0, 8, 7, 18, 5, 17, 2, 10, 1, 9, 30, 31}, new Integer[]{11, 3, 9});
        grass = new TerrainPlacement(new Integer[] {5, 18, 16, 21, 0, 22, 5, 13, 2, 23, 0, 22, 7, 15, 6, 19}, new Integer[] {19, 20, 4, 12, 14});
    }
    
    private static void loadTiles(int tileSize)
    {
        BufferedImage tileImage = loadToCompatibleImage("/tileset.png");
        tiles = new BufferedImage[(tileImage.getWidth()/tileSize) * (tileImage.getHeight() / tileSize)];
        int width = tileImage.getWidth()/tileSize;
        System.out.println(tiles.length);
        for(int i = 0; i < tiles.length; i++)
        {
            tiles[i] = tileImage.getSubimage((i%width) * tileSize, (i/width) * tileSize, tileSize, tileSize);
        }
    }
    
    public static BufferedImage getTile(int tileId)
    {
        return tiles[tileId];
    }

    public static BufferedImage loadToCompatibleImage(String url) {
        BufferedImage image;
        try {
            image = ImageIO.read(Image.class.getResource(url));
        } catch (IOException ex) {
            return null;
        }

        // obtain the current system graphical settings
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

        /*
        * if image is already compatible and optimized for current system 
        * settings, simply return it
         */
        if (image.getColorModel().equals(gfx_config.getColorModel())) {
            return image;
        }

        // image is not optimized, so create a new image that is
        BufferedImage new_image = gfx_config.createCompatibleImage(
                image.getWidth(), image.getHeight(), image.getTransparency());

        // get the graphics context of the new image to draw the old image on
        Graphics2D g2d = (Graphics2D) new_image.getGraphics();

        // actually draw the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // return the new optimized image
        return new_image;
    }
    
    public static BufferedImage loadAndResizeImage(String url, int width, int height)
    {
        BufferedImage img = loadToCompatibleImage(url);
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(width, height, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, width, height, 0, 0, w, h, null);  
        g.dispose();  
        return dimg; 
    }

}
