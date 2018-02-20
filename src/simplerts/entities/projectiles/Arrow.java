/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.projectiles;

import simplerts.entities.Entity;
import simplerts.entities.interfaces.Attacker;
import simplerts.gfx.AnimationController;
import simplerts.utils.Utilities;

/**
 *
 * @author Markus
 */
public class Arrow extends Projectile {

    public Arrow(Attacker owner, Entity target) {
        super(owner, target);
        ac = this.owner.getPlayer().getSpriteManager().getArrowAC();
        ac.setAnimation("default");
        ac.setDirection(Utilities.getFacingDirection(target.getX() - x, target.getY() - y));
        speed = 5;
    }
}
