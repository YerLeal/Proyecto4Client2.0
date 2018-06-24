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

/**
 *
 * @author maikel
 */
public class SpaceShip extends Thread {

    private int x, y, size;
    private int life;
    private int iCont;
    private int type, player;
    private ArrayList<Image> sprites;

    public SpaceShip(int x, int y, int size, int life, int type, int player) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.life = life;
        iCont = 0;
        this.player = player;
        this.type = type;
        sprites = new ArrayList<>();
        chargeSprites();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void impact() {
        life--;
    }

    public int getLife() {
        return life;
    }

    public void chargeSprites() {

        for (int i = 0; i < 9; i++) {
            if (player == 1) {
                if (type == 1) {
                    this.sprites.add(new Image("/assets/mE" + i + ".png"));
                }else{
                    this.sprites.add(new Image("/assets/pn" + i + ".png"));
                }
            } else {
                if (type == 1) {
                    this.sprites.add(new Image("/assets/mED" + i + ".png"));
                }else{
                    this.sprites.add(new Image("/assets/n" + i + ".png"));
                }
            }
        }
    }

    public int getiCont() {
        return iCont;
    }

    
    
    
    @Override
    public void run() {
        while (iCont<9) {
            try {
                iCont++;
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void draw(GraphicsContext gc) {
        
        gc.drawImage(sprites.get(iCont), x * size, y * size, size, size);
    }
}
