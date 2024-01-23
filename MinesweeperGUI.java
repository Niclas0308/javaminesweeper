package Minesweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.lang.System.exit;


public class MinesweeperGUI {
    private GameField gameField;
    private JFrame frame;
    public static JButton[][] buttons;
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem newGameMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu optionsMenu;
    private JMenuItem mineCountMenuItem;
    private JMenuItem boardSizeMenuItem;
    public int defaultRows = 8;
    public int defaultCols = 8;
    public int mines = defaultRows*defaultCols/5;

    private JButton[][] updateButtons(){
        for (int i = 0; i < gameField.getRows(); i++) {
            for (int j = 0; j < gameField.getCols(); j++) {
                Field field = gameField.getField(i, j);
                if (field.isOpen() && field.isMine()) {
                    buttons[i][j].setText("X");
                } else if (field.isOpen()) {
                    buttons[i][j].setText(Integer.toString(field.getAdjacentMines()));
                } else {
                    buttons[i][j].setText("");
                }
            }
        }
        return buttons;
    }
    /*private void startNewGame(int rows, int cols) {
        // Reset game fields
        gameField = new GameField(rows, cols, 10);
        // Reset buttons
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBackground(Color.WHITE);
            }
        }
    }*/
    private void startNewGame(int rows, int cols, int mines) {
        gameField = new GameField(rows, cols, mines);
        // Remove existing buttons
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                frame.remove(buttons[i][j]);
            }
        }
        // Initialize new buttons
        buttons = new JButton[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(25, 25));
                final int row = i, col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gameField.leftClick(row, col);
                        updateButtons();
                    }
                });
                buttons[i][j] = button;
                frame.add(button);
            }
        }
        // Revalidate and repaint the frame
        frame.revalidate();
        frame.repaint();
    }

    public MinesweeperGUI() {
        gameField = new GameField(8, 8, 10);
        buttons = new JButton[gameField.getRows()][gameField.getCols()];

        // Initialize JFrame
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize JMenuBar
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // Initialize JMenu
        gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);

        // Initialize JMenuItems
        newGameMenuItem = new JMenuItem("New Game");
        gameMenu.add(newGameMenuItem);
        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame(defaultRows, defaultCols, mines);
            }
        });

        exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        optionsMenu = new JMenu("Options");
        menuBar.add(optionsMenu);
        mineCountMenuItem = new JMenuItem("Change Mine Count");
        optionsMenu.add(mineCountMenuItem);
        mineCountMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Easy - 10%", "Intermediate - 30%", "Hard - 70%"};
                int choice = JOptionPane.showOptionDialog(frame, "Choose difficulty:", "Difficulty", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

                if (choice == JOptionPane.YES_OPTION) {
                    mines = (int)Math.round(defaultRows*defaultCols*0.1);
                    startNewGame(defaultRows,defaultCols, mines);
                } else if (choice == JOptionPane.NO_OPTION) {
                    mines = (int)Math.round(defaultRows*defaultCols*0.3);
                    startNewGame(defaultRows,defaultCols, mines);
                } else if (choice == JOptionPane.CANCEL_OPTION) {
                    mines = (int)Math.round(defaultRows*defaultCols*0.7);
                    startNewGame(defaultRows, defaultCols, mines);
                }
            }
        });
        boardSizeMenuItem = new JMenuItem("Change Board Size");
        optionsMenu.add(boardSizeMenuItem);
        boardSizeMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Object[] options = {"8x8", "12x12", "Cancel"};
            int choice = JOptionPane.showOptionDialog(frame, "Choose board size:", "Board Size", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

            if (choice == JOptionPane.YES_OPTION) {
                // 8x8 option chosen
                defaultRows = 8;
                defaultCols = 8;
                mines = defaultRows*defaultCols/5;
                // restart game with new size
                startNewGame(8,8, mines);
            } else if (choice == JOptionPane.NO_OPTION) {
                defaultRows = 12;
                defaultCols = 12;
                mines = defaultRows*defaultCols/5;
                // restart game with new size
                startNewGame(12,12, mines);
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                startNewGame(defaultRows, defaultCols, mines);
            }
        }
    });

        // Initialize JButtons
        for (int i = 0; i < gameField.getRows(); i++) {
            for (int j = 0; j < gameField.getCols(); j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(50, 50));
                final int row = i, col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gameField.leftClick(row, col);
                        updateButtons();
                    }
                });
                frame.add(buttons[i][j]);
            }
        }

        
    
    
    

        // Set layout and pack JFrame
        frame.setLayout(new GridLayout(gameField.getRows(), gameField.getCols()));
        frame.pack();
        frame.setVisible(true);
    }

    public JButton[][] getButtons() {
        return buttons;
    }

    public static void main(String[] args){
        MinesweeperGUI game1 = new MinesweeperGUI();
    }
}
