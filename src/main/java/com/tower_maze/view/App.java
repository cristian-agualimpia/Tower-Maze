package com.tower_maze.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tower_maze/game-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // 1) Cargamos el style.css desde la misma carpeta resources/com/tower_maze
        scene.getStylesheets().add(
            getClass().getResource("/com/tower_maze/style.css").toExternalForm()
        );

        primaryStage.setScene(scene);
        primaryStage.setTitle("Tower Maze");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
