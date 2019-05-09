package application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player {
    int playerNum;
    int port;
    String ip;
    Consumer<Serializable> callback;
    playerThread player;

    ArrayList<String> handType = new ArrayList<String>();
    ArrayList<Integer> handRank = new ArrayList<Integer>();

    Player(int port, String ip,Consumer<Serializable> callback){
        this.port = port;
        this.ip = ip;

        this.callback = callback;
        player = new playerThread();
        System.out.println("here in1 client" + port + "  " + ip);
        player.start();
        System.out.println("here in2 client");
    }



    class playerThread extends Thread {
        Socket socket;
        ObjectOutputStream out;
        ObjectInputStream in;

        public void run() {
            try(Socket socket = new Socket(ip,port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                socket.setTcpNoDelay(true);
                this.socket = socket;
                this.out = out;
                this.in = in;

                System.out.println("here in client");
                while(true) {
                    Serializable data = (Serializable) in.readObject();
                    if(data.toString().equals("-pn")) {
                        Serializable data1 = (Serializable) in.readObject();
                        playerNum = Integer.parseInt(data1.toString());
                        Platform.runLater(() -> { PlayerFX.mainStage.setTitle("Player " + playerNum); });
                    }else if(data.toString().equals("-ms")) {
                    	Serializable dataR = (Serializable) in.readObject();
                    	Serializable dataT = (Serializable) in.readObject();
                    	int rank = Integer.parseInt(dataR.toString());
                    	String type = dataT.toString();
                        handRank.add(rank);
                        handType.add(type);
                    }else if(data.toString().equals("-cm")) {
                    	Platform.runLater(() -> PlayerFX.stack.getChildren().clear());
                    	Serializable data1 = (Serializable) in.readObject();
                    	callback.accept(data1);
                    }
                    else if(data.toString().equals("-sgw")) {
                        Platform.runLater(() -> { PlayerFX.GameWindow();});
                    }
                    else if(data.toString().equals("-gh")) {
                        handRank = (ArrayList<Integer>) in.readObject();
                        handType = (ArrayList<String>) in.readObject();
                    }else if(data.toString().equals("-pw")){
                        Serializable data1 = (Serializable) in.readObject();
                        Serializable url = (Serializable) in.readObject();
                        callback.accept(data1.toString());
                        Platform.runLater(() -> { PlayerFX.stack.getChildren().clear();
                        ImageView pic = new ImageView(url.toString());
                        pic.setFitWidth(55);
                        pic.setFitHeight(75);
                        pic.setPreserveRatio(true);
                        PlayerFX.stack.getChildren().add(pic);
                        });
                    }else if(data.toString().equals("-tts")){
                        Serializable data1 = (Serializable) in.readObject();
                        System.out.println(data1.toString()+"........here");
                        PlayerFX.takeTurn(Integer.parseInt(data1.toString()));
                        System.out.println(data1.toString()+"here....");
                    }else if(data.toString().equals("-winner")) {
                    	Serializable data1 = (Serializable) in.readObject();
                    	Platform.runLater(() -> PlayerFX.gameOver(data1.toString()));
                    }
                    else {
                        callback.accept(data);
                    }
                }
        

            }catch(Exception e) {
                callback.accept("Client Thread Closed!");
            }
        }
    }
}