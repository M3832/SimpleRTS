/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import simplerts.entities.Unit;
import simplerts.display.Assets;
import simplerts.display.Camera;
import simplerts.display.Display;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFileChooser;
import simplerts.display.Message;
import simplerts.display.MessageManager;
import simplerts.entities.Builder;
import simplerts.entities.Tower;
import simplerts.entities.TownHall;
import simplerts.input.MouseInput;
import simplerts.ui.GUI;
import simplerts.ui.UIImageButton;
//import javax.swing.JFileChooser;
//import static org.lwjgl.glfw.GLFW.*;
//import org.lwjgl.glfw.*;
//import org.lwjgl.opengl.GL;
//import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Markus
 */
public class Game implements Runnable {

    public static int WIDTH = 980;
    public static int HEIGHT = 512;
    public static int GUIHEIGHT = 225;
    public static float GAMESPEED = 1f;
    public static int CELLSIZE = 50;
    
    public static long millisSinceLastRender = 0;
    public static int RENDERS_PER_SECOND = 120;
    public static int TICKS_PER_SECOND = 50;
    private long nextSecond = 0;

    public Map map;
    public Camera camera;

    public Handler handler;
    
    public Display display;
    
    public GUI gui;
    
    public MessageManager mm;

    public CopyOnWriteArrayList<Player> players;
    
    public Controller controller;

    @Override
    public void run() {
        Assets.setup();
        
        players = new CopyOnWriteArrayList<>();
        
        camera = new Camera();
        
        display = new Display(WIDTH, HEIGHT, GUIHEIGHT);
        
        JFileChooser openFile = new JFileChooser();
//        openFile.showOpenDialog(display.window);
//        map = MapIO.loadMap(openFile.getSelectedFile());
        map = MapIO.loadMap("/asd");
        
        handler = new Handler(this, map, camera, display);
        
        mm = new MessageManager(handler);
        
        map.setHandler(handler);
        display.getGamePanel().setHandler(handler);
//        display.getGUIPanel().setHandler(handler);
        
        gui = new GUI(map, handler);
        
        camera.setHandler(handler);

        Player player = new Player(handler);
        controller = new Controller(handler, player);
        ((MouseInput)display.getGamePanel().getMouseListeners()[0]).setGUI(gui);
        players.add(player);
        map.addEntity(new Builder(10, 6, player));
//        map.addEntity(new Builder(11, 6, player));
//        map.addEntity(new Builder(12, 6, player));
//        map.addEntity(new Builder(10, 7, player));
//        map.addEntity(new Builder(11, 7, player));
//        map.addEntity(new Builder(12, 7, player));
//        map.addEntity(new Builder(10, 8, player));
//        map.addEntity(new Builder(11, 8, player));
        
//        map.addEntity(new Tower(6, 2, 2, player, true));
//        map.addEntity(new Tower(8, 4, 2, player, true));
//        map.addEntity(new TownHall(4, 6, 4, player, true));



        int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
        int MAX_FRAMESKIP = 10;
        
        int SKIP_RENDERS = 1000 / RENDERS_PER_SECOND;
        int MAX_RENDERSKIP = 10;

        long next_game_tick = System.currentTimeMillis();
        int loops;

        boolean game_is_running = true;
        
//        new Thread(() -> {
//            while(game_is_running){
//                renderGUI(); 
//                try {
//                    Thread.sleep(5);
//                } catch (InterruptedException ex) {}
//            }
//        }).start();
        
        new Thread(() -> {
            int renderloops;
            long next_game_render = System.currentTimeMillis();
            long b = System.currentTimeMillis();
            while(game_is_running)
            {
                renderloops = 0;
                while(System.currentTimeMillis() > next_game_render && renderloops < MAX_RENDERSKIP)
                {
                    millisSinceLastRender = System.currentTimeMillis() - b;
                    render();
                    next_game_render += SKIP_RENDERS;
                    renderloops++;
                    b = System.currentTimeMillis();
                }
            }
        }).start();
        
        mm.addMessage(new Message("Welcome to this nameless RTS-game!"));
        
        while (game_is_running) {
            loops = 0;
            while (System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {
                update();
                next_game_tick += SKIP_TICKS;
                loops++;
            }
        }

    } 
    
    public void render()
    {
        display.getGamePanel().repaint();
    }
    
//    public void renderGUI()
//    {
//        display.getGUIPanel().repaint();
//    }
    
    public void update()
    {
        if(controller != null)
            controller.update();
        handler.map.update();
    }
}
