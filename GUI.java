import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class GUI extends JFrame {

    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 600;
    public static final String DEFAULT_TITLE = "Chinese Checker Game";
    private final Board board;

    public GUI() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE);
    }

    public GUI(int width, int height, String title) {

        // Setup the window
        super(title);
        super.setSize(width, height);
        super.setLocationByPlatform(true);

        // Setup the components
        JPanel layout = new JPanel(new BorderLayout());
        this.board = new Board(this);
        OptionPanel opts = new OptionPanel(this);
        layout.add(board, BorderLayout.CENTER);
        layout.add(opts, BorderLayout.SOUTH);
        this.add(layout);
    }

    public void restart() {
        this.board.getGame().restart();
        this.board.update();
    }

    public static void main(String[] args) {

        //Set the look and feel to the OS look and feel
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a window to display the checkers game
        GUI window = new GUI();
        window.setDefaultCloseOperation(GUI.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}