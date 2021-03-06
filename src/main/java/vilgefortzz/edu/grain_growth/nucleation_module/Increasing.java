package vilgefortzz.edu.grain_growth.nucleation_module;

import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;

/**
 * Created by vilgefortzz on 15/12/18
 */
public class Increasing extends NucleationModule {

    @Override
    public void addNucleons(Growth growth, Grid grid) {

        nucleating.nucleating(growth, grid, numberOfGrains);
        numberOfGrains += increasing;
    }

    @Override
    public String toString() {
        return "Increasing";
    }
}
