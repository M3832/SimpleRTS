/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.display;

import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import javax.swing.BoxLayout;
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
    
    public GamePanel gamePanel;
    public JPanel guiPanel;
    
    
    public Display(int width, int height)
    {
        window = new JFrame();
//        window.setSize(new Dimension(width, height));
        Container pane = window.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
//        canvas = new Canvas();
//        canvas.setSize(new Dimension(width, height));
//        canvas.setFocusable(false);

          gamePanel = new GamePanel();
          gamePanel.setSize(new Dimension(width, height));
          gamePanel.setMinimumSize(new Dimension(width, height));
          gamePanel.setMaximumSize(new Dimension(width, height));
          gamePanel.setPreferredSize(new Dimension(width, height));
        
        guicanvas = new Canvas();
        guicanvas.setSize(new Dimension(width, 225));
        guicanvas.setFocusable(false);
        
//        pane.add(canvas);
//        pane.setBorder(new EmptyBorder(0, 0, 0, 0));
        pane.add(gamePanel);
        pane.add(guicanvas);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        window.pack();
//        canvas.createBufferStrategy(2);
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
    
    public GamePanel getGamePanel()
    {
        return gamePanel;
    }
}
