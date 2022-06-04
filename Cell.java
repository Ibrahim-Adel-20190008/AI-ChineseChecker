import java.util.ArrayList;

public class Cell {
    public int x, y;
    public ArrayList<Integer> AvailableCells;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        AvailableCells = new ArrayList<>();
    }
}