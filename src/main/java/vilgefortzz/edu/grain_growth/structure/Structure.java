package vilgefortzz.edu.grain_growth.structure;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;

import java.util.List;

/**
 * Created by vilgefortzz on 30/10/18
 */
public interface Structure {

    List<Cell> selectGrains(Growth growth, Grid grid, int numberOfStructures);
}
