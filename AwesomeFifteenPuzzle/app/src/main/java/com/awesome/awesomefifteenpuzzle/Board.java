package com.awesome.awesomefifteenpuzzle;


/**
 * <h1>The Puzzle Board</h1>
 * The model class of the project which manipulate the actual array puzzle and then
 * the {@link FifteenPuzzle} invokes the display accordingly
 * <p>
 * This class is a <b>singleton</b> since there should be only one instance in the whole project
 *
 * @author Team Awesome 2.0
 * @version 1.0
 * @since 12/2/2016
 */
public class Board {

    FifteenPuzzle activity;
    int[] puzzle = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
    int zero = 15;

    private static Board instance = null;

    /**
     * a private empty constructor for the singleton class
     */
    private Board() {}

    /**
     * A getter for the singleton board
     * @return The Single instance of Board
     */
    public static Board getInstance() {
        if(instance == null) {
            instance = new Board();
        }
        return instance;
    }

    /**
     * method to be called from the main activity to send a reference to the board
     * @param fifteenPuzzle a reference to the main activity
     */
    public void setActivity(FifteenPuzzle fifteenPuzzle){
        activity = fifteenPuzzle;
    }

    /**
     * Takes a tile and moves it if possible then returns the type of movement:
     * <ul>
     * <li>r: move right</li>
     * <li>l: move left</li>
     * <li>u: move up</li>
     * <li>d: move down</li>
     * <li>f: false movement</li>
     * </ul>
     *
     * @param tile to be moved
     * @return type of movement
     */
    public char move(int tile) {
        int index = find(tile);
        char valid_move;
        if(zero/4 == index/4)
            switch (index%4 - zero%4){
                case -1:
                    valid_move = 'r';
                    break;
                case 1:
                    valid_move = 'l';
                    break;
                default:
                    return 'f';
            }
        else if (index%4 == zero%4)
            switch (zero/4 - index/4){
                case 1:
                    valid_move = 'd';
                    break;
                case -1:
                    valid_move = 'u';
                    break;
                default:
                    return 'f';
            }
        else return 'f';

        puzzle[zero] = tile;
        puzzle[index] = 0;
        zero = index;
        for(int i =0; i<16;i++)
            System.out.print(puzzle[i]+" ");
        System.out.print("\n");
        return valid_move;
    }

    /**
     * Finds the index of a given tile in the puzzle array
     * @param tile of which index is needed
     * @return the index or -100 if it does not exists
     */
    public int find(int tile) {
        for (int i = 0; i < puzzle.length; i++)
            if (puzzle[i] == tile)
                return i;
        return -100;
    }

    /**
     * Updates the puzzle array as the display after a shuffle in the game
     * @param stringPuzzle the newly shuffled board pulled from the server
     */
    public void updatePuzzle(String stringPuzzle){
        stringPuzzle = stringPuzzle.replaceAll("[^0-9^,]", "");
        String[] parts = stringPuzzle.split("[,]");
        int [] temp = new int[16];
        try {
            for (int i = 0; i < 16; i++)
                temp[i] = Integer.parseInt(parts[i]);

        } catch (NumberFormatException nfe) {
            System.out.println("Error in connection");
        }

        int [][] displacement = new int[16][2];
        int current_pos;

        for(int i = 0; i < 16; i++){
            if (temp[i] == 0)
                zero = i;
            current_pos = find(temp[i]);
            displacement[current_pos][0] = (i%4 - current_pos%4);
            displacement[current_pos][1] = (i/4 - current_pos/4);
        }

        activity.updateDisplay(displacement, temp);
        System.arraycopy(temp, 0, puzzle, 0, temp.length);
    }

    /**
     * Updates the puzzle array as well as the display after solving the game
     * @param stringMoves the list of tiles to move to solve the puzzle
     */
    public void solve(String stringMoves){
        System.out.println(stringMoves);
        stringMoves = stringMoves.replaceAll("[^0-9^,]", "");
        String[] parts = stringMoves.split("[,]");
        int[] moves = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++)
                moves[i] = Integer.parseInt(parts[i]);

        } catch (NumberFormatException nfe) {
            System.out.println("Error in connection");
        }
        activity.multipleMoves(moves);
    }

    /**
     * Transforms the board to a string of its state
     * @return the string representation of the board
     */
    public String toString(){
        String s = "["+puzzle[0];
        for(int i = 1; i<16;i++){
            s += "," + puzzle[i];
        }
        return s+"]";
    }
}
