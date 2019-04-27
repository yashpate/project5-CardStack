package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
            Card c = new Card("spade",i+1,new Image(s));
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
            Card c = new Card("diamond",i+1,new Image(s));
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
            Card c = new Card("clubs",i+1,new Image(s));
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
            Card c = new Card("heart",i+1,new Image(s));
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
	
	public void send(int playerNum,Serializable data) {
		try {
			playerList.get(playerNum-1).out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				
				System.out.println("here..2..in server");
				callback.accept("Player " + playerNum + " is connected!");
				out.writeObject("You are now connected!");
				System.out.println("here..3..in server");
				out.writeObject((Serializable) "-pn");
				out.writeObject((Serializable)playerNum);
				System.out.println("here..5..in server");
				while(true) {
					Serializable data = (Serializable) in.readObject();
					callback.accept(data);
				}
				
			}catch(Exception e) {
				callback.accept("Client Thread Closed!");
			}
		}
	}

	
}

