package com.tower_maze.view;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.tower_maze.controller.NavigationController;
import com.tower_maze.model.Tower;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class GameController implements Initializable {
    
    @FXML
    private Label lblLocation; // Para mostrar la ubicación actual

    @FXML
    private ListView<String> lstMoves; // Para listar los movimientos disponibles

    private Tower tower;
    private NavigationController navController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Se ejecuta al cargar la vista. Instanciamos Tower y NavigationController.
        tower = new Tower();
        navController = new NavigationController(tower);
        updateLocationLabel();
    }

    @FXML
    private void onShowMoves(ActionEvent event) {
        // Mostrar los movimientos válidos en la lista lstMoves
        List<Integer> validMoves = navController.getValidMoves();
        lstMoves.getItems().clear();
        for (Integer move : validMoves) {
            // Si move == -1, mostramos "Lobby", si no, el número de la sala
            String display = (move == -1) ? "Lobby" : String.valueOf(move);
            lstMoves.getItems().add(display);
        }
    }

    @FXML
    private void onGo(ActionEvent event) {
        // Usuario elige uno de los movimientos y pulsamos "Ir"
        String selected = lstMoves.getSelectionModel().getSelectedItem();
        if (selected == null) {
            // No hay elemento seleccionado
            return;
        }

        // Convertimos el texto seleccionado a un entero
        int target = selected.equals("Lobby") ? -1 : Integer.parseInt(selected);

        // Realizamos el movimiento
        navController.moveToRoom(target);

        // Si hemos llegado a la “sala” -1 (Lobby), preguntamos si se desea extender la torre
        if (navController.getCurrentRoom() == -1) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Extender torre");
            alert.setHeaderText("Estás en el Lobby");
            alert.setContentText("¿Quieres extender la torre?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                tower.autoExpandTower();
            }
        }

        // Actualizamos label para mostrar la nueva ubicación
        updateLocationLabel();
    }

    @FXML
    private void onExit(ActionEvent event) {
        // Botón "Salir"
        Platform.exit();
    }

    private void updateLocationLabel() {
        int current = navController.getCurrentRoom();
        String text = (current == -1) 
            ? "Current Location: LOBBY" 
            : "Current Room: " + current;
        lblLocation.setText(text);
    }
}
