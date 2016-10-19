import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Scanner;

/**
 * Created by Daniel and Karan 13-Oct-16.
 * Extensions 1, 2 and 3 implemented
 */
public class GameOfLife extends JPanel {
    Cell[][] grid; //contains the grid of cells that represents the universe of the game of life
    String birthFilename = "birth.txt"; //name of the file where initial generation is stored
    int lifecounter;//counts the number of alive neighbors in a particular cell
    int numRow; //stores number of rows from the start file
    int numCol; //stores number of columns from
    JFrame frame;
    JPanel panel;
    JPanel startpanel;
    JButton[][] squares;//contains a grid of buttons, each of them representing a cell
    JButton startButton;
    JButton stopButton;
    static boolean mousepressed;//keeps track of a mouse being pressed
    static boolean lastmousestate;//keeps track of the last changed state for drag extension
    static boolean clickmouse;//to make sure a mouse press does not lead to a conflict with mouse clicked




    //builds the GUI
    void buildGUI(){

        frame = new JFrame ("The Game of Life");

        startButton = new JButton("Start");
        startpanel = new JPanel();
        startpanel.add(startButton);

        stopButton = new JButton("Stop");
        startpanel.add(stopButton);

        int delay = 200; //set how often the program updates in milliseconds

        //updates the grid
        ActionListener updateGUI = (new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateNumNeighbors();
                nextGeneration();

                for(int i = 0; i < squares.length; i++){
                    for (int j = 0; j < squares[0].length; j++){
                        if(grid[i][j].isAlive()){
                            squares[i][j].setBackground(Color.black);
                            grid[i][j].fadeaway = false;
                            grid[i][j].gencount = 0;
                        } else if(grid[i][j].fadeaway){
                            Color fading = new Color(80 + 10 * grid[i][j].gencount, 80 + 10 * grid[i][j].gencount,
                                    80 + 10 * grid[i][j].gencount);
                            squares[i][j].setBackground(fading);
                            grid[i][j].gencount++;
                            if(grid[i][j].gencount > 9){
                                grid[i][j].fadeaway = false;
                                grid[i][j].gencount = 0;
                            }
                        } else squares[i][j].setBackground(Color.white);
                    }
                }
            }
        });
        Timer timer = new Timer(delay, updateGUI);

        //starts the timer when start button is pressed
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
            }
        });

        //stops the timer when the stop button is pressed
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
            }
        });


        squares = new JButton[numRow][numCol];
        panel = new JPanel();
        panel.setLayout(new GridLayout(numRow, numCol));

        for(int i = 0; i < squares.length; i++){
            for (int j = 0; j < squares[0].length; j++){
                squares[i][j] = new JButton();
                panel.add(squares[i][j]);
                if(grid[i][j].isAlive()){
                    squares[i][j].setBackground(Color.black);
                } else squares[i][j].setBackground(Color.white);

                squares[i][j].addMouseListener(new MouseAdapter() {

                    //Extension 1: Pressing the left mouse button on a cell will turn that cell from dead to
                    // alive or vice versa.
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        clickmouse = true;
                        for(int i = 0; i < squares.length; i++){
                            for (int j = 0; j < squares[0].length; j++){
                                if (e.getSource() == squares[i][j] && grid[i][j].isAlive()){
                                    grid[i][j].setAlive(false);
                                    squares[i][j].setBackground(Color.white);
                                } else if (e.getSource() == squares[i][j] && grid[i][j].isAlive() == false){
                                    grid[i][j].setAlive(true);
                                    squares[i][j].setBackground(Color.black);
                                }
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mousepressed = true;
                        for(int i = 0; i < squares.length; i++) {
                            for (int j = 0; j < squares[0].length; j++) {
                                if(e.getSource() == squares[i][j]) {
                                    lastmousestate = grid[i][j].isAlive();
                                    if (grid[i][j].isAlive() && !clickmouse) {
                                        grid[i][j].setAlive(false);
                                        squares[i][j].setBackground(Color.white);
                                    } else if (!grid[i][j].isAlive() && !clickmouse) {
                                        grid[i][j].setAlive(true);
                                        squares[i][j].setBackground(Color.black);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mousepressed = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        for(int i = 0; i < squares.length; i++) {
                            for (int j = 0; j < squares[0].length; j++) {
                                if (e.getSource() == squares[i][j] && mousepressed) {
                                    if (lastmousestate) {
                                        grid[i][j].setAlive(false);
                                        squares[i][j].setBackground(Color.white);
                                    } else if (!lastmousestate) {
                                        grid[i][j].setAlive(true);
                                        squares[i][j].setBackground(Color.black);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });
            }
        }

        frame.add(startpanel, BorderLayout.SOUTH);

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

    public void readInitial() {
        try {
            File file = new File(birthFilename);
            Scanner scanner = new Scanner(file);
            numRow = scanner.nextInt();
            numCol = scanner.nextInt();
            grid = new Cell[numRow][numCol];

            //initializes every cell in the array
            for (int i = 0; i < numRow; i++) {
                for (int j = 0; j < numCol; j++) {
                    grid[i][j] = new Cell();
                }
            }

            if (scanner.hasNextLine()) {
                for (int i = 0; i < numRow; i++) {
                    for (int j = 0; j < numCol; j++) {
                        if (scanner.next().equals("*")) {
                            grid[i][j].setAlive(true);
                        } else {
                            grid[i][j].setAlive(false);
                        }
                    }
                }
            }
            //handles the case of a file not being found, loads a default configuration
        } catch (FileNotFoundException e) {
            Scanner scanner = new Scanner("20 20 "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . * * * * * * * * . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . "
                    + ". . . . . . . . . . . . . . . . . . . . ");
            numRow = scanner.nextInt();
            numCol = scanner.nextInt();
            grid = new Cell[numRow][numCol];

            //initializes every cell in the array
            for (int i = 0; i < numRow; i++) {
                for (int j = 0; j < numCol; j++) {
                    grid[i][j] = new Cell();
                }
            }

            if (scanner.hasNextLine()) {
                for (int i = 0; i < numRow; i++) {
                    for (int j = 0; j < numCol; j++) {
                        if (scanner.next().equals("*")) {
                            grid[i][j].setAlive(true);
                        } else {
                            grid[i][j].setAlive(false);
                        }
                    }
                }
            }
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
    //left in the code for testing purposes, but not used
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
    }

    public static void main(String[] args) {
        new GameOfLife().run();
    }
}