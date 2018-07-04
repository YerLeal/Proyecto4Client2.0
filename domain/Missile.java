package domain;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import proyecto4client2.Window;

public class Missile extends Thread {

    private int xI, yI, xF, action, size;
    private ArrayList<Image> sprites;
    private int imageCount, player;
    private boolean end = false, end1 = false;

    public Missile(int xI, int yI, int xF, int player, int action, int size) {
        this.xI = xI;
        this.yI = yI + 1;
        this.xF = xF;
        this.imageCount = 0;
        this.player = player;
        this.sprites = new ArrayList<>();
        chargeSprites();
        this.action = action;
        this.size = size;
    } // constructor

    @Override
    public void run() {
        while (xI != xF) {
            if (player == 1) {
                xI += 10;
            } else {
                xI -= 10;
            }
            imageCount++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (action == 1) {
            end1 = true;
            imageCount = 4;
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (end) {
                Window.flag = false;
            }
        }
    } // run

    public void setEnd(boolean end) {
        this.end = end;
    }

    private void chargeSprites() {
        for (int i = 0; i < 5; i++) {
            if (player == 1) {
                this.sprites.add(new Image("/assets/mu" + i + ".png"));
            } else {
                this.sprites.add(new Image("/assets/md" + i + ".png"));
            }
        }
    } // chargeSprites

    public int getxI() {
        return xI;
    }

    public int getyI() {
        return yI;
    }

    public void draw(GraphicsContext gc) {
        if (imageCount > 3 && end1 != true) {
            imageCount = 0;
        }

        if (end1 == true) {
            if (size == 150) {
                gc.drawImage(sprites.get(imageCount), xI + 30, yI + 45, 60, 60);
            } else {
                gc.drawImage(sprites.get(imageCount), xI + 25, yI + 15, 40, 40);
            }
        } else {
            if (size == 150) {
                gc.drawImage(sprites.get(imageCount), xI + 20, yI + 65, 60, 20);
            } else {
                gc.drawImage(sprites.get(imageCount), xI, yI + 35, 40, 20);
            }
        }
    } // draw

} // end class
