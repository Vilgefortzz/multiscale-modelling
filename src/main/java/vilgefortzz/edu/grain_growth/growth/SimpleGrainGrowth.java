package vilgefortzz.edu.grain_growth.growth;

import javafx.scene.paint.Color;
import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.image.ColorGenerator;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;

import java.util.List;
import java.util.Random;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class SimpleGrainGrowth extends Growth {

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
            List<Cell> neighbours = neighbourhood.listWithNeighbours(previousGrid, cell);

            if (cell.getState() == 0 && anyNeighbourIsNucleating(neighbours)) {
                int state = getMostFrequentState(neighbours);
                cell.setState(state);
                changed = true;
            }
        });

        if (!changed) finished = true;
    }

    @Override
    public void changeState(Cell cell) {

        if (cell.getState() == 0) {
            cell.setState(createNewType());
        }
    }

    private boolean anyNeighbourIsNucleating(List<Cell> neighbours) {

        return neighbours.stream().anyMatch(cell -> (
                cell.getState() != 0 && cell.getState() != -1 && cell.isChangeable())
        );
    }

    public int getMostFrequentState(List<Cell> neighbours) {

        Random random = new Random();
        int[] freq = new int[type + 1];

        int max = 0;
        int mostState = 0;

        for (Cell cell: neighbours) {

            int state = cell.getState();
            if (!cell.isChangeable() || state == Cell.STRUCTURE_STATE || state == Cell.INCLUSION_STATE) continue;

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

        return mostState;
    }

    @Override
    public String toString() {
        return "Simple grain growth";
    }
}
