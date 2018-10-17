package vilgefortzz.edu.grain_growth;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import vilgefortzz.edu.grain_growth.grid.Grid;

/**
 * Created by vilgefortzz on 08/10/18
 */
public class StepController extends ScheduledService<Grid> {

    private Solver solver;
    private int iteration;

    public void initialize() {
        iteration = 0;
    }

    public int getIteration() {
        return iteration;
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    public boolean isFinished() {
        return solver.getGrowth().isFinished();
    }

    @Override
    protected Task<Grid> createTask() {
        return new Task<Grid>() {
            @Override
            protected Grid call() throws Exception {

                if (isFinished()) cancel();
                iteration++;

                return solver.realizeStep();
            }
        };
    }

    public String prepareData() {
        return solver.getGrowth().isFinished() + " " + solver.getGrowth().getType();
    }
}
