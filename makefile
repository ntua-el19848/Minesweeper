run: App.class Controllers.class
	java --module-path "/Users/georgesotiropoulos/java/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml App

Controllers.class: Controllers.java App.class
	javac --module-path "/Users/georgesotiropoulos/java/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml Controllers.java

App.class: App.java
	javac --module-path "/Users/georgesotiropoulos/java/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml App.java
	

