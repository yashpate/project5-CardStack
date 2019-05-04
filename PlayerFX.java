package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Modality;
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
            Button rulesButton = new Button("Game Rules");

            rulesButton.setOnAction(e->{
                dialogFunction();
            });


            quitButton.setOnAction(e->mainStage.close());

            TextField portField = new TextField("5555");
            TextField ipField = new TextField("127.0.0.1");
            HBox hb = new HBox(10,portField,ipField,startButton,quitButton,rulesButton);



            startButton.setOnAction(e->{port = Integer.parseInt(portField.getText());
                ip = ipField.getText();
                p = createPlayer();});

            hb.setAlignment(Pos.CENTER);

            root.setTop(welcomePrompt);
            root.setCenter(hb);
            root.setBottom(messages);


            Scene scene = new Scene(root,600,300);

            mainStage.setScene(scene);
            mainStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void dialogFunction() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.initOwner(mainStage);
        Text welcome = new Text("Rules: \n\n" +
                "Card Stacking game is interesting card game. \n" +
                "It can be played with 2 or more players but in\n" +
                " our version of teh game, it will be played\n" +
                " by 4 players. The cards will be dealt to all 4 \n" +
                "players so that they each will have 13 cards. One\n" +
                " at a time players will start stacing cards in the \n" +
                "middle and if the card that you put in the middle \n" +
                "is of a same type, then all the cards in the middle \n" +
                "will of to the player that had a match with the top \n" +
                "card in the middle. This will be repeated until all \n" +
                "player except one runs out of card. Then, we get a winner! ");
        VBox rulesBox = new VBox(welcome);
        Scene dialogScene = new Scene(rulesBox,350,300);
        dialog.setScene(dialogScene);
        dialog.show();
    }


    private Player createPlayer() {
        // TODO Auto-generated method stub
        return new Player(port,ip, data-> {
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");
            });
        });
    }
    static HBox stack = new HBox();

    public static void gameFunction(){
        stack.getChildren().clear();
        System.out.println(p.handRank.size() + " " + p.handType.size());
        if(p.handRank.size() > 0) {
            int rank = p.handRank.get(p.handRank.size()-1);
            String type = p.handType.get(p.handType.size()-1);
            String s = "";
            if(rank <= 10){
                s = rank+type+".png";
            }else if(rank == 11) {
                s = "J" + type + ".png";
            }else if(rank == 12){
                s = "Q"+type+".png";
            }else if(rank == 13) {
                s = "K" + type + ".png";
            }else if(rank == 14){
                s = "A"+type+".png";
            }
            Card c = new Card(type,rank,new Image(s));

            ImageView pic = c.pic;
            pic.setFitWidth(55);
            pic.setFitHeight(75);
            pic.setPreserveRatio(true);

            stack.getChildren().add(pic);
            p.handType.remove(p.handType.size()-1);
            p.handRank.remove(p.handRank.size()-1);

            try {
                p.player.out.writeObject("-pc");
                p.player.out.writeObject(rank);
                p.player.out.writeObject(type);
            }catch(Exception ex){

            }

        }
    }


    public static void takeTurn(int id){
        p.callback.accept(p.handRank.size());
        if(id==1){
            player1Card.setDisable(false);
            player2Card.setDisable(true);
            player3Card.setDisable(true);
            player4Card.setDisable(true);
        }
        else if(id == 2){
            player2Card.setDisable(false);
            player1Card.setDisable(true);
            player3Card.setDisable(true);
            player4Card.setDisable(true);
        }
        else if(id == 3){
            player3Card.setDisable(false);
            player1Card.setDisable(true);
            player2Card.setDisable(true);
            player4Card.setDisable(true);
        }
        else if(id == 4){
            player4Card.setDisable(false);
            player1Card.setDisable(true);
            player2Card.setDisable(true);
            player3Card.setDisable(true);
        }
    }

    static Button player1Card = new Button();
    static Button player2Card = new Button();
    static Button player3Card = new Button();
    static Button player4Card = new Button();

    public static void GameWindow() {
        BorderPane root = new BorderPane();

        ImageView i1 = new ImageView("blue_back.png");
        ImageView i2 = new ImageView("gray_back.png");
        ImageView i3 = new ImageView("green_back.png");
        ImageView i4 = new ImageView("purple_back.png");

        i1.setFitWidth(55);
        i1.setFitHeight(75);
        i1.setPreserveRatio(true);

        i2.setFitWidth(55);
        i2.setFitHeight(75);
        i2.setPreserveRatio(true);

        i3.setFitWidth(55);
        i3.setFitHeight(75);
        i3.setPreserveRatio(true);

        i4.setFitWidth(55);
        i4.setFitHeight(75);
        i4.setPreserveRatio(true);

        player1Card.setGraphic(i1);
        player2Card.setGraphic(i2);
        player3Card.setGraphic(i3);
        player4Card.setGraphic(i4);



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
        messages.setPrefHeight(100);
        HBox msg = new HBox(messages);
        msg.setAlignment(Pos.CENTER);

        stack.setAlignment(Pos.CENTER);
        HBox center = new HBox(5,msg,stack);
        center.setAlignment(Pos.CENTER);

        takeTurn(1);
        if(p.playerNum == 1) {
            System.out.println(p.handRank.size() + " " + p.handType.size());
            player1Card.setOnAction(e->{//stack.getChildren().clear();
//                System.out.println(p.handRank.size() + " " + p.handType.size());
//                if(p.handRank.size() > 0) {
//                    int rank = p.handRank.get(p.handRank.size()-1);
//                    String type = p.handType.get(p.handType.size()-1);
//                    String s = "";
//                    if(rank <= 10){
//                        s = rank+ type +".png";
//                    }else if(rank == 11) {
//                        s = "J" + type + ".png";
//                    }else if(rank == 12){
//                        s = "Q"+ type + ".png";
//                    }else if(rank == 13) {
//                        s = "K" + type + ".png";
//                    }else if(rank == 14){
//                        s = "A" + type + ".png";
//                    }
//                    Card c = new Card(type,rank,new Image(s));
//
//                    ImageView pic = c.pic;
//                    pic.setFitWidth(55);
//                    pic.setFitHeight(75);
//                    pic.setPreserveRatio(true);
//
//                    stack.getChildren().add(c.pic);
//                    p.handType.remove(p.handType.size()-1);
//                    p.handRank.remove(p.handRank.size()-1);
//                }
                gameFunction();
                //takeTurn(p.playerNum+1);
                try{
                    p.player.out.writeObject("-tt");
                    p.player.out.writeObject(p.playerNum+1);
                }catch(Exception ex){

                }

            });
        }else if(p.playerNum == 2) {
            System.out.println(p.handRank.size() + " " + p.handType.size());
            player2Card.setOnAction(e->{//stack.getChildren().clear();
//                System.out.println(p.handRank.size() + " " + p.handType.size());
//                if(p.handRank.size() > 0) {
//                    int rank = p.handRank.get(p.handRank.size()-1);
//                    String type = p.handType.get(p.handType.size()-1);
//                    String s = "";
//                    if(rank <= 10){
//                        s = rank+type+".png";
//                    }else if(rank == 11) {
//                        s = "J" + type + ".png";
//                    }else if(rank == 12){
//                        s = "Q"+type+".png";
//                    }else if(rank == 13) {
//                        s = "K" + type + ".png";
//                    }else if(rank == 14){
//                        s = "A"+type+".png";
//                    }
//                    Card c = new Card(type,rank,new Image(s));
//
//                    ImageView pic = c.pic;
//                    pic.setFitWidth(55);
//                    pic.setFitHeight(75);
//                    pic.setPreserveRatio(true);
//
//                    stack.getChildren().add(pic);
//                    p.handType.remove(p.handType.size()-1);
//                    p.handRank.remove(p.handRank.size()-1);
//
//                    try {
//                        p.player.out.writeObject("-pc");
//                        p.player.out.writeObject(rank);
//                        p.player.out.writeObject(type);
//                    }catch(Exception ex){
//
//                    }
//
//                }
                gameFunction();
                //takeTurn(p.playerNum+1);
                try{
                    p.player.out.writeObject("-tt");
                    p.player.out.writeObject(p.playerNum+1);
                }catch(Exception ex){

                }
            });
        }else if(p.playerNum == 3) {
            System.out.println(p.handRank.size() + " " + p.handType.size());
            player3Card.setOnAction(e->{//stack.getChildren().clear();
//                System.out.println(p.handRank.size() + " " + p.handType.size());
//                if(p.handRank.size() > 0) {
//                    int rank = p.handRank.get(p.handRank.size()-1);
//                    String type = p.handType.get(p.handType.size()-1);
//                    String s = "";
//                    if(rank <= 10){
//                        s = rank+type+".png";
//                    }else if(rank == 11) {
//                        s = "J" + type + ".png";
//                    }else if(rank == 12){
//                        s = "Q"+type+".png";
//                    }else if(rank == 13) {
//                        s = "K" + type + ".png";
//                    }else if(rank == 14){
//                        s = "A"+type+".png";
//                    }
//                    Card c = new Card(type,rank,new Image(s));
//
//                    ImageView pic = c.pic;
//                    pic.setFitWidth(55);
//                    pic.setFitHeight(75);
//                    pic.setPreserveRatio(true);
//
//                    stack.getChildren().add(pic);
//                    p.handType.remove(p.handType.size()-1);
//                    p.handRank.remove(p.handRank.size()-1);
//                }
                gameFunction();
                //takeTurn(p.playerNum+1);
                try{
                    p.player.out.writeObject("-tt");
                    p.player.out.writeObject(p.playerNum+1);
                }catch(Exception ex){

                }
            });
        }else if(p.playerNum == 4) {
            System.out.println(p.handRank.size() + " " + p.handType.size());
            player4Card.setOnAction(e->{//stack.getChildren().clear();
//                System.out.println(p.handRank.size() + " " + p.handType.size());
//                if(p.handRank.size() > 0) {
//                    int rank = p.handRank.get(p.handRank.size()-1);
//                    String type = p.handType.get(p.handType.size()-1);
//                    String s = "";
//                    if(rank <= 10){
//                        s = rank+type+".png";
//                    }else if(rank == 11) {
//                        s = "J" + type + ".png";
//                    }else if(rank == 12){
//                        s = "Q"+type+".png";
//                    }else if(rank == 13) {
//                        s = "K" + type + ".png";
//                    }else if(rank == 14){
//                        s = "A"+type+".png";
//                    }
//                    Card c = new Card(type,rank,new Image(s));
//
//                    ImageView pic = c.pic;
//                    pic.setFitWidth(55);
//                    pic.setFitHeight(75);
//                    pic.setPreserveRatio(true);
//
//                    stack.getChildren().add(pic);
//                    p.handType.remove(p.handType.size()-1);
//                    p.handRank.remove(p.handRank.size()-1);
//                }
                gameFunction();
                //takeTurn(1);
                try{
                    p.player.out.writeObject("-tt");
                    p.player.out.writeObject(1);
                }catch(Exception ex){

                }
            });
        }


        root.setTop(p1);
        root.setRight(p2);
        root.setBottom(p3);
        root.setLeft(p4);
        root.setCenter(stack);


        Button help = new Button("?");
        help.setOnAction(e->{
            dialogFunction();
        });

        BorderPane pane = new BorderPane();
        pane.setTop(messages);
        pane.setCenter(root);

        pane.setBottom(help);


        Scene scene = new Scene(pane,400,400);
        mainStage.setScene(scene);
        mainStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


}
