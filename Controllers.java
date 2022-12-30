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
    private TextField supermine;
    @FXML
    private TextField difficulty;
    @FXML
    private Button loadgameButton;
    @FXML
    private TextField scenario;

    //application menu
    @FXML
    void ApplicationAction(ActionEvent event) throws Exception{
        //na kleinei to proigoumeno
        LaunchApplication window = new LaunchApplication();
        window.menu();
    }

        @FXML
        void CreateAction(ActionEvent event) throws Exception{
            //na kleinei to proigoumeno
            LaunchCreate window = new LaunchCreate();
            window.menu();
        }

            @FXML
            void CreateGameAction(ActionEvent event) throws Exception{
                //here the file scenario will be created  (IT WORKS!!!!)
                try{
                    File scenario = new File("SCENARIOS/scenario_id.txt");
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
                //THELW NA ME PETAEI PISO STO LAUNCH APPLICATION

                LaunchApplication applicationWindow = new LaunchApplication();
                applicationWindow.menu();
            }


        @FXML
        void LoadAction(ActionEvent event) throws Exception{
            LaunchLoad window = new LaunchLoad();
            window.menu();
        }
            
            @FXML
            void LoadGameAction(ActionEvent event) throws Exception{
                // here a random game will be created....
            }

        @FXML
        void StartAction(ActionEvent event) {

        }

    @FXML
    void DetailAction(ActionEvent event) {

    }

    @FXML
    void ExitAction(ActionEvent event) throws Exception{
        super.stage.close();
    }







 


}
