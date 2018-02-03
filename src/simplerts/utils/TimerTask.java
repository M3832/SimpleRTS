/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.utils;

/**
 *
 * @author Markus
 */
public class TimerTask {
    
    private final long executeTime;
    private final Runnable r;
    
    public TimerTask(int time, Runnable r)
    {
        executeTime = System.currentTimeMillis() + time;
        this.r = r;
    }
    
    public boolean run()
    {
        if(System.currentTimeMillis() < executeTime)
        {
            return false;
        } else {
            r.run();
            return true;
        }
    }
    
}
