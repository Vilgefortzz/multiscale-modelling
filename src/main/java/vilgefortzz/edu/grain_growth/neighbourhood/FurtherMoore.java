package vilgefortzz.edu.grain_growth.neighbourhood;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vilgefortzz on 25/10/18
 */
public class FurtherMoore extends Neighbourhood {

    public FurtherMoore() {
        cells = new ArrayList<>();
    }

    public List<Cell> listWithNeighbours(Grid grid, Cell cell) {

        cells.clear();

        int cellX = cell.getX();
        int cellY = cell.getY();

        addCellToNeighboursList(grid.getCell(cellX + 1, cellY + 1));
        addCellToNeighboursList(grid.getCell(cellX - 1, cellY + 1));
        addCellToNeighboursList(grid.getCell(cellX + 1, cellY - 1));
        addCellToNeighboursList(grid.getCell(cellX - 1, cellY - 1));

        return cells;
    }

    @Override
    public String toString() {
        return "Further Moore";
    }
}
