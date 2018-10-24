package vilgefortzz.edu.grain_growth;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;
import vilgefortzz.edu.grain_growth.nucleating.Nucleating;

/**
 * Created by vilgefortzz on 08/10/18
 */
public class Solver {

    private Grid grid;

    private Growth growth;
    private Neighbourhood neighbourhood;
    private Nucleating nucleating;

    public void initialize() throws Exception {

        if (grid == null || growth == null || neighbourhood == null) {
            throw new Exception("Options are not set correctly");
        }

        growth.initialize(grid);
    }

    public void nucleating(int numberOfGrains) throws Exception {

        if (grid == null || growth == null || nucleating == null) {
            throw new Exception("Options are not set correctly");
        }

        nucleating.nucleating(growth, grid, numberOfGrains);
    }

    public void addInclusions(int amountOfInclusions, int sizeOfInclusion, int typeOfInclusion) throws Exception {

        if (grid == null || growth == null) {
            throw new Exception("Options are not set correctly");
        }

        grid.addInclusions(amountOfInclusions, sizeOfInclusion, typeOfInclusion, growth.isFinished());
    }

    public void switchState(Cell c){
        growth.changeState(c);
    }

    public void setGrowth(Growth growth){
        this.growth = growth;
    }

    public Growth getGrowth() {
        return growth;
    }

    public void setNeighbourhood(Neighbourhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

    public void setNucleating(Nucleating nucleating) {
        this.nucleating = nucleating;
    }

    public Nucleating getNucleating() {
        return nucleating;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    public Grid realizeStep() {

        growth.mark(grid, neighbourhood);
        return grid;
    }
}
