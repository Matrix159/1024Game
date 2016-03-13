package game1024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Eldridge on 2/10/2016.
 */
public class GameLogic implements NumberSlider
{

    private int height, width, winningValue;
    private ArrayList<Cell> cellArrayList;
    public GameLogic(int height, int width, int winningValue)
    {
        this.height = height;
        this.width = width;
        this.winningValue = winningValue;
        cellArrayList = new ArrayList<>();
        for(int x = 0; x < (this.height); x++)
        {
            for(int y = 0; y < (this.width); y++)
            cellArrayList.add(new Cell(x, y, 0));
        }
        placeRandomValue();
        placeRandomValue();
    }

    @Override
    public void resizeBoard(int height, int width, int winningValue)
    {
        this.height = height;
        this.width = width;
        this.winningValue = winningValue;
        cellArrayList = new ArrayList<>();
        for(int x = 0; x < (this.height); x++)
        {
            for(int y = 0; y < (this.width); y++)
                cellArrayList.add(new Cell(x, y, 0));
        }
        placeRandomValue();
        placeRandomValue();
    }

    @Override
    public void reset() {
        cellArrayList = new ArrayList<>();
        for(int x = 0; x < (this.height); x++)
        {
            for(int y = 0; y < (this.width); y++)
                cellArrayList.add(new Cell(x, y, 0));
        }
        placeRandomValue();
        placeRandomValue();
    }

    @Override
    public void setValues(int[][] ref) {
        cellArrayList = new ArrayList<>();
        for(int x = 0; x < ref.length; x++)
        {
            for(int y = 0; y < ref[x].length; y++)
                cellArrayList.add(new Cell(x, y, ref[x][y]));
        }
    }

    @Override
    public Cell placeRandomValue()
    {
        Cell returnCell;
        ArrayList<Cell> emptyTiles = new ArrayList<>();
        for(Cell x : cellArrayList)
        {
            if(x.value == 0)
            {
                emptyTiles.add(x);
            }
        }
        if(emptyTiles.size() == 0)
            return null;
        Random tileRand = new Random();
        if(tileRand.nextInt(10)  < 8)
        {

            returnCell = emptyTiles.get(tileRand.nextInt(emptyTiles.size()));
            returnCell.value = 2;
        }
        else
        {
            returnCell = emptyTiles.get(tileRand.nextInt(emptyTiles.size()));
            returnCell.value = 4;
        }
        return returnCell;
    }

    @Override
    public boolean slide(SlideDirection dir)
    {
        boolean moved = false;
        switch(dir)
        {
            case UP:
                if(moveCellsUp())
                moved = true;
                System.out.println(moved);
                break;

            case DOWN:
                // Only care about cell columns again
                if(moveCellsDown())
                moved = true;
                System.out.println(moved);
                break;

            case LEFT:
                // Only care about cells in the same row
                // Get cells in row 1 and start merging from left side?
                if(moveCellsLeft())
                moved = true;
                System.out.println(moved);
                break;

            case RIGHT:
                // Only care about cells in the same row
                if(moveCellsRight())
                moved = true;
                System.out.println(moved);
                break;

            default:
                moved = false;
                break;
        }
        if(moved)
        placeRandomValue();
        return moved;
    }

    @Override
    public ArrayList<Cell> getNonEmptyTiles()
    {
        ArrayList<Cell> nonEmptyList = new ArrayList<>();
        for(Cell x : cellArrayList)
        {
            if(x.value != 0){
                nonEmptyList.add(x);
            }
        }
        return nonEmptyList;
    }

    @Override
    public GameStatus getStatus()
    {
        for(Cell x : cellArrayList)
        {
            if(x.value == winningValue)
                return GameStatus.USER_WON;
        }
        if(hasLost())
            return GameStatus.USER_LOST;
        else
            return GameStatus.IN_PROGRESS;
    }

    public boolean moveCellsUp()
    {
        // We only care about cell columns here
        // Iterate through each column
        int[] initial = new int[cellArrayList.size()];
        for(int jj = 0; jj < cellArrayList.size(); jj++)
        {
            initial[jj] = cellArrayList.get(jj).value;
        }
        for(int i = 0; i < width; i++) {
            // Get cells in this column
            ArrayList<Cell> cellsInColumn = new ArrayList<>();
            for (Cell y :cellArrayList) {
                if (y.column == i) {
                    cellsInColumn.add(y);
                }
            }
            // Merge cells upward.
            Collections.sort(cellsInColumn);

            for (int j = 0; j < cellsInColumn.size() - 1; j++)
            {
                // Another for loop?
                // (2, 0, 2, 2) -> (4, 0, 0, 2)
                for(int ij = j; ij <cellsInColumn.size() -1; ij++)
                {
                    if((cellsInColumn.get(j).value != cellsInColumn.get(ij + 1).value)
                            &&  cellsInColumn.get(ij + 1).value != 0)
                    {
                        break;
                    }
                    if(cellsInColumn.get(j).value == cellsInColumn.get(ij + 1).value)
                    {
                        cellsInColumn.get(j).value *= 2;
                        cellsInColumn.get(ij + 1).value = 0;
                        break;
                    }
                }
            }
            // Slide remaining cells
            int[] values = new int[height];
            int dd = 0;
            for(Cell xy : cellsInColumn)
            {

                if(xy.value != 0)
                {
                    values[dd] = xy.value;
                    dd++;
                }
            }
            int di = 0;
            for(Cell xxy : cellsInColumn)
            {

                xxy.value = values[di];
                di++;
            }
        }
        int[] after = new int[cellArrayList.size()];
        for(int jj = 0; jj < cellArrayList.size(); jj++)
        {
            after[jj] = cellArrayList.get(jj).value;
        }

        if(Arrays.equals(initial, after))
        {
            return false;
        }
        else
            return true;
    }

    public boolean moveCellsDown()
    {
        int[] initial = new int[cellArrayList.size()];
        for(int jj = 0; jj < cellArrayList.size(); jj++)
        {
            initial[jj] = cellArrayList.get(jj).value;
        }
        // We only care about cell columns here
        // Iterate through each column
        for(int i = 0; i < width; i++) {
            // Get cells in this column
            ArrayList<Cell> cellsInColumn = new ArrayList<>();
            for (Cell y :cellArrayList) {
                if (y.column == i) {
                    cellsInColumn.add(y);
                }
            }
            // Merge cells upward. Somehow...
            Collections.sort(cellsInColumn);
            Collections.reverse(cellsInColumn);

            for (int j = 0; j < cellsInColumn.size() - 1; j++)
            {
                for(int ij = j; ij <cellsInColumn.size() -1; ij++)
                {
                    if((cellsInColumn.get(j).value != cellsInColumn.get(ij + 1).value)
                            &&  cellsInColumn.get(ij + 1).value != 0)
                    {
                        break;
                    }
                    if(cellsInColumn.get(j).value == cellsInColumn.get(ij + 1).value)
                    {
                        cellsInColumn.get(j).value *= 2;
                        cellsInColumn.get(ij + 1).value = 0;
                        break;
                    }
                }
            }

            int[] values = new int[height];
            int dd = 0;
            for(Cell xy : cellsInColumn)
            {

                if(xy.value != 0)
                {
                    values[dd] = xy.value;
                    dd++;
                }
            }
            int di = 0;
            for(Cell xxy : cellsInColumn)
            {

                xxy.value = values[di];
                di++;
            }



        }
        int[] after = new int[cellArrayList.size()];
        for(int jj = 0; jj < cellArrayList.size(); jj++)
        {
            after[jj] = cellArrayList.get(jj).value;
        }

        if(Arrays.equals(initial, after))
        {
            return false;
        }
        else
            return true;
    }

    public boolean moveCellsRight()
    {
        int[] initial = new int[cellArrayList.size()];
        for(int jj = 0; jj < cellArrayList.size(); jj++)
        {
            initial[jj] = cellArrayList.get(jj).value;
        }
        // We only care about cell rows here
        // Iterate through each row
        for(int i = 0; i < height; i++) {
            // Get cells in this column
            ArrayList<Cell> cellsInRow = new ArrayList<>();
            for (Cell y :cellArrayList) {
                if (y.row == i) {
                    cellsInRow.add(y);
                }
            }
            // Merge cells upward. Somehow...
            Collections.sort(cellsInRow);
            Collections.reverse(cellsInRow);
            for (int j = 0; j < cellsInRow.size() - 1; j++)
            {
                for(int ij = j; ij <cellsInRow.size() -1; ij++)
                {
                    if((cellsInRow.get(j).value != cellsInRow.get(ij + 1).value)
                            &&  cellsInRow.get(ij + 1).value != 0)
                    {
                        break;
                    }
                    if(cellsInRow.get(j).value == cellsInRow.get(ij + 1).value)
                    {
                        cellsInRow.get(j).value *= 2;
                        cellsInRow.get(ij + 1).value = 0;
                        break;
                    }
                }
            }

            int[] values = new int[width];
            int dd = 0;
            for(Cell xy : cellsInRow)
            {

                if(xy.value != 0)
                {
                    values[dd] = xy.value;
                    dd++;
                }
            }
            int di = 0;
            for(Cell xxy : cellsInRow)
            {

                xxy.value = values[di];
                di++;
            }


        }
        int[] after = new int[cellArrayList.size()];
        for(int jj = 0; jj < cellArrayList.size(); jj++)
        {
            after[jj] = cellArrayList.get(jj).value;
        }

        if(Arrays.equals(initial, after))
        {
            return false;
        }
        else
            return true;
    }

    public boolean moveCellsLeft()
    {
        int[] initial = new int[cellArrayList.size()];
        for(int jj = 0; jj < cellArrayList.size(); jj++)
        {
            initial[jj] = cellArrayList.get(jj).value;
        }
        // We only care about cell columns here
        // Iterate through each column
        for(int i = 0; i < height; i++) {
            // Get cells in this column
            ArrayList<Cell> cellsInRow = new ArrayList<>();
            for (Cell y :cellArrayList) {
                if (y.row == i) {
                    cellsInRow.add(y);
                }
            }
            // Merge cells upward. Somehow...
            Collections.sort(cellsInRow);

            for (int j = 0; j < cellsInRow.size() - 1; j++)
            {
                for(int ij = j; ij <cellsInRow.size() -1; ij++)
                {
                    if((cellsInRow.get(j).value != cellsInRow.get(ij + 1).value)
                            &&  cellsInRow.get(ij + 1).value != 0)
                    {
                        break;
                    }
                    if(cellsInRow.get(j).value == cellsInRow.get(ij + 1).value)
                    {
                        cellsInRow.get(j).value *= 2;
                        cellsInRow.get(ij + 1).value = 0;
                        break;
                    }
                }
            }

            int[] values = new int[width];
            int dd = 0;
            for(Cell xy : cellsInRow)
            {

                if(xy.value != 0)
                {
                    values[dd] = xy.value;
                    dd++;
                }
            }
            int di = 0;
            for(Cell xxy : cellsInRow)
            {

                xxy.value = values[di];
                di++;
            }


        }
        int[] after = new int[cellArrayList.size()];
        for(int jj = 0; jj < cellArrayList.size(); jj++)
        {
            after[jj] = cellArrayList.get(jj).value;
        }

        if(Arrays.equals(initial, after))
        {
            return false;
        }
        else
            return true;
    }

    public boolean hasEqualNeighbors(Cell x)
    {
        int row, column;
        row = x.row;
        column = x.column;
        // Create temp arraylist to hold any surrounding cells
        ArrayList<Cell> temp = new ArrayList<>();
        for(Cell y : cellArrayList)
        {
            if((column - 1 == y.column && row == y.row)
                    || (column + 1 == y.column && row == y.row)
                    || (row - 1 == y.row && column == y.column)
                    || (row + 1 == y.row && column == y.column))
            {
                temp.add(y);
            }
        }
        // Check temp arraylist to see if cells are equal to parameter
        for(Cell dy : temp)
        {
            if(dy.value == x.value)
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasLost()
    {
        boolean lost = true;
        for(Cell x : cellArrayList)
        {
            if(x.value == 0)
            {
                lost = false;
            }
        }
        for(Cell y : cellArrayList)
        {
            if (hasEqualNeighbors(y))
                lost = false;
        }
        return lost;
    }
}
