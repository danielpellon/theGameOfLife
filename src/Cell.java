import javax.swing.*;

/**
 * Created by danie on 13-Oct-16.
 */
public class Cell extends JLabel {

    boolean alive; //it is true when the cell is alive and false when the cell is dead
    int numNeighbors = 0; //number of alive neighboring cells

    //changes the alive/dead state of the cell
    void setAlive(boolean state){
        alive = state;
    }

    //returns the alive/dead state of the cell
    boolean isAlive(){
        return alive;
    }

    //sets numNeighbors of the cell to n
    void setNumNeighbors(int n){
        numNeighbors = n;
    }

    //takes the cell to the next generation: it changes the instance variable alive
    //according to the rules above, using the value of the instance variables numNeighbors and
    //alive.
    void update(){
        if(isAlive() && (numNeighbors < 2 || numNeighbors > 3)){
            setAlive(false);
        } else if(isAlive() && (numNeighbors == 2 || numNeighbors ==3)){
            setAlive(true);
        } else if(!isAlive() && numNeighbors == 3){
            setAlive(true);
        }
    }
}
