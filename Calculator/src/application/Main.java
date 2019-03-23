package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	private static BorderPane root = new BorderPane();

	public static BorderPane getRoot() {
		return root;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent rootFX = FXMLLoader.load(getClass().getResource("/application/MainController.fxml"));
			root.setCenter(rootFX);
			Scene scene = new Scene(root, 600, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
			primaryStage.setTitle("Calculator");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
