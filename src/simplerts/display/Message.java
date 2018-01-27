/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.display;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author Markus
 */
public class Message {
    
    public static Font DEFAULT_MESSAGE = new Font("Verdana", Font.BOLD, 16);
    public static Color DEFAULT_MESSAGE_COLOR = Color.WHITE;
    public static Color ERROR_MESSAGE_COLOR = Color.RED;
    protected String message;
    protected Color color;
    protected Font font;
    
    public Message(String message)
    {
        this.message = message;
        font = DEFAULT_MESSAGE;
        color = DEFAULT_MESSAGE_COLOR;
    }
    
    public Message(String message, Color color, Font font)
    {
        this(message);
        this.font = font;
        this.color = color;
    }
    
    public String getMessage()
    {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    public void setFont(Font font) {
        this.font = font;
    }
    
}
