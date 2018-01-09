/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author Markus
 */
public class Game implements Runnable {

    public static int WIDTH = 980;
    public static int HEIGHT = 720;
    public static float GAMESPEED = 1f;
    public static int CELLSIZE = 50;
    
    private int renderUpdates = 0;
    private long nextSecond = 0;

    public Map map;
    public Camera camera;

    public Handler handler;

    public CopyOnWriteArrayList<Player> players;

    @Override
    public void run() {
        Display display = new Display(WIDTH, HEIGHT);
        players = new CopyOnWriteArrayList<>();
        SpriteHolder.setup();
        JFileChooser openFile = new JFileChooser();
        openFile.showOpenDialog(display.window);
        map = Map.loadMap(openFile.getSelectedFile());
        camera = new Camera();
        handler = new Handler(this, map, camera, display);

        camera.setHandler(handler);

        Player player = new Player(map, display.getCanvas(), handler);
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
            render(display.getCanvas());
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
    
    public void update()
    {
        players.stream().forEach(player -> player.input());
        handler.map.update();
    }
    
}
