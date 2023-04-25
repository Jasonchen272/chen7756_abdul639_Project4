// Written by Jason Chen chen7756 and Nadir Abdullahi abdul639
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
public class Minefield {
    /**
    Global Section
    */
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_GREY = "\u001b[37m";
    public static final String ANSI_MAGENTA = "\033[38;5;206m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_GREY_BG = "\u001b[0m";
    public static final String ANSI_PURPLE = "\033[0;35m";
    public static final String ANSI_CYAN = "\033[0;36m";


    private int row;//rows in field
    private int cols;//columns in field
    private int flag;//number of flags left
    //list of colors which number of mines are used as indexes
    private String[] colors ={ANSI_YELLOW, ANSI_BLUE_BRIGHT, ANSI_GREEN, ANSI_PURPLE,ANSI_CYAN,ANSI_MAGENTA,ANSI_GREY,ANSI_GREY_BG};
    private Cell[][] display;//what user sees(same as field unless there are flags)
    private Cell[][] field;//what is actually on field
    private boolean finished = false;//boolean representing if game is finished
    private int mines;//number of mines in field
    public boolean win;//true if player wins false otherwise
    /**
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    //creates 2 2d arrays of type Cell with Cell status as "-"
    public Minefield(int rows, int columns, int flags) {
        row = rows;
        cols = columns;
        display = new Cell[row][cols];//2d array that is shown to user
        field = new Cell[row][cols];//2d array of all locations(for debugging)
        for(int i = 0; i<rows; i++){//fill display with cells that are hidden and status "-"
            for(int j = 0; j<cols;j++){
                display[i][j] = new Cell(false, "-");

            }
        }
        this.flag = flags;
        mines = flags;
    }
    /**
     * evaluateField
     *
     * @function When a mine is found in the field, calculate the surrounding 9x9 tiles values. If a mine is found, increase the count for the square.
     */
    public void evaluateField() {
        int count = 0;//number of neighbor mines
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < cols; j++) {
                if (!display[i][j].getStatus().equals("M")) {//if element is a mine, it should not be a number of mines
                    //increases count if adjacent spot is mine
                    //tests if that spot is in range

                    //top left
                    if (inBounds(i - 1, j - 1) && display[i - 1][j - 1].getStatus().equals("M")) {
                        count++;
                    }
                    //top middle
                    if (inBounds(i - 1, j) && display[i - 1][j].getStatus().equals("M")) {
                        count++;
                    }
                    //top right
                    if (inBounds(i - 1, j + 1) && display[i - 1][j + 1].getStatus().equals("M")) {
                        count++;
                    }
                    //middle left
                    if (inBounds(i, j - 1) && display[i][j - 1].getStatus().equals("M")) {
                        count++;
                    }
                    //middle right
                    if (inBounds(i, j + 1) && display[i][j + 1].getStatus().equals("M")) {
                        count++;
                    }
                    //bottom left
                    if (inBounds(i + 1, j - 1) && display[i + 1][j - 1].getStatus().equals("M")) {
                        count++;
                    }
                    //bottom middle
                    if (inBounds(i + 1, j) && display[i + 1][j].getStatus().equals("M")) {
                        count++;
                    }
                    //bottom right
                    if (inBounds(i + 1, j + 1) && display[i + 1][j + 1].getStatus().equals("M")) {
                        count++;
                    }
                    display[i][j].setStatus(String.valueOf(count));//changes cell value to number of adjacent mines
                    count = 0;//reset
                }
            }
        }
        for (int i = 0; i < display.length; i++) {//copy to field with new cell objects so flags won't change value
            for(int j = 0; j<cols; j++) {
                field[i][j] =new Cell(false, display[i][j].getStatus());
            }
        }
    }//evaluate field
    /**
     * createMines
     *
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {
        int xCor;
        int yCor;
        int numMines = 0;
        Random rand = new Random();
        if(mines==0 || mines > row*cols-1){//must have at least 1 mine and 1 space not a mine
            System.out.println("number of mines out of range");
            return;
        }
        while(numMines != mines){//while there are not enough mines
            xCor = rand.nextInt(row);//random and within bounds
            yCor = rand.nextInt(cols);//random and within bounds
            if (!display[xCor][yCor].getStatus().equals("M")){//if there is not already a mine
                //if the new coordinates are not ones in parameter
                if(!(xCor == x && yCor == y)) {
                    display[xCor][yCor].setStatus("M");//change coordinate to a mine
                    numMines++;//increment number of mines
                }
            }
        }//while
    }//createMines

    /**∂
     * guess
     *
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */

    public boolean guess(int x, int y, boolean flag) {
        if(!inBounds(x,y)){//if guess in not in bounds
            System.out.println("Invalid Location");
            return false;//no mine hit
        }
        //if you chose a mine without placing a flag
        if(display[x][y].getStatus().equals("M") && !flag){
            display[x][y].setRevealed(true);//reveals cell(mine) you chose
            finished = true;//game over
            win = false;//loss
            return true;//mine clicked
        }
        else if(flag){//if user places  flag and there is not already a flag
            if(display[x][y].getStatus().equals("F")){
                System.out.println("There is already a flag there");
                return false;
            }
            display[x][y].setStatus("F");//user sees F where why placed flag
            display[x][y].setRevealed(true);//reveals cell(flag) you chose
            this.flag--;//lose a flag
            if (!field[x][y].getStatus().equals("M")){//flag on non mine
                System.out.println("Wrong flag placement");
                win = false;
                finished = true;
                return false;
            }
            if(this.flag == 0){//if  there are no more flags test if they are in right place
                win = true;
                finished = true;
                return false;
//
            }
            return false;//flag placed
        }
        else{//no flag and didn't hit mine
            revealZeroes(x,y);//reveal zeros around what you chose
            return false;//no mine placed
        }
    }
    /**
     * gameOver
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otherwise return true.
     */
    public boolean gameOver() {
        return finished;//finished is global variable representing if game is over
    }

    /**
     * revealField
     * This method should follow the pseudocode given.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x      The x value the user entered.
     * @param y      The y value the user entered.
     */
    //reveals islands of 0s around what user chose
    public void revealZeroes(int x, int y) {
        Stack1Gen stack = new Stack1Gen<>();//new stack
        stack.push(new int[]{x, y});//push array of coordinates on stack
        while(!stack.isEmpty()){//while there are still things in stack
            int [] temp = (int[]) stack.pop();//remove top(last in) element
            display[temp[0]][temp[1]].setRevealed(true);//top element coordinates are set to revealed
            //if element to above is in range and is not already revealed and is a 0 it is added to top of stack
            if(inBounds(temp[0] -1 ,temp[1]) && !display[temp[0] -1][temp[1]].getRevealed() && display[temp[0]-1][temp[1]].getStatus().equals("0")){
                stack.push(new int[] {temp[0]-1,temp[1]});
            }
            //if element below is in range and is not already revealed and is a 0 it is added to top of stack
            if(inBounds(temp[0]+1 ,temp[1]) && !display[temp[0]+1][temp[1]].getRevealed() && display[temp[0]+1][temp[1]].getStatus().equals("0")){
                stack.push(new int[]{temp[0] + 1, temp[1]});
            }
            //if element to left is in range and is not already revealed and is a 0 it is added to top of stack
            if(inBounds(temp[0],temp[1] -1) && !display[temp[0]][temp[1]-1].getRevealed() && display[temp[0]][temp[1]-1].getStatus().equals("0")){
                stack.push(new int[] {temp[0], temp[1] - 1});
            }
            //if element to right is in range and is not already revealed and is a 0 it is added to top of stack
            if(inBounds(temp[0] ,temp[1]+1) && !display[temp[0]][temp[1]+1].getRevealed() && display[temp[0]][temp[1]+1].getStatus().equals("0")){
                stack.push(new int[] {temp[0], temp[1] + 1});            }
        }//while
    }//revealZeros

    /**
     * revealMines
     * This method should follow the pseudocode given.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    //used on first guess to help user get started, reveals cells until it reaches a mine
    public void revealMines(int x, int y) {
        Q1Gen queue = new Q1Gen();//new queue
        queue.add(new int[] {x,y});//adds array of coordinates
        while (queue.length() != 0){//while queue is not empty
            int [] temp = (int[])queue.remove();//take first item off(first in)
            display[temp[0]][temp[1]].setRevealed(true);//reveals element at coordinate of top of queue
            if(display[temp[0]][temp[1]].getStatus().equals("M")){//stops when a mine is revealed
                break;
            }
            //if in bounds and is not revealed, coordinate above is added to queue
            if(inBounds(temp[0]-1,temp[1]) && !display[temp[0]-1][temp[1]].getRevealed()){
                queue.add(new int[] {temp[0]-1,temp[1]});
            }
            //if in bounds and is not revealed, coordinate below is added to queue
            if(inBounds(temp[0]+1,temp[1]) && !display[temp[0]+1][temp[1]].getRevealed()){
                queue.add(new int[] {temp[0]+1,temp[1]});
            }
            //if in bounds and is not revealed, coordinate to left is added to queue
            if(inBounds(temp[0],temp[1]-1) && !display[temp[0]][temp[1]-1].getRevealed()){
                queue.add(new int[] {temp[0],temp[1]-1});
            }
            //if in bounds and is not revealed, coordinate to right is added to queue
            if(inBounds(temp[0],temp[1]+1) && !display[temp[0]][temp[1]+1].getRevealed()){
                queue.add(new int[] {temp[0],temp[1]+1});
            }
        }//while
    }//revealMines

    /**
     * revealStart
     *
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     */
    //reveals display at given indexes
    public void revealStart(int x, int y) {
        display[x][y].setRevealed(true);
    }
    /**
     * printMinefield
     *
     * @fuctnion This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected. 
     */
    //prints out the field of mines and the values(the answer basically)
    //used for debugging
    public void printMinefield() {
        //formatting so field is straight with double digits cols
        String s = "   ";//print string
        for(int i = 0; i< 10 && i<cols; i++){
            s+= i + "  ";
        }
        for(int i = 10; i<cols; i++){
            s+= i + " ";
        }
        s+= '\n';
        for (int i = 0; i<row; i++) {
            //formatting so field is straight with double digits rows
            if (i < 10) {
                s += i + "  ";
            } else {
                s += i + " ";
            }
            for (int j = 0; j < cols; j++) {
                if (field[i][j].getStatus().equals("M")) {//if there is a mine, add M in red  to print string and reset to grey
                    s += ANSI_RED + 'M' + ANSI_GREY_BG + "  ";
                }
                else {//adds the evaluated number with that number being the index of array colors to print string
                    s += colors[Integer.parseInt(field[i][j].getStatus())] + field[i][j].getStatus() + ANSI_GREY_BG + "  ";
                }
            }
            s += '\n';
        }
        System.out.println(s);//prints out
    }//printMinefield

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {
        String s = "   ";//return string
        //formatting so field is straight with double digits cols
        for (int i = 0; i < 10 && i < cols; i++) {
            s += i + "  ";
        }
        for (int i = 10; i < cols; i++) {
            s += i + " ";
        }
        s += '\n';
        for (int i = 0; i < row; i++) {
            //formatting so field is straight with double digits rows
            if (i < 10) {
                s += i + "  ";
            } else {
                s += i + " ";
            }
            for (int j = 0; j < cols; j++) {
                if (display[i][j].getRevealed()) {//it is revealed
                    if (display[i][j].getStatus().equals("M")) {//if there is a mine, add M in red to return string  and reset color to grey
                        s += ANSI_RED + 'M' + ANSI_GREY_BG + "  ";
                    }
                    else if(display[i][j].getStatus().equals("F")) {//if there is a flag, add F to return string
                        s +=  "F" + "  ";
                    }
                    else{//adds the evaluated number with that number being the index of array colors to return string
                        s += colors[Integer.parseInt(display[i][j].getStatus())] + display[i][j].getStatus() + ANSI_GREY_BG + "  ";
                    }
                }
                else {//not revealed adds "-" to return string
                    s += "-  ";
                }
            }
            s += '\n';
        }
        return s;//return string
    }//toString
    private boolean inBounds(int row, int col){//helper function that makes sure coordinates are in range
        if(row >= 0 && row < display.length && col >= 0 && col < display[0].length){
            return true;//in range
        }
        return false;//out of bounds
    }
    public int getFlags(){
        return flag;
    }//getter
    public static void main(String args[]){

    }
}
