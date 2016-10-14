import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

/**
 * Created by daniel on 13-Oct-16.
 */
public class GameOfLife extends JPanel {
    Cell[][] grid; //contains the grid of cells that represents the universe of the game of life
    String birthFilename = "birth.txt"; //name of the file where initial generation is stored
    int lifecounter;//counts the number of alive neighbors in a particular cell
    int numRow;
    int numCol;
    JFrame frame;
    JPanel panel;
    JButton[][] squares;




    //builds the GUI
    void buildGUI(){

        squares = new JButton[numRow][numCol];
        frame = new JFrame ("The Game of Life");
        panel = new JPanel();

        panel.setLayout(new GridLayout(numRow, numCol));

        for(int i = 0; i < squares.length; i++){
            for (int j = 0; j < squares[0].length; j++){
                squares[i][j] = new JButton();
                panel.add(squares[i][j]);

                if(grid[i][j].isAlive()){
                    squares[i][j].setBackground(Color.black);
                } else squares[i][j].setBackground(Color.white);
            }
        }


        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    //calculates the number of living neighbors of each cell
    //and sets numNeighbors; it will not update the dead/alive state of the cells
    void calculateNumNeighbors(){

        for(int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                lifecounter = 0;
                //left neighbor
                if(i != 0 && grid[i-1][j].isAlive()) {
                    lifecounter++;
                }

                //right neighbor
                if((i + 1) != grid.length && grid[i+1][j].isAlive()){
                    lifecounter++;
                }

                //upper neighbor
                if(j != 0 && grid[i][j-1].isAlive()){
                    lifecounter++;
                }

                //lower neighbor
                if((j + 1) != grid[0].length && grid[i][j+1].isAlive()){
                    lifecounter++;
                }

                //corner neighbors

                //upper left
                if(i != 0 && j != 0 && grid[i-1][j-1].isAlive()){
                    lifecounter++;
                }
                //upper right
                if((i + 1) != grid.length && j != 0 && grid[i+1][j-1].isAlive()){
                    lifecounter++;
                }

                //lower left
                if(i != 0 && (j + 1) != grid[0].length && grid[i-1][j+1].isAlive()){
                    lifecounter++;
                }

                //lower right
                if((i + 1) != grid.length && (j + 1) != grid[0].length && grid[i+1][j+1].isAlive()){
                    lifecounter++;
                }

                grid[i][j].setNumNeighbors(lifecounter);
            }
        }

    }

    //creates the grid and reads the initial generation from the file with the
    //name stored in cd birthFilename

    public void readInitial(){
        try {
            File file = new File(birthFilename);
            Scanner scanner = new Scanner(file);
            numRow = scanner.nextInt();
            numCol = scanner.nextInt();
            grid = new Cell[numRow][numCol];

            //initializes every cell in the array
            for(int i = 0; i < numRow; i++){
                for (int j = 0; j < numCol; j++){
                    grid[i][j] = new Cell();
                }
            }

            if(scanner.hasNextLine()){
                for(int i = 0; i < numRow; i++) {
                    for (int j = 0; j < numCol; j++) {
                        if (scanner.next().equals("*")) {
                            grid[i][j].setAlive(true);
                        }
                        else {
                            grid[i][j].setAlive(false);
                        }
                    }
                }
            }

        } catch(FileNotFoundException e){
            System.out.println("Fix yo file");
        }
    }

    //updates the grid to the next generation, using the values of numNeighbors
    //in the cells
    void nextGeneration(){
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j].update();
            }
        }
    }


    //generates a command line visualization of the program
    void print(){
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {

                if (grid[i][j].isAlive()){
                    System.out.print("* ");
                } else System.out.print("  ");
            }
            System.out.print("\n");
        }
    }


    //runs the program
    void run(){
        readInitial();
        buildGUI();
        print();
        calculateNumNeighbors();
        buildGUI();
        nextGeneration();
        System.out.print("\n");
        buildGUI();
    }

    public static void main(String[] args) {
        new GameOfLife().run();
    }
}

