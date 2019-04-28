package application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;

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
						Platform.runLater(() -> { PlayerFX.mainStage.setTitle("Player " + data1.toString()); });
					}
					else if(data.toString().equals("-sgw")) {
						Platform.runLater(() -> { PlayerFX.GameWindow();});
					}
					else if(data.toString().equals("-gh")) {
						hand = (ArrayList<Card>) in.readObject();
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
