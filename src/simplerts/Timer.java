/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

/**
 *
 * @author Markus
 */
public class Timer extends Thread {
    
    public int waitTime;
    
    public Timer(int waitTime, Runnable r)
    {
        super(() -> {
            long startTime = System.currentTimeMillis() + waitTime;
            while(true)
            {
                if(System.currentTimeMillis() > startTime)
                {
                    new Thread(r).start();
                    return;
                }
            }
        });
        this.waitTime = waitTime;
    } 
}
