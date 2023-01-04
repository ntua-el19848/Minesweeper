run: App.class Controllers.class Game.class
	java --module-path "/home/georgesotiropoulos/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml App

Controllers.class: Controllers.java App.class
	javac --module-path "/home/georgesotiropoulos/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml Controllers.java

App.class: App.java
	javac --module-path "/home/georgesotiropoulos/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml App.java
	
Game.class: Game.java
	javac --module-path "/home/georgesotiropoulos/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml Game.java
