package eg.edu.alexu.csd.oop.jdbc.cs15.gui;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import eg.edu.alexu.csd.oop.db.Database;
import eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39.CDataBase;
import eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39.CheckSyntax;
import eg.edu.alexu.csd.oop.jdbc.cs15.JDriver;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Alert.AlertType;

public class controller {
	@FXML
	private javafx.scene.control.Label commandLine;
	@FXML
	private javafx.scene.control.Label result;
	@FXML
	private TextArea resultArea;
	@FXML
	private Button excute;
	@FXML
	private javafx.scene.control.TextField pathText;
	@FXML
	private Button clearBatchButton;

	@FXML
	private TextField commandArea;

	@FXML
	private Button addButton;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Button excuteBatchButtonS;
	@FXML
	private ObservableList<ObservableList> data;
	@FXML
	private TableView tableview;
	@FXML
	private Button closeButton;
	private Driver driver;
	private Connection connection;
	
	
	private Statement statement;
	private CheckSyntax check;
	private Database db;
	//private ResultSet resultSet ;
	private ObservableList<ObservableList> tableData;

	public void setter() {
		if (driver == null) {

			Properties properties = new Properties();
			properties.put("path", pathText.getText());
			try {
				connection = driver.connect("DBMS", properties);
				statement = connection.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			check = new CheckSyntax();
		}
	}


	@FXML
	private void browseButtonHandler(ActionEvent event) {
		driver = new JDriver();
		final DirectoryChooser dirchoose = new DirectoryChooser();
		Stage stage = (Stage) anchorPane.getScene().getWindow();
		File file = dirchoose.showDialog(stage);
		if (file != null) {
			pathText.setText(file.getAbsolutePath());
			Properties properties = new Properties();
			properties.put("path", file);
			try {
				connection = driver.connect("DBMS", properties);
				statement = connection.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			check = new CheckSyntax();
			
		}
	}

	/**
	 * addButton
	 * 
	 * @param event
	 */
	@FXML
	void add(ActionEvent event) {
		try {
			String sql = commandArea.getText();
			int checker = check.checkSyntax(sql);
			if (checker == -1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("input Error");
				alert.setHeaderText(null);
				alert.setContentText("input error, check your input!!");
				alert.showAndWait();
			} else {
				try {
					statement.addBatch(sql);
					AlertMessage("batch added");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("input Error");
			alert.setHeaderText(null);
			alert.setContentText("error has occoured!!");
			alert.showAndWait();
		}

	}

	/**
	 * excuteButton
	 * 
	 * @param event
	 */
	@FXML
	public void excuteHandeler(ActionEvent event) {
		try {
			String sql = commandArea.getText();
			check = new CheckSyntax();
			int checker = check.checkSyntax(sql);
			if (checker == -1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("input Error");
				alert.setHeaderText(null);
				alert.setContentText("input error, check your input!!");
				alert.showAndWait();
			} else if (checker == 2 || checker == 3 || checker == 5) {
				// update
				try {
					statement.executeUpdate(sql);
					AlertMessage("table updated successfully!");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("input Error");
					alert.setHeaderText(null);
					alert.setContentText("Your database is not connected!!");
					alert.showAndWait();
					e.printStackTrace();
				}
			} else if (checker == 0 || checker == 1 || checker == 4 || checker == 7) {
				// structural
				try {
					db = new CDataBase();
					String s = sql.toLowerCase();
					String databaseName;
					if (s.contains("create database")) {
						databaseName = db.getDatabaseName(sql);
						driver = new JDriver();
						Properties properties = new Properties();
						properties.put("path", new File(databaseName));
						connection = driver.connect("DBMS", properties);
						statement = connection.createStatement();
					} else {
						statement.execute(sql);
					}
					statement.execute(sql);
					if (checker == 0)
						AlertMessage("Database Created");
					else if (checker == 1)
						AlertMessage("Table Created");
					else if (checker == 4)
						AlertMessage("Database Dropped");
					else if (checker == 7)
						AlertMessage("Table Dropped");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("input Error");
					alert.setHeaderText(null);
					alert.setContentText("Your database is not connected!!");
					alert.showAndWait();
					e.printStackTrace();
					
				}
			} else if (checker == 6) {
				try {
				    
					tableViewSetter(statement.executeQuery(sql));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("input Error");
					alert.setHeaderText(null);
					alert.setContentText("Your database is not connected!!");
					alert.showAndWait();
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("input Error");
			alert.setHeaderText(null);
			alert.setContentText("error has occoured!!");
			alert.showAndWait();
			e.printStackTrace();
		}

	}

	/**
	 * clearButton
	 * 
	 * @param event
	 */
	@FXML
	private void clearBatchHandler(ActionEvent event) {
		try {
			try {
				statement.clearBatch();
				AlertMessage("batch cleared");
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("input Error");
				alert.setHeaderText(null);
				alert.setContentText("Your database is not connected!!");
				alert.showAndWait();
				e.printStackTrace();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("input Error");
			alert.setHeaderText(null);
			alert.setContentText("error has occoured!!");
			alert.showAndWait();
		}
	}

	private void tableViewSetter(ResultSet resultSet) {
		try {
			tableData = FXCollections.observableArrayList();
			tableview.getColumns().clear();
			for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
				col.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							@Override
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(j).toString());
							}
						});

				tableview.getColumns().addAll(col);
				System.out.println("Column [" + i + "] ");
			}

			while (resultSet.next()) {
				// Iterate Row
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					// Iterate Column
					row.add(resultSet.getString(i));
				}
				tableData.add(row);

			}

			tableview.setItems(tableData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void excuteAll(ActionEvent event) {
		try {
			try {
				statement.executeBatch();
				AlertMessage("batch excuted");
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("input Error");
				alert.setHeaderText(null);
				alert.setContentText("Your database is not connected!!");
				alert.showAndWait();
				e.printStackTrace();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("input Error");
			alert.setHeaderText(null);
			alert.setContentText("error has occoured!!");
			alert.showAndWait();
		}
	}

	private void AlertMessage(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("mission accomplished");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	@FXML
	private void close()
	{
		try {
			statement.close();
			connection.close();
			pathText.setText(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
	}
}
