/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.application.gold_minner;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author phamquan
 */
public class Barrow extends Pane{

    public Barrow() {
        setMinHeight(50);
        setMinWidth(80);
        setMaxWidth(80);
        setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
    }
}
