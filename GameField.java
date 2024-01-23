package Minesweeper;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameField {
    private Field[][] fields;
    private int rows;
    private int cols;
    private int mines;

    public GameField(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        fields = new Field[rows][cols];

        // Initialize all fields
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                fields[i][j] = new Field();
            }
        }

        // Randomly place mines
        int placedMines = 0;
        while (placedMines < mines) {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * cols);
            if (!fields[row][col].isMine()) {
                fields[row][col].setMine(true);
                placedMines++;
            }
        }

        // Calculate adjacent mines for each field
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!fields[i][j].isMine()) {
                    int adjacentMines = 0;
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if (x == 0 && y == 0) {
                                continue;
                            }
                            int adjacentRow = i + x;
                            int adjacentCol = j + y;
                            if (adjacentRow >= 0 && adjacentRow < rows && adjacentCol >= 0 && adjacentCol < cols) {
                                if (fields[adjacentRow][adjacentCol].isMine()) {
                                    adjacentMines++;
                                }
                            }
                        }
                    }
                    fields[i][j].setAdjacentMines(adjacentMines);
                }
            }
        }
    }
    public void leftClick(int x, int y) {
        if (x >= 0 && x < rows && y >= 0 && y < cols) {
            Field clickedField = fields[x][y];
            if (!clickedField.isOpen()) {
                clickedField.setOpen(true);
                if (clickedField.isMine()) {
                    // Play sound
                    try {
                        File bombSound = new File("bomb.wav");
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bombSound);
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                    } catch (Exception ex) {
                        System.out.println("Error playing sound: " + ex.getMessage());
                    }

                    // Game over logic
                    // Mark the mine as red
                    MinesweeperGUI.buttons[x][y].setBackground(Color.red);
                    // Show all mines and make all fields unclickable
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            if (fields[i][j].isMine()) {
                                MinesweeperGUI.buttons[i][j].setBackground(Color.RED);
                            }
                            MinesweeperGUI.buttons[i][j].setEnabled(false);
                        }
                    }
                } else if (clickedField.getAdjacentMines() == 0) {
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (i == 0 && j == 0) {
                                continue;
                            }
                            int adjacentX = x + i;
                            int adjacentY = y + j;
                            if (adjacentX >= 0 && adjacentX < rows && adjacentY >= 0 && adjacentY < cols) {
                                leftClick(adjacentX, adjacentY);
                            }
                        }
                    }
                }
            }
        }
    }
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
    public int getMines() {
        return mines;
    }
    public Field getField(int i, int j) {
        return fields[i][j];
    }
}
