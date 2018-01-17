/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.display.Assets;
import simplerts.display.Camera;
import simplerts.display.Display;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
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
    
    private int renderUpdates = 0;
    private long nextSecond = 0;

    public Map map;
    public Camera camera;

    public Handler handler;
    
    public Display display;
    
    public GUI gui;

    public CopyOnWriteArrayList<Player> players;

    @Override
    public void run() {
        
//        if(!glfwInit())
//        {
//            throw new IllegalStateException("Falied to initialize GLFW!");
//        }
//        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
//        long window = glfwCreateWindow(WIDTH, HEIGHT, "Simple RTS", 0, 0);
//        
//        if(window == 0) {
//            throw new IllegalStateException("Failed to create window");
//        }
//        
//        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//        glfwSetWindowPos(window, (videoMode.width() - WIDTH)/2, (videoMode.height() - HEIGHT)/2);
//        glfwShowWindow(window);
//        
//        glfwMakeContextCurrent(window);
//        GL.createCapabilities();
//        
//        while(!glfwWindowShouldClose(window))
//        {
//            glfwPollEvents();
//            
//            glClear(GL_COLOR_BUFFER_BIT);
//            glBegin(GL_QUADS);
//                glColor4f(1, 0, 0, 0);
//                glVertex2f(-0.5f, 0.5f);
//                glVertex2f(0.5f, 0.5f);
//                glColor4f(0, 0, 1, 0);
//                glVertex2f(0.5f, -0.5f);
//                glVertex2f(-0.5f, -0.5f);
//            glEnd();
//            
//            glfwSwapBuffers(window);
//        }
//        
//        glfwTerminate();

        players = new CopyOnWriteArrayList<>();
        Assets.setup();
        camera = new Camera();
        display = new Display(WIDTH, HEIGHT);
        JFileChooser openFile = new JFileChooser();
        openFile.showOpenDialog(display.window);
        map = Map.loadMap(openFile.getSelectedFile());
        handler = new Handler(this, map, camera, display);
        map.setHandler(handler);
        display.getGamePanel().setHandler(handler);
        
        gui = new GUI(map, handler);
        
        camera.setHandler(handler);

        Player player = new Player(handler);
        players.add(player);

        int TICKS_PER_SECOND = 50;
        int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
        int MAX_FRAMESKIP = 10;

        long next_game_tick = System.currentTimeMillis();
        int loops;

        boolean game_is_running = true;
        while (game_is_running) {
            loops = 0;
            while (System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {
                update();
                next_game_tick += SKIP_TICKS;
                loops++;
            }
            render();
            renderGUI(display.getGUICanvas());
        }

    }
    
    public void render(Canvas c)
    {
        BufferStrategy bs = c.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        camera.render(g);
        
        if(System.currentTimeMillis() > nextSecond)
        {
            System.out.println(renderUpdates);
            renderUpdates = 0;
            nextSecond = System.currentTimeMillis() + 1000;
        }
        renderUpdates++;
        players.stream().forEach(player -> player.render(g));
        bs.show();
        g.dispose();
    }
    
    public void render()
    {
        display.getGamePanel().repaint();
    }
    
    public void renderGUI(Canvas c)
    {
        BufferStrategy bs = c.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();
        gui.render(g);
        bs.show();
        g.dispose();
    }
    
    public void update()
    {
        players.stream().forEach(player -> player.input());
        handler.map.update();
    }
    
}
