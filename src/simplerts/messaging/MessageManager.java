/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.messaging;

import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.Game;
import simplerts.Handler;
import simplerts.utils.TaskManager;
import simplerts.utils.TimerTask;

/**
 *
 * @author Markus
 */
public class MessageManager {
    
    
    public static int MESSAGE_LENGTH = 3000;
    private Handler handler;
    private CopyOnWriteArrayList<Message> messages;
    private TaskManager taskManager;
    
    public MessageManager(Handler handler)
    {
        this.handler = handler;
        messages = new CopyOnWriteArrayList<>();
        taskManager = new TaskManager();
    }
    
    public void update()
    {
        taskManager.update();
    }
    
    public void addMessage(Message message)
    {
        messages.add(0, message);
        taskManager.addTask(new TimerTask(MESSAGE_LENGTH, () -> {messages.remove(message);}));
        checkCheats(message.message);
    }
    
    public void render(Graphics g)
    {
        Message current;
        for(int i = 0; i < messages.size(); i++)
        {
            current = messages.get(i);
            g.setFont(current.getFont());
            g.setColor(current.getColor());
            g.drawString(current.getMessage(), Game.WIDTH/2 - g.getFontMetrics(current.getFont()).stringWidth(current.getMessage())/2, Game.HEIGHT - 20 - i * g.getFontMetrics(current.getFont()).getHeight());
        }
    }

    private void checkCheats(String message) {
        switch(message)
        {
            case "showall":
                handler.game.controller.showAll();
                break;
            case "debug":
                Game.DEBUG = !Game.DEBUG;
                addMessage(new Message("Debug: " + Game.DEBUG));
                break;
        }
    }
    
}
