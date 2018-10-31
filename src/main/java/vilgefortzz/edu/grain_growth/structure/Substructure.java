package vilgefortzz.edu.grain_growth.structure;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;
import vilgefortzz.edu.grain_growth.image.ColorGenerator;

import java.util.List;
import java.util.Random;

/**
 * Created by vilgefortzz on 30/10/18
 */
public class Substructure implements Structure {

    @Override
    public List<Cell> selectGrains(Growth growth, Grid grid) {

        Random random = new Random();
        int randomState = (random.nextInt(growth.getType()) + 1);

        ColorGenerator.setColor(Cell.STRUCTURE_STATE, ColorGenerator.getColor(randomState));

        grid.forEachCells(cell -> {
            if (cell.getState() == randomState) {
                cell.setState(Cell.STRUCTURE_STATE);
            } else {
                cell.setState(Cell.INITIALIZE_STATE);
            }
        });

        return grid.getCells();
    }

    @Override
    public String toString() {
        return "Substructure";
    }
}
