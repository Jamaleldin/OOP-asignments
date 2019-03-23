package eg.edu.alexu.csd.oop.jdbc.cs15.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("GUI1.fxml"));
			 primaryStage.setTitle("Hello World");
		        primaryStage.setScene(new Scene(root));
		        primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
