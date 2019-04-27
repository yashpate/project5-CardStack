package application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Player {
	int playerNum;
	int port;
	String ip;
	Consumer<Serializable> callback;
	playerThread player;
	
	ArrayList<Card> hand = new ArrayList<Card>();
	
	Player(int port, String ip,Consumer<Serializable> callback){
		this.port = port;
		this.ip = ip;
		this.callback = callback;
		player = new playerThread();
		player.start();
	}
	
	
	
	class playerThread extends Thread {
		Socket socket;
		ObjectOutputStream out;
		ObjectInputStream in;
			
		public void run() {
			try(Socket socket = new Socket(ip,port); 
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
				
				this.out = out;
				this.in = in;
				
				
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
