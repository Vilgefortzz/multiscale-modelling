package vilgefortzz.edu.grain_growth.nucleating;

import vilgefortzz.edu.grain_growth.algorithm.Algorithm;
import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;

import java.util.Random;

/**
 * Created by vilgefortzz on 08/10/18
 */
public class RandomNucleating implements Nucleating {

    @Override
    public void nucleating(Algorithm algorithm, Grid grid, int numberOfGrains) {

        Random random = new Random();

        int x, y;
        Cell cell;

        for (int i = 0; i < numberOfGrains; i++) {

            x = random.nextInt(grid.getWidth());
            y = random.nextInt(grid.getHeight());

            cell = grid.getCell(x, y);
            algorithm.changeState(cell);
        }
    }

    @Override
    public String toString() {
        return "Random nucleating";
    }
}
