package com.tower_maze.view;

import com.tower_maze.controller.NavigationController;
import com.tower_maze.model.Tower;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameController {
    @FXML private Label currentRoomLabel;
    @FXML private VBox movesContainer;
    @FXML private Label statusLabel;

    private NavigationController navController;

    public void initialize(NavigationController navController) {
        this.navController = navController;
        updateUI();
    }

    @FXML
private void updateUI() {
    // ... existing code ...

    if (navController.checkWinCondition()) {
        statusLabel.setText("Congratulations! You returned to the Lobby!");
        movesContainer.getChildren().clear();
        Button restartBtn = new Button("Restart Game");
        restartBtn.setOnAction(e -> resetGame());
        movesContainer.getChildren().add(restartBtn);
    }
}

private void resetGame() {
    Tower newTower = new Tower();
    navController = new NavigationController(newTower);
    updateUI();
    statusLabel.setText("");
}
}