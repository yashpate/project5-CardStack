package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class PlayerFX extends Application {
	static boolean startThread = false;
	static Player p;
	String ip;
	int port;
	
	static TextArea messages = new TextArea();
	
	static Stage mainStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			mainStage = primaryStage;
			BorderPane root = new BorderPane();
			
			Text welcome = new Text("Welcome to CardStack Game!");
			welcome.setFill(Color.DARKRED);
			welcome.setFont(new Font(25));
			welcome.setTextAlignment(TextAlignment.CENTER);
			
			Text prompt = new Text("Please enter port number and ip in the text fields below!");
			prompt.setTextAlignment(TextAlignment.CENTER);
			
			VBox welcomePrompt = new VBox(20,welcome,prompt);
			welcomePrompt.setAlignment(Pos.CENTER);
			
			Button startButton = new Button("Start Game");
			Button quitButton = new Button("Quit Game");
			
			quitButton.setOnAction(e->mainStage.close());
			
			TextField portField = new TextField("5555");
			TextField ipField = new TextField("127.0.0.1");
			HBox hb = new HBox(10,portField,ipField,startButton,quitButton);
			
			startButton.setOnAction(e->{port = Integer.parseInt(portField.getText());
										ip = ipField.getText();
										p = createPlayer();});
			
			hb.setAlignment(Pos.CENTER);
			
			root.setTop(welcomePrompt);
			root.setCenter(hb);
			root.setBottom(messages);
			
			
			Scene scene = new Scene(root,500,250);
			
			mainStage.setScene(scene);
			mainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	private Player createPlayer() {
		// TODO Auto-generated method stub
		return new Player(port,ip, data-> {
			Platform.runLater(()->{
				messages.appendText(data.toString() + "\n"); 
				});
			});
	}
	
	public static void GameWindow() {
		BorderPane root = new BorderPane();
		ImageView player1Card = new ImageView("purple_back.png");
		ImageView player2Card = new ImageView("green_back.png");
		ImageView player3Card = new ImageView("blue_back.png");
		ImageView player4Card = new ImageView("gray_back.png");
		
		
		player1Card.setFitHeight(75);
		player1Card.setFitWidth(55);
		player1Card.setPreserveRatio(true);
		
		player2Card.setFitHeight(75);
		player2Card.setFitWidth(55);
		player2Card.setPreserveRatio(true);
		
		player3Card.setFitHeight(75);
		player3Card.setFitWidth(55);
		player3Card.setPreserveRatio(true);
		
		player4Card.setFitHeight(75);
		player4Card.setFitWidth(55);
		player4Card.setPreserveRatio(true);
		
		
		
		
		
		
		HBox p1 = new HBox(player1Card);
		VBox p2 = new VBox(player2Card);
		HBox p3 = new HBox(player3Card);
		VBox p4 = new VBox(player4Card);
		
		p1.setAlignment(Pos.CENTER);
		p2.setAlignment(Pos.CENTER);
		p3.setAlignment(Pos.CENTER);
		p4.setAlignment(Pos.CENTER);
		
		
		messages.clear();
		messages.setText("Welcome to the CardStack Game!");
		HBox msg = new HBox(messages);
		msg.setAlignment(Pos.CENTER);
		HBox stack = new HBox();
		stack.setAlignment(Pos.CENTER);
		HBox center = new HBox(5,msg,stack);
		center.setAlignment(Pos.CENTER);
		
		
		if(p.playerNum == 1) {
			player1Card.setOnMouseClicked(e->{center.getChildren().clear();
				if(p.hand.size() > 0) {
					center.getChildren().add(p.hand.get(p.hand.size()-1).pic);
					p.hand.remove(p.hand.size()-1);
				}
			});
		}else if(p.playerNum == 2) {
			player2Card.setOnMouseClicked(e->{center.getChildren().clear();
				if(p.hand.size() > 0) {
					center.getChildren().add(p.hand.get(p.hand.size()-1).pic);
					p.hand.remove(p.hand.size()-1);
				}
			});
		}else if(p.playerNum == 3) {
			player3Card.setOnMouseClicked(e->{center.getChildren().clear();
				if(p.hand.size() > 0) {
					center.getChildren().add(p.hand.get(p.hand.size()-1).pic);
					p.hand.remove(p.hand.size()-1);
				}
			});
		}else if(p.playerNum == 4) {
			player4Card.setOnMouseClicked(e->{center.getChildren().clear();
				if(p.hand.size() > 0) {
					center.getChildren().add(p.hand.get(p.hand.size()-1).pic);
					p.hand.remove(p.hand.size()-1);
				}
			});
		}
		
		
		root.setTop(p1);
		root.setRight(p2);
		root.setBottom(p3);
		root.setLeft(p4);
		root.setCenter(center);
		
		Scene scene = new Scene(root,500,250);
		mainStage.setScene(scene);
		mainStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
}
