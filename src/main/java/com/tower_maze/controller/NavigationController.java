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
        if (currentRoom == -1) return List.of(2, 41, 43, 97);
    
        Floor currentFloor = tower.getFloorByRoom(currentRoom);
        if (currentFloor == null) return moves;
    
        // Elevators
        moves.addAll(currentFloor.getElevatorConnections());
    
        // Stairs (check parity)
        if (currentFloor.getStairsTarget() != null) {
            int currentLastRoom = currentFloor.getRooms().get(currentFloor.getRooms().size() - 1);
            Floor targetFloor = tower.getFloorByRoom(currentFloor.getStairsTarget());
            if (targetFloor != null) {
                int targetLastRoom = targetFloor.getRooms().get(targetFloor.getRooms().size() - 1);
                if (currentLastRoom % 2 == targetLastRoom % 2) {
                    moves.add(targetLastRoom);
                }
            }
        }
    
        // Filter previous room
        moves.removeIf(room -> room == previousRoom);
        return moves;
    }

    public boolean moveToRoom(int target) {
        previousRoom = currentRoom;
        currentRoom = target;
        return checkWinCondition();
    }

    private boolean checkWinCondition() {
        return (currentRoom == -1 && List.of(2, 41, 43, 97).contains(previousRoom));
    }
}