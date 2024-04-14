/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.application.gold_minner;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author phamquan
 */
public class Asset extends Pane{
    public boolean free;
    public double postionX;
    public double postionY;
    public Asset(int x, int y){
        free = true;
        postionX = x;
        postionY = y;
        setMinWidth(50);
        setMinHeight(50);
        Rectangle clip = new Rectangle(50, 50);
        clip.setArcWidth(100);
        clip.setArcHeight(100);
        setClip(clip);
        setBackground(new Background(new BackgroundFill(Color.GOLD,null, null)));
        setTranslateX(postionX-25);
        setTranslateY(postionY-25);
    }

    public double getPostionX() {
        return postionX;
    }

    public void setPostionX(double postionX) {
        this.postionX = postionX;
    }

    public double getPostionY() {
        return postionY;
    }

    public void setPostionY(double postionY) {
        this.postionY = postionY;
    }
    public void updatePostion(double x,double y){
        setTranslateX(x);
        setTranslateY(y);
        setPostionX(x);
        setPostionY(y);
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
    
}
