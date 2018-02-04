/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.editor;

/**
 *
 * @author Markus
 */
public class LoadedObject {
    
    public String name;
    public int gridX;
    public int gridY;
    
    public LoadedObject(String name, int gridX, int gridY)
    {
        this.name = name;
        this.gridX = gridX;
        this.gridY = gridY;
    }
    
}
