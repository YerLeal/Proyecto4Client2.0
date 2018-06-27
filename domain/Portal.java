package domain;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import proyecto4client2.Window;

public class Portal extends Thread {

    private int x, y;
    private ArrayList<Image> sprites;
    private int iCont, player;
    private int state = 0;
    private boolean flag = true;
    private boolean end = false;

    public Portal(int x, int y, int player) {
        this.x = x;
        this.y = y;
        this.iCont = 7;
        this.player = player;
        this.sprites = new ArrayList<>();
        chargeSprites();
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
                this.iCont--;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };
    } // run

    public int getX() {
        return this.x;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getiCont() {
        return this.iCont;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void chargeSprites() {
        for (int i = 0; i < 9; i++) {
            if (player == 1) {
                this.sprites.add(new Image("/assets/p" + i + ".png"));
            } else {
                this.sprites.add(new Image("/assets/pD" + i + ".png"));
            }
        }
    } // chargeSprites

    public void portalClose() {
        while (this.iCont < 8) {
            this.iCont++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Portal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.flag = false;
    } // portalClose

    public void draw(GraphicsContext gc) {
        if (this.iCont < 0) {
            this.iCont = 3;
        }
        gc.drawImage(this.sprites.get(this.iCont), this.x, this.y, 35, 150);
    } // draw

} // end class
