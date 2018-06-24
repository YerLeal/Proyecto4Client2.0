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
public class Portal extends Thread {

    private int x, y;
    private ArrayList<Image> sprites;
    private int iCont, player;
    private int state = 0;
    private boolean s = true;
    private boolean end=false;
    public Portal(int x, int y, int player) {
        this.x = x;
        this.y = y;
        this.iCont = 7;
        this.player = player;
        this.sprites = new ArrayList<>();
        chargeSprites();
    }

    public int getX() {
        return x;
    }

    public void setState(int state) {
        this.state = state;
    }
    
    

    @Override
    public void run() {

        while (s) {
            if (state == 1) {
                portalClose();
                if(end){
                    Window.cosa=false;
                }
            } else {
                iCont--;

                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public int getiCont() {
        return iCont;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void chargeSprites() {
        for (int i = 0; i < 9; i++) {
            if (player == 1) {
                this.sprites.add(new Image("/assets/p" + i + ".png"));
            } else {
                this.sprites.add(new Image("/assets/md" + i + ".png"));
            }
        }
    }

    public void portalClose() {
        while (iCont < 8) {
            iCont++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Portal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        s = false;
    }

    public void draw(GraphicsContext gc) {
        if (iCont < 0) {
            iCont = 3;
        }
        gc.drawImage(sprites.get(iCont), x, y, 35, 150);
    }

}
