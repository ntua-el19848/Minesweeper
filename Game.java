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
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/*
 * BEFORE EVERY PUBLIC METHOD OF THIS 
 * CLASS THERE IS DOUMENTATION
 * javadoc[3]
 */



// This Class is the game class that manages the initialization, creation, flow of the game
public class Game extends App{
    // Extends App only to inherit the stage

    /*
     *     Methods this class  supports:
     * - Setters, Getters, Initializators...
     *
     * - ValidationCheck (Checks the validity of the scenario and either displayes approriate message or doesnt if all ok!)
     * 
     * - SetupBoard (Setup Board and writes to mines.txt the coordinates of the mines, 
     *   then calls the other functions to print(debug) and fill the rest of the board)
     *
     * - printhiddenboard (prints the board to the console (for debugging only))
     * 
     * - BuildBoard (builds the rest of the board with regard to mine placement)
     * 
     * - WriteBoard (writes board to loadedboard.txt (debugging))
     * 
     * - endGame (checks whether the game has ended or not)
     * 
     * - StartGame (Starts the game)
     * 
     * - Solution (Displayes the solution)
     * 
     * - FixVisible (Displayes the approriate icons in buttons depending of the current state of the board)
     * 
     * - Move (handles clicks from the user)
     * 
     * - FixNeighboors (recursively opens all nearby zeros)
     * 
     * - MarkMine (handles right click -> mark as flag)
     */

    // Private Fields
    static private int difficulty; // holds difficulty
    static private int mines; // number of mines
    static private boolean endgame; // true -> THE GAME HAS ENDED, false -> THE GAME IS BEING PLAYED OR INITIALIZED
    static private int status=0; // status=0 (playing), status=1(won), status=-1(lost)
    static private int size=17; // size of the board
    static private int time; // time for 1 game
    static private int supermine; // 0 -> no supermine, 1 -> supermine (needs to be int for validation)
    static private int [][] boardvisible = new int[size][size]; // visible board --> board that user sees
    static private int [][] boardhidden = new int[size][size]; // hidden board --> board that holds the solution
    static private boolean scenario_validity = false; // holds a boolean about if a scenario is valid or not
    static private Button btn[][] = new Button[size][size]; // button array that holds the board
    static private int moves=0; // moves counter
    static private int flags=0; // flags counter
    static private int time_remaining; // time_remaining that is initialized = time and decreases every second
    static private int nonvisiblemines; // number of nonvisible mines (is required due to reveal of mines from marking supermine)
    static private Timeline timeline; // timeline that decreases the time_remaining

    /*
     * The following are some codes for the hidden and visible board that hold the information
     * about a current box (e.g. boardhidden[1][1]=-1 --> at (1,1) there is a mine). 
     * 
     * Integer codes that mean something by aggreement. Everywhere in the program those codes are referenced
     * by their following names! For good practise
     */
    static final int minecode=-1; // there is a mine here
    static final int superminecode=-2; // there is a supermine here
    static final int nondisplayedcode=-10; // this box hasnt been opened yet 
    static final int flagedcode=200; // this box is currently flagged
    static final int visibleminecode=-5; // this mine has been revealed (only if you mark a supermine)
    static final int visiblesuperminecode=-6; // this supermine has been revealed (only if you mark a supermine)


    // Setters
    /*
     * Public method --> because it is called from other classes (Controllers) that have no inheritance 
     * Returns --> Nothing
     * Purpose --> To Exit the current game that is being played. By stoping the timeline (timer of the game).
     */
    public static void ExitGame(){
        timeline.stop();
        flags = 0;
        moves = 0;
        status = 0;
        endgame = false;
    }

    // sets the difficulty and size of the board
    private static void setDifficulty(int x){
        difficulty = x;
        // initilize size and board based on difficulty
        if(x==1) size=9;
        if(x==2) size=16;
    }
    
    // sets number of mines
    private static void setMines(int x){
        mines = x;
    }

    // sets supermine value
    private static void setSupermine(int x){
        supermine = x;
    }

    // sets time
    private static void setTime(int x){
        time = x;
    }

    // sets scenario_validity
    private static void setScenarioValidity(boolean x){
        scenario_validity = x;
    }

    // Initialized visible board with nondisplaycode
    private static void InitializeVisibleBoard(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                // code for visible initialazation
                boardvisible[i][j]=nondisplayedcode;
            }
        }
    }

    // initializes buttons
    private static void InitializeButtons(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                // code for visible initialazation
                btn[i][j] = new Button();
            }
        }
    }

    // initializes hidden board with zeros
    private static void InitializeHiddenBoard(){
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                // code for visible initialazation
                boardhidden[i][j]=0;
            }
        }
    }


    // Getters
    
    // returns difficulty
    private static int getDifficulty(){
        return difficulty;
    }

    // returns number of mines
    private static int getMines(){
        return mines;
    }

    // returns
    private static int getSupermine(){
        return supermine;
    }
    
    // returns time
    private static int getTime(){
        return time;
    }

    /*
     * Public method -> because it is called by controllers for whether to start the game or display appropriate pop up message
     * Returns --> boolean value (true -> Game valid, false -> not valid)
     */
    public static boolean getScenarioValidity(){
        return scenario_validity;
    }

    //returns visible board value for certain coordinates
    private static int getBoardValue(int i, int j){
        return boardvisible[i][j];
    }

    //returns hidden board value for certain coordinates
    private static int getHiddenBoardValue(int i, int j){
        return boardhidden[i][j];
    }

    // Other Methods

    // Initializes the variables from input and checks for the right values.
    /*
     * 
     */
    public static void ValidationCheck(String scenario) throws Exception { 
        try{
            // read from file with name of parameter
            File input = new File("medialab/"+scenario+".txt");
            Scanner scan = new Scanner(input);

            // initialize with -1 just for the description check. if it is left -1 --> it means lines are missing
            setDifficulty(-1);
            setMines(-1);
            setTime(-1);
            setSupermine(-1);

            // Initialize everything - start brand new (no junk values from previous game)
            InitializeVisibleBoard();
            InitializeButtons();
            flags = 0;
            moves = 0;
            
            // Get every value from txt
            if(scan.hasNext()) setDifficulty(scan.nextInt());
            if(scan.hasNext()) setMines(scan.nextInt());
            nonvisiblemines=mines;
            if(scan.hasNext()) setTime(scan.nextInt());
            if(scan.hasNext()) setSupermine(scan.nextInt());
            scan.close();

            // Invalid Description Exception Check!
            if (getDifficulty() == -1 || getMines() == -1 || getTime() == -1 || getSupermine() == -1){
                LaunchDescriptionException window = new LaunchDescriptionException();
                window.menu();
                throw new InvalidDescriptionException("The description provided is not valid as it does not contain information about all attributes of the game (difficulty, mines, time, supermine)");
            }

            // Invalid Value Exception Check!
            // Initalize valid = true
            boolean valid=true;
            switch(getDifficulty()){
                // constaints for easy game
                case 1: {if (getMines() > 11 || getMines() < 9 || getTime() < 120 || getTime() > 180 || getSupermine() != 0){
                                valid=false;
                            }
                            else break;
                        }
                // constaints for hard game
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
    /*
     * 
     */
    public static void setupBoard() throws Exception{ 
        InitializeHiddenBoard();
        InitializeButtons();
        InitializeVisibleBoard();
        //intialize board with mines
        //and write the coordinates of the mines in the mines.txt file
        //this function only randomly generates mine placement and does not fill the entire board!

        // writes coordinates of mines to mines.txt 
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

        bw.close(); // close fd
        BuildBoard(); // build the rest of the board according to mine placement
        printhiddenboard(); // debugging purpose
        WriteBoard(); // writes board to loadedboard.txt
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
        // if some mine is pressed endgame with status -1 -> LOST
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if (boardvisible[i][j]==minecode || boardvisible[i][j]==superminecode){
                    status = -1;
                    return true;
                }

            }
        }

        // for every box that is not visible or flaged count up -> if the count = with the number of non visible mines then -> WON
        int cnt = 0;
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if (boardvisible[i][j]==nondisplayedcode || boardvisible[i][j]==flagedcode) cnt++;
            }
        }
        if (cnt==nonvisiblemines){
            status = 1;
            return true;
        }
        // else the game is still playing
        return false;
    }

    // method that runs when game is being started
    /*
     * 
     */
    public static void StartGame() throws Exception{
        try{
            // initialize root Pane 
            Pane root = new Pane();
            // initialize stage
            root = FXMLLoader.load(Game.class.getResource("FXML/board.fxml"));
            stage.setTitle("Minesweeper Game");


            // initialize time_remaining = time
            time_remaining=time;

            // label for time
            Label timesec = new Label("Time: "+time+"");
            timesec.setLayoutX(50);
            timesec.setLayoutY(115);
            timesec.setFont(new Font("MesloLGS NF Bold", 16));

            // Timeline that makes the time running
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                // every second reduce the time_remaining
                try{
                    time_remaining--;
                    timesec.setText("Time: "+time_remaining+"");
                    // if time_remaining = 0 -> you lost
                    if(time_remaining==0){
                        Solution(); // open solution
                        LostGame(); // register as loss and pop up lost message
                        ExitGame(); // leave no junk and stop time
                    }
                } 
                catch (Exception e) {
                    System.out.println("Something wrong in the timeline!");
                    e.printStackTrace();
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
                            // for recognising the left click and right click
                            MouseButton button = event.getButton();

                            // if you press left click
                            if(button==MouseButton.PRIMARY){
                                try{
                                    // register and handle the move via Move method (fix neighboors etc)
                                    Move(x,y);

                                    // sync flags
                                    Flags.setText("Flags: "+flags+"");

                                    // check if this move ended the game
                                    endgame = endGameMove();

                                    // in case of ending game
                                    if (endgame){
                                        if(status==1){
                                            //won
                                            Solution(); // preview the solution
                                            WonGame(); // pop up win message and register as win
                                            ExitGame(); // leave no junk and stop time
                                        }
                                        else if(status==-1){
                                            // lost
                                            Solution(); // preview the solution
                                            LostGame(); // pop up lost message and register as loss
                                            ExitGame(); // leave no junk and stop time
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
            grid.setLayoutX(30);
            grid.setLayoutY(160);

            // add all elements to root pane
            root.getChildren().add(grid);
            root.getChildren().add(Flags);
            root.getChildren().add(Mines);
            root.getChildren().add(timesec);
            
            // Button exit game that is in the game stage
            Button exitgame = new Button("Exit Game");
            exitgame.setPrefSize(90, 40);

            // exit game button event
            exitgame.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try{
                        // go to application window
                        LaunchApplication window = new LaunchApplication();
                        window.menu();
                        // stop the time from running
                        ExitGame(); // leave no junk and stop time
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    
                }
            });

            // Solution Button
            Button solutionbutton = new Button("Solution");
            solutionbutton.setPrefSize(90, 40);
            
            // Solution Button event
            solutionbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try{
                        Solution(); // view solution
                        LostGame(); // mark as loss and pop up lost prompt
                        ExitGame(); // leave no junk and stop time
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

            // add buttons to pane
            root.getChildren().add(exitgame);
            root.getChildren().add(solutionbutton);

            // if game in difficulty 1 -> load small window
            if(getDifficulty()==1){
                Scene scene = new Scene(root, 420, 620);
                // grid layout
                exitgame.setLayoutX(30);
                exitgame.setLayoutY(550);

                solutionbutton.setLayoutX(250);
                solutionbutton.setLayoutY(550);

                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            }
            // if game in difficulty 2 -> load large window
            else{
                Scene scene = new Scene(root, 700, 920);

                exitgame.setLayoutX(30);
                exitgame.setLayoutY(850);

                solutionbutton.setLayoutX(250);
                solutionbutton.setLayoutY(850);

                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // method called to retrieve solution
    /*
     * 
     */
    public static void Solution() throws Exception{
        // the game has to be played once to show solution
        try{
            // if board is empty do not show zeros as solution -> POP UP prompt with no solution
            boolean boardempty=true;
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    if(boardhidden[i][j]!=0){
                        boardempty = false;
                        break;
                    }
                }
            }
            if (boardempty){
                LaunchSolutionEmpty window = new LaunchSolutionEmpty();
                window.menu();
                return;
            }

            // if not empty

            // load pane and FXML
            Pane root = new Pane();
            root = FXMLLoader.load(Game.class.getResource("FXML/solution.fxml"));
            stage.setTitle("Minesweeper Game");

            // initialize buttons array 
            Button[][] sol = new Button[size][size];
            Image mine = new Image(Game.class.getResourceAsStream("images/mine.jpg"));
            Image supermine = new Image(Game.class.getResourceAsStream("images/supermine.jpg"));
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){   
                    if(getHiddenBoardValue(i,j)==minecode){
                        sol[i][j] = new Button();
                        sol[i][j].setPrefSize(40,40);
                        sol[i][j].setGraphic(new ImageView(mine));
                        sol[i][j].setDisable(true);
                    }
                    else if(getHiddenBoardValue(i,j)==superminecode){
                        sol[i][j] = new Button();
                        sol[i][j].setPrefSize(40,40);
                        sol[i][j].setGraphic(new ImageView(supermine));
                        sol[i][j].setDisable(true);
                    }
                    else{
                        sol[i][j] = new Button(""+getHiddenBoardValue(i,j)+"");
                        sol[i][j].setPrefSize(40,40);
                        sol[i][j].setDisable(true);
                    }
                }
            }

            GridPane grid = new GridPane();
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){                      
                    grid.add(sol[i][j], j,i);
                }
            }

            grid.setLayoutX(30);
            grid.setLayoutY(160);
            root.getChildren().add(grid);
            
            if(getDifficulty()==1){
                Scene scene = new Scene(root, 420, 600);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
            else{
                Scene scene = new Scene(root, 700, 900);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }

        
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // fixes the visible board in button array  
    private static void FixVisible(){

        // load images for flag, mine, supermine
        Image mine = new Image(Game.class.getResourceAsStream("images/mine.jpg"));
        Image supermine = new Image(Game.class.getResourceAsStream("images/supermine.jpg"));
        Image flag = new Image(Game.class.getResourceAsStream("images/flag.jpg"));

        // for every button
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                // if mine or visible mine -> load mine image
                if(getBoardValue(i,j)==minecode || getBoardValue(i, j)==visibleminecode){
                    btn[i][j].setPrefSize(40,40);
                    btn[i][j].setGraphic(new ImageView(mine));
                    if (!btn[i][j].isDisable()) btn[i][j].setDisable(true);
                }
                // if supermine or visible supermine -> load mine image
                else if(getBoardValue(i,j)==superminecode || getBoardValue(i, j)==visiblesuperminecode){
                    btn[i][j].setPrefSize(40,40);
                    btn[i][j].setGraphic(new ImageView(supermine));
                    if (!btn[i][j].isDisable()) btn[i][j].setDisable(true);
                }
                // if zero -> empty box (opened)
                else if(getBoardValue(i, j)==0){
                    btn[i][j].setGraphic(new ImageView());
                    btn[i][j].setPrefSize(40,40);
                    if (!btn[i][j].isDisable()) btn[i][j].setDisable(true);
                }
                // if not opened -> not opened, not disabled
                else if(getBoardValue(i, j)==nondisplayedcode){
                    btn[i][j].setGraphic(new ImageView());
                    btn[i][j].setPrefSize(40,40);
                }
                // if flagged -> load flag image
                else if(getBoardValue(i, j)==flagedcode){
                    btn[i][j].setPrefSize(40, 40);
                    btn[i][j].setGraphic(new ImageView(flag));
                }
                // else load value number
                else{
                    btn[i][j].setGraphic(new ImageView());
                    btn[i][j].setText(""+getBoardValue(i, j)+"");
                    btn[i][j].setPrefSize(40,40);
                    if (!btn[i][j].isDisable()) btn[i][j].setDisable(true);
                }
            }
        }
    }

    // Move Method
    private static void Move(int i, int j){
        // move counter increase
        moves++;

        // if you press a zero calculate which blocks should open as well
        if (boardhidden[i][j]==0){
            FixNeighboors(i,j);
        }
        else{
            if(boardvisible[i][j]==flagedcode) flags--;
            boardvisible[i][j] = boardhidden[i][j];
        }
        FixVisible();
    }

    // Fix Neighboors (which displayed blocks should change and which not) in case of picking zero
    static void FixNeighboors(int i, int j){

        try{
            if(boardvisible[i][j]!=nondisplayedcode && boardvisible[i][j]!=flagedcode){
                return;
            }

            if(boardvisible[i][j]==flagedcode) flags--;
            boardvisible[i][j] = boardhidden[i][j];

            if(i!=0){
                //left from current box
                if(boardhidden[i-1][j]==0) {
                    FixNeighboors(i-1,j);
                }
                else{
                    boardvisible[i-1][j] = boardhidden[i-1][j];
                }
                if(j!=0){
                    //left and down from down box
                    if(boardhidden[i-1][j-1]==0){;
                        FixNeighboors(i-1,j-1);
                    }
                    else{
                        boardvisible[i-1][j-1] = boardhidden[i-1][j-1];
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
                }
                if(j!=size-1){
                    //right and up from current box
                    if(boardhidden[i+1][j+1]==0){
                        FixNeighboors(i+1, j+1);
                    } 
                    else{
                        boardvisible[i+1][j+1] = boardhidden[i+1][j+1];
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
                }
                if(i!=size-1){
                    // down and right from current box
                    if(boardhidden[i+1][j-1] == 0) {
                        FixNeighboors(i+1,j-1);
                    }
                    else{
                        boardvisible[i+1][j-1] = boardhidden[i+1][j-1];
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
                }
                if(i!=0){
                    // up and left from current box
                    
                    if(boardhidden[i-1][j+1]==0) {
                        FixNeighboors(i-1,j+1);
                    }
                    else{
                        boardvisible[i-1][j+1] = boardhidden[i-1][j+1];
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
        if(boardvisible[i][j]==flagedcode){
            flags--;
            boardvisible[i][j] = nondisplayedcode;
        }
        // if you want to mark a mine
        else{
            if (flags<mines){
                flags++;
                // if you flag a supermine in the first 4 moves
                if(boardhidden[i][j]==superminecode && moves<5){
                    for(int a=0; a<size; a++){
                        if(boardvisible[i][a]==flagedcode) flags--;
                        if(boardvisible[a][j]==flagedcode) flags--;
                        boardvisible[i][a]=boardhidden[i][a];
                        boardvisible[a][j]=boardhidden[a][j];
                        if (!btn[i][a].isDisable()) btn[i][a].setDisable(true);
                        if (!btn[a][j].isDisable()) btn[a][j].setDisable(true);
                        
                        if(boardhidden[i][a]==minecode){
                            boardvisible[i][a]=visibleminecode;
                            nonvisiblemines--;
                        }
                        if(boardhidden[a][j]==minecode){
                            boardvisible[a][j]=visibleminecode;
                            nonvisiblemines--;
                        }
                    }
                    boardvisible[i][j]=visiblesuperminecode;
                    //because it passes two times from supermine
                    nonvisiblemines++;
                }
                // if you dont mark a supermine
                else{
                    //for the current button
                    boardvisible[i][j] = flagedcode;
                }
                
            }
            else{
                System.out.println("Cant flag more boxes than the number of mines");
            }
        }
        FixVisible();
    }

    // Lost Game
    public static void LostGame(){
        //lost
        // store results to log
        try{
            FileWriter fw = new FileWriter("roundslog/log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(""+mines+" "+moves+" "+time+" Computer");
            bw.close();


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
