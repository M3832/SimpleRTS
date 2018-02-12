/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities.resources;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import simplerts.Game;
import simplerts.Player;
import simplerts.display.Camera;
import simplerts.entities.Building;
import simplerts.entities.Entity;
import simplerts.entities.Unit;
import simplerts.utils.Utils;
import simplerts.entities.actions.TurnInGold;
import simplerts.gfx.Assets;
import simplerts.entities.interfaces.GoldProvider;
import simplerts.entities.interfaces.Goldminer;
import simplerts.utils.TimerTask;

/**
 *
 * @author Markus
 */
public class Goldmine extends Building implements GoldProvider{
    
    private Goldminer miner;
    private int gold;
    private BufferedImage mineOn;

    public Goldmine(int x, int y, Player player) {
        super(x, y, 3, player, true);
        sprite = Assets.loadAndResizeImage("/mine.png", width, height);
        mineOn = Assets.loadAndResizeImage("/mineon.png", width, height);
        gold = 5000;
    }

    @Override
    public Entity duplicate() {
        return new Goldmine(x, y, player);
    }

    @Override
    public boolean enterGatherer(Goldminer g) {
        if(miner == null)
        {
            miner = g;
            miner.enter();
            miner.setGold(miner.getGoldCapacity());
            gold -= miner.getGoldCapacity();
            g.setLatestMine(this);
            addTask(new TimerTask(1000, () -> {
                exitGatherer();
            }));
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public void exitGatherer()
    {
        if(miner != null)
        {
            miner.exit(((Unit)miner).getDestination());
            ((Unit)miner).addAction(new TurnInGold(((Unit)miner)));
            miner = null;
        }
    }
    
    @Override
    public void render(Graphics g, Camera camera)
    {
        super.render(g, camera);
        if(miner != null)
        {
            g.drawImage(mineOn, x - offsetX, y - offsetY, null);
        }
    }
    
    @Override
    public void renderGUI(Graphics g)
    {
        super.renderGUI(g);
        String goldString = "Gold: " + gold;
        Utils.drawWithShadow(g, goldString, Game.WIDTH/2 - g.getFontMetrics().stringWidth(goldString)/2, Game.HEIGHT + 100);
    }
    
}
