/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import proyecto4client2.Window;

/**
 *
 * @author maikel
 */
public class Missile extends Thread {

    private int xI, yI, xF, action;
    private ArrayList<Image> sprites;
    private int iCont, player;
    private boolean end = false, end1 = false;

    public Missile(int xI, int yI, int xF, int player, int action) {
        this.xI = xI;
        this.yI = yI;
        this.xF = xF;
        this.iCont = 0;
        this.player = player;
        this.sprites = new ArrayList<>();
        chargeSprites();
        this.action = action;
    }

    @Override
    public void run() {
//        System.err.println("Missile Init");
        while (xI != xF) {
            if (player == 1) {
                xI += 10;
            } else {
                xI -= 10;
            }
            iCont++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (action == 1) {
            end1 = true;
            iCont = 4;
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (end) {
                Window.flag = false;
            }
        }
        //System.err.println("Missile Dead");
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void chargeSprites() {
        for (int i = 0; i < 5; i++) {
            if (player == 1) {
                this.sprites.add(new Image("/assets/mu" + i + ".png"));
            } else {
                this.sprites.add(new Image("/assets/md" + i + ".png"));
            }
        }
    }

    public int getxI() {
        return xI;
    }

    public int getyI() {
        return yI;
    }

    public void draw(GraphicsContext gc) {
        if (iCont > 3 && end1 != true) {
            iCont = 0;
        }

        if (end1 == true) {
            gc.drawImage(sprites.get(iCont), xI + 20, yI + 45, 60, 60);
        } else {
            gc.drawImage(sprites.get(iCont), xI, yI + 65, 60, 20);
        }

    }

}
