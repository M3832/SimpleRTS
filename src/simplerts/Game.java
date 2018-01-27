/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.gfx.Assets;
import simplerts.display.Camera;
import simplerts.display.Display;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFileChooser;
import simplerts.messaging.Message;
import simplerts.messaging.MessageManager;
import simplerts.entities.Builder;
import simplerts.entities.Goldmine;
import simplerts.input.MouseInput;
import simplerts.ui.GUI;
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
    public static int CELLSIZE = 50;
    public static float GAMESPEED = 1f;
    
    public static int TICKS_PER_SECOND = 50;
    public static int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    public static int MAX_FRAMESKIP = 10;
    public static long TIME_SINCE_LAST_UPDATE = 0;
        
    public static int RENDERS_PER_SECOND = 120;
    public static int SKIP_RENDERS = 1000 / RENDERS_PER_SECOND;
    public static int MAX_RENDERSKIP = 10;
    public static long TIME_SINCE_LAST_RENDER = 0;
    
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
        JFileChooser openFile = new JFileChooser();
        players = new CopyOnWriteArrayList<>();
        camera = new Camera();
        display = new Display(WIDTH, HEIGHT, GUIHEIGHT);
//        openFile.showOpenDialog(display.window);
//        map = MapIO.loadMap(openFile.getSelectedFile());
        map = MapIO.loadMap("/asd");
        handler = new Handler(this, map, camera, display);
        mm = new MessageManager(handler);
        gui = new GUI(map, handler);

        
        Player player = new Player(handler);
        controller = new Controller(handler, player);
        players.add(player);

        ((MouseInput)display.getGamePanel().getMouseListeners()[0]).setGUI(gui);
        
        map.addEntity(new Builder(10, 6, player));
        
        map.addEntity(new Goldmine(15, 6, map.getNeutral()));


        long next_game_tick = System.currentTimeMillis();
        int loops;

        boolean game_is_running = true;
        
        new Thread(() -> {
            int renderloops;
            long next_game_render = System.currentTimeMillis();
            long lastRender = System.currentTimeMillis();
            while(game_is_running)
            {
                renderloops = 0;
                while(System.currentTimeMillis() > next_game_render && renderloops < MAX_RENDERSKIP)
                {
                    TIME_SINCE_LAST_RENDER = System.currentTimeMillis() - lastRender;
                    render();
                    next_game_render += SKIP_RENDERS;
                    renderloops++;
                    lastRender = System.currentTimeMillis();
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
    
    public void update()
    {
        if(controller != null)
            controller.update();
        handler.map.update();
    }
}
