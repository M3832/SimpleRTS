/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.actions;

import java.awt.Graphics;
import simplerts.display.Camera;
import simplerts.entities.Entity;
import simplerts.entities.Unit;

/**
 *
 * @author Markus
 */
public class Follow extends Action {

    private final Entity target;
    private MoveTo movePath;
    
    public Follow(Unit owner, Entity target) {
        super(owner);
        this.target = target;
    }

    @Override
    public void performAction() {
        if(movePath == null)
        {
            movePath = new MoveTo(owner, target);
            System.out.println("New movepath for follow");
        }
        
        if(movePath != null)
            movePath.performAction();
        
        
    }
    
    @Override
    public void render(Graphics g, Camera camera)
    {
        if(movePath != null)
            movePath.render(g, camera);
        
        g.setColor(owner.getPlayer().getController().getColorFromAllegiance(target));
        g.drawRect(target.getX() - (int)camera.getOffsetX(), target.getY() - (int)camera.getOffsetY(), target.getWidth(), target.getHeight());
    }
    
    @Override
    public boolean isMoving()
    {
        if(movePath != null)
            return movePath.isMoving();
        
        return true;
    }
}
