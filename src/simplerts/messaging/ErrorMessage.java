/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.messaging;

/**
 *
 * @author Markus
 */
public class ErrorMessage extends Message {

    public ErrorMessage(String message) {
        super(message);
        font = Message.DEFAULT_MESSAGE;
        color = Message.ERROR_MESSAGE_COLOR;
    }
    
}
