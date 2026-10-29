import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TicTacToe extends JFrame {

    char[] board = new char[9];
    char currentPlayer = 'X';

    boolean gameOver = false;
    boolean computerThinking = false;

    int xWins = 0, oWins = 0, draws = 0;

    String[] modes = {
        "Human vs Computer",
        "Human vs Human"
    };

    String[] levels = {
        "Easy",
        "Medium",
        "Hard"
    };

    String[] themes = {
        "Neon Gaming",
        "Cyberpunk",
        "Minimal Premium"
    };

    JComboBox<String> modeBox = new JComboBox<>(modes);
    JComboBox<String> levelBox = new JComboBox<>(levels);
    JComboBox<String> themeBox = new JComboBox<>(themes);

    JLabel status = new JLabel("", SwingConstants.CENTER);
    JLabel score = new JLabel("", SwingConstants.CENTER);

    JButton restartButton = new JButton("New Round");
    JButton resetButton = new JButton("Reset Scores");

    BoardPanel boardPanel = new BoardPanel();

    Random random = new Random();

    Color background;
    Color tile;
    Color xColor;
    Color oColor;
    Color accent;
    Color textColor;
    Color mutedColor;

    int[] winningLine = null;

    public TicTacToe() {

        setTitle("Tic Tac Toe | Ultimate Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(620, 790);
        setMinimumSize(new Dimension(480, 650));
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(0, 0));

        applyTheme();

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(BorderFactory.createEmptyBorder(22, 25, 22, 25));
        main.setOpaque(false);

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 5));
        header.setOpaque(false);

        JLabel title = new JLabel(
            "TIC TAC TOE",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(accent);

        JLabel subtitle = new JLabel(
            "THE ULTIMATE EDITION",
            SwingConstants.CENTER
        );

        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(mutedColor);

        header.add(title);
        header.add(subtitle);

        // Settings
        JPanel settings = new JPanel(new GridLayout(2, 3, 10, 6));
        settings.setOpaque(false);

        addSetting(settings, "GAME MODE", modeBox);
        addSetting(settings, "DIFFICULTY", levelBox);
        addSetting(settings, "THEME", themeBox);

        // Status and scoreboard
        JPanel info = new JPanel(new GridLayout(2, 1, 0, 7));
        info.setOpaque(false);

        status.setFont(new Font("Segoe UI", Font.BOLD, 17));
        status.setForeground(textColor);

        score.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        score.setForeground(mutedColor);

        info.add(status);
        info.add(score);

        // Controls
        JPanel controls = new JPanel(new GridLayout(1, 2, 12, 0));
        controls.setOpaque(false);

        styleButton(restartButton);
        styleButton(resetButton);

        controls.add(restartButton);
        controls.add(resetButton);

        JPanel top = new JPanel(new BorderLayout(0, 18));
        top.setOpaque(false);

        top.add(header, BorderLayout.NORTH);

        JPanel topMiddle = new JPanel(new BorderLayout(0, 18));
        topMiddle.setOpaque(false);
        topMiddle.add(settings, BorderLayout.NORTH);
        topMiddle.add(info, BorderLayout.SOUTH);

        top.add(topMiddle, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(boardPanel, BorderLayout.CENTER);
        main.add(controls, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);

        modeBox.addActionListener(e -> {
            updateDifficultyAvailability();
            newRound();
        });

        levelBox.addActionListener(e -> newRound());

        themeBox.addActionListener(e -> {
            applyTheme();
            refreshColors();
            repaint();
        });

        restartButton.addActionListener(e -> newRound());

        resetButton.addActionListener(e -> {
            xWins = 0;
            oWins = 0;
            draws = 0;
            updateScore();
            newRound();
        });

        updateDifficultyAvailability();
        updateScore();
        newRound();

        setVisible(true);
    }

    void addSetting(
        JPanel panel,
        String labelText,
        JComboBox<String> combo
    ) {
        JPanel box = new JPanel(new BorderLayout(0, 5));
        box.setOpaque(false);

        JLabel label = new JLabel(
            labelText,
            SwingConstants.CENTER
        );

        label.setForeground(mutedColor);
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));

        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        box.add(label, BorderLayout.NORTH);
        box.add(combo, BorderLayout.CENTER);

        panel.add(box);
    }

    void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));
    }

    void applyTheme() {

        String selected = (String) themeBox.getSelectedItem();

        if ("Cyberpunk".equals(selected)) {

            background = new Color(20, 10, 28);
            tile = new Color(42, 20, 48);
            xColor = new Color(255, 65, 170);
            oColor = new Color(65, 235, 255);
            accent = new Color(255, 65, 170);
            textColor = new Color(255, 245, 255);
            mutedColor = new Color(190, 145, 205);

        } else if ("Minimal Premium".equals(selected)) {

            background = new Color(242, 245, 249);
            tile = Color.WHITE;
            xColor = new Color(35, 100, 210);
            oColor = new Color(225, 100, 65);
            accent = new Color(35, 100, 210);
            textColor = new Color(30, 40, 55);
            mutedColor = new Color(100, 110, 125);

        } else {

            background = new Color(12, 16, 35);
            tile = new Color(25, 31, 60);
            xColor = new Color(50, 220, 255);
            oColor = new Color(190, 90, 255);
            accent = new Color(50, 220, 255);
            textColor = new Color(245, 248, 255);
            mutedColor = new Color(155, 170, 205);
        }
    }

    void refreshColors() {

        getContentPane().setBackground(background);

        status.setForeground(textColor);
        score.setForeground(mutedColor);

        restartButton.setBackground(accent);
        restartButton.setForeground(
            themeBox.getSelectedItem().equals("Minimal Premium")
                ? Color.WHITE
                : background
        );

        resetButton.setBackground(tile);
        resetButton.setForeground(textColor);

        boardPanel.repaint();
    }

    void updateDifficultyAvailability() {
        boolean computerMode =
            modeBox.getSelectedItem().equals("Human vs Computer");

        levelBox.setEnabled(computerMode);
    }

    void newRound() {

        for (int i = 0; i < 9; i++) {
            board[i] = ' ';
        }

        currentPlayer = 'X';
        gameOver = false;
        computerThinking = false;
        winningLine = null;

        status.setText("Player X's turn");

        boardPanel.repaint();
    }

    void updateScore() {
        score.setText(
            "PLAYER X: " + xWins
            + "     |     PLAYER O: " + oWins
            + "     |     DRAWS: " + draws
        );
    }

    boolean isComputerMode() {
        return modeBox.getSelectedItem()
            .equals("Human vs Computer");
    }

    void handleMove(int position) {

        if (gameOver || computerThinking) {
            return;
        }

        if (board[position] != ' ') {
            return;
        }

        if (isComputerMode() && currentPlayer == 'O') {
            return;
        }

        board[position] = currentPlayer;
        boardPanel.repaint();

        if (checkGameResult()) {
            return;
        }

        currentPlayer = currentPlayer == 'X' ? 'O' : 'X';

        if (isComputerMode() && currentPlayer == 'O') {

            computerThinking = true;
            status.setText("Computer is thinking...");

            Timer timer = new Timer(350, e -> {
                ((Timer) e.getSource()).stop();

                if (!gameOver && isComputerMode()
                    && currentPlayer == 'O') {

                    int move = getComputerMove();

                    if (move != -1) {
                        board[move] = 'O';
                    }

                    computerThinking = false;
                    boardPanel.repaint();

                    if (!checkGameResult()) {
                        currentPlayer = 'X';
                        status.setText("Your turn (X)");
                    }
                } else {
                    computerThinking = false;
                }
            });

            timer.setRepeats(false);
            timer.start();

        } else {

            status.setText("Player " + currentPlayer + "'s turn");
        }
    }

    boolean checkGameResult() {

        int[][] combinations = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
        };

        for (int[] combination : combinations) {

            char first = board[combination[0]];

            if (first != ' '
                && first == board[combination[1]]
                && first == board[combination[2]]) {

                gameOver = true;
                winningLine = combination;

                if (first == 'X') {
                    xWins++;
                } else {
                    oWins++;
                }

                if (isComputerMode()) {
                    status.setText(
                        first == 'X' ? "YOU WIN!" : "COMPUTER WINS!"
                    );
                } else {
                    status.setText("PLAYER " + first + " WINS!");
                }

                updateScore();
                boardPanel.repaint();
                return true;
            }
        }

        boolean full = true;

        for (char cell : board) {
            if (cell == ' ') {
                full = false;
                break;
            }
        }

        if (full) {
            gameOver = true;
            draws++;
            status.setText("IT'S A DRAW!");
            updateScore();
            boardPanel.repaint();
            return true;
        }

        return false;
    }

    int getComputerMove() {

        String difficulty =
            (String) levelBox.getSelectedItem();

        if ("Easy".equals(difficulty)) {
            return randomMove();
        }

        if ("Medium".equals(difficulty)) {

            int move = findTacticalMove('O');

            if (move != -1) {
                return move;
            }

            move = findTacticalMove('X');

            if (move != -1) {
                return move;
            }

            if (board[4] == ' ') {
                return 4;
            }

            int[] corners = {0, 2, 6, 8};

            for (int corner : corners) {
                if (board[corner] == ' ') {
                    return corner;
                }
            }

            return randomMove();
        }

        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int i = 0; i < 9; i++) {

            if (board[i] == ' ') {

                board[i] = 'O';

                int scoreValue = minimax(false);

                board[i] = ' ';

                if (scoreValue > bestScore) {
                    bestScore = scoreValue;
                    bestMove = i;
                }
            }
        }

        return bestMove;
    }

    int randomMove() {

        int[] empty = new int[9];
        int count = 0;

        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                empty[count++] = i;
            }
        }

        if (count == 0) {
            return -1;
        }

        return empty[random.nextInt(count)];
    }

    int findTacticalMove(char player) {

        for (int i = 0; i < 9; i++) {

            if (board[i] == ' ') {

                board[i] = player;
                boolean wins = hasWon(player);
                board[i] = ' ';

                if (wins) {
                    return i;
                }
            }
        }

        return -1;
    }

    int minimax(boolean maximizing) {

        if (hasWon('O')) {
            return 10;
        }

        if (hasWon('X')) {
            return -10;
        }

        if (isBoardFull()) {
            return 0;
        }

        if (maximizing) {

            int best = Integer.MIN_VALUE;

            for (int i = 0; i < 9; i++) {

                if (board[i] == ' ') {

                    board[i] = 'O';

                    best = Math.max(best, minimax(false));

                    board[i] = ' ';
                }
            }

            return best;

        } else {

            int best = Integer.MAX_VALUE;

            for (int i = 0; i < 9; i++) {

                if (board[i] == ' ') {

                    board[i] = 'X';

                    best = Math.min(best, minimax(true));

                    board[i] = ' ';
                }
            }

            return best;
        }
    }

    boolean hasWon(char player) {

        int[][] combinations = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
        };

        for (int[] c : combinations) {

            if (board[c[0]] == player
                && board[c[1]] == player
                && board[c[2]] == player) {

                return true;
            }
        }

        return false;
    }

    boolean isBoardFull() {

        for (char cell : board) {
            if (cell == ' ') {
                return false;
            }
        }

        return true;
    }

    class BoardPanel extends JPanel {

        BoardPanel() {

            setOpaque(false);
            setPreferredSize(new Dimension(450, 450));

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {

                    int size = Math.min(getWidth(), getHeight());
                    int cellSize = size / 3;

                    int startX = (getWidth() - size) / 2;
                    int startY = (getHeight() - size) / 2;

                    int x = e.getX() - startX;
                    int y = e.getY() - startY;

                    if (x < 0 || y < 0 || x >= size || y >= size) {
                        return;
                    }

                    int column = x / cellSize;
                    int row = y / cellSize;

                    handleMove(row * 3 + column);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics graphics) {

            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();

            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int size = Math.min(getWidth(), getHeight()) - 12;
            int cellSize = size / 3;

            int startX = (getWidth() - size) / 2;
            int startY = (getHeight() - size) / 2;

            // Board tiles
            for (int i = 0; i < 9; i++) {

                int row = i / 3;
                int column = i % 3;

                int x = startX + column * cellSize + 5;
                int y = startY + row * cellSize + 5;

                boolean winningCell = false;

                if (winningLine != null) {
                    for (int index : winningLine) {
                        if (index == i) {
                            winningCell = true;
                        }
                    }
                }

                g.setColor(
                    winningCell
                        ? accent
                        : tile
                );

                g.fillRoundRect(
                    x, y,
                    cellSize - 10,
                    cellSize - 10,
                    24, 24
                );

                char mark = board[i];

                if (mark != ' ') {

                    g.setFont(
                        new Font("Segoe UI", Font.BOLD, cellSize / 2)
                    );

                    g.setColor(
                        mark == 'X' ? xColor : oColor
                    );

                    FontMetrics fm = g.getFontMetrics();

                    String symbol = String.valueOf(mark);

                    int textX = x
                        + ((cellSize - 10) - fm.stringWidth(symbol)) / 2;

                    int textY = y
                        + ((cellSize - 10) - fm.getHeight()) / 2
                        + fm.getAscent();

                    g.drawString(symbol, textX, textY);
                }
            }

            g.dispose();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(TicTacToe::new);
    }
}

