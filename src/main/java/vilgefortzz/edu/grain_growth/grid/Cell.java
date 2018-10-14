package vilgefortzz.edu.grain_growth.grid;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class Cell {

    private int x;
    private int y;
    private int phase;
    private int state;

    public Cell(int x, int y) {

        this.x = x;
        this.y = y;
        this.phase = 0;
        this.state = 0;
    }

    public Cell(int x, int y, int state) {

        this.x = x;
        this.y = y;
        this.phase = 0;
        this.state = state;
    }

    public Cell(int x, int y, int phase, int state) {

        this.x = x;
        this.y = y;
        this.phase = phase;
        this.state = state;
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
}
