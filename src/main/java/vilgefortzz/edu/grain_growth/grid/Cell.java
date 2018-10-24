package vilgefortzz.edu.grain_growth.grid;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class Cell {

    public static final int INCLUSION_STATE = -1;
    public static final int INITIALIZE_STATE = 0;

    public static final int SQUARE_TYPE = 0;
    public static final int CIRCULAR_TYPE = 1;

    private int x;
    private int y;
    private int phase;
    private int state;

    private int type = SQUARE_TYPE;

    public Cell(int x, int y) {

        this.x = x;
        this.y = y;
        this.phase = 0;
        this.state = INITIALIZE_STATE;
    }

    public Cell(int x, int y, int state) {

        this.x = x;
        this.y = y;
        this.phase = 0;
        this.state = state;
    }

    public Cell(int x, int y, int phase, int state, int type) {

        this.x = x;
        this.y = y;
        this.phase = phase;
        this.state = state;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
