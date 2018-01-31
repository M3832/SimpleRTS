/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.audio;

import javax.sound.sampled.Clip;

/**
 *
 * @author Markus
 */
public class Sound {
    
    private final Clip soundClip;
    
    public Sound(Clip c)
    {
        soundClip = c;
    }
    
    public void play()
    {
        soundClip.setMicrosecondPosition(0);
        soundClip.start();
    }

    void setLoop() {
        soundClip.loop(10);
    }
    
}
