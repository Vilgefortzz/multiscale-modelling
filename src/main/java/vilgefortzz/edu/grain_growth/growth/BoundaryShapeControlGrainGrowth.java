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
        if (type != 0) return;

        type = 0;
        ColorGenerator.setColor(Cell.INITIALIZE_STATE, Color.WHITE);
        ColorGenerator.setColor(Cell.INCLUSION_STATE, Color.BLACK);
        ColorGenerator.setColor(Cell.STRUCTURE_STATE, Color.PINK);
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
        int[] freq = new int[type + 1];

        int max = 0;
        int mostState = 0;

        for (Cell cell: neighbours) {

            int state = cell.getState();
            if (!cell.isChangable() || state == Cell.STRUCTURE_STATE || state == Cell.INCLUSION_STATE) continue;

            try {
                freq[state]++;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (freq[state] > max && state != Cell.INITIALIZE_STATE) {
                max = freq[state];
                mostState = state;
            } else if (freq[state] == max && state != Cell.INITIALIZE_STATE) {
                if (random.nextDouble() > .5) {
                    max = freq[state];
                    mostState = state;
                }
            }
        }

        if (freq[mostState] >= minAmount && freq[mostState] <= maxAmount) {
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
