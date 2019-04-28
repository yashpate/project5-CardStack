package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
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

    static Stage mainStage;

    public Scene welcomeClient(){
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

        startButton.setOnAction(e->{
            port = Integer.parseInt(portField.getText());
            ip = ipField.getText();
            p = createPlayer();
            Platform.runLater(() ->{
                mainStage.setScene(gamePlay());
            });
        });

        hb.setAlignment(Pos.CENTER);

        root.setTop(welcomePrompt);
        root.setCenter(hb);
        messages.setMinSize(500,70);
        root.setBottom(messages);


        Scene scene = new Scene(root,550,180);
        return scene;
    }

    public Scene gamePlay(){

        BorderPane pane = new BorderPane();
        VBox vBoxTop = new VBox();
        VBox vBoxBottom = new VBox();
        VBox vBoxLeft = new VBox();
        VBox vBoxRight = new VBox();

        //player number labels
        Label p1Label = new Label("PLayer 1");
        Label p2Label = new Label("PLayer 2");
        Label p3Label = new Label("PLayer 3");
        Label p4Label = new Label("PLayer 4");


        //Cards for each player
        Button p1Card = new Button("Cards");
//        ImageView imgView = new ImageView(new Image("blue_back.png"));
//
//            imgView.setFitWidth(100);
//            imgView.setFitHeight(100);
//            imgView.setPreserveRatio(true);
//
//        p1Card.setGraphic(imgView);


        vBoxTop.getChildren().addAll(p1Label);
        vBoxBottom.getChildren().addAll(p3Label,p1Card);
        vBoxRight.getChildren().addAll(p2Label);
        vBoxLeft.getChildren().addAll(p4Label);


        //Alignment and Spacing
        vBoxTop.setAlignment(Pos.CENTER);
        vBoxBottom.setAlignment(Pos.CENTER);
        vBoxLeft.setAlignment(Pos.CENTER);
        vBoxRight.setAlignment(Pos.CENTER);

        pane.setTop(vBoxTop);
        pane.setBottom(vBoxBottom);
        pane.setLeft(vBoxLeft);
        pane.setRight(vBoxRight);

        Scene scene = new Scene(pane,500,500);

        return scene;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            mainStage = primaryStage;
            mainStage.setScene(welcomeClient());
            mainStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void ClientSideWindow() {
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
