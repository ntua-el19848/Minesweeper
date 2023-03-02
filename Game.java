import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Random;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


// This Class is the game class that manages the initialization, creation, flow of the game
public class Game{

    /*
    Methods this class  supports:
    - Setters, Getters...
    - ValidationCheck (Checks the validity of the scenario and either displayes approriate message or doesnt if all ok!)
    - SetupBoard (Setup Board and writes to mines.txt the coordinates of the mines, 
        then calls the other functions to print(debug) and fill the rest of the board)
    - printhiddenboard (prints the board to the console (for debugging only))
    - BuildBoard (builds the rest of the board with regard to mine placement)
    - WriteBoard (writes board to loadedboard.txt)

    */

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
    static final int superminecode=-200;
    static private boolean scenario_validity = false;

    /*  Encoding that I use for the boards
        FOR THE HIDDEN BOARD
     *  minecode --> integer that i have assigned (non used) to know if its a mine
     *  superminecode --> integer that i have assigned (non used) to know if its a mine
     * 
     *  FOR THE VISIVBLE BOARD
     *  displaycode --> TRUE 
     *  hiddencode -->  FALSE
     */

    // Setters
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

    public static void setScenarioValidity(boolean x){
        scenario_validity = x;
    }

    private static void InitializeVisibleBoard(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                // code for visible initialazation
                boardvisible[i][j]=-10;
            }
        }
    }

    // Getters
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

    public static boolean getScenarioValidity(){
        return scenario_validity;
    }

    public static int getBoardValue(int i, int j){
        return boardvisible[i][j];
    }

    public static int getHiddenBoardValue(int i, int j){
        return boardhidden[i][j];
    }


    // Other Methods

    // Initializes the variables from input and checks for the right values.
    public static void ValidationCheck(String scenario) throws Exception { 
        try{
            File input = new File("SCENARIOS/"+scenario+".txt");
            Scanner scan = new Scanner(input);

            // initialize with -1 just for the description check.
            setDifficulty(-1);
            setMines(-1);
            setTime(-1);
            setSupermine(-1);
            InitializeVisibleBoard();
            
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
                setScenarioValidity(false);
                throw new InvalidValueException("The value of the difficulty, mines, time or supermine does not fit the requirements");
            }
            else{ // all the checks have been made!!!   
                setScenarioValidity(true);
            }
        }
        catch(FileNotFoundException e){
            LaunchFileException window = new LaunchFileException();
            window.menu();
            setScenarioValidity(false);
            e.printStackTrace();
        }
    }

    // It setups the mines and calls the BuildBoard to fill the other boxes and writes it to BOARD folder
    public static void setupBoard() throws Exception{ 

        //intialize board with mines
        //and write the coordinates of the mines in the mines.txt file
        //this function only randomly generates mine placement and does not fill the entire board!

        File minesfile = new File("mines/mines.txt");
        minesfile.createNewFile();
        FileWriter fw = new FileWriter(minesfile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        
        int var=0;
        // to not have two mines randomly placed in same place
        //int random_placements[getMines()];

        while(var!=getMines()-1) {
            Random random = new Random();
            int i = random.nextInt(size-1);
            int j = random.nextInt(size-1);
            boardhidden[i][j] = minecode;
            bw.write(""+i+", "+j+", 0");
            bw.write("\n");
            var++;
        }

        if (supermine==1){
            Random random = new Random();
            int i = random.nextInt(size-1);
            int j = random.nextInt(size-1);
            boardhidden[i][j] = superminecode;
            bw.write(""+i+", "+j+", 1");
            var++;
        }
        else{
            Random random = new Random();
            int i = random.nextInt(size-1);
            int j = random.nextInt(size-1);
            boardhidden[i][j] = minecode;
            bw.write(""+i+", "+j+", 0");
            var++;
        }

        bw.close();
        BuildBoard();
        //printhiddenboard(); // debugging purpose
        WriteBoard();
    }


    // Prints the hidden board DEBUGGING REASONS
    private static void printhiddenboard(){ 
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
                else if(boardhidden[i][j]==superminecode){
                    System.out.print("S");
                }
                else{
                    System.out.print(boardhidden[i][j]);
                }
                System.out.print(" | ");
            }
            System.out.print("\n");
        }
    }

    // Builds the rest of the board (except mines) with respect to mine placement
    private static void BuildBoard(){ 
        for(int i=0; i<size; i++){ 
            for(int j=0; j<size; j++){
                int cnt=0;
                if(boardhidden[i][j]!=minecode && boardhidden[i][j]!=superminecode){
                    if(i!=0){
                        if(boardhidden[i-1][j]==minecode || boardhidden[i-1][j]==superminecode) cnt++;
                        if(j!=0){
                            if(boardhidden[i-1][j-1]==minecode || boardhidden[i-1][j-1]==superminecode) cnt++;
                        }
                    }
                    if(i!=(size-1)){
                        if(boardhidden[i+1][j]==minecode || boardhidden[i+1][j]==superminecode) cnt++;
                        if(j!=9){
                            if(boardhidden[i+1][j+1]==minecode || boardhidden[i+1][j+1]==superminecode) cnt++;
                        }
                    }
                    if(j!=0){
                        if(boardhidden[i][j-1]==minecode || boardhidden[i][j-1]==superminecode) cnt++;
                        if(i!=9){
                            if(boardhidden[i+1][j-1]==minecode || boardhidden[i+1][j-1]==superminecode) cnt++;
                        }
                    }
                    if(j!=(size-1)){
                        if(boardhidden[i][j+1]==minecode || boardhidden[i][j+1]==superminecode) cnt++;
                        if(i!=0){
                            if(boardhidden[i-1][j+1]==minecode || boardhidden[i-1][j+1]==superminecode) cnt++;
                        }
                    }
                    boardhidden[i][j] = cnt;
                }
            }
        }
    }

    // Writes the BOARD to a file in BOARDS folder
    private static void WriteBoard() throws Exception{ 
        try{
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

    // Condition that checks whether the game has ended or not
    private static boolean endGame(){
        if(getTime()==0){
            return true;
        }
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

    // method that runs when game is being started
    public static void StartGame() throws Exception{
        try{
            Pane root = new Pane();
            Stage stage = new Stage();
            root = FXMLLoader.load(Game.class.getResource("FXML/board.fxml"));
            stage.setTitle("Minesweeper Game");
            Label timesec = new Label(""+getTime()+"");
            timesec.setLayoutX(120);
            timesec.setLayoutY(115);

            Button btn[][] = DisplayVisible();

            GridPane grid = new GridPane();
                for(int i=0; i<size; i++){
                    for(int j=0; j<size; j++){                      
                        grid.add(btn[i][j], j,i);
                    }
                } 
            grid.setLayoutX(51);
            grid.setLayoutY(160);

            root.getChildren().add(grid);
            root.getChildren().add(timesec);
            if(getDifficulty()==1){
                Scene scene = new Scene(root, 400, 600);
                stage.setScene(scene);
                stage.show();
            }
            else{
                Scene scene = new Scene(root, 600, 800);
                stage.setScene(scene);
                stage.show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // method called to retrieve solution
    public static void Solution() throws Exception{
        // the game has to be played once to show solution
        try{
            Pane root = new Pane();
            if(getDifficulty()==1 || getDifficulty()==2){
                Stage stage = new Stage();
                root = FXMLLoader.load(Game.class.getResource("FXML/solution.fxml"));
                stage.setTitle("Minesweeper Game");

                Button[][] btn = new Button[size][size];

                for(int i=0; i<size; i++){
                    for(int j=0; j<size; j++){   
                        if(getHiddenBoardValue(i,j)==-1){
                            btn[i][j] = new Button("X");
                            btn[i][j].setPrefSize(30,30);
                        }
                        else if(getHiddenBoardValue(i,j)==-2){
                            btn[i][j] = new Button("S");
                            btn[i][j].setPrefSize(30,30);
                        }
                        else{
                            btn[i][j] = new Button(""+getHiddenBoardValue(i,j)+"");
                            btn[i][j].setPrefSize(30,30);
                        }
                    }
                }

                GridPane grid = new GridPane();
                for(int i=0; i<size; i++){
                    for(int j=0; j<size; j++){                      
                        grid.add(btn[i][j], j,i);
                    }
                }

                grid.setLayoutX(50);
                grid.setLayoutY(175);
                root.getChildren().add(grid);
                if(getDifficulty()==1){
                    Scene scene = new Scene(root, 400, 600);
                    stage.setScene(scene);
                    stage.show();
                }
                else{
                    Scene scene = new Scene(root, 600, 800);
                    stage.setScene(scene);
                    stage.show();
                }
            }
            else{
                System.out.println("Something Went terribly Wrong");
            }
        
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // returns the visible board in button array  
    public static Button[][] DisplayVisible(){
        // initialize buttons
        Button[][] btn = new Button[size][size];
        // for every button put the right value 
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(getBoardValue(i,j)==-1){
                    btn[i][j] = new Button("X");
                    btn[i][j].setPrefSize(30,30);
                }
                else if(getBoardValue(i,j)==-2){
                    btn[i][j] = new Button("S");
                    btn[i][j].setPrefSize(30,30);
                }
                else if(getBoardValue(i, j)==-10){
                    btn[i][j] = new Button();
                    btn[i][j].setPrefSize(30,30);

                }
                else{
                    btn[i][j] = new Button(""+getBoardValue(i,j)+"");
                    btn[i][j].setPrefSize(30,30);
                }
            }
        }

        return btn;
    }

    // Move Method
    public static void Move(){
    }

    // +fix visible + fix neighboors

};





// EXCEPTION CLASSES
class InvalidValueException extends Exception{
    public InvalidValueException(String message){
        super(message);
    }
};

class InvalidDescriptionException extends Exception{
    public InvalidDescriptionException(String message){
        super(message);
    }
};
