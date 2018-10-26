package vilgefortzz.edu.grain_growth.growth;

import javafx.scene.paint.Color;
import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.image.ColorGenerator;
import vilgefortzz.edu.grain_growth.neighbourhood.FurtherMoore;
import vilgefortzz.edu.grain_growth.neighbourhood.Moore;
import vilgefortzz.edu.grain_growth.neighbourhood.NearestMoore;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;

import java.util.List;
import java.util.Random;

/**
 * Created by vilgefortzz on 25/10/18
 */
public class BoundaryShapeControlGrainGrowth extends Growth {

    /**
     * Neighbourhoods
     */
    private Moore moore;
    private NearestMoore nearestMoore;
    private FurtherMoore furtherMoore;

    public BoundaryShapeControlGrainGrowth() {

        moore = new Moore();
        nearestMoore = new NearestMoore();
        furtherMoore = new FurtherMoore();
    }

    @Override
    public void initialize(Grid grid) {

        finished = false;
        type = 0;

        ColorGenerator.setColor(type, Color.WHITE);
        ColorGenerator.setColor(type - 1, Color.BLACK);
    }

    @Override
    public void mark(Grid grid, Neighbourhood neighbourhood) {

        Grid previousGrid = new Grid(grid);
        changed = false;

        grid.forEachCells(cell -> {
            if (cell.getState() != 0) return;

            if (firstRule(previousGrid, cell, moore)) {
                return;
            }

            if (secondRule(previousGrid, cell, nearestMoore)) {
                return;
            }

            if (thirdRule(previousGrid, cell, furtherMoore)) {
                return;
            }

            fourthRule(previousGrid, cell, moore);
        });

        if (!changed) finished = true;
    }

    @Override
    public void changeState(Cell cell) {

        if (cell.getState() == 0) {
            cell.setState(createNewType());
        }
    }

    public int getMostFrequentState(List<Cell> neighbours, int minAmount, int maxAmount) {

        Random random = new Random();
        int[] freq = new int[type + 2];

        int max = 0;
        int mostState = 0;

        for (Cell cell: neighbours) {

            int state = cell.getState();

            try {
                freq[state + 1]++;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (freq[state + 1] > max && state != 0 && state != -1) {
                max = freq[state + 1];
                mostState = state;
            } else if (freq[state + 1] == max && state != 0 && state != -1) {
                if (random.nextDouble() > .5) {
                    max = freq[state + 1];
                    mostState = state;
                }
            }
        }

        if (freq[mostState + 1] >= minAmount && freq[mostState + 1] <= maxAmount) {
            return mostState;
        } else {
            return 0;
        }
    }

    private boolean firstRule(Grid grid, Cell cell, Moore moore) {

        List<Cell> neighbours = moore.listWithNeighbours(grid, cell);
        int state = getMostFrequentState(neighbours, 5, 8);
        if (state > 0) {
            cell.setState(state);
            changed = true;
            return true;
        }

        return false;
    }

    private boolean secondRule(Grid grid, Cell cell, NearestMoore nearestMoore) {

        List<Cell> neighbours = nearestMoore.listWithNeighbours(grid, cell);
        int state = getMostFrequentState(neighbours, 3, 4);
        if (state > 0) {
            cell.setState(state);
            changed = true;
            return true;
        }

        return false;
    }

    private boolean thirdRule(Grid grid, Cell cell, FurtherMoore furtherMoore) {

        List<Cell> neighbours = furtherMoore.listWithNeighbours(grid, cell);
        int state = getMostFrequentState(neighbours, 3, 4);
        if (state > 0) {
            cell.setState(state);
            changed = true;
            return true;
        }

        return false;
    }

    private void fourthRule(Grid grid, Cell cell, Moore moore) {

        Random random = new Random();
        List<Cell> neighbours = moore.listWithNeighbours(grid, cell);
        int state = getMostFrequentState(neighbours, 1, 9);
        if (state > 0) {
            if ((random.nextInt(100) + 1) <= probability) {
                cell.setState(state);
            }
            changed = true;
        }
    }

    @Override
    public String toString() {
        return "Boundary shape control grain growth";
    }
}
