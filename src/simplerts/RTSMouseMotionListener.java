/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Markus
 */
public class RTSMouseMotionListener implements MouseMotionListener {

    public int posX, posY;
    
    @Override
    public void mouseDragged(MouseEvent e) {
        posX = e.getX();
        posY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        posX = e.getX();
        posY = e.getY();
    }
    
}
