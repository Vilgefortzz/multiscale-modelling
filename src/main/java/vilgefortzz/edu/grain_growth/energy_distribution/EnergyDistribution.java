package vilgefortzz.edu.grain_growth.energy_distribution;

import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;

/**
 * Created by vilgefortzz on 8/12/18
 */
public interface EnergyDistribution {

    void calculateEnergy(Growth growth, Grid grid, int energyInside, int energyOnEdges);
    void showEnergy(Growth growth, Grid grid, int energyInside, int energyOnEdges);
    void showMicrostructure(Growth growth, Grid grid, int energyInside, int energyOnEdges);
}
