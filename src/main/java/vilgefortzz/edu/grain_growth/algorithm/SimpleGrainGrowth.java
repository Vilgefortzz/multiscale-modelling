package vilgefortzz.edu.grain_growth.algorithm;

import javafx.scene.paint.Color;
import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;
import vilgefortzz.edu.grain_growth.grid.ColorGenerator;

import java.util.List;
import java.util.Random;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class SimpleGrainGrowth extends Algorithm {

    private boolean finished;
    private boolean changed;
    private int type;

    @Override
    public void initialize(Grid grid) {

        finished = false;
        type = 0;

        ColorGenerator.setColor(type, Color.WHITE);
    }

    @Override
    public void mark(Grid grid, Neighbourhood neighbourhood) {

        Grid previousGrid = new Grid(grid);

        changed = false;
        grid.forEach(c -> {
            List<Cell> neighbours = neighbourhood.neighbours(previousGrid, c);

            if (c.getState() == 0 && anyNeighbourIsNucleating(neighbours)) {
                int state = getMostFrequentState(neighbours);
                c.setState(state);
                changed = true;
            }
        });

        if (!changed) finished = true;

        grid.setState(type);
    }

    @Override
    public void changeState(Cell cell) {

        if (cell.getState() == 0) {
            cell.setState(createNewType());
        }
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public int getType() {
        return type;
    }

    public int createNewType() {
        return type++;
    }

    private boolean anyNeighbourIsNucleating(List<Cell> neighbours) {

        return neighbours.stream().anyMatch(cell -> cell.getState() != 0);
    }

    public int getMostFrequentState(List<Cell> neighbours) {

        Random random = new Random();
        int[] freq = new int[type + 1];

        int max = 0;
        int mostState = 0;
        for (Cell cell: neighbours) {
            int state = cell.getState();

            freq[state]++;

            if (freq[state] > max && state != 0) {
                max = freq[state];
                mostState = state;
            } else if (freq[state] == max && state != 0) {
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
