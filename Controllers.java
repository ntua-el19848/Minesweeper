import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controllers extends App{

    @FXML
    private Button createButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button loadButton;
    @FXML
    private Button startButton;
    @FXML
    private Button applicationButton;
    @FXML
    private Button detailsButton;
    @FXML
    private Button creategameButton;//inside create game menu
    @FXML
    private TextField mines;
    @FXML
    private TextField time;
    @FXML
    private TextField scenarioname;
    @FXML
    private TextField difficulty;
    @FXML
    private Button loadgameButton;
    @FXML
    private Button solutionButton;
    @FXML
    private Button roundsButton;
    @FXML
    private TextField scenario_to_load;
    @FXML
    private Button startgameButton;


    //application menu button
    @FXML
    void ApplicationAction(ActionEvent event) throws Exception{//DONE (IT WORKS)
        stage.close();
        LaunchApplication window = new LaunchApplication();
        window.menu();
    }

    // create menu button
    @FXML
    void CreateAction(ActionEvent event) throws Exception{//DONE (IT WORKS)
        LaunchCreate window = new LaunchCreate();
        window.menu();
    }

    // create description button
    @FXML
    void CreateGameAction(ActionEvent event) throws Exception{//DONE (IT WORKS)
        try{
            String name = scenarioname.getText();
            File scenario = new File("medialab/"+name+".txt");
            scenario.createNewFile();

            FileWriter fw = new FileWriter(scenario.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            String dif = difficulty.getText();
            String mn = mines.getText();
            String t = time.getText();
            String smn;

            if(dif.equals("1")){
                smn="0";
            }   
            else if(dif.equals("2")){
                smn="1";
            }
            else{
                smn="-1";
            }

            bw.write(dif);
            bw.write("\n");
            bw.write(mn);
            bw.write("\n");
            bw.write(t);
            bw.write("\n");
            bw.write(smn);
            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        LaunchApplication applicationWindow = new LaunchApplication();
        applicationWindow.menu();
    }

    // Load menu button
    @FXML
    void LoadAction(ActionEvent event) throws Exception{//DONE (IT WORKS)
        LaunchLoad window = new LaunchLoad();
        window.menu();
    }
    
    // Load description button    
    @FXML
    void LoadGameAction(ActionEvent event) throws Exception{ // button action
        String name = scenario_to_load.getText();
        Game.ValidationCheck(name);
        if(Game.getScenarioValidity()){
            LaunchStart window = new LaunchStart();
            window.menu();
        }
    }

    // Start menu button
    @FXML
    void StartAction(ActionEvent event) throws Exception{//DONE (IT WORKS)
        LaunchStart window = new LaunchStart();
        window.menu();
    }

    // Start game button
    @FXML
    void StartGameAction(ActionEvent event) throws Exception{
        if(Game.getScenarioValidity()){
            // to start a valid loaded game
            Game.setupBoard();
            Game.StartGame();
        }
        else{
            // to not start a non valid game
            LanuchInvalidGameException window = new LanuchInvalidGameException();
            window.menu();
        }
    }

    // Details menu button
    @FXML
    void DetailAction(ActionEvent event) throws Exception {
        LaunchDetails window = new LaunchDetails();
        window.menu();
    }

    // Solution button
    @FXML
    void SolutionAction(ActionEvent event) throws Exception {
        Game.Solution();
    }

    @FXML
    void RoundsAction(ActionEvent event) throws Exception{
        LaunchRounds window = new LaunchRounds();
        window.menu();
    }

    @FXML
    void RulesAction(ActionEvent event) throws Exception{
        LaunchRules window = new LaunchRules();
        window.menu();
    }

    // exit from program
    @FXML
    void MainExitAction(ActionEvent event) throws Exception{ 
        System.exit(0);
    }
};
