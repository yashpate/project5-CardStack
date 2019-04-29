package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

import javafx.scene.image.Image;




public class Dealer extends Thread {
    serverThread game;
    int port;
    ServerSocket server;

    ArrayList<Card> deck = new ArrayList<Card>();
    int numCards;

    ArrayList<playerThread> playerList = new ArrayList<playerThread>();
    Consumer<Serializable> callback;

    ArrayList<Card> middleStack = new ArrayList<Card>();

    Dealer(int port, Consumer<Serializable> callback) {
        this.port = port;
        this.callback = callback;
        //
        //
        numCards = 52;
        int i = 1;
        while(i<=13){
            int n = i+1;
            String s = n+"S.png";
            if(n==11){
                s = "JS.png";
            }
            else if(n==12){
                s = "QS.png";
            }
            else if(n==13){
                s = "KS.png";
            }
            else if(n==14){
                s = "AS.png";
            }
//            Card c = new Card("spade",i+1,new Image(s));
            Card c = new Card("S",i+1);
            deck.add(c);
            i++;
        }
        i = 1;
        while(i<=13){
            int n = i+1;
            String s = n+"D.png";
            if(n==11){
                s = "JD.png";
            }
            else if(n==12){
                s = "QD.png";
            }
            else if(n==13){
                s = "KD.png";
            }
            else if(n==14){
                s = "AD.png";
            }
//            Card c = new Card("diamond",i+1,new Image(s));
            Card c = new Card("D",i+1);
            deck.add(c);
            i++;
        }
        i = 1;
        while(i<=13){
            int n = i+1;
            String s = n+"C.png";
            if(n==11){
                s = "JC.png";
            }
            else if(n==12){
                s = "QC.png";
            }
            else if(n==13){
                s = "KC.png";
            }
            else if(n==14){
                s = "AC.png";
            }
//            Card c = new Card("clubs",i+1,new Image(s));
            Card c = new Card("C",i+1);
            deck.add(c);
            i++;
        }
        i = 1;
        while(i<=13){
            int n = i+1;
            String s = n+"H.png";
            if(n==11){
                s = "JH.png";
            }
            else if(n==12){
                s = "QH.png";
            }
            else if(n==13){
                s = "KH.png";
            }
            else if(n==14){
                s = "AH.png";
            }
//            Card c = new Card("heart",i+1,new Image(s));
            Card c = new Card("H",i+1);
            deck.add(c);
            i++;
        }
        //
        //
        game = new serverThread();
        game.start();
    }

    class serverThread extends Thread {

        public void run() {
            try(ServerSocket server = new ServerSocket(port)) {

                System.out.println("here..1..in server");
                for(int i = 0; i < 4; i++) {
                    System.out.println("here..1..in server");
                    playerThread p = new playerThread(server.accept(),i+1);
                    playerList.add(p);
                    p.start();
                }

            }catch(Exception e) {
                callback.accept("Server Thread Closed!");
            }
        }
    }

    public void startGameWindow() {
        for(int i = 0; i < 4; i++) {
            try {
                playerList.get(i).out.writeObject("-sgw");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Card getCard() {
        Random rand = new Random();

        int x = rand.nextInt(numCards);

        Card c = deck.get(x);
        deck.remove(x);
        numCards--;
        return c;
    }

    ArrayList<Integer> handRank;
    ArrayList<String> handType;
    public void getHand(){
        handRank = new ArrayList<Integer>();
        handType = new ArrayList<String>();

        for(int i = 0; i < 13; i++) {
            Card c = getCard();
            handRank.add(c.rank);
            handType.add(c.type);
        }
    }

    public void dealHand() {
        for(int i = 0; i < 4; i++) {
            try {
                getHand();
                playerList.get(i).out.writeObject("-gh");
                playerList.get(i).out.writeObject(handRank);
                playerList.get(i).out.writeObject(handType);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void send(int playerNum,Serializable data) {
        try {
            playerList.get(playerNum-1).out.writeObject(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendPlayed(String s,String url){
        for(int i = 0; i < 4; i++){
            try{
                playerList.get(i).out.writeObject("-pw");
                playerList.get(i).out.writeObject(s);
                playerList.get(i).out.writeObject(url);
            }
            catch(Exception ex){

            }
        }
    }
int count = 0;
    public void sendTurn(int playerNum){
        for(int i = 0; i < 4; i++){
            try{
                System.out.println("....Yup here in SendTurn.... before");
                count++;
                playerList.get(i).out.writeObject("-tts");
                playerList.get(i).out.writeObject(playerNum);
                System.out.println("....Yup here in SendTurn.... after "+ count+"--------" );
            }
            catch(Exception ex){
                System.out.println("....Yup here in SendTurn.... catch");
            }
        }
    }
    class playerThread extends Thread {
        Socket socket;
        ObjectOutputStream out;
        ObjectInputStream in;
        int playerNum;
        playerThread( Socket socket,int playerNum){

            this.socket = socket;
            this.playerNum = playerNum;
        }

        public void run() {
            try(ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                socket.setTcpNoDelay(true);
                this.out = out;
                this.in = in;


                callback.accept((Serializable) ("Player " + playerNum + " is connected!"));

                out.writeObject("-pn");
                out.writeObject(playerNum);

                out.writeObject("You are now connected!");

                out.writeObject("Waiting for other players to connect!");
                out.writeObject("Once all players are connected, then game window will open");

                if(playerNum == 4) {
                    startGameWindow();
                    dealHand();
                }

                while(true) {
                    Serializable data = (Serializable) in.readObject();
                    if(data.toString().equals("-pc")){
                        Serializable rank = (Serializable) in.readObject();
                        if(Integer.parseInt(rank.toString()) == 11){
                            Serializable type = (Serializable) in.readObject();
                            callback.accept(playerNum + " played " + "J" + type.toString());
                            sendPlayed(playerNum + " played " + "J" + type.toString(),"J" +type.toString()+".png");
                        }else if(Integer.parseInt(rank.toString()) == 12){
                            Serializable type = (Serializable) in.readObject();
                            callback.accept(playerNum + " played " + "Q" + type.toString());
                            sendPlayed(playerNum + " played " + "Q" + type.toString(),"Q" +type.toString()+".png");

                        }else if(Integer.parseInt(rank.toString()) == 13){
                            Serializable type = (Serializable) in.readObject();
                            callback.accept(playerNum + " played " + "K" + type.toString());
                            sendPlayed(playerNum + " played " + "K" + type.toString(),"K" +type.toString()+".png");

                        }else if(Integer.parseInt(rank.toString()) == 14){
                            Serializable type = (Serializable) in.readObject();
                            callback.accept(playerNum + " played " + "A" + type.toString());
                            sendPlayed(playerNum + " played " + "A" + type.toString(),"A" +type.toString()+".png");

                        }else{
                            Serializable type = (Serializable) in.readObject();
                            callback.accept(playerNum + " played " + rank.toString() + type.toString());
                            sendPlayed(playerNum + " played " + rank.toString() + type.toString(),rank.toString()+type.toString()+".png");
                        }
                    }else if(data.toString().equals("-tt")){
                        Serializable data1 = (Serializable) in.readObject();
                        callback.accept("Player " + data1.toString() + " turn!");
                        System.out.println(data.toString()+"....Yup here in server serddsf....");
                        sendTurn(Integer.parseInt(data1.toString()));
                        System.out.println(data.toString()+"....Yup here in server....");
                    }else{
                        callback.accept(data);
                    }

                }

            }catch(Exception e) {
                callback.accept("Client Thread Closed!");
            }
        }
    }


}
