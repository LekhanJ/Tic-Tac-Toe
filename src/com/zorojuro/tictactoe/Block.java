package com.zorojuro.tictactoe;

import javax.swing.*;
import java.awt.*;

class Block extends JPanel {
    private JLabel symbolLabel;
    private boolean available = true;

    Block(int id) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.WHITE);

        symbolLabel = new JLabel("", SwingConstants.CENTER);
        symbolLabel.setFont(new Font("Arial", Font.BOLD, 60));
        add(symbolLabel, BorderLayout.CENTER);
    }

    public void setSymbol(char symbol) {
        if (available) {
            symbolLabel.setText(String.valueOf(symbol));
            available = false;
        }
    }

    public boolean isAvailable() {
        return available;
    }

    public void reset() {
        symbolLabel.setText("");
        available = true;
    }
}