import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame implements ActionListener {

    private JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true;
    private int moves = 0;
    private JLabel statusLabel;

    public TicTacToe() {
        setTitle("TicTacToe – Joc X și 0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Turul jucătorului: X", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(statusLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        add(gridPanel, BorderLayout.CENTER);

        Font buttonFont = new Font("Arial", Font.BOLD, 60);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton b = new JButton();
                b.setFont(buttonFont);
                b.setFocusPainted(false);
                b.addActionListener(this);
                buttons[i][j] = b;
                gridPanel.add(b);
            }
        }

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetGame());
        add(resetButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if (!source.getText().equals("")) {
            return;
        }

        if (xTurn) {
            source.setText("X");
            statusLabel.setText("Turul jucătorului: 0");
        } else {
            source.setText("0");
            statusLabel.setText("Turul jucătorului: X");
        }

        moves++;
        if (checkWin()) {
            String winner = xTurn ? "X" : "0";
            statusLabel.setText("A câștigat: " + winner);
            disableBoard();
            int choice = JOptionPane.showConfirmDialog(this,
                    "A câștigat " + winner + ". Joci din nou?",
                    "Final joc",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            }
            return;
        }

        if (moves == 9) {
            statusLabel.setText("Egalitate!");
            int choice = JOptionPane.showConfirmDialog(this,
                    "Este egalitate. Joci din nou?",
                    "Final joc",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            }
            return;
        }

        xTurn = !xTurn;
    }

    private boolean checkWin() {
        String symbol = xTurn ? "X" : "0";

        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(symbol) &&
                    buttons[i][1].getText().equals(symbol) &&
                    buttons[i][2].getText().equals(symbol)) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (buttons[0][j].getText().equals(symbol) &&
                    buttons[1][j].getText().equals(symbol) &&
                    buttons[2][j].getText().equals(symbol)) {
                return true;
            }
        }

        if (buttons[0][0].getText().equals(symbol) &&
                buttons[1][1].getText().equals(symbol) &&
                buttons[2][2].getText().equals(symbol)) {
            return true;
        }

        if (buttons[0][2].getText().equals(symbol) &&
                buttons[1][1].getText().equals(symbol) &&
                buttons[2][0].getText().equals(symbol)) {
            return true;
        }

        return false;
    }

    private void resetGame() {
        xTurn = true;
        moves = 0;
        statusLabel.setText("Turul jucătorului: X");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }

    private void disableBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
