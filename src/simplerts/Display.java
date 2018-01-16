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
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Markus
 */
public class Display {
    public JFrame window;
    
    public Canvas canvas;
    public Canvas guicanvas;
    
    public Display(int width, int height)
    {
        window = new JFrame();
        window.setSize(new Dimension(width, height + 200));
        
        JPanel panel = new JPanel();
        
        window.getContentPane().add(panel);
        
        canvas = new Canvas();
        canvas.setSize(new Dimension(width, height));
        canvas.setFocusable(false);
        
        guicanvas = new Canvas();
        guicanvas.setSize(new Dimension(width, 200));
        guicanvas.setFocusable(false);
        
        panel.add(canvas);
        panel.add(guicanvas);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        canvas.createBufferStrategy(2);
        guicanvas.createBufferStrategy(2);
    }
    
    public Graphics GetGraphics(){
        return canvas.getGraphics();
    }
    
    public Canvas getCanvas()
    {
        return canvas;
    }
    
    public Canvas getGUICanvas()
    {
        return guicanvas;
    }
}
