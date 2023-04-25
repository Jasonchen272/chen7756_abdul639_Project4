// Written by Jason Chen chen7756 and Nadir Abdullahi abdul639
import java.util.Scanner;
public class Main {
    public static void main(String args[]){
        Scanner s = new Scanner(System.in);
        System.out.println("---Welcome to Minesweeper---");
        System.out.println("----------------------------");
        System.out.println("Select a mode: Easy, Medium, Hard");
        String mode = s.next();//mode is string user enters, Easy, Medium, or Hard
        while(!mode.equals("Easy")  && !mode.equals("Medium") && !mode.equals("Hard")){//while input is not one of these
            //prompts user to select mode until a valid mode chosen
            System.out.println("Select a mode: Easy, Medium, Hard");
            mode = s.next();
        }
        System.out.println("Debug mode? false for no, true for yes");
        boolean debug = s.nextBoolean();
        Minefield m;
        if(mode.equals("Hard")){//chose hard mode
            m = new Minefield(20,20,40);//new instance of minefield with hard parameters
            System.out.println("First, type coordinates: [0<=x<20][0<=y<20]");
            System.out.println("There are 40 Mines");
            int guessX = s.nextInt();//asks user for first guess
            int guessY = s.nextInt();
            while(guessX < 0 || guessX >= 20 || guessY < 0 || guessY >= 20){//makes sure first guess in bounds
                //asks for new coordinates
                System.out.println("Invalid location");
                guessX = s.nextInt();
                guessY = s.nextInt();
            }
            m.createMines(guessX,guessY,40);//creates 40 mines not in starting position
            m.evaluateField();//changes cells status to correct values
            if(debug) {
                m.printMinefield();//for debugging
            }
            m.revealZeroes(guessX,guessY);//reveals any zeros for first guess
            m.revealMines(guessX,guessY);//reveals a close mine to help user get started
            System.out.println(m);
        }
        else if(mode.equals("Medium")){
            m = new Minefield(9,9,12);//new instance of minefield with medium parameters
            System.out.println("First, type coordinates: [0<=x<9][0<=y<9]");
            System.out.println("There are 12 Mines");
            int guessX = s.nextInt();//asks user for first guess
            int guessY = s.nextInt();
            while(guessX < 0 || guessX >= 12 || guessY < 0 || guessY >= 12){//makes sure first guess in bounds
                //asks for new coordinates
                System.out.println("Invalid location");
                guessX = s.nextInt();
                guessY = s.nextInt();
            }
            m.createMines(guessX,guessY,12);//creates 12 mines not in starting position
            m.evaluateField();//changes cells status to correct values
            if(debug) {
                m.printMinefield();//for debugging
            }
            m.revealZeroes(guessX,guessY);//reveals any zeros for first guess
            m.revealMines(guessX,guessY);//reveals a close mine to help user get started
            System.out.println(m);
        }
        else{
            m = new Minefield(5,5,5);//new instance of minefield with easy parameters
            System.out.println("First, type coordinates: [0<=x<5][0<=y<5] ");
            System.out.println("There are 5 Mines");
            int guessX = s.nextInt();//asks user for first guess
            int guessY = s.nextInt();
            while(guessX < 0 || guessX >= 5 || guessY < 0 || guessY >= 5){//makes sure first guess in bounds
                //asks for new coordinates
                System.out.println("Invalid location");
                guessX = s.nextInt();
                guessY = s.nextInt();
            }
            m.createMines(guessX,guessY,5);//creates 5 mines not in starting position
            m.evaluateField();//changes cells status to correct values
            if(debug) {
                m.printMinefield();//for debugging
            }
            m.revealZeroes(guessX,guessY);//reveals any zeros for first guess
            m.revealMines(guessX,guessY);//reveals a close mine to help user get started
            System.out.println(m);
        }
        while(!m.gameOver()) {//while game is not done
            System.out.println(m);//displays minefield to user
            //prompts user to type coordinates and if they want a flag shows how many flags are left
            System.out.println("Type coordinates and if you want a flag(" +m.getFlags()+" Remaining): [x][y] flag: [any number]/ no flag:[0]");
            int guessX = s.nextInt();
            int guessY = s.nextInt();
            int guessFlag = s.nextInt();
            boolean place = false;//defaults to no flag
            if (guessFlag != 0){//if guess flag is anything but 0, it shows user wants a flag
                place = true;
            }
            m.guess(guessX,guessY,place);//user guess
            m.revealZeroes(0, 0);//reveals adjacent zeros by user guess
        }//while
        //game finished
        if (!m.win) {//if user won
            System.out.println("Game Over, You Lose");
        }
        else {//if user lost
            System.out.println("Congratulations, You Won!");
        }
    }
}
