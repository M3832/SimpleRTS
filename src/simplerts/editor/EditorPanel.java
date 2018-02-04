/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.editor;

import java.awt.Graphics;
import java.awt.ScrollPane;
import java.util.ArrayList;
import javax.swing.JPanel;
import simplerts.Game;
import simplerts.map.BackEndMap;
import simplerts.map.FrontEndMap;

/**
 *
 * @author Markus
 */
public class EditorPanel extends JPanel {

    private FrontEndMap map;
    private ScrollPane sp;
    private ArrayList<EditorObject> editorObjects;
    private float offsetX, offsetY;
    private long nextSecond = 0;
    
    public EditorPanel(FrontEndMap map)
    {
        super();
        this.map = map;
        editorObjects = new ArrayList<>();
    }
    
    public void setScrollPane(ScrollPane sp)
    {
        this.sp = sp;
    }
    
    public void setMap(FrontEndMap map)
    {
        this.map = map;
    }
    
    public void setEditorObjects(ArrayList<EditorObject> editorObjects)
    {
        this.editorObjects = editorObjects;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        offsetX = sp.getScrollPosition().x;
        offsetY = sp.getScrollPosition().y;
        
        if(System.currentTimeMillis() > nextSecond)
        {
            int startX = Math.max((int)offsetX / Game.CELLSIZE, 0);
            int endX = Math.min(2 + startX + Game.WIDTH / Game.CELLSIZE, map.getCells().length);
        
            int startY = Math.max((int)offsetY / Game.CELLSIZE, 0);
            int endY = Math.min(1 + startY + Game.WIDTH / Game.CELLSIZE, map.getCells()[0].length);

            for(int i = startX; i < endX; i++)
            {
                for(int j = startY; j < endY; j++)
                {
                    g.drawImage(map.getCells()[i][j].getImage(), (int)((i * Game.CELLSIZE)), (int)((j * Game.CELLSIZE)), Game.CELLSIZE, Game.CELLSIZE, null);
                }
            }
            
            for(EditorObject e : editorObjects)
            {
                g.drawImage(e.sprite, e.gridX * Game.CELLSIZE, e.gridY * Game.CELLSIZE, null);
            }
            nextSecond = System.currentTimeMillis() + 10;
        }
    }
}
