/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.map.BackEndMap;
import simplerts.display.Camera;
import simplerts.display.Display;
import simplerts.map.FrontEndMap;

/**
 *
 * @author Markus
 */
public class Handler {
    
    public Game game;
    
    public BackEndMap map;
    public FrontEndMap renderMap;
    
    //public Camera camera;
    
    public Display display;
    
    public Handler(Game game, BackEndMap map, Display display)
    {
        this.game = game;
        this.map = map;
        this.display = display;
        
        this.map.setHandler(this);
        this.display.getGamePanel().setHandler(this);
    }
    
//    public Camera getCamera()
//    {
//        return camera;
//    }
    
    public Display getDisplay()
    {
        return display;
    }
    
//    public void setCamera(Camera c)
//    {
//        camera = c;
//    }
    
    public void setRenderMap(FrontEndMap renderMap)
    {
        this.renderMap = renderMap;
    }
    
    public void sendErrorMessage(String message)
    {
        
    }
}
