import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.Timer;

public class Board extends JButton {

    private static final int PADDING = 16;

    private final Game game;
    private GUI window;
    private Player player1;
    private Player player2;
    private Cell selected;
    private Cell lastValid;
    private final ArrayList<Cell> availableCells;
    private boolean isGameOver;
    private Timer timer;

    public Board(GUI window) {
        this(window, new Game(), new HumanPlayer(), new BotPlayer());
    }

    public Board(GUI window, Game game,
                 Player player1, Player player2) {

        // Setup the component
        super.setBorderPainted(false);
        super.setFocusPainted(false);
        super.setContentAreaFilled(false);
        super.setBackground(Color.LIGHT_GRAY);
        this.addActionListener(new ClickListener());

        // Setup the game
        this.game = (game == null) ? new Game() : game;
        this.window = window;
        this.selected = new Cell(0, 0);
        this.lastValid = new Cell(0, 0);
        this.availableCells = new ArrayList<>();
        setPlayer1(player1);
        setPlayer2(player2);
    }

    public void update() {
        runPlayer();
        this.isGameOver = game.gameOver();
        repaint();
    }

    private void runPlayer() {

        // Nothing to do
        Player player = getCurrentPlayer();

        // Set a timer to run
        this.timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getCurrentPlayer().updateGame(game);
                timer.stop();
                update();
            }
        });
        this.timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        Game game = this.game.copy();

        // Perform calculations
        final int CELL_SIZE = 40, X_OFFSET = 350, Y_OFFSET = 150;

//        // Draw checker board
        g.setColor(Color.WHITE);
        g.drawRect(X_OFFSET, Y_OFFSET, CELL_SIZE * Game.WIDTH, CELL_SIZE * Game.HEIGHT);
        g.fillRect(X_OFFSET, Y_OFFSET, CELL_SIZE * Game.WIDTH, CELL_SIZE * Game.HEIGHT);

        if (game.Turn == 1) {

            // Highlight the selected tile if valid
            if (game.board[selected.y][selected.x] == Game.HUMAN_PLAYER) {

                lastValid = selected;

                g.setColor(Color.GREEN);
                g.fillRect(X_OFFSET + selected.x * CELL_SIZE,
                        Y_OFFSET + selected.y * CELL_SIZE,
                        CELL_SIZE, CELL_SIZE);

                ArrayList<Cell> cells = game.getAvailableCells(1);

                availableCells.clear();

                for (Cell cell : cells) {
                    if (cell.x == selected.x && cell.y == selected.y) {
                        for (int i = 0; i < cell.AvailableCells.size(); i += 2) {
                            availableCells.add(new Cell(cell.AvailableCells.get(i), cell.AvailableCells.get(i + 1)));
                            g.setColor(Color.BLACK);
                            g.fillRect(X_OFFSET + cell.AvailableCells.get(i) * CELL_SIZE,
                                    Y_OFFSET + cell.AvailableCells.get(i + 1) * CELL_SIZE,
                                    CELL_SIZE, CELL_SIZE);
                        }
                    }
                }
            } else if (game.board[selected.y][selected.x] == Game.EMPTY) {
                for (Cell cell : availableCells) {
                    if (cell.x == selected.x && cell.y == selected.y) {
                        game.move(lastValid.x, lastValid.y, selected.x, selected.y);
                        availableCells.clear();
                        game.Turn = 0;
                        break;
                    }
                }
            }
        }

        // Draw the checkers
        for (int x = 0; x < Game.WIDTH; x++) {
            for (int y = 0; y < Game.HEIGHT; y++) {

                int cx = X_OFFSET + x * CELL_SIZE;
                int cy = Y_OFFSET + y * CELL_SIZE;

                switch (game.board[y][x]) {
                    case Game.INVALID:
                        g.setColor(Color.WHITE);
                        g.drawOval(cx, cy, CELL_SIZE, CELL_SIZE);
                        g.fillOval(cx, cy, CELL_SIZE, CELL_SIZE);

                        break;

                    case Game.EMPTY:
                        g.setColor(Color.GRAY);
                        g.drawOval(cx, cy, CELL_SIZE, CELL_SIZE);
                        g.fillOval(cx, cy, CELL_SIZE, CELL_SIZE);
                        break;

                    case Game.HUMAN_PLAYER:
                        g.setColor(Color.RED);
                        g.drawOval(cx, cy, CELL_SIZE, CELL_SIZE);
                        g.fillOval(cx, cy, CELL_SIZE, CELL_SIZE);
                        break;

                    case Game.BOT_PLAYER:
                        g.setColor(Color.BLUE);
                        g.drawOval(cx, cy, CELL_SIZE, CELL_SIZE);
                        g.fillOval(cx, cy, CELL_SIZE, CELL_SIZE);
                        break;
                }
            }
        }

        final int W = getWidth(), H = getHeight();
        final int DIM = Math.min(W, H), BOX_SIZE = (DIM - 2 * PADDING) / 8;

        // Draw the player turn sign
        String msg = game.Turn == 1 ? "human's turn" : "bot turn";
        int width = g.getFontMetrics().stringWidth(msg);
        Color back = Color.BLACK;
        Color front = Color.WHITE;
        g.setColor(back);
        g.fillRect(X_OFFSET + 450, Y_OFFSET - 50,
                width + 10, 15);
        g.setColor(front);
        g.drawString(msg, X_OFFSET + 455, Y_OFFSET - 40);

        // Draw a game over sign
        if (isGameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            msg = game.playerWon() ? "Human Won" : "Bot Won";
            width = g.getFontMetrics().stringWidth(msg);
            g.setColor(new Color(240, 240, 255));
            g.fillRoundRect(W / 2 - width / 2 - 5,
                    Y_OFFSET + BOX_SIZE * 4 - 16,
                    width + 10, 30, 10, 10);
            g.setColor(Color.RED);
            g.drawString(msg, W / 2 - width / 2, Y_OFFSET + BOX_SIZE * 4 + 7);
        }
    }

    public Game getGame() {
        return game;
    }

    public void setPlayer1(Player player1) {
        this.player1 = (player1 == null) ? new HumanPlayer() : player1;
        if (game.Turn == 1 && !this.player1.isHuman()) {
            this.selected = null;
        }
    }

    public void setPlayer2(Player player2) {
        this.player2 = (player2 == null) ? new BotPlayer() : player2;
        if (game.Turn == 0 && !this.player2.isHuman()) {
            this.selected = null;
        }
    }

    public Player getCurrentPlayer() {
        return game.Turn == 1 ? player1 : player2;
    }

    private void handleClick(int x, int y) {

        // The game is over or the current player isn't human
        if (isGameOver || !getCurrentPlayer().isHuman()) {
            return;
        }

        final int CELL_SIZE = 40, X_OFFSET = 350, Y_OFFSET = 150;

        x = (x - X_OFFSET) / CELL_SIZE;
        y = (y - Y_OFFSET) / CELL_SIZE;

        if (x < 0 || x >= Game.HEIGHT || y < 0 || y >= Game.WIDTH)
            return;

        selected = new Cell(x, y);

        update();
    }

    private class ClickListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // Get the new mouse coordinates and handle the click
            Point point = Board.this.getMousePosition();
            if (point != null) {
                handleClick(point.x, point.y);
            }
        }
    }
}