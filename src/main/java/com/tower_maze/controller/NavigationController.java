package com.tower_maze.controller;

import java.util.ArrayList;
import java.util.List;

import com.tower_maze.model.Floor;
import com.tower_maze.model.Tower;

public class NavigationController {
    private Tower tower;
    private int currentRoom;
    private int previousRoom;

    public NavigationController(Tower tower) {
        this.tower = tower;
        this.currentRoom = -1; // -1 = lobby
    }

    // GETTERS 

    public Tower getTower(){
        return this.tower;
    }

    public int getCurrentRoom() {
        return this.currentRoom;
    }

    public int getPreviousRoom() {
        return this.previousRoom;
    }

    public List<Integer> getValidMoves() {
        List<Integer> moves = new ArrayList<>();
        if (currentRoom == -1) {
            // Lobby connects to first, middle1, middle2, last primes
            return List.of(tower.firstPrime, tower.middlePrime1, tower.middlePrime2, tower.lastPrime);
        } else {
            Floor currentFloor = tower.getFloorByRoom(currentRoom);
            if (currentFloor == null) {
                System.out.println("[ERROR] Floor not found for room " + currentRoom);
                return moves;
            }
            System.out.println("[DEBUG] Current floor: " + currentFloor.getPrime());
            System.out.println("[DEBUG] Corridor connections: " + currentFloor.getCorridorConnections(currentRoom));
            System.out.println("[DEBUG] Stairs target: " + currentFloor.getStairsTarget());
            
            // Add elevator connections (only if current room is the prime)
            if (currentRoom == currentFloor.getPrime()) {
                moves.addAll(currentFloor.getElevatorConnections());
            }
            // Add corridor connections
            moves.addAll(currentFloor.getCorridorConnections(currentRoom));
            // Add stairs connection
            if (currentFloor.getStairsTarget() != null) {
                moves.add(currentFloor.getStairsTarget());
            }
        }
        System.out.println("[DEBUG] Final valid moves: " + moves);
        return moves;
    }

    public boolean moveToRoom(int target) {
        previousRoom = currentRoom;
        currentRoom = target;
        return checkWinCondition();
    }

    public boolean checkWinCondition() {
        return (currentRoom == -1 && List.of(2, 41, 43, 97).contains(previousRoom));
    }
}