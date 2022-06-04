import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class OptionPanel extends JPanel {
    private final GUI window;
    private final JButton restartBtn;

    public OptionPanel(GUI window) {
        super(new GridLayout(0, 1));

        this.window = window;

        // Initialize the components
        OptionListener ol = new OptionListener();
        this.restartBtn = new JButton("Restart");
        this.restartBtn.addActionListener(ol);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel middle = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Add components to the layout
        top.add(restartBtn);
        this.add(top);
        this.add(middle);
        this.add(bottom);
    }

    private class OptionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // No window to update
            if (window == null) {
                return;
            }

            Object src = e.getSource();

            if (src == restartBtn) {
                window.restart();
            }
        }
    }
}