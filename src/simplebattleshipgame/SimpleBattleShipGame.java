/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplebattleshipgame;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Saray Arce
 */
public class SimpleBattleShipGame {

    final static char WATER_NOT_TOUCHED = '_';
    final static char WATER = 'W';
    final static char TOUCHED = 'T';

    // Board size
    final static int SIZE = 10;

    private static Scanner scanner;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        scanner = new Scanner(System.in);

        // user & computer map       
        char[][] userMap = new char[SIZE][SIZE];
        char[][] computerMap = new char[SIZE][SIZE];

        // map to show and note the movements
        // from the user to the computer
        char[][] mapUserVsComputer = new char[SIZE][SIZE];

        // initial score
        int userScore = 24;
        int computerScore = 24;

        // to indicates if the user doesn'shoot have more ships
        boolean isEndGame = false;

        // to indicates successful shoot
        boolean isSuccessfulShoot = false;

        // shoot positions
        int[] shoot = new int[2];

        // init maps
        startingGame(userMap, computerMap);
        initRegisterMap(mapUserVsComputer);

        while (!isEndGame) {
            // at the beginning: print the user map
            System.out.println("USER MAP");
            printMap(userMap);

            System.out.printf("REMAINING POINTS: %d\n", userScore);
            System.out.println("USER'S TURN");

            // User's game starts            
            isSuccessfulShoot = false;
            while (!isSuccessfulShoot) {
                shoot = requestBox();

                if (shoot[0] != -1 && shoot[1] != -1) {
                    isSuccessfulShoot = evaluateShoot(computerMap, shoot);

                    if (!isSuccessfulShoot) {
                        System.out.println("WRONG SHOOT");
                    }
                } else {
                    System.out.println("");
                }

                // Update Computer's map & computer's score
                int oldComputerScore = computerScore;
                computerScore = updateMap(computerMap, shoot, computerScore);

                // Update User's map & user's score
                char typeOfShoot = (oldComputerScore - computerScore) > 0 ? TOUCHED : WATER;
                updateRegisterMap(mapUserVsComputer, shoot, typeOfShoot);

                System.out.println("\nREGISTERING COMPUTER'S MAP");
                printMap(mapUserVsComputer);

                // the game is ending when the score == 0
                isEndGame = (computerScore == 0);

                // if the computers doesn'shoot win will be the user's turn
                if (!isEndGame) {
                    System.out.printf("REMAINING COMPUTER'S SCORE: %d\n\n", computerScore);
                    System.out.println("COMPUTER'S TURN");
                    isSuccessfulShoot = false;

                    while (!isSuccessfulShoot) {
                        shoot = generateRandomShoot();
                        isSuccessfulShoot = evaluateShoot(userMap, shoot);
                    }
                }

                // Update User's map
                userScore = updateMap(userMap, shoot, userScore);

                isEndGame = (userScore == 0);

            } 
        } // END GAME

            if( computerScore == 0){
                System.out.println("THE WINNER IS: \nCOMPUTER!");
            }
            else{
                System.out.println("THE WINNER IS: \nUSER!");
            }  
        
        scanner.close();
    }

    /**
     * Method to initialize both maps
     *
     * @param userMap
     * @param computerMap
     */
    private static void startingGame(char[][] userMap, char[][] computerMap) {
        initMap(userMap);
        initMap(computerMap);
    }

    /**
     * Method to show the map with the computer's shoots
     *
     * @param mapUserVsComputer
     */
    private static void initRegisterMap(char[][] mapUserVsComputer) {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                mapUserVsComputer[i][j] = WATER_NOT_TOUCHED;
            }
        }
    }

    /**
     * Method to print a map
     *
     * @param map
     */
    private static void printMap(char[][] map) {

        char[] letters = new char[SIZE];
        for (int i = 0; i < SIZE; i++) {
            letters[i] = (char) ('A' + i);
        }

        // print header
        System.out.print("   ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print("[" + i + "]");
        }
        System.out.println("");

        // print the other rows
        for (int i = 0; i < SIZE; i++) {
            System.out.print("[" + letters[i] + "] ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j] + "  ");
            }
            System.out.println("");
        }
        System.out.println("\n");
    }

    /**
     * Method to request a box to shoot to the user
     *
     * @return
     */
    private static int[] requestBox() {
        System.out.println("Set the box to shoot (ex: E3): ");

        String line = scanner.nextLine();

        // text to upperCase
        line = line.toUpperCase();
        int[] shoot;

        // Check the user's selection
        if (line.matches("^[A-Z][0-9]*$")) {

            // getting the letter.
            char letter = line.charAt(0);
            // row number =  LETTER_VALUE - NUM_VALUE.
            int row = Character.getNumericValue(letter) - Character.getNumericValue('A');

            // for the column
            int column = Integer.parseInt(line.substring(1, line.length()));

            // if the user's selection is OK return the result
            if (row >= 0 && row < SIZE && column >= 0 && column <= SIZE) {

                shoot = new int[]{row, column};

            } else // if is not OK, ask for othe shoot
            {
                shoot = new int[]{-1, -1};
            }
        } else {
            shoot = new int[]{-1, -1};
        }

        return shoot;
    }

    /**
     * Method to check the shoot
     *
     * @param map
     * @param shoot
     * @return
     */
    private static boolean evaluateShoot(char[][] map, int[] shoot) {

        int row = shoot[0];
        int column = shoot[1];

        return map[row][column] == WATER_NOT_TOUCHED || (map[row][column] >= '1' && map[row][column] <= '5');
    }

    /**
     * Method to update a map (computer or user)
     *
     * @param map
     * @param shoot
     * @param score
     * @return
     */
    private static int updateMap(char[][] map, int[] shoot, int score) {

        int row = shoot[0];
        int column = shoot[1];

        if (map[row][column] == WATER_NOT_TOUCHED) {

            map[row][column] = WATER;
            System.out.println("WATER");

        } else {
            map[row][column] = TOUCHED;
            System.out.println("TOUCHED SHIP!");
            --score;
        }
        return score;
    }

    /**
     * Method to update regitered map
     *
     * @param map
     * @param shoot
     * @param typeOfShoot
     */
    private static void updateRegisterMap(char[][] map, int[] shoot, char typeOfShoot) {
        int row = shoot[0];
        int column = shoot[1];

        map[row][column] = typeOfShoot;
    }

    /**
     * Method to make computer's shoots
     *
     * @return
     */
    private static int[] generateRandomShoot() {
        return new int[]{getRandom(), getRandom()};
    }

    /**
     * Method to get a random number
     *
     * @return random number
     */
    private static int getRandom() {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(SIZE);
    }

    /**
     * Method to init a map
     *
     * @param computerMap
     */
    private static void initMap(char[][] map) {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = WATER_NOT_TOUCHED;
            }
        }

        // ships: 2x5 boxes ; 3x3 boxes; 5x1 boxes
        int[] ships = {5, 5, 3, 3, 3, 1, 1, 1, 1, 1};

        // position
        char[] position = {'V', 'H'};

        // for each ship      
        for (int s : ships) {
            boolean isFixed = false;
            while (!isFixed) {
                int row = getRandom();
                int column = getRandom();
                char optionalPosition = position[getRandom() % 2];

                // if the ship fix in?
                if (optionalPosition == 'V') { // Vertical position
                    if (row + s <= (SIZE - 1)) {
                        // there are other ship in this position?
                        boolean isAnotherShip = false;

                        for (int i = row; (i <= row + s) && !isAnotherShip; i++) {
                            if (map[i][column] != WATER_NOT_TOUCHED) {
                                isAnotherShip = true;
                            }
                        }
                        if (!isAnotherShip) {
                            for (int i = row; i < row + s; i++) {
                                map[i][column] = Integer.toString(s).charAt(0);
                            }
                            isFixed = true;
                        }
                    }
                } else { // Horizontal position
                    if (column + s <= (SIZE - 1)) {
                        boolean isAnotherShip = false;
                        for (int j = column; (j <= column + s) && !isAnotherShip; j++) {
                            if (map[row][j] != WATER_NOT_TOUCHED) {
                                isAnotherShip = true;
                            }
                        }
                        if (!isAnotherShip) {
                            for (int j = column; j < column + s; j++) {
                                map[row][j] = Integer.toString(s).charAt(0);
                            }
                            isFixed = true;
                        }
                    }
                }
            } // end loop fixing ships
        }
    }
}
