package vilgefortzz.edu.grain_growth.grain;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by vilgefortzz on 10/11/18
 */
public class NGrains implements GrainSelection {

    @Override
    public List<Cell> selectEdgeGrains(Growth growth, Grid grid, int numberOfGrains) {

        Random random = new Random();
        List<Cell> edgeCells = grid.getEdgeSelectionCells();
        List<Cell> selectedGrains = new ArrayList<>();
        Cell randomCell;

        for (int i = 0; i < numberOfGrains; i++) {

            do {
                int randomKey = random.nextInt(edgeCells.size());
                randomCell = edgeCells.get(randomKey);
            } while (selectedGrains.contains(randomCell));

            selectedGrains.addAll(grid.getEdgeCells(edgeCells, randomCell.getState()));
        }

        grid.forEachCells(cell -> {
            if (selectedGrains.contains(cell)) {
                cell.setState(Cell.INCLUSION_STATE);
            } else if (cell.getState() != Cell.INCLUSION_STATE) {
                cell.setState(Cell.INITIALIZE_STATE);
            }
        });

        return grid.getCells();
    }

    @Override
    public String toString() {
        return "N grains";
    }
}
