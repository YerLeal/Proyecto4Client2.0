package domain;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import proyecto4client2.Window;

public class Portal extends Thread {

    private int x, y, size;
    private ArrayList<Image> sprites;
    private int imageCount, player;
    private int state = 0;
    private boolean flag = true;
    private boolean end = false;

    public Portal(int x, int y, int player, int size) {
        this.x = x;
        this.y = y;
        this.imageCount = 7;
        this.player = player;
        this.sprites = new ArrayList<>();
        chargeSprites();
        this.size = size;
    } // constructor

    @Override
    public void run() {
        this.flag = true;
        this.state = 0;
        while (this.flag) {
            if (this.state == 1) {
                portalClose();
                if (this.end) {
                    Window.flag = false;
                }
            } else {
                this.imageCount--;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    } // run

    public int getX() {
        return this.x;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getImageCount() {
        return this.imageCount;
    } // getImageCount

    public void setEnd(boolean end) {
        this.end = end;
    } // setEnd

    private void chargeSprites() {
        for (int i = 0; i < 9; i++) {
            if (player == 1) {
                this.sprites.add(new Image("/assets/p" + i + ".png"));
            } else {
                this.sprites.add(new Image("/assets/pD" + i + ".png"));
            }
        }
    } // chargeSprites

    public void portalClose() {
        while (this.imageCount < 8) {
            this.imageCount++;
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Portal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.flag = false;
    } // portalClose

    public void draw(GraphicsContext gc) {
        if (this.imageCount < 0) {
            this.imageCount = 3;
        }
        gc.drawImage(this.sprites.get(this.imageCount), this.x, this.y, 35, size);
    } // draw

} // end class
