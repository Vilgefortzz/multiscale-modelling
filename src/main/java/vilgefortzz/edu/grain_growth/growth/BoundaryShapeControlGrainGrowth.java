package vilgefortzz.edu.grain_growth.growth;

import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;

/**
 * Created by vilgefortzz on 25/10/18
 */
public class BoundaryShapeControlGrainGrowth extends Growth {

    private boolean finished;
    private boolean changed;
    private int type;

    @Override
    public void initialize(Grid grid) {

        finished = false;
        type = 0;
    }

    @Override
    public void mark(Grid grid, Neighbourhood neighbourhood) {

    }

    @Override
    public void changeState(Cell cell) {

    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Boundary shape control grain growth";
    }
}
