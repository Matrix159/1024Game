package game1024;

import game1024.Cell;

import java.util.ArrayList;

/**
 * Created by Hans Dulimarta on Feb 08, 2016.
 */
public interface NumberSlider {
    /**
     * Reset the game logic to handle a board of a given dimension
     *
     * @param height the number of rows in the board
     * @param width the number of columns in the board
     * @param winningValue the value that must appear on the board to
     *                     win the game
     */
    void resizeBoard (int height, int width, int winningValue);


    /**
     * Remove all numbered tiles from the board and place
     * TWO non-zero values at random location
     */
    void reset();

    /**
     * Set the game board to the desired values given in the 2D array.
     * The method should use nested loops to copy each element from the
     * desired array to your own internal array. Do not just assign the
     * entire array object to your internal array object. Otherwise, your
     * internal array may get corrupted by the array used in the JUnit
     * test file. This method is mainly used by the JUnit tester.
     * @param ref
     */
    void setValues(final int[][] ref);

    /**
     * Insert one random tile into an empty spot in the board.
     *
     * @return a Cell object with its row, column, and value attributes
     *  initialized properly
     *
     * @throws IllegalStateException when the board has no empty cell
     */
    Cell placeRandomValue();

    /**
     * Slide all the tiles in the board in the requested direction
     * @param dir move direction of the tiles
     *
     * @return true when the board changes
     */
    boolean slide (SlideDirection dir);

    /**
     *
     * @return an arraylist of Cells. Each cell holds the (row,column) and
     * value of a tile
     */
    ArrayList<Cell> getNonEmptyTiles();

    /**
     * Return the current state of the game
     * @return one of the possible values of GameStatus enum
     */
    GameStatus getStatus();
}