package application;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import eg.edu.alexu.csd.oop.calculator.cs15.Calculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class MainController {
	@FXML
	JFXTextArea operation;
	@FXML
	Label result;
	@FXML
	Label status;
	@FXML
	JFXButton prev;
	@FXML
	JFXButton next;
	@FXML
	JFXButton current;
	@FXML
	JFXButton save;
	@FXML
	JFXButton load;
	Calculator c = new Calculator();

	public void takeInput(KeyEvent event) {
		if (event.getCode().isDigitKey()) {
			if (operation.getText().equals(null)) {
				operation.setText(event.getText());
			} else {
				operation.setText(operation.getText() + event.getText());
			}
		}
		switch (event.getCode()) {
		case DECIMAL:
			operation.setText(operation.getText() + event.getText());
			break;
		case ADD:
			operation.setText(operation.getText() + event.getText());
			break;
		case SUBTRACT:
			operation.setText(operation.getText() + event.getText());
			break;
		case ASTERISK:
			operation.setText(operation.getText() + event.getText());
			break;
		case DIVIDE:
			operation.setText(operation.getText() + event.getText());
			break;
		case BACK_SPACE:
			if (operation.getText().length() != 0) {
				operation.setText(operation.getText().substring(0, operation.getText().length() - 1));
			}
			break;
		case ENTER:
			if (check(operation.getText())) {
				c.input(operation.getText());
				result.setText(c.getResult());
				status.setText(null);
			} else {
				status.setText("Wrong formate!");
				status.setTextFill(Color.RED);
			}
			break;
		default:
			break;
		}
	}

	public void getprev(ActionEvent event) {
		String s = c.prev();
		if (s != null) {
			operation.setText(s);
			result.setText(null);
			status.setText(null);
		} else {
			status.setText("There is no Perevious!");
			status.setTextFill(Color.RED);
		}
	}

	public void getNext(ActionEvent event) {
		String s = c.next();
		if (s != null) {
			operation.setText(s);
			status.setText(null);
			result.setText(null);
		} else {
			status.setText("There is no Next!");
			status.setTextFill(Color.RED);
		}
	}

	public void getCurrent(ActionEvent event) {
		String s = c.current();
		if (s != null) {
			operation.setText(s);
			status.setText(null);
			result.setText(null);
		} else {
			status.setText("There is no history!");
			status.setTextFill(Color.RED);
		}
	}

	public void save(ActionEvent event) {
		c.save();
		status.setText("History saved");
		status.setTextFill(Color.GREEN);
	}

	public void load(ActionEvent event) {
		File f = new File("operations.txt");
		if (f.exists()) {
			c.load();
			status.setText("History loaded");
			status.setTextFill(Color.GREEN);
		} else {
			status.setText("There is no saved data");
			status.setTextFill(Color.RED);
		}

	}

	private boolean check(String s) {
		String pattern = "^(-)?\\d+(\\.\\d+)?[+-/*](-)?\\d+(\\.\\d+)?$";
		Pattern p = Pattern.compile(pattern);
		Matcher regexMatcher = p.matcher(s);
		return regexMatcher.matches();
	}

}
