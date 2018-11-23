package vilgefortzz.edu.grain_growth.growth;

/**
 * Created by vilgefortzz on 07/10/18
 */
public abstract class Growth implements Algorithm {

    protected boolean finished;
    protected boolean changed;
    protected int type;
    protected int probability;

    /**
     * Monte Carlo grain boundary energy
     */
    protected double grainBoundaryEnergy;

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public double getGrainBoundaryEnergy() {
        return grainBoundaryEnergy;
    }

    public void setGrainBoundaryEnergy(double grainBoundaryEnergy) {
        this.grainBoundaryEnergy = grainBoundaryEnergy;
    }

    protected int createNewType() {
        return ++type;
    }
}
