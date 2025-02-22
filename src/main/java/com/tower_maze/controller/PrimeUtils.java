package com.tower_maze.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class PrimeUtils {
    // Generate primes up to N using Sieve of Eratosthenes
    public static List<Integer> generatePrimes(int n) {
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        
        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) primes.add(i);
        }
        return primes;
    }

    // Get non-primes in range and group them into chunks of 3
    public static Queue<Integer> getNonPrimes(int start, int end, List<Integer> primes) {
        Set<Integer> primeSet = new HashSet<>(primes);
        Queue<Integer> nonPrimes = new LinkedList<>();
        
        for (int i = start; i <= end; i++) {
            if (!primeSet.contains(i)) nonPrimes.add(i);
        }
        return nonPrimes;
    }

    // Calculate average of primes for elevator direction logic
    public static double calculateAveragePrime(List<Integer> primes) {
        return primes.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    // Modified method to accept range
    public static List<Integer> generatePrimes(int start, int end) {
        boolean[] isPrime = new boolean[end + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        
        for (int i = 2; i * i <= end; i++) {
            if (isPrime[i]) {
                int startMultiple = Math.max(i * i, start);
                for (int j = startMultiple; j <= end; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        
        List<Integer> primes = new ArrayList<>();
        for (int i = Math.max(2, start); i <= end; i++) {
            if (isPrime[i]) primes.add(i);
        }
        return primes;
    }
}