package com.tower_maze.model;

import java.util.ArrayList;
import java.util.List;

public class Floor {
    private int prime;
    private List<Integer> rooms;
    private List<Integer> elevatorConnections;
    private Integer stairsTarget;

    public Floor(int prime, List<Integer> nonPrimes, List<Integer> elevatorConnections, Integer stairsTarget) {
        this.prime = prime;
        this.rooms = new ArrayList<>();
        this.rooms.add(prime);
        this.rooms.addAll(nonPrimes);
        this.elevatorConnections = elevatorConnections;
        this.stairsTarget = stairsTarget;
    }

    // Getters
    public List<Integer> getRooms() { return rooms; }
    public List<Integer> getElevatorConnections() { return elevatorConnections; }
    public Integer getStairsTarget() { return stairsTarget; }
}