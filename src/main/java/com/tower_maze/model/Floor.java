package com.tower_maze.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Floor {
    private int prime;
    private List<Integer> rooms;
    private List<Integer> elevatorConnections;
    private Integer stairsTarget;
    private Map<Integer, List<Integer>> corridorConnections; // Room → List of connected rooms

    public Floor(int prime, List<Integer> nonPrimes, List<Integer> elevatorConnections, Integer stairsTarget) {
        this.prime = prime;
        this.rooms = new ArrayList<>();
        this.rooms.add(prime);
        this.rooms.addAll(nonPrimes);
        this.elevatorConnections = elevatorConnections;
        this.stairsTarget = stairsTarget;
        this.corridorConnections = new HashMap<>();
        calculateCorridorConnections();
    }

    // Calculate corridor links based on room values vs floor average
    private void calculateCorridorConnections() {
        System.out.println("[DEBUG] Calculating corridors for floor " + prime + ", rooms: " + rooms);
        for (int room : rooms) {
            List<Integer> links = new ArrayList<>();
            for (int other : rooms) {
                if (room != other) {
                    links.add(other);
                }
            }
            corridorConnections.put(room, links);
            System.out.println("[DEBUG] Room " + room + " → " + links);
        }
    }

    // Get corridor connections for a specific room
    public List<Integer> getCorridorConnections(int room) {
        return corridorConnections.getOrDefault(room, Collections.emptyList());
    }

    // Getters (existing code)
    public List<Integer> getRooms() { return rooms; }
    public List<Integer> getElevatorConnections() { return elevatorConnections; }
    public Integer getStairsTarget() { return stairsTarget; }
    public int getPrime() { return prime; }

    public void setStairsTarget(Integer target){
        this.stairsTarget = target;
    }
}