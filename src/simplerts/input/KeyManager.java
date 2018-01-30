/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import simplerts.messaging.PlayerMessager;

/**
 *
 * @author Markus
 */
public class KeyManager implements KeyListener {
    
    private boolean[] keys, keysLast;
    public static final int ATTACK_KEY = KeyEvent.VK_ENTER;
    public static final int SPELL_KEY = KeyEvent.VK_SPACE;
    public static final int SCORE_KEY = KeyEvent.VK_TAB;
    
    private PlayerMessager pm;
    
    public KeyManager(){
        keys = new boolean[256];
        keysLast = new boolean[keys.length];
    }
    
    public void tick(){
        
    }
    
    public boolean isPressedOnce(int k){
        if(keys[k] == true && keysLast[k] == false){
            keysLast[k] = true;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isPressed(int k){
        return keys[k];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() < keys.length)
            keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() < keys.length){
            keys[e.getKeyCode()] = false;
            keysLast[e.getKeyCode()] = false;
        }
        pm.keyPressed(e);
    }
    
    public void setPlayerMessager(PlayerMessager pm)
    {
        this.pm = pm;
    }
    
}
