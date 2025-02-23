package com.tower_maze.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.tower_maze.controller.PrimeUtils;

public class Tower {
    private Map<Integer, Floor> floors;
    private Map<Integer, Integer> roomToPrime; // Maps ANY room (prime or non-prime) to its floor's prime
    private Queue<Integer> unassignedNonPrimes;
    private List<Integer> allPrimes;
    private double averagePrime;
    public int firstPrime = 2;
    public int middlePrime1 = 41;
    public int middlePrime2 = 43;
    public int lastPrime = 97;
    private int expansionRange = 100; // Track current range

    public Tower() {
        this.floors = new HashMap<>();
        this.roomToPrime = new HashMap<>(); // Initialize here!
        this.unassignedNonPrimes = new LinkedList<>();
        this.allPrimes = PrimeUtils.generatePrimes(100);
        this.averagePrime = PrimeUtils.calculateAveragePrime(allPrimes);
        

        firstPrime = allPrimes.get(0);
        lastPrime = allPrimes.get(allPrimes.size() - 1);
        int middleIndex = allPrimes.size() / 2;
        middlePrime1 = allPrimes.get(middleIndex - 1); // e.g., 41
        middlePrime2 = allPrimes.get(middleIndex);      // e.g., 43
        initializeFloors();
    }

    private void refreshAllElevators() {
        this.averagePrime = PrimeUtils.calculateAveragePrime(allPrimes);
        for (int prime : allPrimes) {
            Floor floor = floors.get(prime);
            if (floor == null) continue;
            
            List<Integer> updatedElevators = calculateElevators(prime);
            // Y luego meter o no el lobby:
            if (prime == firstPrime || prime == middlePrime1 
                || prime == middlePrime2 || prime == lastPrime) {
                updatedElevators.add(-1);
            }
            floor.setElevatorConnections(updatedElevators);
        }
    }
    

    private void initializeFloors() {
        Queue<Integer> nonPrimes = PrimeUtils.getNonPrimes(1, 100, allPrimes);
    
        // First pass: Create floors without stairs
        for (int prime : allPrimes) {
            List<Integer> floorNonPrimes = new ArrayList<>();
            for (int i = 0; i < 3 && !nonPrimes.isEmpty(); i++) {
                floorNonPrimes.add(nonPrimes.poll());
            }

            
            List<Integer> elevators = calculateElevators(prime);

            // Add lobby (-1) to elevator connections for specific primes
            if (prime == firstPrime || prime == middlePrime1 
                || prime == middlePrime2 || prime == lastPrime) {
                elevators.add(-1); // -1 represents the lobby
            }

            Floor floor = new Floor(prime, floorNonPrimes, elevators, null); // Stairs set later
            floors.put(prime, floor);
            for (int room : floor.getRooms()) {
                roomToPrime.put(room, prime);
            }
        }

        // After creating floors and roomToPrime mappings:
        System.out.println("[DEBUG] roomToPrime mappings:");
        for (Map.Entry<Integer, Integer> entry : roomToPrime.entrySet()) {
            System.out.println("Room " + entry.getKey() + " → Floor " + entry.getValue());
        }
    
        // Second pass: Set stairs connections
        for (int i = 0; i < allPrimes.size(); i++) {
            int prime = allPrimes.get(i);
            Floor currentFloor = floors.get(prime);
            if (i + 1 < allPrimes.size()) {
                int nextPrime = allPrimes.get(i + 1);
                Floor nextFloor = floors.get(nextPrime);
                int currentLastRoom = currentFloor.getRooms().get(currentFloor.getRooms().size() - 1);
                int nextLastRoom = nextFloor.getRooms().get(nextFloor.getRooms().size() - 1);
                if (currentLastRoom % 2 == nextLastRoom % 2) {
                    currentFloor.setStairsTarget(nextLastRoom);
                }
            }
        }

        // After setting stairs connections:
        System.out.println("\n[DEBUG] Stairs connections:");
        for (int prime : allPrimes) {
            Floor floor = floors.get(prime);
            if (floor.getStairsTarget() != null) {
                System.out.println("Floor " + prime + " last room (" + floor.getRooms().get(3) + 
                    ") → " + floor.getStairsTarget());
            }
        }
    }


    private List<Integer> calculateElevators(int prime) {
        List<Integer> elevators = new ArrayList<>();
        int primeIndex = allPrimes.indexOf(prime);
        if (prime < averagePrime) {
            if (primeIndex + 1 < allPrimes.size()) elevators.add(allPrimes.get(primeIndex + 1));
            if (primeIndex + 2 < allPrimes.size()) elevators.add(allPrimes.get(primeIndex + 2));
        } else {
            if (primeIndex - 1 >= 0) elevators.add(allPrimes.get(primeIndex - 1));
            if (primeIndex - 2 >= 0) elevators.add(allPrimes.get(primeIndex - 2));
        }
        return elevators;
    }

    public int getExpansionRange(){
        return this.expansionRange;
    }

    public void autoExpandTower() {
        if (unassignedNonPrimes.size() < 3) {
            // Ejemplo de "siguiente bloque" => 110, 120, 130, etc.:
            int nextMultipleOf10 = ((lastPrime / 10) + 1) * 10; 
            // Asegúrate de que sea mayor a expansionRange
            if (nextMultipleOf10 <= expansionRange) {
                nextMultipleOf10 = expansionRange + 10;
            }
    
            expandTower(nextMultipleOf10);
            System.out.println("Tower expanded to " + nextMultipleOf10 + "!");
        } else {
            System.out.println("No need to expand tower right now.");
        }
    }
    

    public void expandTower(int rangeEnd) {
        // Generate new primes in the expanded range
        List<Integer> newPrimes = PrimeUtils.generatePrimes(expansionRange + 1, rangeEnd);
        allPrimes.addAll(newPrimes);
        expansionRange = rangeEnd;

        // Assign non-primes to new floors
        Queue<Integer> newNonPrimes = PrimeUtils.getNonPrimes(expansionRange + 1, rangeEnd, allPrimes);
        unassignedNonPrimes.addAll(newNonPrimes);

        // Create new floors
        for (int prime : newPrimes) {
            List<Integer> floorNonPrimes = new ArrayList<>();
            for (int i = 0; i < 3 && !unassignedNonPrimes.isEmpty(); i++) {
                floorNonPrimes.add(unassignedNonPrimes.poll());
            }
            Floor floor = new Floor(prime, floorNonPrimes, 
                calculateElevators(prime), null);
            floors.put(prime, floor);
            for (int room : floor.getRooms()) {
                roomToPrime.put(room, prime);
            }

            
        }

        // Vuelve a enlazar TODAS las escaleras, no solo las "nuevas"
        for (int i = 0; i < allPrimes.size() - 1; i++) {
            int prime = allPrimes.get(i);
            int nextPrime = allPrimes.get(i + 1);

            Floor currentFloor = floors.get(prime);
            Floor nextFloor = floors.get(nextPrime);

            // Si por algún motivo no existen, sáltate
            if (currentFloor == null || nextFloor == null) continue;

            int currentLastRoom = currentFloor.getRooms().get(currentFloor.getRooms().size() - 1);
            int nextLastRoom = nextFloor.getRooms().get(nextFloor.getRooms().size() - 1);

            if (currentLastRoom % 2 == nextLastRoom % 2) {
                currentFloor.setStairsTarget(nextLastRoom);
            } else {
                // Por si antes tenían escaleras y ahora no aplica
                currentFloor.setStairsTarget(null);
            }
        }


        updateSpecialPrimes(); // Critical: Update first/middle/last primes
        refreshLobbyConnections(); // Refresh elevator links
        refreshAllElevators();
    }

    private void updateSpecialPrimes() {
        if (allPrimes.isEmpty()) return;
        firstPrime = allPrimes.get(0);
        lastPrime = allPrimes.get(allPrimes.size() - 1);
        int mid = allPrimes.size() / 2;
        middlePrime1 = allPrimes.get(mid - 1);
        middlePrime2 = allPrimes.get(mid);
    }

    private void refreshLobbyConnections() {
        List<Integer> specialPrimes = List.of(firstPrime, middlePrime1, middlePrime2, lastPrime);
        for (Floor floor : floors.values()) {
            List<Integer> elevators = new ArrayList<>(floor.getElevatorConnections());
            if (specialPrimes.contains(floor.getPrime())) {
                if (!elevators.contains(-1)) elevators.add(-1);
            } else {
                elevators.remove(Integer.valueOf(-1));
            }
            floor.setElevatorConnections(elevators);
        }
    }

    // Getters for lobby connections
    public List<Integer> getLobbyConnections() {
        return List.of(firstPrime, middlePrime1, middlePrime2, lastPrime);
    }


    // Getters
    public Floor getFloorByRoom(int room) {
        Integer prime = roomToPrime.get(room);
        if (prime == null) {
            System.out.println("[ERROR] Room " + room + " not mapped to any floor!");
            return null;
        }
        Floor floor = floors.get(prime);
        if (floor == null) {
            System.out.println("[ERROR] Floor " + prime + " missing from floors map!");
        }
        return floor;
    }
}