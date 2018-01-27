/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.display;

import java.awt.Graphics;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import simplerts.Game;
import simplerts.Handler;

/**
 *
 * @author Markus
 */
public class MessageManager {
    
    public static int MESSAGE_LENGTH = 3000;
    private Handler handler;
    private CopyOnWriteArrayList<Message> messages;
    
    public MessageManager(Handler handler)
    {
        this.handler = handler;
        messages = new CopyOnWriteArrayList<>();
    }
    
    public void addMessage(Message message)
    {
        messages.add(0, message);
        new Thread(() -> {
            long done = System.currentTimeMillis() + MESSAGE_LENGTH;
            while(true)
            {
                if(System.currentTimeMillis() >= done)
                {
                    messages.remove(message);
                    break;
                }
            }
        }).start();
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
    
}
