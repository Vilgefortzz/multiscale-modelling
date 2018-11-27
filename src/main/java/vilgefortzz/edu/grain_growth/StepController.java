package vilgefortzz.edu.grain_growth;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import vilgefortzz.edu.grain_growth.grid.Grid;

/**
 * Created by vilgefortzz on 08/10/18
 */
public class StepController extends ScheduledService<Grid> {

    private Solver solver;
    private int step;

    public void initialize() {
        step = 0;
    }

    public int getStep() {
        return step;
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    public boolean isFinished() {
        return solver.getGrowth().isFinished();
    }

    public void setFinished(boolean isFinished) {
        this.solver.getGrowth().setFinished(isFinished);
    }

    public void clearStep() {
        this.step = 0;
    }

    @Override
    protected Task<Grid> createTask() {
        return new Task<Grid>() {
            @Override
            protected Grid call() throws Exception {

                if (isFinished()) cancel();
                step++;

                return solver.realizeStep();
            }
        };
    }

    public String prepareData() {
        return solver.getGrowth().isFinished() + " " + solver.getGrowth().getType();
    }
}
