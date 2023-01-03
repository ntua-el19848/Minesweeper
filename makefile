run: App.class Controllers.class
	java --module-path "/home/georgesotiropoulos/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml App

Controllers.class: Controllers.java App.class
	javac --module-path "/home/georgesotiropoulos/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml Controllers.java

App.class: App.java
	rm *.class
	javac --module-path "/home/georgesotiropoulos/javafx-sdk-19/lib" --add-modules javafx.controls,javafx.fxml App.java
	

