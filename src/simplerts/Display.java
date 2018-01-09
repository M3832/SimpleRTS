/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author Markus
 */
public class Display {
    public JFrame window;
    
    public Canvas canvas;
    
    public Display(int width, int height)
    {
        window = new JFrame();
        window.setSize(new Dimension(width, height));
        
        canvas = new Canvas();
        canvas.setSize(new Dimension(width, height));
        canvas.setFocusable(false);
        
        window.add(canvas);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        canvas.createBufferStrategy(2);
    }
    
    public Graphics GetGraphics(){
        return canvas.getGraphics();
    }
    
    public Canvas getCanvas()
    {
        return canvas;
    }
}
