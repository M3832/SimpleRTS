/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.entities.Unit;
import simplerts.display.Assets;
import simplerts.display.Camera;
import simplerts.display.Display;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFileChooser;
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
    public static float GAMESPEED = 1f;
    public static int CELLSIZE = 60;
    
    public static long millisSinceLastRender = 0;
    public static int RENDERS_PER_SECOND = 120;
    private long nextSecond = 0;

    public Map map;
    public Camera camera;

    public Handler handler;
    
    public Display display;
    
    public GUI gui;

    public CopyOnWriteArrayList<Player> players;
    
    public Controller controller;

    @Override
    public void run() {
        Assets.setup();
        
        players = new CopyOnWriteArrayList<>();
        
        camera = new Camera();
        
        display = new Display(WIDTH, HEIGHT);
        
        JFileChooser openFile = new JFileChooser();
        openFile.showOpenDialog(display.window);
        map = MapIO.loadMap(openFile.getSelectedFile());
        
        handler = new Handler(this, map, camera, display);
        
        map.setHandler(handler);
        display.getGamePanel().setHandler(handler);
        display.getGUIPanel().setHandler(handler);
        
        gui = new GUI(map, handler);
        
        camera.setHandler(handler);

        Player player = new Player(handler);
        controller = new Controller(handler, player);
        players.add(player);
        map.addEntity(new Unit());
        map.addEntity(new Unit(300, 300));


        int TICKS_PER_SECOND = 50;
        int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
        int MAX_FRAMESKIP = 10;
        
        int SKIP_RENDERS = 1000 / RENDERS_PER_SECOND;
        int MAX_RENDERSKIP = 10;

        long next_game_tick = System.currentTimeMillis();
        int loops;

        boolean game_is_running = true;
        
        new Thread(() -> {
            while(game_is_running){
                renderGUI(); 
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {}
            }
        }).start();
        
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
    
    public void renderGUI()
    {
        display.getGUIPanel().repaint();
    }
    
    public void update()
    {
        if(controller != null)
            controller.update();
        players.stream().forEach(player -> player.input());
        handler.map.update();
    }
    
//    public void render(Canvas c)
//    {
//        BufferStrategy bs = c.getBufferStrategy();
//        Graphics g = bs.getDrawGraphics();
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, WIDTH, HEIGHT);
//        camera.render(g);
//        
//        if(System.currentTimeMillis() > nextSecond)
//        {
//            System.out.println(renderUpdates);
//            renderUpdates = 0;
//            nextSecond = System.currentTimeMillis() + 1000;
//        }
//        renderUpdates++;
//        players.stream().forEach(player -> player.render(g));
//        bs.show();
//        g.dispose();
//    }
}
