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
    static private String scenario;
    static private int difficulty;
    static private int mines;
    static private int size=17;
    static private int time;
    static private int supermine;
    static private int [][] boardvisible = new int[size][size];
    static private int [][] boardhidden = new int[size][size];
    static final int minecode=-1;

    /*  Encoding that I use for the boards
        FOR THE HIDDEN BOARD
     *  minecode --> integer that i have assigned (non used) to know if its a mine
     *  superminecode --> integer that i have assigned (non used) to know if its a mine
     * 
     *  FOR THE VISIVBLE BOARD
     *  displaycode --> TRUE 
     *  hiddencode -->  FALSE
     */
    
    public static void setDifficulty(int x){
        difficulty = x;
        // initilize size and board based on difficulty
        if(x==1) size=9;
        if(x==2) size=16;
    }

    public static void setMines(int x){
        mines = x;
    }

    public static void setSupermine(int x){
        supermine = x;
    }

    public static void setTime(int x){
        time = x;
    }

    public static void setScenario(String x){
        scenario = x;
    }

    public static int getDifficulty(){
        return difficulty;
    }

    public static int getMines(){
        return mines;
    }

    public static int getSupermine(){
        return supermine;
    }

    public static int getTime(){
        return time;
    }

    public static String getScenario(){
        return scenario;
    }
    
    public static void CheckWriteBoard(String scenario) throws Exception { // (IT WORKS) initializes the variables from input and checks for the right values.
        try{
            File input = new File("SCENARIOS/"+scenario+".txt");
            Scanner scan = new Scanner(input);

            // initialize with -1 just for the description check.
            setDifficulty(-1);
            setMines(-1);
            setTime(-1);
            setSupermine(-1);
            
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
                Game.setupBoard();
            }
        }
        catch(FileNotFoundException e){
            LaunchFileException window = new LaunchFileException();
            window.menu();
            e.printStackTrace();
        }
        finally{
            //printStackTrace();
        }
    }

    private static void setupBoard() throws Exception{ // (IT WORKS) it setups the mines and calls the BuildBoard to fill the other boxes and writes it to BOARD folder
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

    private static void printhiddenboard(){ // (IT WORKS) prints the hidden board DEBUGGING REASONS
        System.out.print("\t ");
        for(int i=0; i<size; i++){
            System.out.print(" " + (i+1) + "  ");
        }
        System.out.print("\n");
        for(int i=0; i<size; i++){
            System.out.print(i+1 + "\t| ");
            for(int j=0; j<size; j++){
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

    private static void BuildBoard(){ // (IT WORKS) builds the rest of the board (except mines) with respect to mine placement
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                int cnt=0;
                if(boardhidden[i][j]!=minecode){
                    if(i!=0){
                        if(boardhidden[i-1][j]==minecode) cnt++;
                        if(j!=0){
                            if(boardhidden[i-1][j-1]==minecode) cnt++;
                        }
                    }
                    if(i!=(size-1)){
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
                    if(j!=(size-1)){
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

    private static void WriteBoard() throws Exception{ // (IT WORKS) writes the BOARD to a file in BOARDS folder
        try{
            //String name = scenario;
            //File board = new File("BOARDS/"+name+"_board.txt");// etsi an thelw polla
            File board = new File("BOARDS/loadedboard.txt");//etsi an thelw mono ena
            board.createNewFile();
            FileWriter fw = new FileWriter(board.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    bw.write(""+boardhidden[i][j]+" ");
                }
                bw.write("\n");
            }
            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static boolean endGame(){ // IT WORKS
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(boardvisible[i][j]==0){
                    if(boardhidden[i][j]!=minecode){
                        return false;
                    }
                }
            }
        }
        return true;
    }



//=================apo edw kai katw doulevw=================================




    public static void StartGame(){
        System.out.println("eftases ws to to StartGame with mines= "+mines+"");
    }

    public static void DisplayHidden(){
        // gia to solution
    }

    public static void DisplayVisible(){
        // to do via calling fix neighboors
    }

    public static void Move(){
        // kiniseis tou paixtei tha kaleite apo to controller
    }

    // +fix visible + fix neighboors

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
