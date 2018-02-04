/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.map.BackEndMap;
import simplerts.map.MapIO;
import simplerts.gfx.Assets;
import simplerts.display.Display;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Color;
import javax.swing.JFileChooser;
import simplerts.messaging.Message;
import simplerts.messaging.MessageManager;
import simplerts.entities.Builder;
import simplerts.entities.Footman;
import simplerts.entities.Goldmine;
//import javax.swing.JFileChooser;

/**
 *
 * @author Markus
 */
public class Game implements Runnable {

    public static final int WIDTH = 980;
    public static final int HEIGHT = 512;
    public static final int GUIHEIGHT = 225;
    public static final int CELLSIZE = 50;
    public static final float GAMESPEED = 1f;
    
    public static final int TICKS_PER_SECOND = 50;
    public static final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    public static final int MAX_FRAMESKIP = 10;
    public static final long TIME_SINCE_LAST_UPDATE = 0;
        
    public static final int RENDERS_PER_SECOND = 120;
    public static final int SKIP_RENDERS = 1000 / RENDERS_PER_SECOND;
    public static final int MAX_RENDERSKIP = 10;
    public static long time_since_last_render = 0;

    public BackEndMap map;
    public Handler handler;
    public Display display;
    public MessageManager mm;
    public CopyOnWriteArrayList<Player> players;
    public Controller controller;

    @Override
    public void run() {
        Assets.setup();
        JFileChooser openFile = new JFileChooser();
        players = new CopyOnWriteArrayList<>();
        display = new Display(WIDTH, HEIGHT, GUIHEIGHT);
//        openFile.showOpenDialog(display.window);
//        map = MapIO.loadMap(openFile.getSelectedFile());
        map = MapIO.loadMap("/asd");
        handler = new Handler(this, map, display);
        mm = new MessageManager(handler);
        
        Player player = new Player(handler, Color.WHITE);
        controller = new Controller(handler, player);
        players.add(player);
        Player enemy = new Player(handler, Color.BLUE);
        
        map.addEntity(new Builder(10, 6, player));
        map.addEntity(new Footman(25, 26, player));
        map.addEntity(new Footman(25, 27, player));
        map.addEntity(new Goldmine(4, 5, map.getNeutral()));

        map.addEntity(new Builder(25, 25, enemy));
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
                    time_since_last_render = System.currentTimeMillis() - lastRender;
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
        mm.update();
    }
}
