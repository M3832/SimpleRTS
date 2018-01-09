/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.editor;

import simplerts.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Markus
 */
public class EditorMouseListener implements MouseListener {
        
    public boolean isMouseDown;
    public boolean isScrollDown;

    public int posX, posY;
    
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!isScrollDown && e.getButton() == MouseEvent.BUTTON2)
            isScrollDown = true;
        if(!isMouseDown && e.getButton() == MouseEvent.BUTTON1)
            isMouseDown = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(isScrollDown && e.getButton() == MouseEvent.BUTTON2)
            isScrollDown = false;
        if(isMouseDown && e.getButton() == MouseEvent.BUTTON1)
            isMouseDown = false;
        posX = e.getX();
        posY = e.getY();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    
    public boolean isMouseClicked()
    {
        if(isMouseDown)
        {
            isMouseDown = false;
            return true;
        }
        
        return false;
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
