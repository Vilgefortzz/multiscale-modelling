package vilgefortzz.edu.grain_growth.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class Grid {

    private int width;
    private int height;
    private List<Cell> cells;

    private final boolean isCyclic = true;
    private boolean isCircular;

    public Grid(int width, int height, boolean isCircular) {

        this.width = width;
        this.height = height;
        this.isCircular = isCircular;

        initializeGrid();
    }

    public Grid(Grid existingGrid) {

        this.width = existingGrid.width;
        this.height = existingGrid.height;
        this.cells = new ArrayList<>();

        for (Cell cell : existingGrid.cells) {

            Cell newCell = new Cell(cell.getX(), cell.getY());
            newCell.setState(cell.getState());
            newCell.setChangable(cell.isChangable());

            cells.add(newCell);
        }
    }

    public Grid(int width, int height, boolean isCircular, List<Cell> cells) {

        this.width = width;
        this.height = height;
        this.isCircular = isCircular;

        this.cells = cells;
    }

    public Grid(int width, int height, boolean isCircular, int numberOfStates) {

        this.width = width;
        this.height = height;
        this.isCircular = isCircular;

        initializeGrid(numberOfStates);
    }

    public Grid(int width, int height, boolean isCircular, int numberOfStates, List<Cell> cells) {

        this.width = width;
        this.height = height;
        this.isCircular = isCircular;

        initializeGrid(numberOfStates, cells);
    }

    private void initializeGrid() {

        cells = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells.add(new Cell(x, y));
            }
        }
    }

    private void initializeGrid(int numberOfStates) {

        Random random = new Random();
        cells = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int randomState = random.nextInt(numberOfStates) + 1;
                cells.add(new Cell(x, y, randomState));
            }
        }
    }

    private void initializeGrid(int numberOfStates, List<Cell> structureCells) {

        Random random = new Random();
        cells = new ArrayList<>();

        structureCells.forEach(structureCell -> {
            if (structureCell.getState() == Cell.STRUCTURE_STATE) {
                cells.add(new Cell(structureCell.getX(), structureCell.getY(), Cell.STRUCTURE_STATE));
            } else {
                int randomState = random.nextInt(numberOfStates) + 1;
                cells.add(new Cell(structureCell.getX(), structureCell.getY(), randomState));
            }
        });
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

    public void addInclusions(int amountOfInclusions, int sizeOfInclusion,
                              int typeOfInclusion, boolean onEdges) {

        Random random = new Random();

        if (onEdges) {

            List<Cell> edgeCells = getEdgeCells();

            for (int i = 0; i < amountOfInclusions; i++) {

                int randomKey = random.nextInt(edgeCells.size());
                Cell cell = edgeCells.get(randomKey);
                addInclusion(cell.getX(), cell.getY(), sizeOfInclusion, typeOfInclusion);
            }
        } else {

            for (int i = 0; i < amountOfInclusions; i++) {

                int x = random.nextInt(width);
                int y = random.nextInt(height);
                addInclusion(x, y, sizeOfInclusion, typeOfInclusion);
            }
        }
    }

    private void addInclusion(int x, int y, int sizeOfInclusion, int typeOfInclusion) {

        if (typeOfInclusion == Cell.CIRCULAR_TYPE) {
            addCircularInclusion(x, y, sizeOfInclusion);
        } else {
            addSquareInclusion(x, y, sizeOfInclusion);
        }
    }

    private void addSquareInclusion(int x, int y, int sizeOfInclusion) {

        for (int j = 0; j < sizeOfInclusion; j++) {
            for (int k = 0; k < sizeOfInclusion; k++) {

                Cell cell = getCell(x + j, y + k);

                if (cell != null) {
                    cell.setState(Cell.INCLUSION_STATE);
                    cell.setType(Cell.SQUARE_TYPE);
                }
            }
        }
    }

    private void addCircularInclusion(int x, int y, int sizeOfInclusion) {

        Cell chosenCell = getCell(x, y);
        chosenCell.setState(Cell.INCLUSION_STATE);
        chosenCell.setType(Cell.CIRCULAR_TYPE);

        forEachCells(cell -> {
            double twoPointsDistance = Math.sqrt(Math.pow(cell.getX() - chosenCell.getX(), 2) +
                    Math.pow(cell.getY() - chosenCell.getY(), 2));

            if (twoPointsDistance <= sizeOfInclusion) {
                cell.setState(Cell.INCLUSION_STATE);
                cell.setType(Cell.CIRCULAR_TYPE);
            }
        });
    }

    public List<Cell> getCells() {
        return cells;
    }

    public List<Cell> getEdgeCells(List<Cell> edgeCells, int state) {
        return edgeCells.stream().filter(cell -> cell.getState() == state).collect(Collectors.toList());
    }

    public List<Cell> getEdgeCells() {

        List<Cell> edgeCells = new ArrayList<>();

        forEachCells(cell -> {

            List<Cell> neighbours = new ArrayList<>();

            neighbours.add(getCell(cell.getX() + 1, cell.getY()));
            neighbours.add(getCell(cell.getX(), cell.getY() + 1));
            neighbours.add(getCell(cell.getX() + 1, cell.getY() + 1));

            if (neighbours.stream().anyMatch(neighbour -> neighbour.getState() != cell.getState())) {
                edgeCells.add(cell);
            }
        });

        return edgeCells;
    }

    public List<Cell> getEdgeSelectionCells() {

        List<Cell> edgeCells = new ArrayList<>();

        forEachCells(cell -> {

            List<Cell> neighbours = new ArrayList<>();

            neighbours.add(getCell(cell.getX() + 1, cell.getY()));
            neighbours.add(getCell(cell.getX() - 1, cell.getY()));

            neighbours.add(getCell(cell.getX(), cell.getY() + 1));
            neighbours.add(getCell(cell.getX(), cell.getY() - 1));

            neighbours.add(getCell(cell.getX() + 1, cell.getY() + 1));
            neighbours.add(getCell(cell.getX() - 1, cell.getY() + 1));
            neighbours.add(getCell(cell.getX() + 1, cell.getY() - 1));
            neighbours.add(getCell(cell.getX() - 1, cell.getY() - 1));

            if (neighbours.stream().anyMatch(neighbour -> neighbour.getState() != cell.getState())) {
                edgeCells.add(cell);
            }
        });

        return edgeCells;
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

    public int getSize(){
        return width * height;
    }

    public boolean isCircular() {
        return isCircular;
    }

    public Cell getCell(int x, int y) {
        return getCellCyclic(x, y);
    }

    public List<String> prepareData() {

        List<String> data = new ArrayList<>();

        // Grid dimensions + type
        data.add(Integer.toString(width) + " " + height + " " + isCircular);

        // Cell values
        forEachCells(cell -> data.add(
                Integer.toString(cell.getX()) + " "
                        + cell.getY() + " "
                        + cell.getPhase() + " "
                        + cell.getState() + " "
                        + cell.getType()
        ));

        return data;
    }
}
