package domain;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SpaceShip extends Thread {

    private int x, y, size;
    private int life;
    private int imageCount;
    private int type, player;
    private ArrayList<Image> sprites;

    public SpaceShip(int x, int y, int size, int life, int type, int player) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.life = life;
        this.imageCount = 0;
        this.player = player;
        this.type = type;
        this.sprites = new ArrayList<>();
        chargeSprites();
    } // constructor

    public int getX() {
        return this.x;
    } // getX

    public int getY() {
        return this.y;
    } // getY

    public void impact() {
        this.life--;
    } // impact

    public int getLife() {
        return this.life;
    } // getLife

    private void chargeSprites() {
        for (int i = 0; i < 9; i++) {
            if (this.player == 1) {
                if (this.type == 1) {
                    this.sprites.add(new Image("/assets/mE" + i + ".png"));
                } else {
                    this.sprites.add(new Image("/assets/pn" + i + ".png"));
                }
            } else {
                if (this.type == 1) {
                    this.sprites.add(new Image("/assets/mED" + i + ".png"));
                } else {
                    this.sprites.add(new Image("/assets/n" + i + ".png"));
                }
            }
        } // for
    } // chargeSprites

    public int getImageCount() {
        return this.imageCount;
    } // getImageCount

    public int getType() {
        return type;
    }

    @Override
    public void run() {
        while (this.imageCount < 9) {
            try {
                this.imageCount++;
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(SpaceShip.class.getName()).log(Level.SEVERE, null, ex);
            }
        } // while
    } // run

    public void draw(GraphicsContext gc) {
        gc.drawImage(this.sprites.get(imageCount), x * size, y * size, size, size);
    } // draw

} // end class
