package com.tower_maze.view;

import com.tower_maze.controller.NavigationController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView extends Application {
    private NavigationController controller;
    private Label currentRoomLabel;
    private VBox movesContainer;  // Buttons for valid moves

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        currentRoomLabel = new Label();
        movesContainer = new VBox(10);
        
        // Layout setup
        root.setTop(currentRoomLabel);
        root.setCenter(movesContainer);
        
        // Initial render
        updateUI();
        
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void updateUI() {
        currentRoomLabel.setText("Current Room: " + controller.getCurrentRoom());
        movesContainer.getChildren().clear();
        
        // Add buttons for valid moves
        for (Integer target : controller.getValidMoves()) {
            Button moveButton = new Button("Go to " + target);
            moveButton.setOnAction(e -> {
                controller.moveToRoom(target);
                updateUI();
            });
            movesContainer.getChildren().add(moveButton);
        }
    }
}