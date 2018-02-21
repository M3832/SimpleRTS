/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.audio;

/**
 *
 * @author Markus
 */
public class SoundManager {
    
    public Sound backgroundSound;
    
    public SoundManager()
    {
        backgroundSound = SoundController.getSoundFromURL("/music.wav");
        init();
    }
    
    private void init()
    {
        backgroundSound.setLoop();
        backgroundSound.play();
    }
}
