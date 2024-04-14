package com.application.gold_minner;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class App extends Application {

    public volatile List<Asset> asset;
    public volatile Pane pane;
    public volatile Pane hook;
    public volatile int angleUpdate = 1;
    public volatile int chainLengh = 0;
    public volatile Line line;
    public volatile double endX = 500;
    public volatile double endY = 500;
    public volatile Rotate rotate;
    public volatile boolean minerAction = true;

    public enum MinerState {
        BUSY,
        SELLING,
        FREETIME
    }

    public enum Direction {
        RightToLeft,
        LeftToRight
    }
    public volatile Direction direct = Direction.LeftToRight;
    public volatile double angle = -60;
    public volatile MinerState minerState = MinerState.FREETIME;

    @Override
    public void start(Stage primaryStage) {
        asset = new ArrayList<>();
        asset.add(new Asset(200, 400));
        asset.add(new Asset(500, 300));

        // Create top and bottom panes
        BorderPane topPane = new BorderPane();
        topPane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
        Barrow barrow = new Barrow();

        topPane.setBottom(barrow);
        BorderPane.setAlignment(barrow, Pos.CENTER);
        hook = new Pane();

        hook.setMinHeight(15);
        hook.setMinWidth(30);
        hook.setTranslateX(385);
        hook.setTranslateY(150);
        hook.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
        rotate = new Rotate(angle, 30 / 2, 7);
        hook.getTransforms().add(rotate);
        line = new Line(); //instantiating Line class   
        line.setStartX(400); //setting starting X point of Line  
        line.setStartY(150); //setting starting Y point of Line   
        line.setEndX(400); //setting ending X point of Line   
        line.setEndY(150);
        //  handleLine();
        handleAngle();

        Pane bottomPane = new Pane();
        bottomPane.setBackground(new Background(new BackgroundFill(Color.BROWN, null, null)));

        // Create a SplitPane
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.3);
        splitPane.setResizableWithParent(topPane, Boolean.FALSE);

        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.getItems().addAll(topPane, bottomPane);
        StackPane stackPane = new StackPane();
        pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        pane.getChildren().add(line);
        pane.getChildren().add(hook);
        pane.getChildren().addAll(asset);

        stackPane.getChildren().addAll(splitPane, pane);
        // Set up the scene
        Scene scene = new Scene(stackPane, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Gold miner");
        primaryStage.setFullScreen(false);

        // Disable window resizing
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0); // Exit the application
        });
        scene.setOnKeyPressed(event -> {
            String keyCode = event.getCode().toString();
            if (keyCode.equals("DOWN")
                    && minerState == MinerState.FREETIME) {
                minerState = minerState.BUSY;
                handleLine();
            }

        });
        primaryStage.show();
    }

    public synchronized void handleAngle() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    if (angle >= 90) {
                        direct = Direction.RightToLeft;
                    }
                    if (angle < -90) {
                        direct = Direction.LeftToRight;
                    }
                    if (direct == Direction.LeftToRight && angle < 90) {
                        angle += angleUpdate;
                    } else {
                        angle -= angleUpdate;
                    }
                    Thread.sleep(15);
                    Platform.runLater(() -> {
                        rotate.setAngle(angle);
                    });
                } catch (InterruptedException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        thread.start();
    }

    public synchronized void handleLine() {
        angleUpdate = 0;
        double angleRadians = Math.toRadians(angle + 90);

        Thread thread = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(5);
                    endX = 400 + chainLengh * Math.cos(angleRadians);
                    endY = 150 + chainLengh * Math.sin(angleRadians);
                    if (chainLengh < 700 && minerAction) {
                        chainLengh += 1;
                    } else {
                        if (chainLengh >= 0) {
                            chainLengh -= 1;
                        }
                    }
                    if (chainLengh >= 700) {
                        minerAction = false;
                    }
                    Platform.runLater(() -> {
                        line.setEndX(endX);
                        line.setEndY(endY);
                        hook.setTranslateX(endX - 15);
                        hook.setTranslateY(endY);
                    });
                    System.out.println("endX=" + endX + ",endY=" + endY);
                    if (chainLengh < 0) {
                        break;
                    }
                    for (Asset pn : asset) {
                        Platform.runLater(() -> {
                            if (endY > pn.getPostionY() && 
                                    endX > pn.getPostionX()-30 && 
                                        endX < pn.getPostionX()+30) {
                                minerAction = false;
                              pn.setFree(false);
                             //   pane.getChildren().remove(pn);
                            }
                            if(!pn.isFree()){
                              pn.updatePostion(endX, endY);
                              if(endY <151){ 
                                  pane.getChildren().remove(pn);
                                  asset.remove(pn);
                                  System.out.println(asset.size());
                              }
                            }
                        });
                        
                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            chainLengh = 0;
            minerAction = true;
            minerState = MinerState.FREETIME;
            angleUpdate = 1;
        });
        thread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
