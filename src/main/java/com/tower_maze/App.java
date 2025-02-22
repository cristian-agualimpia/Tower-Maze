package com.tower_maze;

import com.tower_maze.controller.NavigationController;
import com.tower_maze.model.Tower;
import com.tower_maze.view.GameController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    

    @Override
    public void start(Stage stage) throws Exception {
        Tower tower = new Tower();
        NavigationController navController = new NavigationController(tower);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("C:\\Users\\esteb\\OneDrive\\Escritorio\\ESTRUCTURAS DE DATOS\\MULTIVERSO\\demo\\src\\main\\resources\\com\\tower_maze\\game-view.fxml"));

        Parent root = loader.load();

        GameController gameController = loader.getController();
        gameController.initialize(navController);

        stage.setTitle("Tower Maze");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}