package vilgefortzz.edu.grain_growth.growth;

import javafx.scene.paint.Color;
import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.image.ColorGenerator;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;

import java.util.List;
import java.util.Random;

/**
 * Created by vilgefortzz on 22/11/18
 */
public class MonteCarloGrainGrowth extends Growth {

    private Random random;

    @Override
    public void initialize(Grid grid) {

        random = new Random();
        ColorGenerator.setColor(Cell.INITIALIZE_STATE, Color.WHITE);
    }

    @Override
    public void mark(Grid grid, Neighbourhood neighbourhood) {

        List<Cell> neighbours;
        Cell cell;

        for (int i = 0; i < grid.getSize(); i++) {

            int cellX = random.nextInt(grid.getWidth());
            int cellY = random.nextInt(grid.getHeight());

            cell = grid.getCell(cellX, cellY);
            neighbours = neighbourhood.listWithNeighbours(grid, cell);

            double energyBefore = getEnergy(cell.getState(), neighbours);
            int newState = getNeighbourState(neighbours);
            double energyAfter = getEnergy(newState, neighbours);
            double energyDifference = energyAfter - energyBefore;

            if (energyDifference <= 0) {
                cell.setState(newState);
            }
        }
    }

    @Override
    public void changeState(Cell cell) {}

    private double getEnergy(int cellState, List<Cell> neighbours) {

        return (grainBoundaryEnergy * neighbours.stream().filter(cell -> cell.getState() != cellState).count());
    }

    private int getNeighbourState(List<Cell> neighbours) {

        return neighbours.get(random.nextInt(neighbours.size())).getState();
    }

    @Override
    public String toString() {
        return "Monte Carlo grain growth";
    }
}
