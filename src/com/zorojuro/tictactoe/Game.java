package com.zorojuro.tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Game extends JFrame {
    private static final int BOARD_SIZE = 9;
    private char[] board = new char[BOARD_SIZE];
    private JPanel gridPanel;
    private JLabel gameLabel;
    private boolean playerTurn = true;
    private boolean gameOver = false;

    public Game() {
        initializeBoard();
        setupFrame();
        createBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[i] = ' ';
        }
    }

    private void setupFrame() {
        setTitle("Tic-Tac-Toe vs AI");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameLabel = new JLabel("Your Turn (X)", SwingConstants.CENTER);
        gameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(gameLabel, BorderLayout.NORTH);
    }

    private void createBoard() {
        gridPanel = new JPanel(new GridLayout(3, 3));

        for (int i = 0; i < BOARD_SIZE; i++) {
            final int index = i;
            Block block = new Block(index);
            block.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (playerTurn && !gameOver && block.isAvailable()) {
                        makePlayerMove(index);
                    }
                }
            });
            gridPanel.add(block);
        }

        add(gridPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void makePlayerMove(int index) {
        board[index] = 'X';
        ((Block)gridPanel.getComponent(index)).setSymbol('X');

        if (checkGameState('X')) return;

        makeAIMove();
    }

    private void makeAIMove() {
        playerTurn = false;
        gameLabel.setText("AI's Turn (O)");

        int aiMove = findBestMove();
        board[aiMove] = 'O';
        ((Block)gridPanel.getComponent(aiMove)).setSymbol('O');

        if (checkGameState('O')) return;

        playerTurn = true;
        gameLabel.setText("Your Turn (X)");
    }

    private boolean checkGameState(char player) {
        if (isWinner(player)) {
            showGameResult(player == 'X' ? "You win!" : "AI wins!");
            return true;
        } else if (isBoardFull()) {
            showGameResult("It's a draw!");
            return true;
        }
        return false;
    }

    private int findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i] == ' ') {
                board[i] = 'O';
                int score = minimax(board, 0, false);
                board[i] = ' ';

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        return bestMove;
    }

    private int minimax(char[] board, int depth, boolean isMaximizing) {
        char result = checkWinner(board);

        if (result != ' ') {
            return scoreResult(result, depth);
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (board[i] == ' ') {
                    board[i] = 'O';
                    int score = minimax(board, depth + 1, false);
                    board[i] = ' ';
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (board[i] == ' ') {
                    board[i] = 'X';
                    int score = minimax(board, depth + 1, true);
                    board[i] = ' ';
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    private int scoreResult(char result, int depth) {
        if (result == 'O') return 10 - depth;
        if (result == 'X') return depth - 10;
        return 0;
    }

    private char checkWinner(char[] board) {
        int[][] winCombos = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6}             // Diagonals
        };

        for (int[] combo : winCombos) {
            if (board[combo[0]] != ' ' &&
                    board[combo[0]] == board[combo[1]] &&
                    board[combo[1]] == board[combo[2]]) {
                return board[combo[0]];
            }
        }

        return isBoardFull() ? 'T' : ' ';
    }

    private boolean isWinner(char player) {
        return checkWinner(board) == player;
    }

    private boolean isBoardFull() {
        for (char cell : board) {
            if (cell == ' ') return false;
        }
        return true;
    }

    private void showGameResult(String message) {
        gameOver = true;
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        resetGame();
    }

    private void resetGame() {
        initializeBoard();
        playerTurn = true;
        gameOver = false;
        gameLabel.setText("Your Turn (X)");

        for (Component comp : gridPanel.getComponents()) {
            if (comp instanceof Block) {
                ((Block)comp).reset();
            }
        }
    }
}