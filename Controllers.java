import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javax.swing.JTextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

public class Controllers extends App{

    @FXML
    private GridPane displayed_board;
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
    private TextField supermine;
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


    //application menu
    @FXML
    void ApplicationAction(ActionEvent event) throws Exception{//DONE (IT WORKS)
        stage.close();
        LaunchApplication window = new LaunchApplication();
        window.menu();
    }

        @FXML
        void CreateAction(ActionEvent event) throws Exception{//DONE (IT WORKS)
            LaunchCreate window = new LaunchCreate();
            window.menu();
        }

            @FXML
            void CreateGameAction(ActionEvent event) throws Exception{//DONE (IT WORKS)
                try{
                    String name = scenarioname.getText();
                    File scenario = new File("SCENARIOS/"+name+".txt");
                    scenario.createNewFile();

                    FileWriter fw = new FileWriter(scenario.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);

                    String dif = difficulty.getText();
                    String mn = mines.getText();
                    String t = time.getText();
                    String smn = supermine.getText();

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

        @FXML
        void LoadAction(ActionEvent event) throws Exception{//DONE (IT WORKS)
            LaunchLoad window = new LaunchLoad();
            window.menu();
        }
            
            @FXML
            void LoadGameAction(ActionEvent event) throws Exception{ // button action
                String name = scenario_to_load.getText();
                Game.ValidationCheck(name);
                if(Game.getScenarioValidity()){
                    LaunchStart window = new LaunchStart();
                    window.menu();
                }
            }

        //Menu StartGame
        @FXML
        void StartAction(ActionEvent event) throws Exception{//DONE (IT WORKS)
            LaunchStart window = new LaunchStart();
            window.menu();
        }

            // button start game inside menu
            @FXML
            void StartGameAction(ActionEvent event) throws Exception{
                if(Game.getScenarioValidity()){
                    // to start a valid loaded game
                    Game.setupBoard();
                    Game.StartGame();
                    LaunchGame window = new LaunchGame();
                    window.menu();
                }
                else{
                    // to not start a non valid game
                    LanuchInvalidGameException window = new LanuchInvalidGameException();
                    window.menu();
                }

            }

    @FXML
    void DetailAction(ActionEvent event) throws Exception {
        LaunchDetails window = new LaunchDetails();
        window.menu();
    }

        @FXML
        void SolutionAction(ActionEvent event) {
            
        }

        @FXML
        void RoundsAction(ActionEvent event) {
            
        }

    // exit between inner stages
    @FXML
    void ExitAction(ActionEvent event) throws Exception{
        stage.close();
    }


    // exit from program
    @FXML
    void MainExitAction(ActionEvent event) throws Exception{ 
        System.exit(0);
    }

        @FXML
    void CheckedBox(MouseEvent event) {

    }
};
