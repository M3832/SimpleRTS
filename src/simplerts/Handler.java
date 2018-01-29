/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.map.Map;
import simplerts.display.Camera;
import simplerts.display.Display;

/**
 *
 * @author Markus
 */
public class Handler {
    
    public Game game;
    
    public Map map;
    
    public Camera camera;
    
    public Display display;
    
    public Handler(Game game, Map map, Camera camera, Display display)
    {
        this.game = game;
        this.map = map;
        this.camera = camera;
        this.display = display;
        
        this.map.setHandler(this);
        this.display.getGamePanel().setHandler(this);
        this.camera.setHandler(this);
    }
    
    public Camera getCamera()
    {
        return camera;
    }
    
    public Display getDisplay()
    {
        return display;
    }
    
    public void sendErrorMessage(String message)
    {
        
    }
}
