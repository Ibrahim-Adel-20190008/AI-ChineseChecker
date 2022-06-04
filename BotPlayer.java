import java.util.ArrayList;

public class BotPlayer extends Player {
    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public void updateGame(Game game) {

        // Nothing to do
        if (game == null || game.gameOver()) {
            return;
        }

        ArrayList<Cell> cells = game.getAvailableCells(0);
        for (Cell cell : cells) {
            if (!cell.AvailableCells.isEmpty()) {
                game.move(cell.x, cell.y, cell.AvailableCells.get(0), cell.AvailableCells.get(1));
                game.Turn = 1;
                break;
            }
        }
    }
}