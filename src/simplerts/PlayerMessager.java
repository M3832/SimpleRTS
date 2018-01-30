/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import simplerts.ui.GUI;

/**
 *
 * @author Markus
 */
public class PlayerMessager {
    
    public Controller controller;
    public String currentString = "";
    public List<Integer> excludeList;
    public boolean writing;
    
    public PlayerMessager(Controller controller)
    {
        this.controller = controller;
        excludeList = new ArrayList<>();
        excludeList.add(KeyEvent.VK_ENTER);
        excludeList.add(KeyEvent.VK_BACK_SPACE);
        excludeList.add(KeyEvent.VK_SHIFT);
    }
    
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if(writing && !currentString.isEmpty())
            {
                controller.sendMessage(currentString);
                currentString = "";
            }
            writing = !writing;
        }
        
        if(writing)
        {
            if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !currentString.isEmpty())
            {
                currentString = currentString.substring(0, currentString.length()-1);
            } else if(!excludeList.stream().anyMatch(i -> i == e.getKeyCode())) {
                currentString += e.getKeyChar();
            }
        }
    }
    
    public void render(Graphics g)
    {
        if(writing)
        {
            g.setColor(Color.gray);
            g.setFont(GUI.BREAD);
            g.drawString(currentString + "_", 10, Game.HEIGHT - 10);
        }
    }
    
}
