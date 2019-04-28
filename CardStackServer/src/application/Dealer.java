package application;

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
	
	public ArrayList<Card> getHand(){
		ArrayList<Card> hand = new ArrayList<Card>();
		
		for(int i = 0; i < 13; i++) {
			hand.add(getCard());
		}
	
		return hand;	
	}
	
	public void dealHand() {
		for(int i = 0; i < 4; i++) {
			try {
				playerList.get(i).out.writeObject("-gh");
				playerList.get(i).out.writeObject((Serializable) getHand());
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
					callback.accept(data);
				}
				
			}catch(Exception e) {
				callback.accept("Client Thread Closed!");
			}
		}
	}

	
}

