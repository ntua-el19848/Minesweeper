import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

// THIS .java FILE CONTAINS ALL THE CODE FOR GENERATING JAVAFX WINDOWS AND NOTHING ELSE....
// THE MEANINGFULL CODE THAT RUNS THE BOARD AND THE GAME IS IN THE Game.java


// App class that is first loaded and loads the main Menu of the game
public class App extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }

    protected static Stage stage = new Stage();

    //only this window doesnot close after going to other scene. the other buttons automatically close the previous window
    @Override
    public void start(Stage stage) throws Exception{
        try{
            Parent root = FXMLLoader.load(getClass().getResource("FXML/mainMenu.fxml"));
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(new Scene(root, 400, 600));
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void close(){
        stage.close();
    }
};

// Class that loads the application menu
class LaunchApplication extends App{
    public void menu() throws Exception{
        try{
            LaunchCreate.close();//if coming from create
            LaunchLoad.close();//if coming from load
            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/applicationMenu.fxml"));
            Scene scene = new Scene(root, 400, 600);
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void close(){
        stage.close();
    }
};

// Class that loads the create menu to create a game description
class LaunchCreate extends App{
    public void menu() throws Exception{
        try{
            LaunchApplication.close();
            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/createMenu.fxml"));
            Scene scene = new Scene(root, 400, 600);
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void close(){
        stage.close();
    }
};

// Class that loads the load menu to load a game description (checks validity)
class LaunchLoad extends App{
    public void menu() throws Exception{
        try{
            LaunchApplication.close();
            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/loadMenu.fxml"));
            Scene scene = new Scene(root, 400, 600);
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void close(){
        stage.close();
    }
};

// Class that loads the exception window when values in desctiption do not meet the requirements
class LaunchValueException extends App{
    
    public void menu() throws Exception{
        try{
            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/InvalidValueException.fxml"));
            Scene scene = new Scene(root, 300, 150);
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
};

// Class that loads the exception window when description does not meet the requirements
class LaunchDescriptionException extends App{
    
    public void menu() throws Exception{
        try{
            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/InvalidDescriptionException.fxml"));
            Scene scene = new Scene(root, 300, 150);
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
};

// Class that loads the exception window when desctiption not found
class LaunchFileException extends App{
    
    public void menu() throws Exception{
        try{
            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/FileNotFoundException.fxml"));
            Scene scene = new Scene(root, 300, 150);
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
};

// Class that loads the start game window
class LaunchStart extends App{
    
    public void menu() throws Exception{
        try{
            LaunchApplication.close();
            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/startMenu.fxml"));
            Scene scene = new Scene(root, 400, 600);
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void close(){
        stage.close();
    }
};

// Class that loads the details window
class LaunchDetails extends App{
    
    public void menu() throws Exception{
        try{
            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/detailsMenu.fxml"));
            Scene scene = new Scene(root, 400, 600);
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void close(){
        stage.close();
    }
};  

// Class that loads when you have lost a game
class LanuchLostPrompt extends App{
    
    public void menu() throws Exception{
        try{
            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/lost.fxml"));
            Scene scene = new Scene(root, 300, 150);
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
};

// Class that loads the exception window when trying to start an invalid game (or not loaded game)
class LanuchInvalidGameException extends App{
    
    public void menu() throws Exception{
        try{
            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/invalidGame.fxml"));
            Scene scene = new Scene(root, 300, 150);
            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
};

class LaunchRounds extends App{
    
    public void menu() throws Exception{
        try{
            BufferedReader br = new BufferedReader(new FileReader("roundslog/log"));
            List<String> lines = new LinkedList<String>();
            for(String tmp; (tmp = br.readLine()) != null;)
            if (lines.add(tmp) && lines.size() > 5){ 
                lines.remove(0);
            }
            br.close();

            String[] game = new String[5];
            for(int i=0; i<5; i++){
               game[4-i] = lines.get(i);
            } 
            
            Scanner[] sc = new Scanner[5];
            for(int i=0; i<5; i++){
                sc[i] = new Scanner(game[i]);
             }
            
            // at this stage i ve got the strings as i want them. Now i have to extract info
            // mine, moves, time, winner
            String[][] rounds = new String[5][4]; 
            for(int i=0; i<5; i++){
                for (int j=0; j<4; j++){
                    rounds[i][j] = sc[i].next();
                }
            }

            Pane root;
            root = FXMLLoader.load(getClass().getResource("FXML/rounds.fxml"));
            Scene scene = new Scene(root, 400, 600);
        
            // Mines labels
            Label mine1 = new Label("Mines: "+rounds[0][0]+"");
            Label mine2 = new Label("Mines: "+rounds[1][0]+"");
            Label mine3 = new Label("Mines: "+rounds[2][0]+"");
            Label mine4 = new Label("Mines: "+rounds[3][0]+"");
            Label mine5 = new Label("Mines: "+rounds[4][0]+"");

            // Moves labels
            Label moves1 = new Label("Moves: "+rounds[0][1]+"");
            Label moves2 = new Label("Moves: "+rounds[1][1]+"");
            Label moves3 = new Label("Moves: "+rounds[2][1]+"");
            Label moves4 = new Label("Moves: "+rounds[3][1]+"");
            Label moves5 = new Label("Moves: "+rounds[4][1]+"");  

            // Time labels
            Label time1 = new Label("Time: "+rounds[0][2]+"");
            Label time2 = new Label("Time: "+rounds[1][2]+"");
            Label time3 = new Label("Time: "+rounds[2][2]+"");
            Label time4 = new Label("Time: "+rounds[3][2]+"");
            Label time5 = new Label("Time: "+rounds[4][2]+"");

            // Winners labels
            Label winner1 = new Label("Winner: "+rounds[0][3]+"");
            Label winner2 = new Label("Winner: "+rounds[1][3]+"");
            Label winner3 = new Label("Winner: "+rounds[2][3]+"");
            Label winner4 = new Label("Winner: "+rounds[3][3]+"");
            Label winner5 = new Label("Winner: "+rounds[4][3]+"");
            
            // allignemnt
            int alx = 25;
            int dist = 80;


            //MINES
            mine1.setLayoutX(alx);
            mine2.setLayoutX(alx);
            mine3.setLayoutX(alx);
            mine4.setLayoutX(alx);
            mine5.setLayoutX(alx);
            
            mine1.setLayoutY(150);
            mine2.setLayoutY(250);
            mine3.setLayoutY(350);
            mine4.setLayoutY(450);
            mine5.setLayoutY(550);

            //MOVES
            moves1.setLayoutX(alx+dist);
            moves2.setLayoutX(alx+dist);
            moves3.setLayoutX(alx+dist);
            moves4.setLayoutX(alx+dist);
            moves5.setLayoutX(alx+dist);
            
            moves1.setLayoutY(150);
            moves2.setLayoutY(250);
            moves3.setLayoutY(350);
            moves4.setLayoutY(450);
            moves5.setLayoutY(550);

            //TIME
            time1.setLayoutX(alx+2*dist);
            time2.setLayoutX(alx+2*dist);
            time3.setLayoutX(alx+2*dist);
            time4.setLayoutX(alx+2*dist);
            time5.setLayoutX(alx+2*dist);
            
            time1.setLayoutY(150);
            time2.setLayoutY(250);
            time3.setLayoutY(350);
            time4.setLayoutY(450);
            time5.setLayoutY(550);

            //WINNER
            winner1.setLayoutX(alx+3*dist);
            winner2.setLayoutX(alx+3*dist);
            winner3.setLayoutX(alx+3*dist);
            winner4.setLayoutX(alx+3*dist);
            winner5.setLayoutX(alx+3*dist);
            
            winner1.setLayoutY(150);
            winner2.setLayoutY(250);
            winner3.setLayoutY(350);
            winner4.setLayoutY(450);
            winner5.setLayoutY(550);

            mine1.setFont(new Font("MesloLGS NF Bold", 12));
            mine2.setFont(new Font("MesloLGS NF Bold", 12));
            mine3.setFont(new Font("MesloLGS NF Bold", 12));
            mine4.setFont(new Font("MesloLGS NF Bold", 12));
            mine5.setFont(new Font("MesloLGS NF Bold", 12));

            moves1.setFont(new Font("MesloLGS NF Bold", 12));
            moves2.setFont(new Font("MesloLGS NF Bold", 12));
            moves3.setFont(new Font("MesloLGS NF Bold", 12));
            moves4.setFont(new Font("MesloLGS NF Bold", 12));
            moves5.setFont(new Font("MesloLGS NF Bold", 12));

            time1.setFont(new Font("MesloLGS NF Bold", 12));
            time2.setFont(new Font("MesloLGS NF Bold", 12));
            time3.setFont(new Font("MesloLGS NF Bold", 12));
            time4.setFont(new Font("MesloLGS NF Bold", 12));
            time5.setFont(new Font("MesloLGS NF Bold", 12));

            winner1.setFont(new Font("MesloLGS NF Bold", 12));
            winner2.setFont(new Font("MesloLGS NF Bold", 12));
            winner3.setFont(new Font("MesloLGS NF Bold", 12));
            winner4.setFont(new Font("MesloLGS NF Bold", 12));
            winner5.setFont(new Font("MesloLGS NF Bold", 12));

            root.getChildren().add(mine1);
            root.getChildren().add(mine2);
            root.getChildren().add(mine3);
            root.getChildren().add(mine4);
            root.getChildren().add(mine5);

            root.getChildren().add(moves1);
            root.getChildren().add(moves2);
            root.getChildren().add(moves3);
            root.getChildren().add(moves4);
            root.getChildren().add(moves5);

            root.getChildren().add(time1);
            root.getChildren().add(time2);
            root.getChildren().add(time3);
            root.getChildren().add(time4);
            root.getChildren().add(time5);

            root.getChildren().add(winner1);
            root.getChildren().add(winner2);
            root.getChildren().add(winner3);
            root.getChildren().add(winner4);
            root.getChildren().add(winner5);

            stage.setTitle("Minesweeper Medialab");
            stage.setScene(scene);
            stage.show();

        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
};