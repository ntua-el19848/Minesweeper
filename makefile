run: App.class Controllers.class Game.class
	java --module-path "/Users/georgesotiropoulos/java/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml App

Controllers.class: Controllers.java App.class Game.class
	javac --module-path "/Users/georgesotiropoulos/java/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml Controllers.java

Game.class: Game.java App.class
	javac --module-path "/Users/georgesotiropoulos/java/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml Game.java

App.class: App.java
	javac --module-path "/Users/georgesotiropoulos/java/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml App.java
	