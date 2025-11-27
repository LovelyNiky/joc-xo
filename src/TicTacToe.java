import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame implements ActionListener {

    private JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true;
    private int moves = 0;
    private JLabel statusLabel;

    private JPanel menuPanel;
    private JPanel gamePanel;

    public TicTacToe() {
        setTitle("TicTacToe – Joc X și 0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 520);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

        createMenuPanel();
        createGamePanel();

        add(menuPanel, "menu");
        add(gamePanel, "game");

        showMenu();
        setVisible(true);
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(new Color(40, 40, 40)); // fundal întunecat

        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        startButton.setFont(new Font("Arial", Font.BOLD, 26));
        exitButton.setFont(new Font("Arial", Font.BOLD, 26));

        startButton.setFocusPainted(false);
        exitButton.setFocusPainted(false);

        startButton.addActionListener(e -> showGame());
        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);

        gbc.gridy = 0;
        menuPanel.add(startButton, gbc);

        gbc.gridy = 1;
        menuPanel.add(exitButton, gbc);
    }

    private void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(30, 30, 30));

        statusLabel = new JLabel("Turul jucătorului: X", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 22));
        statusLabel.setForeground(Color.WHITE);
        gamePanel.add(statusLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        gridPanel.setBackground(new Color(50, 50, 50));
        gamePanel.add(gridPanel, BorderLayout.CENTER);

        Font buttonFont = new Font("Arial", Font.BOLD, 60);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton b = new JButton();
                b.setFont(buttonFont);
                b.setFocusPainted(false);
                b.setBackground(new Color(60, 60, 60));
                b.setForeground(Color.WHITE);
                b.addActionListener(this);
                buttons[i][j] = b;
                gridPanel.add(b);
            }
        }

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetGame());
        resetButton.setFont(new Font("Arial", Font.BOLD, 20));
        resetButton.setBackground(new Color(90, 90, 90));
        resetButton.setForeground(Color.WHITE);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(resetButton);

        gamePanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void showMenu() {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "menu");
    }

    private void showGame() {
        resetGame();
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "game");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if (!source.getText().equals("")) return;

        if (xTurn) {
            source.setText("X");
            source.setForeground(Color.RED);
            statusLabel.setText("Turul jucătorului: 0");
        } else {
            source.setText("0");
            source.setForeground(Color.CYAN);
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
            if (choice == JOptionPane.YES_OPTION) resetGame();
            else showMenu();
            return;
        }

        if (moves == 9) {
            statusLabel.setText("Egalitate!");
            int choice = JOptionPane.showConfirmDialog(this,
                    "Este egalitate. Joci din nou?",
                    "Final joc",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) resetGame();
            else showMenu();
            return;
        }

        xTurn = !xTurn;
    }

    private boolean checkWin() {
        String symbol = xTurn ? "X" : "0";

        for (int i = 0; i < 3; i++)
            if (buttons[i][0].getText().equals(symbol) &&
                    buttons[i][1].getText().equals(symbol) &&
                    buttons[i][2].getText().equals(symbol))
                return true;

        for (int j = 0; j < 3; j++)
            if (buttons[0][j].getText().equals(symbol) &&
                    buttons[1][j].getText().equals(symbol) &&
                    buttons[2][j].getText().equals(symbol))
                return true;

        return (buttons[0][0].getText().equals(symbol) &&
                buttons[1][1].getText().equals(symbol) &&
                buttons[2][2].getText().equals(symbol))
                ||
                (buttons[0][2].getText().equals(symbol) &&
                        buttons[1][1].getText().equals(symbol) &&
                        buttons[2][0].getText().equals(symbol));
    }

    private void resetGame() {
        xTurn = true;
        moves = 0;
        statusLabel.setText("Turul jucătorului: X");

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setForeground(Color.WHITE);
            }
    }

    private void disableBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                buttons[i][j].setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
