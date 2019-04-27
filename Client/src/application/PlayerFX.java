package application;
	
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class PlayerFX extends Application {
	static boolean startThread = false;
	Player p;
	String ip;
	int port;
	
	TextField messages = new TextField();
	
	@Override
	public void start(Stage primaryStage) {
		try {
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
			
			quitButton.setOnAction(e->primaryStage.close());
			
			TextField portField = new TextField("5555");
			TextField ipField = new TextField("127.0.0.1");
			HBox hb = new HBox(10,portField,ipField,startButton,quitButton);
			
			startButton.setOnAction(e->{p = createPlayer();
										port = Integer.parseInt(portField.getText());
										ip = ipField.getText();});
			
			hb.setAlignment(Pos.CENTER);
			
			root.setTop(welcomePrompt);
			root.setCenter(hb);
			messages.setMinSize(500,70);
			root.setBottom(messages);
			
			
			Scene scene = new Scene(root,500,200);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void ClientSideWindow(Stage primaryStage) {
		// TODO Auto-generated method stub
		
	}

	private Player createPlayer() {
		// TODO Auto-generated method stub
		return new Player(port,ip, data-> {
			Platform.runLater(()->{
				messages.appendText(data.toString() + "\n"); 
				});
			});
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
}
