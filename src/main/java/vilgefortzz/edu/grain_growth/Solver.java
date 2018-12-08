package vilgefortzz.edu.grain_growth;

import vilgefortzz.edu.grain_growth.energy_distribution.EnergyDistribution;
import vilgefortzz.edu.grain_growth.grain.GrainSelection;
import vilgefortzz.edu.grain_growth.grid.Cell;
import vilgefortzz.edu.grain_growth.grid.Grid;
import vilgefortzz.edu.grain_growth.growth.Growth;
import vilgefortzz.edu.grain_growth.neighbourhood.Neighbourhood;
import vilgefortzz.edu.grain_growth.nucleating.Nucleating;
import vilgefortzz.edu.grain_growth.structure.Structure;

import java.util.List;

/**
 * Created by vilgefortzz on 08/10/18
 */
public class Solver {

    private Grid grid;

    private Growth growth;
    private Neighbourhood neighbourhood;
    private Nucleating nucleating;
    private Structure structure;
    private GrainSelection grainSelection;

    /**
     * MC Static recrystalization
     */
    private EnergyDistribution energyDistribution;

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

    public List<Cell> selectGrains(int numberOfStructures) throws Exception {

        if (grid == null || growth == null) {
            throw new Exception("Options are not set correctly");
        }

        return structure.selectGrains(growth, grid, numberOfStructures);
    }

    public List<Cell> selectEdgeGrains(int numberOfGrains) throws Exception {

        if (grid == null || growth == null) {
            throw new Exception("Options are not set correctly");
        }

        return grainSelection.selectEdgeGrains(growth, grid, numberOfGrains);
    }

    public void showEnergy(int energyInside, int energyOnEdges) throws Exception {

        if (grid == null || growth == null) {
            throw new Exception("Options are not set correctly");
        }

        energyDistribution.showEnergy(growth, grid, energyInside, energyOnEdges);
    }

    public void switchState(Cell c){
        growth.changeState(c);
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Growth getGrowth() {
        return growth;
    }

    public void setGrowth(Growth growth){
        this.growth = growth;
    }

    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(Neighbourhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public Nucleating getNucleating() {
        return nucleating;
    }

    public void setNucleating(Nucleating nucleating) {
        this.nucleating = nucleating;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public GrainSelection getGrainSelection() {
        return grainSelection;
    }

    public void setGrainSelection(GrainSelection grainSelection) {
        this.grainSelection = grainSelection;
    }

    public EnergyDistribution getEnergyDistribution() {
        return energyDistribution;
    }

    public void setEnergyDistribution(EnergyDistribution energyDistribution) {
        this.energyDistribution = energyDistribution;
    }

    public Grid realizeStep() {

        growth.mark(grid, neighbourhood);
        return grid;
    }
}
