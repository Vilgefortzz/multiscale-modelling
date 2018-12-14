package vilgefortzz.edu.grain_growth.energy_distribution;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;

import java.util.List;

/**
 * Created by vilgefortzz on 8/12/18
 */
public class Heterogeneous implements EnergyDistribution {

    @Override
    public void calculateEnergy(Growth growth, Grid grid, int energyInside, int energyOnEdges) {

        List<Cell> edgeCells = grid.getEdgeCells();
        edgeCells.forEach(edgeCell -> {
            if (!edgeCell.isRecrystallized()) {
                Cell cell = grid.getCell(edgeCell.getX(), edgeCell.getY());
                cell.setEnergyDistribution(energyOnEdges);
            }
        });

        grid.forEachCells(cell -> {
            if (!edgeCells.contains(cell) && !cell.isRecrystallized()) {
                cell.setEnergyDistribution(energyInside);
            }
        });
    }

    @Override
    public void showEnergy(Growth growth, Grid grid, int energyInside, int energyOnEdges) {

        List<Cell> edgeCells = grid.getEdgeCells();

        edgeCells.forEach(edgeCell -> {
            Cell cell = grid.getCell(edgeCell.getX(), edgeCell.getY());
            cell.savePreviousState();
            if (cell.isRecrystallized()) {
                cell.setState(Cell.RECRYSTALLIZED_STATE);
            } else {
                cell.setState(Cell.ENERGY_ON_EDGES_STATE);
            }
        });

        grid.forEachCells(cell -> {
            if (!edgeCells.contains(cell)) {
                cell.savePreviousState();
                if (cell.isRecrystallized()) {
                    cell.setState(Cell.RECRYSTALLIZED_STATE);
                } else {
                    cell.setState(Cell.ENERGY_INSIDE_STATE);
                }
            }
        });
    }

    @Override
    public void showMicrostructure(Growth growth, Grid grid, int energyInside, int energyOnEdges) {
        grid.forEachCells(Cell::restorePreviousState);
    }

    @Override
    public String toString() {
        return "Heterogeneous";
    }
}
