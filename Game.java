import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;


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
    - endGame
    - StartGame
    - Solution
    - FixVisible
    - Move
    - FixNeighboors
    - MarkMine
    */

    //initialize the input with -1 so that i know that if some of these values remain -1 --> InvalidDescriptionException
    static private String scenario;
    static private int difficulty;
    static private int mines;
    static private boolean endgame;
    static private int status=0; // status=0 (playing), status=1(won), status=-1(lost)
    static private int size=17;
    static private int time;
    static private int supermine;
    static private int [][] boardvisible = new int[size][size];
    static private int [][] boardhidden = new int[size][size];
    static final int minecode=-1;
    static final int superminecode=-2;
    static private boolean scenario_validity = false;
    static private Button btn[][] = new Button[size][size];
    static private int moves=0;
    static private int flags=0;
    static private int time_remaining;

    /*  Encoding that I use for the boards
     *  FOR THE HIDDEN BOARD
     *  minecode --> integer that i have assigned (non used) to know if its a mine = -1
     *  superminecode --> integer that i have assigned (non used) to know if its a mine == -2
     *
     *  FOR THE VISIBLE BOARD
     *  CODE 200 --> marked as mine
     *  CODE -10 --> hidden
     *  in any other case it takes the value of the hidden board
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

    private static void InitializeButtons(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                // code for visible initialazation
                btn[i][j] = new Button();
            }
        }
    }

    private static void InitializeHiddenBoard(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                // code for visible initialazation
                boardhidden[i][j]=0;
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
            File input = new File("medialab/"+scenario+".txt");
            Scanner scan = new Scanner(input);

            // initialize with -1 just for the description check.
            setDifficulty(-1);
            setMines(-1);
            setTime(-1);
            setSupermine(-1);

            // these are special resetes
            InitializeVisibleBoard();
            InitializeButtons();
            flags = 0;
            moves = 0;
            
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
        InitializeHiddenBoard();
        InitializeButtons();
        InitializeVisibleBoard();
        //intialize board with mines
        //and write the coordinates of the mines in the mines.txt file
        //this function only randomly generates mine placement and does not fill the entire board!

        File minesfile = new File("mines/mines.txt");
        minesfile.createNewFile();
        FileWriter fw = new FileWriter(minesfile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        
        int var=0;
        // to not have two mines randomly placed in same place
        int[] x = new int[mines];
        int[] y = new int[mines];
        
        while(var!=getMines()-1) {
            Random random = new Random();
            int i = random.nextInt(size-1);
            int j = random.nextInt(size-1);

            // to not have two mines randomly placed in same place
            boolean same = false;
            for(int k=0; k<var; k++){
                if(x[k]==i && y[k]==j) same = true;
            }
            if (same) continue;
            
            // if not same
            boardhidden[i][j] = minecode;
            bw.write(""+i+", "+j+", 0");
            bw.write("\n");
            x[var] = i;
            y[var] = j;
            var++;
        }

        while(true){
            if (supermine==1){
                Random random = new Random();
                int i = random.nextInt(size-1);
                int j = random.nextInt(size-1);

                // to not have two mines randomly placed in same place
                boolean same = false;
                for(int k=0; k<var; k++){
                    if(x[k]==i && y[k]==j) same = true;
                }
                if (same) continue;
                
                // if not same
                boardhidden[i][j] = superminecode;
                bw.write(""+i+", "+j+", 1");
                var++;
                break;
            }
            else{
                Random random = new Random();
                int i = random.nextInt(size-1);
                int j = random.nextInt(size-1);

                // to not have two mines randomly placed in same place
                boolean same = false;
                for(int k=0; k<var; k++){
                    if(x[k]==i && y[k]==j) same = true;
                }
                if (same) continue;
                
                // if not same
                boardhidden[i][j] = minecode;
                bw.write(""+i+", "+j+", 0");
                var++;
                break;
            }
        }

        bw.close();
        BuildBoard();
        printhiddenboard(); // debugging purpose
        WriteBoard();
    }

    // Prints the hidden board DEBUGGING REASONS
    private static void printhiddenboard(){ 
        System.out.print("\t ");
        for(int i=0; i<size; i++){
            System.out.print(" " + (i) + "  ");
        }
        System.out.print("\n");
        for(int i=0; i<size; i++){
            System.out.print(i + "\t| ");
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
                        if(j!=size-1){
                            if(boardhidden[i+1][j+1]==minecode || boardhidden[i+1][j+1]==superminecode) cnt++;
                        }
                    }
                    if(j!=0){
                        if(boardhidden[i][j-1]==minecode || boardhidden[i][j-1]==superminecode) cnt++;
                        if(i!=size-1){
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

    // Writes the BOARD to a file in BOARDS folder (debugging!!!)
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
    private static boolean endGameMove(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if (boardvisible[i][j]==-1 || boardvisible[i][j]==-2){
                    status = -1;
                    return true;
                }

            }
        }

        int cnt = 0;
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if (boardvisible[i][j]==-10) cnt++;
            }
        }
        // System.out.println(cnt);
        if (cnt==mines){
            status = 1;
            return true;
        }
        return false;
    }

    // method that runs when game is being started
    public static void StartGame() throws Exception{
        try{
            // initialize root Pane 
            Pane root = new Pane();
            // initialize stage
            Stage stage = new Stage();
            root = FXMLLoader.load(Game.class.getResource("FXML/board.fxml"));
            stage.setTitle("Minesweeper Game");

            // label for time
            time_remaining=time;
            Label timesec = new Label("Time: "+time+"");
            timesec.setLayoutX(50);
            timesec.setLayoutY(115);
            timesec.setFont(new Font("MesloLGS NF Bold", 16));

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                time_remaining--;
                timesec.setText("Time: "+time_remaining+"");
                if(time_remaining==0){
                    stage.close();
                    try {
                        Solution();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            // initialize the visible board buttons to be in sync with boardvisible array
            FixVisible();

            // no of Mines label
            Label Mines = new Label("Mines: "+mines+"");
            Mines.setLayoutX(150);
            Mines.setLayoutY(115);
            Mines.setFont(new Font("MesloLGS NF Bold", 16));

            // no of flags label
            Label Flags = new Label("Flags: "+flags+"");
            Flags.setLayoutX(250);
            Flags.setLayoutY(115);
            Flags.setFont(new Font("MesloLGS NF Bold", 16));

            // initialize grid
            GridPane grid = new GridPane();

            // for loop to add all buttons to grid and add lambda function to handle button events
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){ 
                    // some tricks to get around errors
                    int x = i; // x -axis
                    int y = j; // y -axis
                    
                    // add buttons to grid
                    grid.add(btn[i][j], j,i);

                    btn[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            MouseButton button = event.getButton();

                            // if you press left click
                            if(button==MouseButton.PRIMARY){
                                try{
                                    // register and handle the move via Move method (fix neighboors etc)
                                    Move(x,y);
                                    endgame = endGameMove();
                                    // in case of ending game
                                    if (endgame){
                                        if(status==1){
                                            //won
                                            stage.close();
                                            WonGame();
                                        }
                                        else if(status==-1){
                                            stage.close();
                                            Solution();
                                        }
                                        else {
                                            // something wrong
                                            System.out.println("Something went terribly wrong, status and endgame are not in sync");
                                        }
                                    }
                                }
                                catch(Exception E){
                                    E.printStackTrace();
                                }
                            }
                            // if you press right click
                            else if(button==MouseButton.SECONDARY){
                                // here we mark a box as a mine
                                MarkMine(x, y);
                                // sync flags label with moves number
                                Flags.setText("Flags: "+flags+"");
                            }
                            else{
                                System.out.println("some other event");
                            }
                        }
                    });
                }
            }

            // grid layout
            grid.setLayoutX(51);
            grid.setLayoutY(160);

            // add all elements to root pane
            root.getChildren().add(grid);
            root.getChildren().add(Flags);
            root.getChildren().add(Mines);
            root.getChildren().add(timesec);

            // if game in difficulty 1 -> load small window
            if(getDifficulty()==1){
                Scene scene = new Scene(root, 400, 600);
                stage.setScene(scene);
                stage.show();
            }
            // if game in difficulty 2 -> load large window
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

                Button[][] sol = new Button[size][size];

                for(int i=0; i<size; i++){
                    for(int j=0; j<size; j++){   
                        if(getHiddenBoardValue(i,j)==-1){
                            sol[i][j] = new Button("X");
                            sol[i][j].setPrefSize(30,30);
                        }
                        else if(getHiddenBoardValue(i,j)==-2){
                            sol[i][j] = new Button("S");
                            sol[i][j].setPrefSize(30,30);
                        }
                        else{
                            sol[i][j] = new Button(""+getHiddenBoardValue(i,j)+"");
                            sol[i][j].setPrefSize(30,30);
                        }
                    }
                }

                GridPane grid = new GridPane();
                for(int i=0; i<size; i++){
                    for(int j=0; j<size; j++){                      
                        grid.add(sol[i][j], j,i);
                    }
                }

                grid.setLayoutX(50);
                grid.setLayoutY(175);
                root.getChildren().add(grid);
                LostGame();
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

    // fixes the visible board in button array  
    private static void FixVisible(){
        // initialize buttons
        // for every button put the right value 
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(getBoardValue(i,j)==minecode){
                    btn[i][j].setText("X");
                    btn[i][j].setPrefSize(30,30);
                }
                else if(getBoardValue(i,j)==superminecode){
                    btn[i][j].setText("S");
                    btn[i][j].setPrefSize(30,30);
                }
                else if(getBoardValue(i, j)==-10 || getBoardValue(i, j)==0){
                    // -10 code for not visible
                    btn[i][j].setText("");
                    btn[i][j].setPrefSize(30,30);
                }
                else if(getBoardValue(i, j)==200){
                    btn[i][j].setText("F");
                    btn[i][j].setPrefSize(30, 30);
                }
                else{
                    btn[i][j].setText(""+getBoardValue(i, j)+"");
                    btn[i][j].setPrefSize(30,30);
                }
            }
        }
    }

    // Move Method
    private static void Move(int i, int j){
        moves++;

        // if you press a zero calculate which blocks should open as well
        if (boardhidden[i][j]==0){
            FixNeighboors(i,j);
        }
        else{
            boardvisible[i][j] = boardhidden[i][j];
            if (!btn[i][j].isDisable()) btn[i][j].setDisable(true);
        }
        FixVisible();
    }

    // Fix Neighboors (which displayed blocks should change and which not) in case of picking zero
    static void FixNeighboors(int i, int j){

        try{
            if(boardvisible[i][j]!=-10){
                //System.out.println("ivbeenhere before");
                return;
            }

            boardvisible[i][j] = boardhidden[i][j];
            if (!btn[i][j].isDisable()) btn[i][j].setDisable(true);

            if(i!=0){
                //left from current box
                if(boardhidden[i-1][j]==0) {
                    FixNeighboors(i-1,j);
                }
                else{
                    boardvisible[i-1][j] = boardhidden[i-1][j];
                    if (!btn[i-1][j].isDisable()) btn[i-1][j].setDisable(true);
                }
                if(j!=0){
                    //left and down from down box
                    if(boardhidden[i-1][j-1]==0){;
                        FixNeighboors(i-1,j-1);
                    }
                    else{
                        boardvisible[i-1][j-1] = boardhidden[i-1][j-1];
                        if (!btn[i-1][j-1].isDisable()) btn[i-1][j-1].setDisable(true);
                    }

                }
            }

            if(i!=size-1){
                // right from current box
                if(boardhidden[i+1][j]==0) {
                    FixNeighboors(i+1,j);
                }
                else{
                    boardvisible[i+1][j] = boardhidden[i+1][j];
                    if (!btn[i+1][j].isDisable()) btn[i+1][j].setDisable(true);
                }
                if(j!=size-1){
                    //right and up from current box
                    if(boardhidden[i+1][j+1]==0){
                        FixNeighboors(i+1, j+1);
                    } 
                    else{
                        boardvisible[i+1][j+1] = boardhidden[i+1][j+1];
                        if (!btn[i+1][j+1].isDisable()) btn[i+1][j+1].setDisable(true);
                    }
                }
            }

            if(j!=0){
                // down from current box
                
                if(boardhidden[i][j-1]==0) {
                    FixNeighboors(i,j-1);
                }
                else{
                    boardvisible[i][j-1] = boardhidden[i][j-1];
                    if (!btn[i][j-1].isDisable()) btn[i][j-1].setDisable(true);
                }
                if(i!=size-1){
                    // down and right from current box
                    if(boardhidden[i+1][j-1] == 0) {
                        FixNeighboors(i+1,j-1);
                    }
                    else{
                        boardvisible[i+1][j-1] = boardhidden[i+1][j-1];
                        if (!btn[i+1][j-1].isDisable()) btn[i+1][j-1].setDisable(true);
                    }
                }
            }

            if(j!=size-1){
                // up from current box
                
                if(boardhidden[i][j+1]==0) {
                    FixNeighboors(i,j+1);
                }
                else{
                    boardvisible[i][j+1] = boardhidden[i][j+1];
                    if (!btn[i][j+1].isDisable()) btn[i][j+1].setDisable(true);
                }
                if(i!=0){
                    // up and left from current box
                    
                    if(boardhidden[i-1][j+1]==0) {
                        FixNeighboors(i-1,j+1);
                    }
                    else{
                        boardvisible[i-1][j+1] = boardhidden[i-1][j+1];
                        if (!btn[i-1][j+1].isDisable()) btn[i-1][j+1].setDisable(true);
                    }
                }
            }
            return;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // Mark Mine
    private static void MarkMine(int i, int j){
        // if you want to unmark a mine
        if(boardvisible[i][j]==200){
            flags--;
            boardvisible[i][j] = -10;
        }
        // if you want to mark a mine
        else{
            if (flags<mines){
                flags++;
                //for the current button
                boardvisible[i][j] = 200;
            }
            else{
                System.out.println("Cant flag more boxes than the number of mines");
            }
        }
        FixVisible();
    }

    // Lost Game
    private static void LostGame(){
        //lost
        // store results to log
        try{
            FileWriter fw = new FileWriter("roundslog/log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(""+mines+" "+moves+" "+time+" Computer");
            bw.close();

            // clear everything for new game and launch lost window
            InitializeVisibleBoard();
            InitializeButtons();
            InitializeHiddenBoard();
            flags = 0;
            moves = 0;
            status = 0;
            endgame = false;
            LanuchLostPrompt window = new LanuchLostPrompt();
            window.menu();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    // Lost Game
    private static void WonGame(){
        //lost
        // store results to log
        try{
            FileWriter fw = new FileWriter("roundslog/log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(""+mines+" "+moves+" "+time+" Human");
            bw.close();

            // clear everything for new game and launch lost window
            InitializeVisibleBoard();
            InitializeButtons();
            InitializeHiddenBoard();
            flags = 0;
            moves = 0;
            status = 0;
            endgame = false;
            LanuchWonPrompt window = new LanuchWonPrompt();
            window.menu();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
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
