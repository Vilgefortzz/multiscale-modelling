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

    private boolean finished;
    private boolean changed;
    private int type;

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

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    public int createNewType() {
        return type++;
    }

    private boolean anyNeighbourIsNucleating(List<Cell> neighbours) {

        return neighbours.stream().anyMatch(cell -> (cell.getState() != 0 && cell.getState() != -1));
    }

    public int getMostFrequentState(List<Cell> neighbours) {

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

        return mostState;
    }

    @Override
    public String toString() {
        return "Simple grain growth";
    }
}
