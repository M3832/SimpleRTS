/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.audio;

import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Markus
 */
public class SoundController {
    
    public static final int CONFIRM = 1;
    public static final int WAKE = 0;
    
    private final ArrayList<Sound> confirm;
    private final ArrayList<Sound> wake;
    
    public SoundController()
    {
        confirm = new ArrayList<>();
        wake = new ArrayList<>();
    }
    
    public void addSound(String url, int type)
    {
            switch(type)
            {
                case WAKE:
                    wake.add(SoundController.getSoundFromURL(url));
                    break;
                case CONFIRM:
                    confirm.add(SoundController.getSoundFromURL(url));
                    break;
            }
    }
    
    public static Sound getSoundFromURL(String url)
    {
        Clip c;
        File soundFile = new File(File.class.getResource(url).getFile());
        try(AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile)){
            c = AudioSystem.getClip();
            c.open(audioIn);
            return new Sound(c);
        }catch(Exception e)
        {
            System.out.println(e);
        }
        
        return null;
    }
    
    public void playSound(int type)
    {
        switch(type)
        {
            case WAKE:
                if(wake.size() > 0)
                {
                    wake.get((int)(Math.random() * wake.size())).play();
                }
                break;
            case CONFIRM:
                if(confirm.size() > 0)
                {
                    confirm.get((int)(Math.random() * confirm.size())).play();
                }
                break;
        }
    }
}
