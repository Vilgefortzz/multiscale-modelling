package vilgefortzz.edu.grain_growth.nucleating;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;

import java.util.List;
import java.util.Random;

/**
 * Created by vilgefortzz on 14/12/18
 */
public class RandomEdgeNucleating implements Nucleating {

    @Override
    public void nucleating(Growth growth, Grid grid, int numberOfGrains) {

        Random random = new Random();
        List<Cell> edgeCells = grid.getEdgeCells();
        Cell cell;

        for (int i = 0; i < numberOfGrains; i++) {

            int randomKey = random.nextInt(edgeCells.size());
            Cell randomEdgeCell = edgeCells.get(randomKey);

            cell = grid.getCell(randomEdgeCell.getX(), randomEdgeCell.getY());
            growth.changeState(cell);
        }
    }

    @Override
    public String toString() {
        return "Random edge nucleating";
    }
}
