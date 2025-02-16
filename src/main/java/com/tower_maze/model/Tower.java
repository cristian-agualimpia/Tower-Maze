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
    private Queue<Integer> unassignedNonPrimes;
    private List<Integer> allPrimes;
    private double averagePrime;

    public Tower() {
        this.floors = new HashMap<>();
        this.unassignedNonPrimes = new LinkedList<>();
        this.allPrimes = PrimeUtils.generatePrimes(100);
        this.averagePrime = PrimeUtils.calculateAveragePrime(allPrimes);
        initializeFloors();
    }

    private void initializeFloors() {
        Queue<Integer> nonPrimes = PrimeUtils.getNonPrimes(1, 100, allPrimes);
        
        for (int prime : allPrimes) {
            List<Integer> floorNonPrimes = new ArrayList<>();
            for (int i = 0; i < 3 && !nonPrimes.isEmpty(); i++) {
                floorNonPrimes.add(nonPrimes.poll());
            }
            
            // Calculate elevator connections
            List<Integer> elevators = new ArrayList<>();
            int primeIndex = allPrimes.indexOf(prime);
            if (prime < averagePrime) {
                // Connect to next 2 primes
                if (primeIndex + 1 < allPrimes.size()) elevators.add(allPrimes.get(primeIndex + 1));
                if (primeIndex + 2 < allPrimes.size()) elevators.add(allPrimes.get(primeIndex + 2));
            } else {
                // Connect to previous 2 primes
                if (primeIndex - 1 >= 0) elevators.add(allPrimes.get(primeIndex - 1));
                if (primeIndex - 2 >= 0) elevators.add(allPrimes.get(primeIndex - 2));
            }
            
            // Calculate stairs connection (last room to next floor's last room)
            Integer stairsTarget = null;
            if (primeIndex + 1 < allPrimes.size()) {
                int nextPrime = allPrimes.get(primeIndex + 1);
                int lastRoomCurrent = floorNonPrimes.get(floorNonPrimes.size() - 1);
                Floor nextFloor = floors.get(nextPrime);
                if (nextFloor != null) {
                    int lastRoomNext = nextFloor.getRooms().get(nextFloor.getRooms().size() - 1);
                    if (lastRoomCurrent % 2 == lastRoomNext % 2) {
                        stairsTarget = lastRoomNext;
                    }
                }
            }
            
            floors.put(prime, new Floor(prime, floorNonPrimes, elevators, stairsTarget));
        }
        
        unassignedNonPrimes.addAll(nonPrimes); // Store leftovers
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
                // Recalculate connections for new floors
                // (Similar logic to initializeFloors())
                floors.put(prime, new Floor(prime, assignedNonPrimes, ...));
            }
        }
        
        allPrimes.addAll(newPrimes);
        averagePrime = PrimeUtils.calculateAveragePrime(allPrimes);
        unassignedNonPrimes = newNonPrimes;
    }

    // Getters
    public Floor getFloorByRoom(int prime) { return floors.get(prime); }

}