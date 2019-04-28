package sample;
//package application;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card {
    int rank;
    String type;
    ImageView pic;

    Card(String type,int rank,Image pic){
        this.rank = rank;
        this.type = type;
        this.pic = new ImageView(pic);
    }
}