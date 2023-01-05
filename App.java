import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

// THIS .java FILE CONTAINS ALL THE CODE FOR GENERATING JAVAFX WINDOWS AND NOTHING ELSE....
// THE MEANINGFULL CODE THAT RUNS THE BOARD AND THE GAME IS IN THE Game.java

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
}

class LaunchApplication extends App{

    public void menu() throws Exception{
        try{
            //App.close();
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
}

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
}

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
}

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
}

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
}

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
}

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
}