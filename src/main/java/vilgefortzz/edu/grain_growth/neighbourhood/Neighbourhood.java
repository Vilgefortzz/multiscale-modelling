package vilgefortzz.edu.grain_growth.neighbourhood;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;

import java.util.List;

/**
 * Created by vilgefortzz on 07/10/18
 */
public abstract class Neighbourhood implements Neighbours {

    protected List<Cell> cells;

    public abstract List<Cell> listWithNeighbours(Grid grid, Cell cell);

    @Override
    public void addCellToNeighboursList(Cell cell) {

        if (cell != null){
            cells.add(cell);
        }
    }
}
