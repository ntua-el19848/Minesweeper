import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
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
import java.io.File;
import java.util.Scanner;

public class Game{

    //initialize the input with -1 so that i know that if some of these values remain -1 --> InvalidDescriptionException
    private String scenario;
    private int difficulty=-1;
    private int mines=-1;
    private int size;
    private int time=-1;
    private int supermine=-1;
    private int [][] boardvisible;
    private int [][] boardhidden;
    
    //setters
    public void setDifficulty(int x){
        this.difficulty = x;
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

    //getters
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



    // constructor that initializes the variables from input and checks for the right values.
    public Game(String scenario) throws Exception {
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
            else{
                // edw einai pou tha sinexisei to flow tou programmatos sthn periptwsh pou ola komple.... (ta leme aurio)
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}

class InvalidValueException extends Exception
{
    public InvalidValueException(String message)
    {
        super(message);
    }
}

class InvalidDescriptionException extends Exception
{
    public InvalidDescriptionException(String message)
    {
        super(message);
    }
}