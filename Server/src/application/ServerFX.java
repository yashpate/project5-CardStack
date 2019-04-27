package application;
	
import java.io.Serializable;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class ServerFX extends Application {
	int port = 0;
	Dealer dealer;
	Consumer<Serializable> callback;
	
	TextField messages = new TextField();
	
	int totalCard1=0;
	int totalCard2=0;
	int totalCard3=0;
	int totalCard4=0;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			BorderPane root = new BorderPane();
			
			Button startButton = new Button("Start");
			TextField portField = new TextField("5555");
			HBox hb = new HBox(10,portField,startButton);
			
			startButton.setOnAction(e->{ServerSideWindow(primaryStage);
										dealer = createDealer();
										port = Integer.parseInt(portField.getText());});
			
			hb.setAlignment(Pos.CENTER);
			
			root.setCenter(hb);
			
			Scene scene = new Scene(root,400,400);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Dealer createDealer() {
	// TODO Auto-generated method stub
		
		return new Dealer(port, data-> {
			Platform.runLater(()->{
				messages.appendText(data.toString() + "\n"); 
				});
			});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void ServerSideWindow(Stage primaryStage) {

		BorderPane root = new BorderPane();
		
		Text player1stats = new Text("Player 1 Total Cards: " + Integer.toString(totalCard1));
		Text player2stats = new Text("Player 2 Total Cards: " + Integer.toString(totalCard2));
		Text player3stats = new Text("Player 3 Total Cards: " + Integer.toString(totalCard3));
		Text player4stats = new Text("Player 4 Total Cards: " + Integer.toString(totalCard4));
		
		VBox player1 = new VBox(player1stats);
		VBox player2 = new VBox(player2stats);
		VBox player3 = new VBox(player3stats);
		VBox player4 = new VBox(player4stats);
		
		player1.setAlignment(Pos.CENTER);
		player2.setAlignment(Pos.CENTER);
		player3.setAlignment(Pos.CENTER);
		player4.setAlignment(Pos.CENTER);
		
		root.setCenter(messages);
		root.setTop(player1);
		root.setRight(player2);
		root.setBottom(player3);
		root.setLeft(player4);
		
		Scene scene = new Scene(root,400,400);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
}
