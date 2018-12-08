package vilgefortzz.edu.grain_growth.growth;

import javafx.scene.paint.Color;
import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.image.ColorGenerator;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by vilgefortzz on 22/11/18
 */
public class MonteCarloGrainGrowth extends Growth {

    private Random random;

    @Override
    public void initialize(Grid grid) {

        finished = false;
        random = new Random();
        ColorGenerator.setColor(Cell.ENERGY_ON_EDGES_STATE, Color.LIGHTGREEN);
        ColorGenerator.setColor(Cell.ENERGY_INSIDE_STATE, Color.BLUE);
        ColorGenerator.setColor(Cell.INITIALIZE_STATE, Color.WHITE);
        ColorGenerator.setColor(Cell.INCLUSION_STATE, Color.BLACK);
        ColorGenerator.setColor(Cell.STRUCTURE_STATE, Color.PINK);
    }

    @Override
    public void mark(Grid grid, Neighbourhood neighbourhood) {

        List<Cell> neighbours;
        Cell cell;

        for (int i = 0; i < grid.getSize(); i++) {

            do {

                int cellX = random.nextInt(grid.getWidth());
                int cellY = random.nextInt(grid.getHeight());
                cell = grid.getCell(cellX, cellY);
            } while (cell.getState() == Cell.STRUCTURE_STATE);

            neighbours = neighbourhood.listWithNeighbours(grid, cell);

            if (neighbours.stream().noneMatch(neighbour -> neighbour.getState() != Cell.STRUCTURE_STATE))
                continue;

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

        List<Cell> spaceCells = neighbours.stream().filter(cell -> cell.getState() != Cell.STRUCTURE_STATE)
                .collect(Collectors.toList());
        return spaceCells.get(random.nextInt(spaceCells.size())).getState();
    }

    @Override
    public String toString() {
        return "Monte Carlo grain growth";
    }
}
