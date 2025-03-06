package Gridwalker;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridWalker implements ActionListener {
    
    private static JFrame frame = new JFrame("The game of Grid Walker");
    private JButton[][] buttons;
    private char[][] grid;
    private int currentRow, currentCol;
    private int rows, cols;
    private boolean gameStarted = false;
    private boolean crashed = false;
    private int steps = 0;
    
    // Unicode characters for directions and target
    private final char L = (char)8592; // ←
    private final char U = (char)8593; // ↑
    private final char R = (char)8594; // →
    private final char D = (char)8595; // ↓
    private final char T = (char)9678; // ○
    
    public GridWalker(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        
        // Initialize grid and buttons
        grid = new char[rows][cols];
        buttons = new JButton[rows][cols];
        
        // Set up the frame
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(rows, cols));
        
        // Fill grid with random directions
        char[] directions = {L, U, R, D};
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                grid[i][j] = directions[(int)(Math.random() * 4)];
            }
        }
        
        // Set the 'Target' element
        int targetRow = (int)(Math.random() * rows);
        int targetCol = (int)(Math.random() * cols);
        grid[targetRow][targetCol] = T;
        
        // Create buttons for the grid
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton(Character.toString(grid[i][j]));
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 45));
                buttons[i][j].addActionListener(this);
                frame.add(buttons[i][j]);
            }
        }
        
        // Choose a random starting position
        do {
            currentRow = (int)(Math.random() * rows);
            currentCol = (int)(Math.random() * cols);
        } while (grid[currentRow][currentCol] == T); // Don't start at target
        
        buttons[currentRow][currentCol].setBackground(Color.cyan);
        
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        String answer;
        answer = JOptionPane.showInputDialog("Enter the number of rows: ");
        int rows = Integer.parseInt(answer);
        answer = JOptionPane.showInputDialog("Enter the number of columns: ");
        int cols = Integer.parseInt(answer);
        
        new GridWalker(rows, cols);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameStarted) {
            gameStarted = true;
            walkGrid();
        }
    }
    
    private void walkGrid() {
        // Create a path array to track visited cells
        char[][] path = new char[rows][cols];
        
        while(!crashed && grid[currentRow][currentCol] != T) {
            // Mark current position as visited
            path[currentRow][currentCol] = '*';
            buttons[currentRow][currentCol].setBackground(Color.yellow);
            
            // Get direction from current cell
            char direction = grid[currentRow][currentCol];
            
            // Move according to direction
            int newRow = currentRow;
            int newCol = currentCol;
            
            if(direction == U) {
                newRow--;
            } else if(direction == D) {
                newRow++;
            } else if(direction == L) {
                newCol--;
            } else if(direction == R) {
                newCol++;
            }
            
            steps++;
            
            // Check if out of bounds
            if(newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                buttons[currentRow][currentCol].setBackground(Color.red);
                JOptionPane.showMessageDialog(null, "You left the grid!" , 
                        "Oopsies!", JOptionPane.PLAIN_MESSAGE);
                crashed = true;
                break;
            }
            
            // Check if path intersects itself
            if(path[newRow][newCol] == '*') {
                buttons[currentRow][currentCol].setBackground(Color.red);
                JOptionPane.showMessageDialog(null, "Your path intersected itself!" , 
                        "Oopsies!", JOptionPane.PLAIN_MESSAGE);
                crashed = true;
                break;
            }
            
            // Update position
            currentRow = newRow;
            currentCol = newCol;
            
            // Add small delay to visualize movement
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Final output
        if(!crashed) {
            buttons[currentRow][currentCol].setBackground(Color.green);
            JOptionPane.showMessageDialog(null, "You finished in " + steps + " steps." , 
                    "Congratulations!", JOptionPane.PLAIN_MESSAGE);
        }
    }
}