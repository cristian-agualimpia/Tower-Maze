package com.tower_maze;

import java.util.List;
import java.util.Scanner;

import com.tower_maze.controller.NavigationController;
import com.tower_maze.model.Tower;

public class ConsoleApp {
    private static NavigationController navController;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Tower tower = new Tower();
        navController = new NavigationController(tower);
        System.out.println("=== Welcome to Tower Maze ===");
        gameLoop(tower);
    }

    private static void gameLoop(Tower tower) {
        while (true) {
            printCurrentLocation();
            
            List<Integer> validMoves = navController.getValidMoves();
            
            if (navController.getCurrentRoom() == -1){
                System.out.println("Do you want to extend the tower? Choose True or False");
                Boolean extend = scanner.nextBoolean();
                if (extend){
                    tower.autoExpandTower();
                    validMoves = navController.getValidMoves();
                }
            }
            // Debug: Print raw moves list
            System.out.println("[DEBUG] Raw valid moves: " + validMoves);
            
            printValidMoves(validMoves);
            int choice = getPlayerChoice(validMoves.size());

            if (choice == 0) {
                System.out.println("Exiting game...");
                break;
            }

            int target = validMoves.get(choice - 1);
            boolean won = navController.moveToRoom(target);
            
            /* 
            if (won) {
                System.out.println("\n=== CONGRATULATIONS! You returned to the Lobby! ===");
                break;
            }
            */
        }
    }
    

    private static void printCurrentLocation() {
        int current = navController.getCurrentRoom();
        System.out.println("\n--------------------------------");
        System.out.println(current == -1 
            ? "Current Location: LOBBY" 
            : "Current Room: " + current);
    }

    private static void printValidMoves(List<Integer> moves) {
        System.out.println("Available moves:");
        for (int i = 0; i < moves.size(); i++) {
            int target = moves.get(i);
            String displayName = (target == -1) ? "Lobby" : String.valueOf(target);
            System.out.printf("%d. Go to %s\n", i + 1, displayName);
        }
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static int getPlayerChoice(int maxOption) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 0 && choice <= maxOption) {
                    return choice;
                }
                System.out.print("Invalid option. Try again: ");
            } catch (NumberFormatException e) {
                System.out.print("Please enter a number: ");
            }
        }
    }
}