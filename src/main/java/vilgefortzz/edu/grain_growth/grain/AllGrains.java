package vilgefortzz.edu.grain_growth.grain;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;

import java.util.List;

/**
 * Created by vilgefortzz on 10/11/18
 */
public class AllGrains implements GrainSelection {

    @Override
    public List<Cell> selectEdgeGrains(Growth growth, Grid grid, int numberOfGrains) {

        List<Cell> edgeCells = grid.getEdgeCells();

        grid.forEachCells(cell -> {
            if (edgeCells.contains(cell)) {
                cell.setState(Cell.INCLUSION_STATE);
            } else if (cell.getState() != Cell.INCLUSION_STATE) {
                cell.setState(Cell.INITIALIZE_STATE);
            }
        });

        return grid.getCells();
    }

    @Override
    public String toString() {
        return "All grains";
    }
}
