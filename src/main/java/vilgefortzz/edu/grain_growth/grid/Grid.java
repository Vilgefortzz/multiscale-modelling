package vilgefortzz.edu.grain_growth.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class Grid {

    private int width;
    private int height;
    private List<Cell> cells;

    private final boolean cyclic = true;

    public Grid(int width, int height) {

        this.width = width;
        this.height = height;

        initailizeGrid();
    }

    public Grid(Grid existingGrid) {

        this.width = existingGrid.width;
        this.height = existingGrid.height;

        this.cells = new ArrayList<>();
        for (Cell cell : existingGrid.cells) {
            Cell nc = new Cell(cell.getX(), cell.getY());
            nc.setState(cell.getState());

            cells.add(nc);
        }
    }

    public Grid(int width, int height, List<Cell> cells) {

        this.width = width;
        this.height = height;
        this.cells = cells;
    }

    private void initailizeGrid() {

        cells = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells.add(new Cell(x, y));
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

        return cells.get(y * width + x);
    }

    public void addInclusions(int amountOfInclusions, int typeOfInclusion) {

        Random random = new Random();

        int x, y;
        Cell cell;

        for (int i = 0; i < amountOfInclusions; i++) {

            x = random.nextInt(width);
            y = random.nextInt(height);

            cell = getCell(x, y);
            cell.setState(Cell.INCLUSION_STATE);
            cell.setType(typeOfInclusion);
        }
    }

    public void forEachCells(Consumer<Cell> cell) {
        cells.forEach(cell);
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

    public List<String> prepareData() {

        List<String> data = new ArrayList<>();

        // Grid dimensions
        data.add(Integer.toString(width) + " " + height);

        // Cell values
        forEachCells(cell -> data.add(
                Integer.toString(cell.getX()) + " "
                        + cell.getY() + " "
                        + cell.getPhase() + " "
                        + cell.getState()
        ));

        return data;
    }
}
