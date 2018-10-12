package vilgefortzz.edu.grain_growth.nucleating;

import vilgefortzz.edu.grain_growth.growth.Growth;
import vilgefortzz.edu.grain_growth.grid.Grid;

/**
 * Created by vilgefortzz on 08/10/18
 */
public interface Nucleating {

    void nucleating(Growth growth, Grid grid, int numberOfGrains);
}
