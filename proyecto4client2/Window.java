package proyecto4client2;

import domain.Missile;
import domain.Portal;
import domain.SpaceShip;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Window extends Application {

    private final int WIDTH = 1360;
    private final int HEIGHT = 700;
    private BorderPane pane;
    private Scene scene;
    private HBox hBox;
    private Canvas canvasPlayer1, canvasPlayer2;
    private GraphicsContext gc1, gc2;
    private int playerNumber = 1;
    private Button btnAddMother, btnLaunch;
    public static Boolean state1 = false, cosa = true;
    private SpaceShip mother;
    private Missile missile;
    private Portal portal;
    private int size = 150;
    private ArrayList<SpaceShip> spaceShips;
    private int x = 2, y = 2, mCont = 0, mP = 0;

    private Runnable launch = new Runnable() {
        @Override
        public void run() {
            
            
            if (playerNumber == 1) {
                missile = new Missile(mother.getX() * size, mother.getY() * size, 450, playerNumber, size);
                portal = new Portal(420, mother.getY() * size, 1);
            } else {
                missile = new Missile(mother.getX() * size+size/2-5, mother.getY() * size, 0, playerNumber, size);
                portal = new Portal(-5, mother.getY() * size, 2);
            }
            missile.start();
            while (cosa) {
                if (portal.getX() - missile.getxI() < 100 && playerNumber == 1 && missile.isAlive() == true && portal.isAlive() == false) {
                    portal.start();
                } else if ((portal.getX() + missile.getxI() < 100 && playerNumber == 2) && missile.isAlive() == true && portal.isAlive() == false) {
                    portal.start();
                }
                System.err.println(missile.getxI());
                if ((missile.getxI() == portal.getX() && playerNumber==1)||(missile.getxI() == portal.getX()+5 && playerNumber==2)) {
                    
                    portal.setState(1);
                    portal.setEnd(true);
                }
                auxDraw();

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    private Runnable recieve = new Runnable() {
        @Override
        public void run() {
            cosa = true;
            if (playerNumber == 1) {
                portal = portal = new Portal(420, y * size, playerNumber);
            } else {
                portal = portal = new Portal(0, y * size, playerNumber);
            }
            portal.start();
            if (playerNumber == 1) {
                missile = new Missile(430, y * size, x * size, 2, size);
            } else {
                missile = new Missile(0, y * size, x * size, 1, size);
            }

            missile.setEnd(true);
            while (cosa) {

                if (portal.getiCont() == 3 && missile.isAlive() == false) {
                    missile.start();
                }
                if ((portal.getX() - missile.getxI() > 50 && playerNumber == 1) || (portal.getX() + missile.getxI() > 50 && playerNumber == 2)) {
                    portal.setState(1);
                }
                auxDraw();
                try {
                    Thread.sleep(50);

                } catch (InterruptedException ex) {
                    Logger.getLogger(Window.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }

            for (int i = 0; i < spaceShips.size(); i++) {
                int x = spaceShips.get(i).getX() * size;
                int y = spaceShips.get(i).getY() * size;
                if ((missile.getxI() >= x && missile.getxI() <= x + size)
                        && (missile.getyI() >= y && missile.getyI() <= y + size)) {
                    spaceShips.get(i).impact();
                    if (spaceShips.get(i).getLife() == 0) {
                        spaceShips.get(i).start();
                        while (spaceShips.get(i).getiCont() < 9) {
                            try {
                                auxDraw();
                                Thread.sleep(50);

                            } catch (InterruptedException ex) {
                                Logger.getLogger(Window.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        spaceShips.remove(i);
                    }
                    break;
                }
            }

            auxDraw();
        }

    };

    public void auxDraw() {
        if (playerNumber == 1) {
            draw(gc1);
        } else {
            draw(gc2);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Jueguito");
        init(primaryStage);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
    } // start

    private void init(Stage primaryStage) {
        spaceShips = new ArrayList<>();
        this.hBox = new HBox();
        this.pane = new BorderPane();
        this.btnAddMother = new Button("Set Mother");
        btnLaunch = new Button("Launch");
        this.canvasPlayer1 = new Canvas(450, 450);
        this.canvasPlayer2 = new Canvas(450, 450);
        canvasPlayer1.setOnMouseClicked(evento);
        canvasPlayer2.setOnMouseClicked(evento);
        gc1 = canvasPlayer1.getGraphicsContext2D();
        gc2 = canvasPlayer2.getGraphicsContext2D();
        drawGrid(gc1);
        drawGrid(gc2);
        btnAddMother.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                state1 = !state1;
            }
        });
        btnLaunch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cosa = true;
                new Thread(launch).start();
            }
        });
        this.hBox.getChildren().add(canvasPlayer1);
        this.hBox.getChildren().add(canvasPlayer2);
        this.pane.setCenter(hBox);
        HBox b = new HBox();
        b.getChildren().addAll(btnAddMother, btnLaunch);
        this.pane.setBottom(b);
        this.scene = new Scene(this.pane, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
    } // init

    EventHandler<MouseEvent> evento = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getSource() == canvasPlayer1 && playerNumber == 1 && state1 == true) {
                double xMouse = event.getX();
                double yMouse = event.getY();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if ((xMouse >= i * size && xMouse <= i * size + size)
                                && (yMouse >= j * size && yMouse <= j * size + size)) {

                            mother = new SpaceShip(i, j, size, 2, 1, playerNumber);
                            if (mCont < 1) {
                                mP = spaceShips.size();
                                spaceShips.add(mother);
                                mCont++;
                            } else {
                                spaceShips.remove(mP);
                                spaceShips.add(mother);
                            }

                            draw(gc1);
                        }
                    }
                }
            } else if (event.getSource() == canvasPlayer2 && playerNumber == 2 && state1 == true) {
                double xMouse = event.getX();
                double yMouse = event.getY();
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if ((xMouse >= i * size && xMouse <= i * size + size)
                                && (yMouse >= j * size && yMouse <= j * size + size)) {
                            mother = new SpaceShip(i, j, size, 2, 1, playerNumber);
                            if (mCont < 1) {
                                mP = spaceShips.size();
                                spaceShips.add(mother);
                                mCont++;
                            } else {
                                spaceShips.remove(mP);
                                spaceShips.add(mother);
                            }
                            draw(gc2);
                        }
                    }
                }
            }
        }
    };

    public static void main(String[] args) {
        launch(args);
    } // main

    public void drawGrid(GraphicsContext gcM) {
        if (playerNumber == 1) {
            for (int x = 0; x <= 3; x++) {
                gcM.strokeLine(0, x * this.size, 3 * this.size, x * this.size); // rows
            } // for x
            for (int y = 0; y <= 3; y++) {
                gcM.strokeLine(y * this.size, 0, y * this.size, this.size * 3); // cols
            } // for y
        } else {
            for (int x = 0; x <= 3; x++) {
                gcM.strokeLine(0, x * this.size, 3 * this.size, x * this.size); // rows
            } // for x
            for (int y = 0; y <= 3; y++) {
                gcM.strokeLine(y * this.size, 0, y * this.size, this.size * 3); // cols
            } // for y
        }

    } // drawGrid: dibuja las lineas del mosaico

    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, 570, 570);
        drawGrid(gc);
        for (int i = 0; i < spaceShips.size(); i++) {
            if (spaceShips.get(i) != null) {

                spaceShips.get(i).draw(gc);

            }
        }

        if (missile != null) {
            if (missile.isAlive() == true) {
                missile.draw(gc);
            }
        }
        if (portal != null) {
            if (portal.isAlive() == true) {
                portal.draw(gc);
            }
        }
    }

} // end class