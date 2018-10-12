package vilgefortzz.edu.grain_growth.growth;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;

/**
 * Created by vilgefortzz on 07/10/18
 */
public interface Algorithm {

    void initialize(Grid grid);
    void mark(Grid grid, Neighbourhood neighbourhood);
    void changeState(Cell cell);
}
