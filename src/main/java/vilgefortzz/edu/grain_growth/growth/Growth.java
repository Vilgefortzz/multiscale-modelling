package vilgefortzz.edu.grain_growth.growth;

/**
 * Created by vilgefortzz on 07/10/18
 */
public abstract class Growth implements Algorithm {

    public abstract boolean isFinished();
    public abstract void setFinished(boolean finished);
    public abstract int getType();
    public abstract void setType(int type);
}
