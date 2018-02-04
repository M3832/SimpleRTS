/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.editor;

import java.awt.image.BufferedImage;

/**
 *
 * @author Markus
 */
public class EditorObject {
    
    public int gridX, gridY, gridWidth, gridHeight;
    public BufferedImage sprite;
    public String name;
    
    public EditorObject(int gridX, int gridY, int gridWidth, int gridHeight, BufferedImage sprite, String name)
    {
        this.gridX = gridX;
        this.gridY = gridY;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.sprite = sprite;
        this.name = name;
    }
}
