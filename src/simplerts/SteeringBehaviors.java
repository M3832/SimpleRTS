/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import simplerts.entities.Entity;
import simplerts.entities.Unit;


/**
 *
 * @author Markus
 */
public class SteeringBehaviors {
    
    public static final int LEADER_BEHIND_DISTANCE = 1;
     
    public static Destination followLeader(Unit leader)
    {
        Vector2D leaderVector = new Vector2D(leader.getDeltaX(), leader.getDeltaY());
        Vector2D force = new Vector2D(0, 0);
        
        leaderVector.scaleBy(-1);
        leaderVector.normalize();
        leaderVector.scaleBy(LEADER_BEHIND_DISTANCE);
        Destination behind = new Destination(leader.getDestination());
        behind.addVector(leaderVector);
        
        return behind;
    }
    
    
    
}
