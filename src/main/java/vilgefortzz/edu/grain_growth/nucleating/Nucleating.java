package vilgefortzz.edu.grain_growth.nucleating;

import vilgefortzz.edu.grain_growth.algorithm.Algorithm;
import vilgefortzz.edu.grain_growth.grid.Grid;

/**
 * Created by vilgefortzz on 08/10/18
 */
public interface Nucleating {

    void nucleating(Algorithm algorithm, Grid grid, int numberOfGrains);
}
