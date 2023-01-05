import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ResourceBundle;
import java.io.File;
import java.util.Scanner;
import java.util.Random;


public class Game{

    //initialize the input with -1 so that i know that if some of these values remain -1 --> InvalidDescriptionException
    private String scenario;
    private int difficulty=-1;
    private int mines=-1;
    private int size=17;
    private int time=-1;
    private int supermine=-1;
    private boolean [][] boardvisible = new boolean[size][size];
    private int [][] boardhidden = new int[size][size];
    final int minecode=-1;

    /*  Encoding that I use for the boards
        FOR THE HIDDEN BOARD
     *  minecode --> integer that i have assigned (non used) to know if its a mine
     *  superminecode --> integer that i have assigned (non used) to know if its a mine
     * 
     *  FOR THE VISIVBLE BOARD
     *  displaycode --> TRUE 
     *  hiddencode -->  FALSE
     */
    
    public void setDifficulty(int x){
        this.difficulty = x;
        // initilize size and board based on difficulty
        if(x==1) this.size=9;
        if(x==2) this.size=16;
    }

    public void setMines(int x){
        this.mines = x;
    }

    public void setSupermine(int x){
        this.supermine = x;
    }

    public void setTime(int x){
        this.time = x;
    }

    public void setScenario(String x){
        this.scenario = x;
    }

    public int getDifficulty(){
        return this.difficulty;
    }

    public int getMines(){
        return this.mines;
    }

    public int getSupermine(){
        return this.supermine;
    }

    public int getTime(){
        return this.time;
    }

    public String getScenario(){
        return this.scenario;
    }
    
    public Game(String scenario) throws Exception { // (IT WORKS) constructor that initializes the variables from input and checks for the right values.
        try{
            setScenario(scenario);
            File input = new File("SCENARIOS/"+scenario+".txt");
            Scanner scan = new Scanner(input);

            // Invalid Description Exception Check!
            if(scan.hasNext()) setDifficulty(scan.nextInt());
            if(scan.hasNext()) setMines(scan.nextInt());
            if(scan.hasNext()) setTime(scan.nextInt());
            if(scan.hasNext()) setSupermine(scan.nextInt());
            scan.close();

            if (getDifficulty() == -1 || getMines() == -1 || getTime() == -1 || getSupermine() == -1){
                LaunchDescriptionException window = new LaunchDescriptionException();
                window.menu();
                throw new InvalidDescriptionException("The description provided is not valid as it does not contain information about all attributes of the game (difficulty, mines, time, supermine)");
            }

            // Invalid Value Exception Check!
            boolean valid=true;
            switch(getDifficulty()){
                case 1: {if (getMines() > 11 || getMines() < 9 || getTime() < 120 || getTime() > 180 || getSupermine() != 0){
                                valid=false;
                            }
                            else break;
                        }
                case 2: {if (getMines() > 45 || getMines() < 35 || getTime() < 240 || getTime() > 360 || getSupermine() > 1 || getSupermine() < 0) {
                                valid=false;
                            }
                            else break;
                        }
                default: valid=false;
            }

            if(!valid){
                LaunchValueException window = new LaunchValueException();
                window.menu();
                throw new InvalidValueException("The value of the difficulty, mines, time or supermine does not fit the requirements");
            }
            else{ // all the checks have been made!!!   
                this.setupBoard();
            }
        }
        catch(FileNotFoundException e){
            LaunchFileException window = new LaunchFileException();
            window.menu();
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setupBoard() throws Exception{ // (IT WORKS) it setups the mines and calls the BuildBoard to fill the other boxes and writes it to BOARD folder
        //intialize board with mines
        int var=0;
        while(var!=mines) {
            Random random = new Random();
            int i = random.nextInt(size-1);
            int j = random.nextInt(size-1);
            boardhidden[i][j] = minecode;
            var++;
        }
        BuildBoard();
        printhiddenboard(); // debugging purpose
        WriteBoard();
    }

    private void printhiddenboard(){ // (IT WORKS) prints the hidden board DEBUGGING REASONS
        System.out.print("\t ");
        for(int i=0; i<this.size; i++){
            System.out.print(" " + (i+1) + "  ");
        }
        System.out.print("\n");
        for(int i=0; i<this.size; i++){
            System.out.print(i+1 + "\t| ");
            for(int j=0; j<this.size; j++){
                if(boardhidden[i][j]==0){
                    System.out.print("0");
                }
                else if(boardhidden[i][j]==minecode){
                    System.out.print("X");
                }
                else{
                    System.out.print(boardhidden[i][j]);
                }
                System.out.print(" | ");
            }
            System.out.print("\n");
        }
    }

    private void BuildBoard(){ // (IT WORKS) builds the rest of the board (except mines) with respect to mine placement
        for(int i=0; i<this.size; i++){
            for(int j=0; j<this.size; j++){
                int cnt=0;
                if(boardhidden[i][j]!=minecode){
                    if(i!=0){
                        if(boardhidden[i-1][j]==minecode) cnt++;
                        if(j!=0){
                            if(boardhidden[i-1][j-1]==minecode) cnt++;
                        }
                    }
                    if(i!=(this.size-1)){
                        if(boardhidden[i+1][j]==minecode) cnt++;
                        if(j!=9){
                            if(boardhidden[i+1][j+1]==minecode) cnt++;
                        }
                    }
                    if(j!=0){
                        if(boardhidden[i][j-1]==minecode) cnt++;
                        if(i!=9){
                            if(boardhidden[i+1][j-1]==minecode) cnt++;
                        }
                    }
                    if(j!=(this.size-1)){
                        if(boardhidden[i][j+1]==minecode) cnt++;
                        if(i!=0){
                            if(boardhidden[i-1][j+1]==minecode) cnt++;
                        }
                    }
                    boardhidden[i][j] = cnt;
                }
            }
        }
    }

    private void WriteBoard() throws Exception{ // (IT WORKS) writes the BOARD to a file in BOARDS folder
        try{
            String name = scenario;
            File board = new File("BOARDS/"+name+"_board.txt");
            board.createNewFile();
            FileWriter fw = new FileWriter(board.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for(int i=0; i<this.size; i++){
                for(int j=0; j<this.size; j++){
                    bw.write(""+this.boardhidden[i][j]+" ");
                }
                bw.write("\n");
            }
            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}





// EXCEPTION CLASSES
class InvalidValueException extends Exception{
    public InvalidValueException(String message){
        super(message);
    }
}

class InvalidDescriptionException extends Exception{
    public InvalidDescriptionException(String message){
        super(message);
    }
}
