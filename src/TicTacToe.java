import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame implements ActionListener {

    private JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true;
    private int moves = 0;
    private int scoreX = 0;
    private int scoreO = 0;

    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JLabel trophyLabel;

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
        menuPanel.setBackground(new Color(40, 40, 40));

        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        startButton.setFont(new Font("Arial", Font.BOLD, 26));
        exitButton.setFont(new Font("Arial", Font.BOLD, 26));
        startButton.addActionListener(e -> showGame());
        exitButton.addActionListener(e -> System.exit(0));

        startButton.setFocusPainted(false);
        exitButton.setFocusPainted(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 0, 25, 0);
        gbc.gridy = 0;
        menuPanel.add(startButton, gbc);
        gbc.gridy = 1;
        menuPanel.add(exitButton, gbc);
    }

    private void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(30, 30, 30));

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setBackground(new Color(30, 30, 30));

        statusLabel = new JLabel("Turul jucătorului: X", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setForeground(Color.WHITE);

        scoreLabel = new JLabel("Scor: X = 0 | 0 = 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.YELLOW);

        topPanel.add(statusLabel);
        topPanel.add(scoreLabel);
        gamePanel.add(topPanel, BorderLayout.NORTH);

        // Layered pane pentru grid + trofeu
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(400, 400));

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        gridPanel.setBackground(new Color(50, 50, 50));
        gridPanel.setBounds(0, 0, 400, 400);
        layeredPane.add(gridPanel, Integer.valueOf(0));

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

        trophyLabel = new JLabel("🏆", SwingConstants.CENTER);
        trophyLabel.setFont(new Font("Arial", Font.BOLD, 150));
        trophyLabel.setForeground(Color.YELLOW);
        trophyLabel.setBounds(0, 100, 400, 200);
        trophyLabel.setVisible(false);

        layeredPane.add(trophyLabel, Integer.valueOf(1));
        gamePanel.add(layeredPane, BorderLayout.CENTER);

        JButton resetButton = new JButton("Reset Board");
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.addActionListener(e -> resetGame());
        resetButton.setBackground(new Color(90, 90, 90));
        resetButton.setForeground(Color.WHITE);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.add(resetButton);
        gamePanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void showMenu() {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "menu");
    }

    private void showGame() {
        resetGame();
        trophyLabel.setVisible(false);
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
            showWinner(winner);
            return;
        }

        if (moves == 9) {
            statusLabel.setText("Egalitate!");
            trophyLabel.setVisible(true);
            return;
        }

        xTurn = !xTurn;
    }

    private void showWinner(String winner) {
        statusLabel.setText("A câștigat: " + winner);
        disableBoard();
        trophyLabel.setVisible(true);

        if (winner.equals("X")) scoreX++;
        else scoreO++;
        scoreLabel.setText("Scor: X = " + scoreX + " | 0 = " + scoreO);

        colorWinLine(winner);
    }

    private void colorWinLine(String winner) {
        Color winColor = new Color(255, 200, 0);

        for (int i = 0; i < 3; i++)
            if (buttons[i][0].getText().equals(winner) &&
                    buttons[i][1].getText().equals(winner) &&
                    buttons[i][2].getText().equals(winner))
            {
                buttons[i][0].setBackground(winColor);
                buttons[i][1].setBackground(winColor);
                buttons[i][2].setBackground(winColor);
                return;
            }

        for (int j = 0; j < 3; j++)
            if (buttons[0][j].getText().equals(winner) &&
                    buttons[1][j].getText().equals(winner) &&
                    buttons[2][j].getText().equals(winner))
            {
                buttons[0][j].setBackground(winColor);
                buttons[1][j].setBackground(winColor);
                buttons[2][j].setBackground(winColor);
                return;
            }

        if (buttons[0][0].getText().equals(winner) &&
                buttons[1][1].getText().equals(winner) &&
                buttons[2][2].getText().equals(winner))
        {
            buttons[0][0].setBackground(winColor);
            buttons[1][1].setBackground(winColor);
            buttons[2][2].setBackground(winColor);
        }

        if (buttons[0][2].getText().equals(winner) &&
                buttons[1][1].getText().equals(winner) &&
                buttons[2][0].getText().equals(winner))
        {
            buttons[0][2].setBackground(winColor);
            buttons[1][1].setBackground(winColor);
            buttons[2][0].setBackground(winColor);
        }
    }

    private boolean checkWin() {
        String s = xTurn ? "X" : "0";

        for (int i = 0; i < 3; i++)
            if (buttons[i][0].getText().equals(s) &&
                    buttons[i][1].getText().equals(s) &&
                    buttons[i][2].getText().equals(s)) return true;

        for (int j = 0; j < 3; j++)
            if (buttons[0][j].getText().equals(s) &&
                    buttons[1][j].getText().equals(s) &&
                    buttons[2][j].getText().equals(s)) return true;

        return (buttons[0][0].getText().equals(s) &&
                buttons[1][1].getText().equals(s) &&
                buttons[2][2].getText().equals(s))
                ||
                (buttons[0][2].getText().equals(s) &&
                        buttons[1][1].getText().equals(s) &&
                        buttons[2][0].getText().equals(s));
    }

    private void resetGame() {
        moves = 0;
        xTurn = true;
        trophyLabel.setVisible(false);
        statusLabel.setText("Turul jucătorului: X");

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(new Color(60, 60, 60));
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
