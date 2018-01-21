/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.entities.Entity;
import simplerts.input.KeyManager;
import simplerts.input.MouseInput;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Markus
 */
public class Player {
    
    private int gold;
    private Color teamColor;
    private CopyOnWriteArrayList<Entity> selectedEntities;

    public Handler handler;
    
    public Player(Handler handler)
    {

        selectedEntities = new CopyOnWriteArrayList<>();
        this.handler = handler;
        gold = 300;
        teamColor = new Color(0, 0, 255);
    }
    
    public void input()
    {

        
        
    }
    
    public void render(Graphics g)
    {

    }
    
    
}
