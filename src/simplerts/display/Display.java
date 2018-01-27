/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.display;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author Markus
 */
public class Display {
    public JFrame window;
    
    public GamePanel gamePanel;
    
    public Display(int width, int height, int guiheight)
    {
        Dimension size = new Dimension(width, height + guiheight);
        
        window = new JFrame();
        Container pane = window.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        gamePanel = new GamePanel();
        gamePanel.setSize(size);
        gamePanel.setMinimumSize(size);
        gamePanel.setMaximumSize(size);
        gamePanel.setPreferredSize(size);
        
        pane.add(gamePanel);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(size);
        window.pack();
    }
    
    public GamePanel getGamePanel()
    {
        return gamePanel;
    }
}
