package vilgefortzz.edu.grain_growth.growth;

import javafx.scene.paint.Color;
import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.image.ColorGenerator;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by vilgefortzz on 8/12/18
 */
public class MonteCarloStaticRecrystallization extends Growth {

    private Random random;

    @Override
    public void initialize(Grid grid) {

        finished = false;
        random = new Random();

        ColorGenerator.setColor(Cell.RECRYSTALLIZED_STATE, Color.RED);
        ColorGenerator.setColor(Cell.ENERGY_ON_EDGES_STATE, Color.LIGHTGREEN);
        ColorGenerator.setColor(Cell.ENERGY_INSIDE_STATE, Color.BLUE);
        ColorGenerator.setColor(Cell.INITIALIZE_STATE, Color.WHITE);
        ColorGenerator.setColor(Cell.INCLUSION_STATE, Color.BLACK);
        ColorGenerator.setColor(Cell.STRUCTURE_STATE, Color.PINK);
    }

    @Override
    public void mark(Grid grid, Neighbourhood neighbourhood) {

        List<Cell> remainingCells = new ArrayList<>();

        try {

            grid.forEachCells(cell -> {
                List<Cell> neighbours = neighbourhood.listWithNeighbours(grid, cell);
                if (!cell.isRecrystallized() && anyNeighbourIsRecrystallized(neighbours)) {
                    remainingCells.add(cell);
                }
            });

            while (remainingCells.size() > 0) {

                Cell randomCell = getRandomCell(remainingCells);
                List<Cell> neighbours = neighbourhood.listWithNeighbours(grid, randomCell);
                Cell randomNeighbour = getRandomNeighbour(neighbours);

                double energyBefore = getEnergy(randomCell.getState(), neighbours) +
                        randomCell.getEnergyDistribution();
                double energyAfter = getEnergy(randomNeighbour.getState(), neighbours);
                double energyDifference = energyAfter - energyBefore;

                if (energyDifference <= 0) {
                    Cell gridCell = grid.getCell(randomCell.getX(), randomCell.getY());
                    gridCell.setState(randomNeighbour.getState());
                    gridCell.setEnergyDistribution(randomNeighbour.getEnergyDistribution());
                    gridCell.setRecrystallized(true);
                }

                remainingCells.remove(randomCell);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeState(Cell cell) {

        if (cell.getState() != Cell.RECRYSTALLIZED_STATE) {
            cell.setState(Cell.RECRYSTALLIZED_STATE);
            cell.setRecrystallized(true);
            cell.setEnergyDistribution(0);
        }
    }

    private boolean anyNeighbourIsRecrystallized(List<Cell> neighbours) {

        return neighbours.stream().anyMatch(Cell::isRecrystallized);
    }

    private double getEnergy(int cellState, List<Cell> neighbours) {

        return (grainBoundaryEnergy * neighbours.stream().filter(cell -> cell.getState() != cellState).count());
    }

    private Cell getRandomNeighbour(List<Cell> neighbours) {

        return neighbours.stream().filter(Cell::isRecrystallized)
                .collect(Collectors.toList()).get(0);
    }

    private Cell getRandomCell(List<Cell> cells) {

        return cells.get(random.nextInt(cells.size()));
    }

    @Override
    public String toString() {
        return "Monte Carlo static recrystallization";
    }
}
