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

    public void autoExpandTower() {
        if (unassignedNonPrimes.size() < 3) {
            int lastPrime = allPrimes.get(allPrimes.size() - 1);
            int nextMultipleOf10 = ((lastPrime / 10) + 1) * 10;
            expandTower(nextMultipleOf10);
        }
    }

    public void expandTower(int rangeEnd) {
        int lastPrime = allPrimes.get(allPrimes.size() - 1);
        List<Integer> newPrimes = PrimeUtils.generatePrimes(lastPrime + 1, rangeEnd);
        Queue<Integer> newNonPrimes = PrimeUtils.getNonPrimes(lastPrime + 1, rangeEnd, newPrimes);
        
        // Merge with previous leftovers
        newNonPrimes.addAll(unassignedNonPrimes);
        
        for (int prime : newPrimes) {
            List<Integer> assignedNonPrimes = new ArrayList<>();
            for (int i = 0; i < 3 && !newNonPrimes.isEmpty(); i++) {
                assignedNonPrimes.add(newNonPrimes.poll());
            }
            
            if (assignedNonPrimes.size() == 3) {
                List<Integer> elevators = new ArrayList<>();
                int primeIndex = allPrimes.indexOf(prime);
                if (prime < averagePrime) {
                    if (primeIndex + 1 < allPrimes.size()) elevators.add(allPrimes.get(primeIndex + 1));
                    if (primeIndex + 2 < allPrimes.size()) elevators.add(allPrimes.get(primeIndex + 2));
                } else {
                    if (primeIndex - 1 >= 0) elevators.add(allPrimes.get(primeIndex - 1));
                    if (primeIndex - 2 >= 0) elevators.add(allPrimes.get(primeIndex - 2));
                }

                Integer stairsTarget = null;
                if (primeIndex + 1 < allPrimes.size()) {
                    int nextPrime = allPrimes.get(primeIndex + 1);
                    int lastRoomCurrent = assignedNonPrimes.get(assignedNonPrimes.size() - 1);
                    Floor nextFloor = floors.get(nextPrime);
                    if (nextFloor != null) {
                        int lastRoomNext = nextFloor.getRooms().get(3); // 4th room (index 3)
                        if (lastRoomCurrent % 2 == lastRoomNext % 2) {
                            stairsTarget = lastRoomNext;
                        }
                    }
                }

                floors.put(prime, new Floor(prime, assignedNonPrimes, elevators, stairsTarget));
                
            }
            
        }
        
        allPrimes.addAll(newPrimes);
        averagePrime = PrimeUtils.calculateAveragePrime(allPrimes);
        unassignedNonPrimes = newNonPrimes;
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