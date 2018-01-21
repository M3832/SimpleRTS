/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.display;

import imageutils.BlendComposite;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import simplerts.Game;
import simplerts.editor.TerrainPlacement;

/**
 *
 * @author Markus
 */
public class Assets {

    public static BufferedImage GRASS;
    private static BufferedImage[] tiles;
    public static TerrainPlacement dirt;    
    public static TerrainPlacement grass;
    public static TerrainPlacement darkGrass;
    public static TerrainPlacement trees;
    public static ArrayList<TerrainPlacement> terrains;
    public static BufferedImage GUI;
    public static BufferedImage iconBG;

    public static void setup() {
        terrains = new ArrayList<>();
        GRASS = loadAndResizeImage("/grass.bmp", Game.CELLSIZE, Game.CELLSIZE);
        loadTiles(64);
        dirt = loadTerrain("/dirt3.bmp", "dirt", true, new Color(129, 61, 0));
        grass = new TerrainPlacement(new Integer[] {5, 18, 16, 21, 0, 22, 5, 13, 2, 23, 0, 22, 7, 15, 6, 19, 38, 39}, new Integer[] {19, 20, 4, 12, 14}, tiles, "grass", true, Color.GREEN);
        terrains.add(grass);
        darkGrass = loadTerrain("/darkgrass2.bmp", "darkGrass", true, new Color(0, 200, 0));
        trees = loadTerrain("/forest.bmp", "trees", false, new Color(0, 125, 0));
        GUI = loadToCompatibleImage("/gui.bmp");
        iconBG = loadToCompatibleImage("/iconbg.bmp");
    }
    
    private static void loadTiles(int tileSize)
    {
        BufferedImage tileImage = loadToCompatibleImage("/tileset.bmp");
        tiles = new BufferedImage[(tileImage.getWidth()/tileSize) * (tileImage.getHeight() / tileSize)];
        int width = tileImage.getWidth()/tileSize;
        System.out.println(tiles.length);
        for(int i = 0; i < tiles.length; i++)
        {
            tiles[i] = tileImage.getSubimage((i%width) * tileSize, (i/width) * tileSize, tileSize, tileSize);
        }
    }
    
    private static TerrainPlacement loadTerrain(String url, String name, boolean walkable, Color color)
    {
        BufferedImage tileMap = loadToCompatibleImage(url);
        BufferedImage[] tempTiles = new BufferedImage[(tileMap.getWidth()/64) * (tileMap.getHeight() / 64)];
        int width = tileMap.getWidth()/64;
        for(int i = 0; i < tempTiles.length; i++)
        {
            tempTiles[i] = tileMap.getSubimage((i%width) * 64, (i/width) * 64, 64, 64);
        }
        TerrainPlacement tp = new TerrainPlacement(new Integer[]{15, 15, 15, 12, 15,15, 0, 6, 15, 14, 15, 13, 2, 8, 1, 7, 3, 9, 4, 5, 11, 10}, new Integer[]{0}, tempTiles, name, walkable, color );
        terrains.add(tp);
        return tp;
    }
    
    public static BufferedImage makeIcon(Color color, BufferedImage icon)
    {
        BufferedImage newIcon = new BufferedImage(icon.getWidth() + 2, icon.getHeight() + 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) newIcon.getGraphics();
        g.drawImage(coverImageWithColor(color, iconBG), 1, 1, null);
        g.drawImage(icon, 1, 1, null);
        return newIcon;
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
    
    public static BufferedImage makeTeamColor(BufferedImage baseSprite, BufferedImage tcSprite, Color color)
    {
        return combineImages(baseSprite, coverImageWithColor(color, tcSprite));
    }
    
    private static BufferedImage coverImageWithColor(Color color, BufferedImage image)
    {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = result.createGraphics();
        BufferedImage x = image;

        gbi.drawImage(x, 0, 0, null);
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D midthing = copy.createGraphics();
        midthing.drawImage(x, 0, 0, null);
        midthing.setComposite(AlphaComposite.SrcAtop);
        midthing.setColor(color);
        midthing.fillRect(0, 0, image.getWidth(), image.getHeight());
        
        gbi.setComposite(BlendComposite.Overlay);
        gbi.drawImage(copy, 0, 0, null);
        return result;
    }
    
    private static BufferedImage combineImages(BufferedImage base, BufferedImage teamColor)
    {
        BufferedImage result = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = result.createGraphics();
        gbi.drawImage(base, 0, 0, null);
        gbi.drawImage(teamColor, 0, 0, null);
        return result;
    }

}