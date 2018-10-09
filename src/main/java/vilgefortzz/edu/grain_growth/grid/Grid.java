package vilgefortzz.edu.grain_growth.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class Grid {

    private List<Cell> grid;
    private int width;
    private int height;

    private final boolean cyclic = true;

    public Grid(int width, int height) {

        this.width = width;
        this.height = height;

        initailizeGrid();
    }

    public Grid(Grid existingGrid) {

        this.width = existingGrid.width;
        this.height = existingGrid.height;

        this.grid = new ArrayList<>();
        for (Cell cell: existingGrid.grid) {
            Cell nc = new Cell(cell.getX(), cell.getY());
            nc.setState(cell.getState());

            grid.add(nc);
        }
    }

    private void initailizeGrid() {

        grid = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++){
                grid.add(new Cell(x, y));
            }
        }
    }

    private Cell getCellCyclic(int x, int y) {

        if (x >= width) x -= width;
        if (y >= height) y -= height;
        if (x < 0) x += width;
        if (y < 0) y += height;

        return getCellBasic(x, y);
    }

    private Cell getCellBasic(int x, int y) {

        if (x >= width || y >= height) return null;
        if (x < 0 || y < 0) return null;

        return grid.get(y * width + x);
    }

    public void forEach(Consumer<Cell> f){
        grid.forEach(f);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int x, int y) {
        return getCellCyclic(x, y);
    }
}
