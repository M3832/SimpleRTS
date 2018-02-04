/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.utils;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Markus
 */
public class TaskManager {
    
    private final CopyOnWriteArrayList<TimerTask> tasks;
    
    public TaskManager()
    {
        tasks = new CopyOnWriteArrayList<>();
    }
    
    public void addTask(TimerTask t)
    {
        tasks.add(t);
    }
    
    public void update()
    {
        if(!tasks.isEmpty())
        {
            for(int i = Math.max(0, tasks.size() - 1); i >= 0; i--)
            {
                if(tasks.get(i).run())
                {
                    tasks.remove(tasks.get(i));
                }
            }
        }
    }
    
}
