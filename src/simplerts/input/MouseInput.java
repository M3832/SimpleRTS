/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import simplerts.ui.GUI;

/**
 *
 * @author Markus
 */
public class MouseInput extends MouseAdapter implements MouseMotionListener {

    public int posX, posY;
    public boolean isMouseDown, isMouseClicked, isRightMouseDown, isRightMouseClicked;
    public boolean isScrollDown;
    public boolean isHovering = true;
    private GUI gui;
    
    public void setGUI(GUI gui)
    {
        this.gui = gui;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        posX = e.getX();
        posY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        posX = e.getX();
        posY = e.getY();
        
        if(gui != null)
            gui.onMouseMove(e);
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        isHovering = false;
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        isHovering = true;
    }
    
    public boolean isMouseClicked()
    {
        if(isMouseClicked)
        {
            isMouseClicked = false;
            return true;
        }
        
        return false;
    }
    
    public boolean isRightMouseClicked()
    {
        if(isRightMouseDown)
        {
            isRightMouseDown = false;
            return true;
        }
        
        return false;
        
    }
        
    @Override
    public void mousePressed(MouseEvent e) {
        if(!isScrollDown && e.getButton() == MouseEvent.BUTTON2)
            isScrollDown = true;
        if(!isMouseDown && e.getButton() == MouseEvent.BUTTON1)
            isMouseDown = true;
        if(!isRightMouseDown && e.getButton() == MouseEvent.BUTTON3)
            isRightMouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isRightMouseDown && e.getButton() == MouseEvent.BUTTON2){
            isRightMouseDown = false;
        }
        if(!isRightMouseDown && e.getButton() == MouseEvent.BUTTON2){
            isRightMouseClicked = true;
        }
        if(isMouseDown && e.getButton() == MouseEvent.BUTTON1){
            isMouseDown = false;
        }
        if(!isMouseDown && e.getButton() == MouseEvent.BUTTON1){
            isMouseClicked = true;
        }
        if(isScrollDown && e.getButton() == MouseEvent.BUTTON2)
            isScrollDown = false;
        posX = e.getX();
        posY = e.getY();
        
        if(gui != null)
            gui.onMouseRelease(e);
    }
    
    public boolean isScrollDown()
    {
        return isScrollDown;
    }
    
    public boolean isScrollClicked()
    {
        if(isScrollDown)
        {
            isScrollDown = false;
            return true;
        }
        return false;
    }
    
}
