package application;

import java.io.Serializable;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class ServerFX extends Application {
    int port = 0;
    Dealer dealer;
    Consumer<Serializable> callback;

    TextArea messages = new TextArea();

    
    static Text player1stats = new Text("Player 1 Total Cards: 0");
    static Text player2stats = new Text("Player 2 Total Cards: 0");
    static Text player3stats = new Text("Player 3 Total Cards: 0");
    static Text player4stats = new Text("Player 4 Total Cards: 0");

    @Override
    public void start(Stage primaryStage) {
        try {

            BorderPane root = new BorderPane();


            Text welcome = new Text("Welcome to CardStack Game Server!");
            welcome.setFill(Color.DARKRED);
            welcome.setFont(new Font(25));
            welcome.setTextAlignment(TextAlignment.CENTER);

            Text prompt = new Text("Please enter port number in the text field below!");
            prompt.setTextAlignment(TextAlignment.CENTER);

            VBox welcomePrompt = new VBox(20,welcome,prompt);
            welcomePrompt.setAlignment(Pos.CENTER);

            Button startButton = new Button("Start Game");
            Button quitButton = new Button("Quit Game");

            quitButton.setOnAction(e->primaryStage.close());

            TextField portField = new TextField("5555");
            HBox hb = new HBox(10,portField,startButton,quitButton);

            startButton.setOnAction(e->{port = Integer.parseInt(portField.getText());
                dealer = createDealer();
                ServerSideWindow(primaryStage);});

            hb.setAlignment(Pos.CENTER);

            root.setTop(welcomePrompt);
            root.setCenter(hb);

            Scene scene = new Scene(root,450,200);


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

        Button stopButton = new Button("Stop Game");
        stopButton.setOnAction(e->primaryStage.close());


        VBox center = new VBox(10,messages,stopButton);
        center.setAlignment(Pos.CENTER);

        

        VBox player1 = new VBox(player1stats);
        VBox player2 = new VBox(player2stats);
        VBox player3 = new VBox(player3stats);
        VBox player4 = new VBox(player4stats);

        player1.setAlignment(Pos.CENTER);
        player2.setAlignment(Pos.CENTER);
        player3.setAlignment(Pos.CENTER);
        player4.setAlignment(Pos.CENTER);

        root.setCenter(center);
        root.setTop(player1);
        root.setRight(player2);
        root.setBottom(player3);
        root.setLeft(player4);

        Scene scene = new Scene(root,500,400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


}