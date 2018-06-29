package proyecto4client2;

import domain.Missile;
import domain.Portal;
import domain.SpaceShip;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;
import utilities.Constants;

public class Window extends Application {

    private final int WIDTH = 1360;
    private final int HEIGHT = 700;
    private Pane principalPane, bottonPane;
    private Scene scene;
    private HBox canvasBox;
    private Canvas canvasPlayer1, canvasPlayer2, backGroundCanvas;
    private GraphicsContext gc1, gc2;
    private Button btnSetMother, btnSetMinions, btnLaunch, btnSendName, btnSendMessage, btnSetFinish;
    public static Boolean motherActive = false, minionActive = false, flag = true;
    private boolean myTurn;
    private SpaceShip mother;
    private Missile missile;
    private Portal portal;
    private ArrayList<SpaceShip> spaceShips;
    private int x, y, xO, yO, mCont = 1, mP = 0, playerNumber = -1, size = 150, rc, minions;
    private Label lbName;
    private TextArea chat;
    private TextField tfdName, tfdMessage;
    private String namePlayer;
    private ComboBox<String> cbxType;
    private Text positions[][];
    private Runnable chatThread = new Runnable() {
        @Override
        public void run() {
            try {
                ServerSocket chatServer = new ServerSocket(Constants.chatPortNumber);
                Socket chatConnection;
                while (true) {
                    chatConnection = chatServer.accept();
                    DataInputStream recieve = new DataInputStream(chatConnection.getInputStream());
                    chat.setStyle("-fx-text-inner-color: red;");
                    chat.appendText(recieve.readUTF() + "\n");
                    recieve.close();
                    chatConnection.close();
                }

            } catch (IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    };

    private Runnable launch = new Runnable() {
        @Override
        public void run() {
            System.out.println("Launch");
            if (playerNumber == 1) {
                missile = new Missile(mother.getX() * size, mother.getY() * size, 450, playerNumber, size, size);
                portal = new Portal(420, mother.getY() * size, 1, size);
            } else {
                missile = new Missile(mother.getX() * size + size / 2 - 5, mother.getY() * size, 0, playerNumber, size, size);
                portal = new Portal(-5, mother.getY() * size, 2, size);
            }
            missile.start();
            while (flag) {
                if (portal.getX() - missile.getxI() < 100 && playerNumber == 1 && missile.isAlive() == true && portal.isAlive() == false) {
                    portal.start();
                } else if ((portal.getX() + missile.getxI() < 100 && playerNumber == 2) && missile.isAlive() == true && portal.isAlive() == false) {
                    portal.start();
                }
                if ((missile.getxI() == portal.getX() && playerNumber == 1) || (missile.getxI() == portal.getX() + 5 && playerNumber == 2)) {
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
            try {
                Socket client = new Socket(utilities.Constants.address, utilities.Constants.socketPortNumber);
                DataOutputStream dat = new DataOutputStream(client.getOutputStream());

                dat.writeUTF("attack&" + playerNumber + "&" + xO + "&" + yO);

                dat.close();
                client.close();

            } catch (IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
            myTurn = false;
        }
    };

    private Runnable recieve = new Runnable() {
        @Override
        public void run() {
            try {
                ServerSocket recibir = new ServerSocket(9090);
                Socket entrada;
                while (true) {
                    entrada = recibir.accept();
                    DataInputStream data = new DataInputStream(entrada.getInputStream());
                    String datos[] = data.readUTF().split("&");
                    if (datos[0].equalsIgnoreCase("end")) {

                    } else {
                        x = Integer.parseInt(datos[0]);
                        y = Integer.parseInt(datos[1]);
                        data.close();
                        flag = true;
                        if (playerNumber == 1) {
                            portal = portal = new Portal(420, y * size, playerNumber, size);
                            portal.start();
                        } else {
                            portal = portal = new Portal(0, y * size, playerNumber, size);
                            portal.start();
                        }
                        if (playerNumber == 1) {
                            missile = new Missile(430, y * size, x * size, 2, 1, size);
                        } else {
                            missile = new Missile(0, y * size, x * size, 1, 1, size);
                        }
                        missile.setEnd(true);
                        while (flag) {
                            if (portal.getImageCount() == 3 && missile.isAlive() == false) {
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
                        isImpact();
                    }

                    auxDraw();
                    myTurn = true;
                }
            } catch (IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    public void auxDraw() {
        if (this.playerNumber == 1) {
            draw(this.gc1);
        } else {
            draw(this.gc2);
        }
    } // auxDraw

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Jueguito");
        initComponents(primaryStage);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
        new Thread(this.recieve).start();
        new Thread(this.chatThread).start();
    } // start

    private void initComponents(Stage primaryStage) {
        backGroundCanvas = new Canvas(WIDTH, HEIGHT);
        backGroundCanvas.getGraphicsContext2D().drawImage(new Image("assets/background.jpg"), 0, 0, WIDTH, HEIGHT);
        //Labels
        this.lbName = new Label("Name: ");

        //buttons
        this.btnSendName = new Button("Send Name");
        this.btnSetMother = new Button("Set Mother");
        this.btnLaunch = new Button("Launch");
        this.btnSendName.setOnAction(buttonsEvents);
        this.btnSendMessage = new Button("Send Message");
        this.btnSetMinions = new Button("SetMinions");
        this.btnSetFinish = new Button("Finish");
        //textField-Area
        this.tfdName = new TextField();
        this.tfdMessage = new TextField();
        this.chat = new TextArea();

        //Panes-Canvas
        this.canvasBox = new HBox();
        this.principalPane = new Pane();
        this.canvasPlayer1 = new Canvas(450, 450);
        this.canvasPlayer2 = new Canvas(450, 450);
        Pane chatPane = new Pane();
        bottonPane = new Pane();

        //setEvents
        this.canvasPlayer1.setOnMouseClicked(this.canvasEvents);
        this.canvasPlayer2.setOnMouseClicked(this.canvasEvents);
        this.btnSetMother.setOnAction(buttonsEvents);
        this.btnLaunch.setOnAction(buttonsEvents);
        this.btnSendMessage.setOnAction(buttonsEvents);
        this.btnSetMinions.setOnAction(buttonsEvents);
        this.btnSetFinish.setOnAction(buttonsEvents);
        //graphicContext
        this.gc1 = this.canvasPlayer1.getGraphicsContext2D();
        this.gc2 = this.canvasPlayer2.getGraphicsContext2D();

        //size and relocated
        this.btnLaunch.setPrefSize(100, 50);
        chatPane.setPrefSize(450, 700);
        this.chat.relocate(75, 0);
        this.chat.setPrefSize(300, 450);
        this.tfdMessage.relocate(120, 480);
        this.btnSendMessage.relocate(300, 480);
        bottonPane.setPrefSize(500, 200);
        this.lbName.relocate(10, 20);
        this.tfdName.relocate(60, 20);
        this.tfdName.setPrefWidth(80);
        this.btnSendName.relocate(150, 20);
        this.btnSetMother.relocate(20, 30);
        this.btnSetMinions.relocate(120, 30);
        this.btnSetFinish.relocate(220, 30);
        this.btnLaunch.relocate(50, 60);
        //other
        this.spaceShips = new ArrayList<>();

        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("3X3", "5X5");
        cbxType = new ComboBox<>(list);
        cbxType.relocate(10, 50);

        //add
        chatPane.getChildren().addAll(chat, btnSendMessage, tfdMessage);
        this.canvasBox.getChildren().addAll(this.canvasPlayer1, this.canvasPlayer2);
        bottonPane.getChildren().addAll(this.lbName, this.tfdName, this.btnSendName, this.cbxType);

        this.canvasBox.relocate(0, 0);
        bottonPane.relocate(0, 450);
        chatPane.relocate(900, 0);

        principalPane.getChildren().addAll(backGroundCanvas, bottonPane, chatPane, canvasBox);
        this.scene = new Scene(this.principalPane, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
    } // init

    EventHandler<MouseEvent> canvasEvents = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (playerNumber == 1 && event.getSource() == canvasPlayer1 && event.getButton() == MouseButton.SECONDARY) {
                deleteSpaceShip(event.getY(), event.getX());
            } else if (playerNumber == 2 && event.getSource() == canvasPlayer2 && event.getButton() == MouseButton.SECONDARY) {
                deleteSpaceShip(event.getY(), event.getX());
            } else if (event.getSource() == canvasPlayer1 && playerNumber == 1 && motherActive) {
                setSpaceShip(event.getY(), event.getX(), 1);
            } else if (event.getSource() == canvasPlayer2 && playerNumber == 2 && motherActive) {
                setSpaceShip(event.getY(), event.getX(), 1);
            } else if (playerNumber == 1 && event.getSource() == canvasPlayer2 && myTurn) {
                selectEnemyPosition(event.getX(), event.getY());
            } else if (playerNumber == 2 && event.getSource() == canvasPlayer1 && myTurn) {
                selectEnemyPosition(event.getX(), event.getY());
            } else if (playerNumber == 1 && event.getSource() == canvasPlayer1 && minionActive && event.getButton() == MouseButton.PRIMARY) {
                setSpaceShip(event.getY(), event.getX(), 2);
            } else if (playerNumber == 2 && event.getSource() == canvasPlayer2 && minionActive && event.getButton() == MouseButton.PRIMARY) {
                setSpaceShip(event.getY(), event.getX(), 2);
            }
        }
    };

    EventHandler<ActionEvent> buttonsEvents = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() == btnLaunch) {
                flag = true;
                new Thread(launch).start();
            } else if (event.getSource() == btnSetMother) {
                motherActive = !motherActive;
                minionActive = false;
            } else if (event.getSource() == btnSendName) {
                sendName();
                bottonPane.getChildren().clear();
                bottonPane.getChildren().addAll(btnSetMother, btnSetMinions, btnSetFinish);
                if (cbxType.getValue().equals("3X3")) {
                    rc = 3;
                    minions = 2;
                    size = 150;
                } else {
                    rc = 5;
                    size = 90;
                    minions = 4;
                }
                fillPositions();
                drawGrid(gc1);
                drawGrid(gc2);
            } else if (event.getSource() == btnSendMessage) {
                sendMessage();
            } else if (event.getSource() == btnSetMinions) {
                motherActive = false;
                minionActive = !minionActive;
            } else if (event.getSource() == btnSetFinish) {
                bottonPane.getChildren().clear();
                bottonPane.getChildren().addAll(btnLaunch);
            }
        }
    };

    public void fillPositions() {
        if(playerNumber==1){
            gc2.setFill(Color.GREENYELLOW);
        }else{
            gc1.setFill(Color.GREENYELLOW);
        }
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {

                switch (i) {
                    case 0:
                        if(playerNumber==1){
                            gc2.fillText("A"+j,i * size + size / 2, j * size + size / 2);
                        }else{
                            gc1.fillText("A"+j,i * size + size / 2, j * size + size / 2);
                        }
                        
                        break;
                    case 1:
                        if(playerNumber==1){
                            gc2.fillText("B"+j,i * size + size / 2, j * size + size / 2);
                        }else{
                            gc1.fillText("B"+j,i * size + size / 2, j * size + size / 2);
                        }break;
                    case 2:
                        if(playerNumber==1){
                            gc2.fillText("C"+j,i * size + size / 2, j * size + size / 2);
                        }else{
                            gc1.fillText("C"+j,i * size + size / 2, j * size + size / 2);
                        }break;
                    case 3:
                        if(playerNumber==1){
                            gc2.fillText("D"+j,i * size + size / 2, j * size + size / 2);
                        }else{
                            gc1.fillText("D"+j,i * size + size / 2, j * size + size / 2);
                        }break;
                    case 4:
                        if(playerNumber==1){
                            gc2.fillText("E"+j,i * size + size / 2, j * size + size / 2);
                        }else{
                            gc1.fillText("E"+j,i * size + size / 2, j * size + size / 2);
                        }break;
                    default:
                        break;
                }
            }
        }
    }

    public void sendName() {
        try {
            namePlayer = tfdName.getText();
            Socket socket = new Socket(utilities.Constants.address, utilities.Constants.socketPortNumber);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("log&" + namePlayer);
            String m = dis.readUTF();
            System.out.println(m);
            playerNumber = Integer.parseInt(m);
            myTurn = playerNumber == 1;
            dos.close();
            dis.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage() {
        chat.setStyle("-fx-text-inner-color: blue;");
        String message = namePlayer + ":" + tfdMessage.getText() + "\n";
        chat.appendText(message);
        try {
            Socket socket = new Socket(Constants.address, Constants.socketPortNumber);
            DataOutputStream dat = new DataOutputStream(socket.getOutputStream());
            dat.writeUTF("chat&" + playerNumber + "&" + tfdMessage.getText());
            dat.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        tfdMessage.clear();

    }

    public static void main(String[] args) {
        launch(args);
    } // main

    private void drawGrid(GraphicsContext gcM) {
        if (this.playerNumber == 1) {
            for (int i = 0; i <= rc; i++) {
                gcM.strokeLine(0, i * this.size, rc * this.size, i * this.size); // rows
            } // for x
            for (int j = 0; j <= rc; j++) {
                gcM.strokeLine(j * this.size, 0, j * this.size, this.size * rc); // cols
            } // for y
        } else {
            for (int i = 0; i <= rc; i++) {
                gcM.strokeLine(0, i * this.size, rc * this.size, i * this.size); // rows
            } // for x
            for (int j = 0; j <= rc; j++) {
                gcM.strokeLine(j * this.size, 0, j * this.size, this.size * rc); // cols
            } // for y
        }
    } // drawGrid: dibuja las lineas del mosaico

    private void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, 570, 570);
        drawGrid(gc);
        for (int i = 0; i < this.spaceShips.size(); i++) {
            if (this.spaceShips.get(i) != null) {
                this.spaceShips.get(i).draw(gc);
            }
        }
        if (this.missile != null) {
            if (this.missile.isAlive() == true) {
                this.missile.draw(gc);
            }
        }
        if (this.portal != null) {
            if (this.portal.isAlive() == true) {
                this.portal.draw(gc);
            }
        }
    } // draw

    public void setSpaceShip(double yClick, double xClick, int type) {
        double xMouse = xClick;
        double yMouse = yClick;
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {
                if ((xMouse >= i * size && xMouse <= i * size + size)
                        && (yMouse >= j * size && yMouse <= j * size + size)) {
                    if (type == 1) {
                        mother = new SpaceShip(i, j, size, 2, 1, playerNumber);
                        if (mCont > 0) {
                            mP = spaceShips.size();
                            spaceShips.add(mother);
                            mCont--;
                        }
                    } else {
                        if (minions > 0) {
                            spaceShips.add(new SpaceShip(i, j, size, 1, 2, playerNumber));
                            minions--;
                        }
                    }
                    if (playerNumber == 1) {
                        draw(gc1);
                    } else {
                        draw(gc2);
                    }
                }
            }
        }
    }

    public void deleteSpaceShip(double yClick, double xClick) {
        for (int i = 0; i < spaceShips.size(); i++) {
            int x = spaceShips.get(i).getX() * size;
            int y = spaceShips.get(i).getY() * size;
            if ((xClick >= x && xClick <= x + size)
                    && (yClick >= y && yClick <= y + size)) {
                if (i == mP) {
                    mCont++;
                    spaceShips.remove(i);
                } else {
                    minions++;
                    spaceShips.remove(i);
                }
                if (playerNumber == 1) {
                    draw(gc1);
                } else {
                    draw(gc2);
                }
                break;
            }
        }
    }

    private void selectEnemyPosition(double x, double y) {
        double xMouse = x;
        double yMouse = y;
        for (int i = 0; i < rc; i++) {
            for (int j = 0; j < rc; j++) {
                if ((xMouse >= i * size && xMouse <= i * size + size)
                        && (yMouse >= j * size && yMouse <= j * size + size)) {
                    xO = i;
                    yO = j;
                    System.out.println(xO + " " + yO);
                }
            }
        }
    }

    public void isImpact() {
        for (int i = 0; i < spaceShips.size(); i++) {
            int x = spaceShips.get(i).getX() * size;
            int y = spaceShips.get(i).getY() * size;
            if ((missile.getxI() >= x && missile.getxI() <= x + size)
                    && (missile.getyI() >= y && missile.getyI() <= y + size)) {
                spaceShips.get(i).impact();
                if (spaceShips.get(i).getLife() == 0) {
                    spaceShips.get(i).start();
                    while (spaceShips.get(i).getImageCount() < 9) {
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
    }

} // end class
