/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.utils.Utils;
import simplerts.map.PathFinder;
import simplerts.map.Destination;
import simplerts.entities.Unit;
import simplerts.entities.Entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import simplerts.entities.actions.Build;
import simplerts.entities.actions.MoveTo;
import simplerts.entities.Builder;
import simplerts.entities.Building;
import simplerts.entities.actions.Chop;
import simplerts.entities.interfaces.Lumberman;
import simplerts.input.KeyManager;
import simplerts.input.MouseInput;
import simplerts.messaging.ErrorMessage;
import simplerts.ui.GUI;

/**
 *
 * @author Markus
 */
public class Controller {
    
    private Player player;
    private Handler handler;
    
    private Placer entityplacer;
    
    private MouseInput ml;
    private MouseInput guiml;
    private KeyManager km;
    
    private float boundary;
    
    private ArrayList<Entity> selected;
    
    private Rectangle selectBox;
    private double startDragX, startDragY;
    private double currentDragX, currentDragY;
    
    public Controller(Handler handler, Player player)
    {
        this.handler = handler;
        this.player = player;
        player.setController(this);
        
        ml = new MouseInput();
        guiml = new MouseInput();
        km = new KeyManager();
        
        selected = new ArrayList<>();
        
        entityplacer = new Placer(handler);
        
        handler.getDisplay().getGamePanel().addMouseListener(ml);
        handler.getDisplay().getGamePanel().addMouseMotionListener(ml);
//        handler.getDisplay().getGUIPanel().addMouseListener(guiml);
//        handler.getDisplay().getGUIPanel().addMouseMotionListener(guiml);
        handler.getDisplay().window.addKeyListener(km);
        
        boundary = 50;
    }
    
    public void update()
    {
        input();
        handler.game.gui.update();
        ml.isMouseClicked();
        entityplacer.setPosition(ml.posX + (int)handler.camera.getOffsetX(), ml.posY + (int)handler.camera.getOffsetY());
    }
    
    public void render(Graphics g)
    {
        if(selectBox != null)
        {
            g.setColor(Color.GREEN);
            g.drawRect((int)(selectBox.getX() - handler.getCamera().getOffsetX()), (int)(selectBox.getY() - handler.getCamera().getOffsetY()), (int)selectBox.getWidth(), (int)selectBox.getHeight());
            g.setColor(new Color(0, 255, 0, 100));
            for(Entity e : (ArrayList<Entity>)handler.map.getEntitiesInSelection(selectBox))
            {
                    g.drawRect(e.getX() - (int)handler.getCamera().getOffsetX(), e.getY() - (int)handler.camera.getOffsetY(), e.getWidth(), e.getHeight());
            }
        }
        if(selected.size() > 0)
        {
            for(int i = 0; i < selected.size(); i++)
            {
                Entity e = selected.get(i);
                g.setColor(Color.GREEN);
                g.drawRect(e.getX() - (int)handler.camera.getOffsetX(), e.getY() - (int)handler.camera.getOffsetY(), e.getWidth(), e.getHeight());
                g.setColor(Color.WHITE);
                g.setFont(GUI.HEADER);
                Utils.drawWithShadow(g, i + "", e.getX() - (int)handler.camera.getOffsetX(), e.getY() - (int)handler.camera.getOffsetY());
                e.renderSelected(g);
            }
        }
        if(entityplacer.entity != null)
            entityplacer.render(g);
        
        renderResources(g);
        
    }
    
    public void setEntityPlacerEntity(Entity e)
    {
        entityplacer.setEntity(e);
    }
    
    public void scrolling()
    {
        int scrollSpeed = 1000;
        float frameScroll = scrollSpeed * ((float)Game.time_since_last_render/1000);
        
        if(ml.posX > Game.WIDTH - boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(frameScroll, 0);
        }
        
        if(ml.posX < 0 + boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(-frameScroll, 0);
        }
        
        if(ml.posY > Game.HEIGHT + Game.GUIHEIGHT - boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(0, frameScroll);
        }
        
        if(ml.posY < 0 + boundary && ml.isHovering)
        {
            handler.getCamera().addOffset(0, -frameScroll);
        }
    }
    
    public void selectEntity(Entity e)
    {
        selected.clear();
        selected.add(e);
        handler.game.gui.setSelectedEntities(selected);

    }
    
    private void input()
    {
        //Mouse in GUI
        if(ml.posY > Game.HEIGHT)
        {
            if(ml.isMouseDown)
            {
                handler.game.gui.onClick(ml.posX, ml.posY);
            }
        }
        
        //Mouse in game window
        if(ml.posY < Game.HEIGHT)
        {
            if(ml.isMouseClicked())
            {
                if(entityplacer.entity != null)
                {
                    if(!selected.isEmpty() && selected.get(0) instanceof Builder && entityplacer.isPlaceable(selected.get(0)))
                    {
                        Builder b = ((Builder)selected.get(0));
                        Building building = (Building)entityplacer.entity.duplicate();
                        building.setPosition(entityplacer.getDestination().getX() * Game.CELLSIZE, entityplacer.getDestination().getY() * Game.CELLSIZE);
                        b.addAction(new MoveTo(b, player.getHandler().map.getPathFinder().findPath(b.getDestination(), new Destination(building.getGridX() + building.getGridWidth()/2, building.getGridY() + building.getGridHeight()/2))));
                        b.addAction(new Build(b, building));
                    } else {
                        handler.game.mm.addMessage(new ErrorMessage("Building can't be placed here."));
                    }
                    entityplacer.entity = null;
                    return;
                }
            }

            if(ml.isMouseDown && entityplacer.entity == null)
            {
                if(selectBox == null)
                {
                    startDragX = ml.posX + (int)handler.getCamera().getOffsetX();
                    startDragY = ml.posY + (int)handler.getCamera().getOffsetY();
                    selectBox = new Rectangle(ml.posX + (int)handler.getCamera().getOffsetX(), ml.posY + (int)handler.getCamera().getOffsetY(), 0, 0);
                } else {
                    currentDragX = ml.posX + (int)handler.getCamera().getOffsetX();
                    currentDragY = ml.posY + (int)handler.getCamera().getOffsetY();
                    selectBox.setBounds((int)Math.min(startDragX, currentDragX), (int)Math.min(startDragY, currentDragY), (int)Math.abs(startDragX - currentDragX), (int)Math.abs(startDragY - currentDragY));
                    handler.map.setSelectBox(selectBox);
                }
            } else {
                if(selectBox != null && selectBox.getSize().getHeight() > 5 && selectBox.getSize().getWidth() > 5)
                {
                    selected = (ArrayList<Entity>)handler.map.getEntitiesInSelection(selectBox);
                    selected.sort(Comparator.comparing(Entity::getGridY).thenComparing(Entity::getGridX));
                    handler.game.gui.setSelectedEntities(selected);
                }

                selectBox = null;
            }

            if(ml.isRightMouseClicked())
            {
                rightMouseClick();
            }
        }
    }
    
    private void rightMouseClick()
    {
        if(entityplacer.entity != null)
        {
            entityplacer.entity = null;
            return;
        }
        
        int index = 0;
        selected.sort(Comparator.comparing(Entity::getGridY).thenComparing(Entity::getGridX));
        for(Entity e : selected)
        {
            if(e instanceof Unit)
            {
                Unit u = (Unit) e;
                u.clearActions();
                int gridX = (int)(ml.posX + handler.camera.getOffsetX()) / Game.CELLSIZE;
                int gridY = (int)(ml.posY + handler.camera.getOffsetY()) / Game.CELLSIZE;
                if(handler.map.getCells()[gridX][gridY].getEntity() != null)
                {
                    u.rightClickAction(handler.map.getCells()[gridX][gridY].getEntity());
                } else if (handler.map.getCells()[gridX][gridY].isForest() && !handler.map.getCells()[gridX][gridY].getForest().isBarren())
                {
                    System.out.println("this cell is a forest and is not barren! Yay!");
                    if(u instanceof Lumberman)
                    {
                        u.addAction(new Chop(u, handler.map.getCells()[gridX][gridY]));
                    }
                } else {
                    int offsetX = (int)(index%(Math.sqrt(selected.size())));
                    int offsetY = (int)(index/(Math.sqrt(selected.size())));
                    int targetX = (int)(ml.posX + handler.getCamera().getOffsetX())/Game.CELLSIZE + offsetX;
                    int targetY = (int)(ml.posY + handler.getCamera().getOffsetY())/Game.CELLSIZE + offsetY;
                    u.addAction(new MoveTo(u, (new PathFinder(handler.map).findPath(u.getDestination(), new Destination(targetX, targetY)))));                    
                }
                index++;
            }
        }
    }

    private void renderResources(Graphics g) {
        int resourceWidth = 300, resourceHeight = 50;
        String gold = "Gold: " + player.getGold(), wood = "Wood: " + player.getLumber(), food = "Food: " + player.getCurrentFood() + "/" + player.getMaxFood();
        g.setColor(new Color(100, 100, 100, 100));
        g.fillRect(Game.WIDTH - resourceWidth, 0, resourceWidth -1, resourceHeight);
        g.setColor(Color.BLACK);
        g.drawRect(Game.WIDTH - resourceWidth, 0, resourceWidth - 1, resourceHeight);
        g.setFont(GUI.BREAD);
        g.setColor(Color.YELLOW);
        Utils.drawWithShadow(g, gold, Game.WIDTH - resourceWidth + 10, 30);
        g.setColor(Color.GREEN);
        Utils.drawWithShadow(g, wood, Game.WIDTH - resourceWidth + g.getFontMetrics(GUI.BREAD).stringWidth(gold) + 20, 30);
        g.setColor(new Color(255, 50, 50));
        Utils.drawWithShadow(g, food, Game.WIDTH - resourceWidth + g.getFontMetrics(GUI.BREAD).stringWidth(gold + wood) + 30, 30);
    }

    public void removeFromSelection(Entity e) {
        if(selected != null)
        {
            if(selected.contains(e))
            {
                selected.remove(e);
            }
        }
    }
    
}
