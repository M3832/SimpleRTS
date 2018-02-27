/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Controller;
import simplerts.entities.Entity;
import simplerts.gfx.Assets;
import simplerts.Game;
import simplerts.entities.interfaces.Attacker;
import simplerts.map.FrontEndMap;

/**
 *
 * @author Markus
 */
public class GUI {
    
    public static Font HEADER = new Font("Verdana", Font.PLAIN, 34);
    public static Font BREAD = new Font("Verdana", Font.PLAIN, 16);
    public static Font SMALL = new Font("Verdana", Font.PLAIN, 10);
    
    private final FrontEndMap renderMap;
    private final MiniMap minimap;
    private final Controller controller;
    private final int mapOffsetX = 27, mapOffsetY = Game.HEIGHT + 14;
    private final CopyOnWriteArrayList<UIObject> objects;
    private ArrayList<UIAction> actionButtons, empty;
    private ArrayList<Entity> entities;
    private boolean isMouseDown;
    
    public GUI(FrontEndMap map, Controller controller) {
        renderMap = map;
        this.controller = controller;
        minimap = renderMap.getMiniMap(197, 197);
        entities = new ArrayList<>();
        objects = new CopyOnWriteArrayList<>();
        actionButtons = new ArrayList<>();
        empty = new ArrayList<>();
        initVariables();
    }
    
    private void initVariables() {
        //A thread that updates the minimap every 5 seconds
        new Thread(() -> {
            int updateTime = 1000;
            long nextMinimapUpdate = System.currentTimeMillis() + updateTime;
            while(true)
            {
                if(nextMinimapUpdate < System.currentTimeMillis())
                {
                    updateMinimap();
                    nextMinimapUpdate = System.currentTimeMillis() + updateTime;
                }
            }
        }).start();
    }
    
    public void onClick(int posX, int posY) { 
        if(posX > mapOffsetX && posX < mapOffsetX + minimap.getMiniMap().getWidth() && posY > mapOffsetY && posY < mapOffsetY + minimap.getMiniMap().getHeight())
        {
            int x1 = (int)((posX - mapOffsetX) / minimap.getPixelRatio()) - Game.WIDTH / 2;
            int y1 = (int)((posY - mapOffsetY) / minimap.getPixelRatio()) - Game.HEIGHT / 2;
            controller.getCamera().setOffset(x1, y1);
        }
    }
    
    public void setSelectedEntities(ArrayList<Entity> elist) {
        entities = elist;
        objects.clear();
        if(entities.size() == 1)
        {
            if(!entities.get(0).getUIActions().isEmpty() && controller.isPlayerControlled(entities.get(0)))
            {
                actionButtons = entities.get(0).getUIActions();
            } else {
                actionButtons = empty;
            }
            if(!entities.get(0).getUIObjects().isEmpty())
            {
                for(UIObject a : entities.get(0).getUIObjects())
                {
                    addUIObject(a);
                }
            }            
        } else {
            if(!entities.isEmpty() && controller.isPlayerControlled(entities.get(0)))
            {
                if(entities.stream().anyMatch(a -> a instanceof Attacker)){
                    setActionButtons(entities.stream().filter(a -> a instanceof Attacker).findFirst().get().getUIActions());
                }
            }
            
            if(entities.isEmpty()){
                actionButtons = empty;
            }
        }
    }
    
    public void update() {
        objects.stream().forEach((UIObject o) -> o.tick());
    }
    
    public void render(Graphics g) {
        g.drawImage(Assets.GUI, 0, Game.HEIGHT, null);
        g.drawImage(minimap.getMinimapWithEntities(), mapOffsetX, mapOffsetY, null);
        g.setColor(Color.WHITE);
        g.drawRect((int)(controller.getCamera().getOffsetX() * minimap.getPixelRatio()) + mapOffsetX, (int)(controller.getCamera().getOffsetY() * minimap.getPixelRatio()) + mapOffsetY, (int)(Game.WIDTH * minimap.getPixelRatio()), (int)(Game.HEIGHT * minimap.getPixelRatio()));
        
        if(entities != null && entities.size() == 1)
        {
            g.setColor(Color.WHITE);
            g.setFont(HEADER);
            entities.get(0).renderGUI(g);
        } else if (entities != null && entities.size() > 1 && entities.size() < 6)
        {
            for(int i = 0; i < entities.size(); i++)
            {
                g.drawImage(entities.get(i).getIcon(), 300 + i * 75, Game.HEIGHT + 75, null);
            }
        } else if (entities != null && entities.size() > 5)
        {
            for(int i = 0; i < entities.size(); i++)
            {
                g.drawImage(entities.get(i).getIcon(), (300 + ((i%5) * 75)), Game.HEIGHT + 50 + (75 * (i/5)), null);
            }
        }
        
        if(actionButtons != null)
            actionButtons.stream().forEach((UIAction button) -> button.render(g));
        
//        objects.stream().forEach((UIObject o) -> {o.render(g);});
    }
    
    public void onMouseMove(MouseEvent e, boolean mouseDown) {
        this.isMouseDown = mouseDown;
        objects.stream().forEach((UIObject o) -> o.onMouseMove(e));
        actionButtons.stream().forEach((UIObject o) -> o.onMouseMove(e));
    }
    
    public void onMouseRelease(MouseEvent e) {
        objects.stream().forEach((UIObject o) -> o.onMouseRelease(e));
        actionButtons.stream().forEach((UIObject o) -> o.onMouseRelease(e));
    }
    
    public void addUIObject(UIObject o) {
        objects.add(o);
    }
    
    public void removeObject(UIObject o) {
        objects.remove(o);
    }
    
    public void updateMinimap() {
        minimap.setImage(renderMap.getMinimapImage(197, 197, false));
    }

    public void setActionButtons(ArrayList<UIAction> actionButtons) {
        this.actionButtons = actionButtons;
        this.actionButtons.stream().forEach(b -> b.clear());
    }

    public void clear() {
        actionButtons = empty;
    }

    public void pressedKey(KeyEvent e) {
        actionButtons.stream().forEach((UIAction action) -> {
            if(action.getHotkey() == e.getKeyChar()){
                action.click();
            }
        });
    }
    
}
