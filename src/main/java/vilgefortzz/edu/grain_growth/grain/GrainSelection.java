package vilgefortzz.edu.grain_growth.grain;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;

import java.util.List;

/**
 * Created by vilgefortzz on 10/11/18
 */
public interface GrainSelection {

    List<Cell> selectEdgeGrains(Growth growth, Grid grid, int numberOfGrains);
}
