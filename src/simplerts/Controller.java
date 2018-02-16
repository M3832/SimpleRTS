/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.messaging.PlayerMessager;
import simplerts.utils.Utilities;
import simplerts.map.PathFinder;
import simplerts.map.Destination;
import simplerts.entities.Unit;
import simplerts.entities.Entity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import simplerts.audio.SoundController;
import simplerts.audio.SoundManager;
import simplerts.display.Camera;
import simplerts.entities.actions.Build;
import simplerts.entities.actions.MoveTo;
import simplerts.entities.units.Builder;
import simplerts.entities.Building;
import simplerts.entities.actions.Chop;
import simplerts.entities.actions.Follow;
import simplerts.entities.buildings.TownHall;
import simplerts.entities.interfaces.Lumberman;
import simplerts.input.KeyManager;
import simplerts.input.MouseInput;
import simplerts.map.FrontEndMap;
import simplerts.messaging.ErrorMessage;
import simplerts.messaging.Message;
import simplerts.ui.GUI;

/**
 *
 * @author Markus
 */
public class Controller {
    
    public static final int SELECT_LIMIT = 9;
    
    private final Player player;
    private final Handler handler;
    
    private Placer entityplacer;
    
    private MouseInput ml;
    private KeyManager km;
    
    private float boundary;
    
    private GUI gui;
    private Camera camera;
    private FrontEndMap renderMap;
    private PlayerMessager messager;
    private SoundManager soundManager;
    
    private ArrayList<Entity> selected;
    
    private Rectangle selectBox;
    private double startDragX, startDragY;
    private double currentDragX, currentDragY;
    
    public Controller(Handler handler, Player player)
    {
        this.handler = handler;
        this.player = player;
        initVariables();
    }
    
    private void initVariables()
    {
        player.setController(this);
        
        selected = new ArrayList<>();
        messager = new PlayerMessager(this);
        entityplacer = new Placer(this);
        renderMap = new FrontEndMap(handler.map);
        handler.setRenderMap(renderMap);
        camera = new Camera(handler);
        gui = new GUI(renderMap, this);
        ml = new MouseInput(camera);
        km = new KeyManager();
        km.setPlayerMessager(messager);
        handler.getDisplay().getGamePanel().addMouseListener(ml);
        handler.getDisplay().getGamePanel().addMouseMotionListener(ml);
        handler.getDisplay().window.addKeyListener(km);
        
        renderMap.setController(this);
        soundManager = new SoundManager();
        
        ((MouseInput)handler.display.getGamePanel().getMouseListeners()[0]).setGUI(gui);
        
        boundary = 20;
    }
    
    public void update()
    {
        input();
        renderMap.update();
        gui.update();
        ml.isMouseClicked();
        entityplacer.setPosition(camera.getMouseX(), camera.getMouseY());
    }
    
    public void render(Graphics g)
    {
        if(selectBox != null)
        {
            g.setColor(Color.GREEN);
            g.drawRect((int)(selectBox.getX() - camera.getOffsetX()), (int)(selectBox.getY() - camera.getOffsetY()), (int)selectBox.getWidth(), (int)selectBox.getHeight());
            g.setColor(new Color(0, 255, 0, 100));
            for(Entity e : (ArrayList<Entity>)handler.map.getEntitiesInSelection(selectBox))
            {
                    g.drawRect(e.getX() - (int)camera.getOffsetX(), e.getY() - (int)camera.getOffsetY(), e.getWidth(), e.getHeight());
            }
        }
        if(selected.size() > 0)
        {
            for(int i = 0; i < selected.size(); i++)
            {
                Entity e = selected.get(i);
                g.setColor(getColorFromAllegiance(e));
                g.drawRect(e.getX() - (int)camera.getOffsetX(), e.getY() - (int)camera.getOffsetY(), e.getWidth(), e.getHeight());
                e.renderSelected(g, camera);
            }
        }
        if(entityplacer.getEntity() != null)
            entityplacer.render(g);
        
        gui.render(g);
        messager.render(g);
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
            camera.addOffset(frameScroll, 0);
        }
        
        if(ml.posX < 0 + boundary && ml.isHovering)
        {
            camera.addOffset(-frameScroll, 0);
        }
        
        if(ml.posY > Game.HEIGHT + Game.GUIHEIGHT - boundary && ml.isHovering)
        {
            camera.addOffset(0, frameScroll);
        }
        
        if(ml.posY < 0 + boundary && ml.isHovering)
        {
            camera.addOffset(0, -frameScroll);
        }
    }
    
    public void selectEntity(Entity e)
    {
        selected.clear();
        selected.add(e);
        gui.setSelectedEntities(selected);

    }
    
    private void input()
    {
        //Mouse in GUI
        if(ml.posY > Game.HEIGHT)
        {
            if(ml.isMouseDown)
            {
                gui.onClick(ml.posX, ml.posY);
            }
        }
        
        //Mouse in game window
        if(ml.posY < Game.HEIGHT)
        {
            if(ml.isMouseClicked())
            {
                if(entityplacer.hasEntity())
                {
                    if(!selected.isEmpty() && selected.get(0) instanceof Builder && entityplacer.isPlaceable(selected.get(0)))
                    {
                        Builder b = ((Builder)selected.get(0));
                        Building building = (Building)entityplacer.getEntity().duplicate();
                        building.setPosition(entityplacer.getDestination().getX() * Game.CELLSIZE, entityplacer.getDestination().getY() * Game.CELLSIZE);
                        b.clearActions();
                        b.playSound(SoundController.CONFIRM);
                        b.addAction(new MoveTo(b, new Destination(building.getGridX() + building.getGridWidth()/2, building.getGridY() + building.getGridHeight()/2)));
                        b.addAction(new Build(b, building));
                    } else {
                        handler.game.mm.addMessage(new ErrorMessage("Building can't be placed here."));
                    }
                    entityplacer.clear();
                    return;
                }
                if(renderMap.getCells()[camera.getMouseX()/Game.CELLSIZE][camera.getMouseY()/Game.CELLSIZE].getEntity() != null)
                {
                    selected.clear();
                    selected.add(renderMap.getCells()[camera.getMouseX()/Game.CELLSIZE][camera.getMouseY()/Game.CELLSIZE].getEntity());
                    select();
                }
            }

            if(ml.isMouseDown && !entityplacer.hasEntity())
            {
                if(selectBox == null)
                {
                    startDragX = camera.getMouseX();
                    startDragY = camera.getMouseY();
                    selectBox = new Rectangle(camera.getMouseX(), camera.getMouseY(), 0, 0);
                } else {
                    currentDragX = camera.getMouseX();
                    currentDragY = camera.getMouseY();
                    selectBox.setBounds((int)Math.min(startDragX, currentDragX), (int)Math.min(startDragY, currentDragY), (int)Math.abs(startDragX - currentDragX), (int)Math.abs(startDragY - currentDragY));
                }
            } else {
                if(selectBox != null && selectBox.getSize().getHeight() > 5 && selectBox.getSize().getWidth() > 5)
                {
                    selected = filterSelection(handler.map.getEntitiesInSelection(selectBox));
                    select();
                }

                selectBox = null;
            }

            if(ml.isRightMouseClicked())
            {
                rightMouseClick();
            }
        }
    }
    
    private void select()
    {
        if(!selected.isEmpty() && isPlayerControlled(selected.get(0)))
            selected.get(0).playSound(SoundController.WAKE);
        selected.sort(Comparator.comparing(Entity::getGridY).thenComparing(Entity::getGridX));
        gui.setSelectedEntities(selected);
    }
    
    private void rightMouseClick()
    {
        if(entityplacer.hasEntity())
        {
            entityplacer.clear();
            return;
        }
        Unit leader = null;
        int index = 0;
        selected.sort(Comparator.comparing(Entity::getGridY).thenComparing(Entity::getGridX));
        if(!selected.isEmpty() && isPlayerControlled(selected.get(0)))
            selected.get(0).playSound(SoundController.CONFIRM);
        for(Entity e : selected)
        {
            if(isPlayerControlled(e) && e instanceof Unit)
            {
                Unit u = (Unit) e;
                u.clearActions();
                int gridX = camera.getMouseGridX();
                int gridY = camera.getMouseGridY();
                if(handler.map.isInBounds(gridX, gridY))
                {
                    if(handler.map.getCells()[gridX][gridY].getEntity() != null && !handler.map.getCells()[gridX][gridY].getEntity().isDead())
                    {
                        u.rightClickAction(handler.map.getCells()[gridX][gridY].getEntity());
                    } else if (handler.map.getCells()[gridX][gridY].isForest() && !handler.map.getCells()[gridX][gridY].getForest().isBarren())
                    {
                        if(u instanceof Lumberman)
                        {
                            u.addAction(new Chop(u, handler.map.getCells()[gridX][gridY]));
                        }
                    } else {
                        int offsetX = (int)(index%(Math.sqrt(selected.size())));
                        int offsetY = (int)(index/(Math.sqrt(selected.size())));
                        int targetX = camera.getMouseGridX() + offsetX;
                        int targetY = camera.getMouseGridY() + offsetY;
                        u.addAction(new MoveTo(u, new Destination(targetX, targetY)));
//                        if(index == 0)
//                        {
//                            u.addAction(new MoveTo(u, new Destination(camera.getMouseGridX(), camera.getMouseGridY())));
//                            leader = u;
//                        } else {
//                            u.addAction(new Follow(u, leader));
//                        }
                    }
                    index++;
                }
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
        Utilities.drawWithShadow(g, gold, Game.WIDTH - resourceWidth + 10, 30);
        g.setColor(Color.GREEN);
        Utilities.drawWithShadow(g, wood, Game.WIDTH - resourceWidth + g.getFontMetrics(GUI.BREAD).stringWidth(gold) + 20, 30);
        g.setColor(new Color(255, 50, 50));
        Utilities.drawWithShadow(g, food, Game.WIDTH - resourceWidth + g.getFontMetrics(GUI.BREAD).stringWidth(gold + wood) + 30, 30);
    }

    public void deselect(Entity e) {
        if(selected != null)
        {
            if(selected.contains(e))
            {
                selected.remove(e);
                gui.setSelectedEntities(selected);
            }
        }
    }
    
    public void sendMessage(String message)
    {
        handler.game.mm.addMessage(new Message(message));
    }

    public Player getPlayer() {
        return player;
    }

    public Color getColorFromAllegiance(Entity e) {
        Color c;
        
        if(e.getPlayer() == player)
        {
            c = Color.GREEN;
        } else if (e.getPlayer() != handler.map.getNeutral())
        {
            c = Color.RED;
        } else {
            c = Color.YELLOW;
        }
        
        return c;
    }

    private ArrayList<Entity> filterSelection(List<Entity> list) {
        ArrayList<Entity> finalResult = (ArrayList<Entity>)list;
        if(list.stream().anyMatch(e -> e.getPlayer() == player))
        {
            finalResult = (ArrayList<Entity>)list.stream().filter(e -> e.getPlayer() == player).collect(Collectors.toList());
        }
        
        if(finalResult.size() > SELECT_LIMIT)
        {
            for(int i = finalResult.size() - 1; i > 8; i--)
            {
                finalResult.remove(i);
            }
        }
        
        return finalResult;
    }

    public boolean isPlayerControlled(Entity entity) {
        return player.getEntities().contains(entity);
    }

    void start() {
        TownHall t = (TownHall)player.getEntities().stream().filter(e -> e instanceof TownHall).findFirst().get();
        camera.centerOnEntity(t);
    }

    public void showAll() {
        renderMap.showAll();
    }
    
    public Camera getCamera()
    {
        return camera;
    }
    
    public FrontEndMap getMap()
    {
        return renderMap;
    }
    
}
