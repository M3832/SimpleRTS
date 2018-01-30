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
public class SoundManager {
    
    public static final int CONFIRM_CLIP = 1;
    public static final int WAKE_CLIP = 0;
    
    private ArrayList<Clip> confirm;
    private ArrayList<Clip> wake;
    
    public SoundManager()
    {
        confirm = new ArrayList<>();
        wake = new ArrayList<>();
    }
    
    public void addSound(String url, int type)
    {
        File soundFile = new File(File.class.getResource(url).getFile());
        System.out.println(soundFile);
        try(AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile)){
            Clip c = AudioSystem.getClip();
            c.open(audioIn);
            switch(type)
            {
                case WAKE_CLIP:
                    wake.add(c);
                    break;
                case CONFIRM_CLIP:
                    confirm.add(c);
                    break;
            }
            
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    public void playSound(int type)
    {
        System.out.println("Playing sound");
        Clip temp;
        switch(type)
        {
            case WAKE_CLIP:
                if(wake.size() > 0)
                {
                    temp = wake.get((int)(Math.random() * wake.size()));
                    temp.setMicrosecondPosition(0);
                    temp.start();
                }
                break;
            case CONFIRM_CLIP:
                if(confirm.size() > 0)
                {
                    temp = confirm.get((int)(Math.random() * confirm.size()));
                    temp.setMicrosecondPosition(0);
                    temp.start();
                }
                break;
        }
    }
}
