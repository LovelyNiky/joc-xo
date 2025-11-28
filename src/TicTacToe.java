import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame implements ActionListener {

    private JButton[][] buttons;
    private int boardSize = 3;
    private int winLength = 3;

    private boolean xTurn = true;
    private int moves = 0;
    private int scoreX = 0;
    private int scoreO = 0;

    private JLabel statusLabel;
    private JLabel scoreLabel;

    private JPanel menuPanel;
    private JPanel gamePanel;
    private JPanel gridPanel;

    public TicTacToe() {
        setTitle("TicTacToe – Joc X și 0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(new Color(40, 40, 40));

        JButton standardButton = new JButton("Standard 3 x 3");
        JButton bigButton = new JButton("Big 9 x 9 (4 in linie)");
        JButton exitButton = new JButton("Exit");

        Font menuFont = new Font("Arial", Font.BOLD, 22);
        standardButton.setFont(menuFont);
        bigButton.setFont(menuFont);
        exitButton.setFont(menuFont);

        standardButton.addActionListener(e -> startStandardMode());
        bigButton.addActionListener(e -> startBigMode());
        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);

        gbc.gridy = 0;
        menuPanel.add(standardButton, gbc);

        gbc.gridy = 1;
        menuPanel.add(bigButton, gbc);

        gbc.gridy = 2;
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

        gridPanel = new JPanel();
        gamePanel.add(gridPanel, BorderLayout.CENTER);

        JButton resetButton = new JButton("Reset Board");
        JButton backButton = new JButton("Back to Menu");

        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setFont(new Font("Arial", Font.BOLD, 16));

        resetButton.setBackground(new Color(90, 90, 90));
        resetButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(90, 90, 90));
        backButton.setForeground(Color.WHITE);

        resetButton.addActionListener(e -> resetGame());
        backButton.addActionListener(e -> showMenu());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.add(resetButton);
        bottomPanel.add(backButton);

        gamePanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void buildBoard() {
        gridPanel.removeAll();
        gridPanel.setBackground(new Color(50, 50, 50));
        gridPanel.setLayout(new GridLayout(boardSize, boardSize));

        buttons = new JButton[boardSize][boardSize];

        int fontSize = (boardSize == 3) ? 60 : 24;
        Font buttonFont = new Font("Arial", Font.BOLD, fontSize);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JButton b = new JButton();
                b.setFont(buttonFont);
                b.setBackground(new Color(60, 60, 60));
                b.setForeground(Color.WHITE);
                b.setFocusPainted(false);
                b.addActionListener(this);
                buttons[i][j] = b;
                gridPanel.add(b);
            }
        }

        moves = 0;
        xTurn = true;
        statusLabel.setText("Turul jucătorului: X");

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void startStandardMode() {
        boardSize = 3;
        winLength = 3;
        setSize(450, 520);
        setLocationRelativeTo(null);
        buildBoard();
        showGame();
    }

    private void startBigMode() {
        boardSize = 9;
        winLength = 4;
        setSize(900, 950);
        setLocationRelativeTo(null);
        buildBoard();
        showGame();
    }

    private void showMenu() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "menu");
    }

    private void showGame() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "game");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if (!source.getText().equals("")) {
            return;
        }

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

        String currentSymbol = xTurn ? "X" : "0";

        if (checkWinAndHighlight(currentSymbol)) {
            showWinner(currentSymbol);
            return;
        }

        if (moves == boardSize * boardSize) {
            statusLabel.setText("Egalitate!");
            disableBoard();
            return;
        }

        xTurn = !xTurn;
    }

    private void showWinner(String winner) {
        statusLabel.setText("A câștigat: " + winner);
        disableBoard();

        if (winner.equals("X")) {
            scoreX++;
        } else {
            scoreO++;
        }
        scoreLabel.setText("Scor: X = " + scoreX + " | 0 = " + scoreO);
    }

    private boolean checkWinAndHighlight(String s) {
        int[][] directions = {
                {1, 0},  // jos
                {0, 1},  // dreapta
                {1, 1},  // diagonală principală
                {1, -1}  // diagonală secundară
        };

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!buttons[i][j].getText().equals(s)) continue;

                for (int[] d : directions) {
                    int dr = d[0];
                    int dc = d[1];

                    int count = 1;
                    int r = i + dr;
                    int c = j + dc;

                    while (r >= 0 && r < boardSize &&
                            c >= 0 && c < boardSize &&
                            buttons[r][c].getText().equals(s)) {
                        count++;
                        r += dr;
                        c += dc;
                    }

                    if (count >= winLength) {
                        highlightLine(i, j, dr, dc, s);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void highlightLine(int startRow, int startCol, int dr, int dc, String s) {
        Color winColor = new Color(255, 200, 0);
        int r = startRow;
        int c = startCol;
        int highlighted = 0;

        while (r >= 0 && r < boardSize &&
                c >= 0 && c < boardSize &&
                buttons[r][c].getText().equals(s) &&
                highlighted < winLength) {
            buttons[r][c].setBackground(winColor);
            highlighted++;
            r += dr;
            c += dc;
        }
    }

    private void resetGame() {
        buildBoard();
    }

    private void disableBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
