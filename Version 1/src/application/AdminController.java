package application;
import java.io.*;
import java.sql.*;
import java.util.*;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class AdminController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	private boolean cuVisited = false;
	private boolean ceVisited = false;
	private boolean csVisited = false;
	private boolean asVisited = false;
	private boolean varVisited = false;
	private boolean elVisited = false;
	
	//SQL variables
	private Connection c;
	
	@FXML	//Very important line
	StackPane adminStackPane;
	Label firstLabel = new Label("Representation of Home Screen");
	Label testLabel;
	
	@FXML	//Very important line
	Label nameLabel;
	
	/* FXML OBJECTS FOR THE STACKPANE */
	@FXML	//Create User Objects
	AnchorPane cuAnchorPane;
	GridPane cuGridPane = new GridPane();
	Label cuUsernameLabel = new Label("Username: ");
	Label cuPasswordLabel = new Label("Password: ");
	Label cuUserTypeLabel = new Label("User Type: ");
	Label cuNameLabel = new Label("Name: ");
	TextField cuUsernameTextField = new TextField();
	TextField cuPasswordTextField = new TextField();
	TextField cuUserTypeTextField = new TextField();
	TextField cuNameTextField = new TextField();
	Button cuButton = new Button("Create");
	@FXML
	Label invalidUsername = new Label();
	@FXML
	Label invalidPassword = new Label();
	
	@FXML //Create Shift Objects
	AnchorPane csAnchorPane;
	GridPane csGridPane = new GridPane();
	Label csEmpLabel = new Label("Employee_ID: ");
	Label csDateLabel = new Label("Date: ");
	Label csStartLabel = new Label("Start Time: ");
	Label csEndLabel = new Label("End Time: ");
	TextField csEmpTextField = new TextField();
	TextField csDateTextField = new TextField();
	TextField csStartTextField = new TextField();
	TextField csEndTextField = new TextField();
	Button csButton = new Button("Create");
	Label invalidEmp = new Label();
	Label invalidDate = new Label();
	Label invalidStart = new Label();
	Label invalidEnd = new Label();
	
	@FXML //Employee Lookup Objects
	AnchorPane elAnchorPane = new AnchorPane();
	TextField elSearchTextField = new TextField();
	Button elSearchButton = new Button("Search");
	Label elSearchLabel = new Label();
	String text = "";
	
	
	/**********************************/
	
	@FXML
	public void initialize() {	//native initialize method for FXML files
		adminStackPane.getChildren().add(firstLabel);
	}

	public void createEmployee(ActionEvent e) {
		if (cuVisited == false) {
			cuGridPane.add(cuUsernameLabel, 0, 0);
			cuGridPane.add(cuUsernameTextField, 1, 0);
			cuGridPane.add(cuPasswordLabel, 0, 1);
			cuGridPane.add(cuPasswordTextField, 1, 1);
			cuGridPane.add(cuUserTypeLabel, 0, 2);
			cuGridPane.add(cuUserTypeTextField, 1, 2);
			cuGridPane.add(cuNameLabel, 0, 3);
			cuGridPane.add(cuNameTextField, 1, 3);
			cuGridPane.add(cuButton, 1, 4);
			
			cuUserTypeTextField.setPromptText("'E' or 'A'");
			
			cuAnchorPane = new AnchorPane(cuGridPane);
			AnchorPane.setTopAnchor(cuGridPane, 0.0);
			AnchorPane.setLeftAnchor(cuGridPane, 0.0);
			AnchorPane.setRightAnchor(cuGridPane, 0.0);
			AnchorPane.setBottomAnchor(cuGridPane, 0.0);
			cuGridPane.setAlignment(Pos.CENTER);
			
			cuVisited = true;
		}
		
		System.out.println("createEmployee");
		adminStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Create Employee");
		//adminStackPane.getChildren().add(testLabel);
		adminStackPane.getChildren().add(cuAnchorPane);
		
		//Action when create button is clicked
		EventHandler<ActionEvent> createUserEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.out.println("Create Button Clicked.");
				
				String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
				
				
				try {
					
					Connection conn = DriverManager.getConnection(url);
					Statement st = conn.createStatement();
					System.out.println("connected");
					
					CreateLogin cl = new CreateLogin(conn);
					cl.Run(cuUsernameTextField.getText(), cuPasswordTextField.getText(), cuUserTypeTextField.getText(), cuNameTextField.getText());
					
					if (cl.valid_login == false) {
						
						System.out.println("invalid entry");
						invalidUsername.setText("(Invalid Username)");
						invalidPassword.setText("(Invalid Password)");
						
						cuUsernameTextField.clear();
						cuPasswordTextField.clear();
						cuUserTypeTextField.clear();
						cuNameTextField.clear();
						
						if (cl.usernameExists == true) {
							cuGridPane.add(invalidUsername, 2, 0);
						}
						
						if (cl.passwordExists == true) {
							cuGridPane.add(invalidPassword, 2, 1);
						}
						st.close();
						conn.close();
					}
					
				} catch (Exception ex) {
					System.out.println(ex);
				}
				
			}
		};
		
		cuButton.setOnAction(createUserEvent);
	}
	
	public void changeEmployee(ActionEvent e) {

		System.out.println("changeEmployee");
		adminStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Change Employee");
		adminStackPane.getChildren().add(testLabel);
	}
	
	public void createShift(ActionEvent e) {
		if (csVisited == false) {
			csGridPane.add(csEmpLabel, 0, 0);
			csGridPane.add(csEmpTextField, 1, 0);
			csGridPane.add(csDateLabel, 0, 1);
			csGridPane.add(csDateTextField, 1, 1);
			csGridPane.add(csStartLabel, 0, 2);
			csGridPane.add(csStartTextField, 1, 2);
			csGridPane.add(csEndLabel, 0, 3);
			csGridPane.add(csEndTextField, 1, 3);
			csGridPane.add(csButton, 1, 4);
			
			csDateTextField.setPromptText("YYYY-MM-DD");
			csStartTextField.setPromptText("HH:MM:SS (Military)");
			csEndTextField.setPromptText("HH:MM:SS (Military)");
			
			csAnchorPane = new AnchorPane(csGridPane);
			AnchorPane.setTopAnchor(csGridPane, 0.0);
			AnchorPane.setLeftAnchor(csGridPane, 0.0);
			AnchorPane.setRightAnchor(csGridPane, 0.0);
			AnchorPane.setBottomAnchor(csGridPane, 0.0);
			csGridPane.setAlignment(Pos.CENTER);
			
			csVisited = true;
		}
		
		System.out.println("createShift");
		adminStackPane.getChildren().remove(0);
		adminStackPane.getChildren().add(csAnchorPane);
		
		EventHandler<ActionEvent> createShiftEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.out.println("CreateButtonClicked");
				invalidEmp.setText("");
				invalidDate.setText("");
				invalidStart.setText("");
				invalidEnd.setText("");
				
				String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
				
				try {
					
					Connection conn = DriverManager.getConnection(url);
					Statement st = conn.createStatement();
					System.out.println("connected");
					
					CreateShift cs = new CreateShift(conn);
					cs.Run(csEmpTextField.getText(), csDateTextField.getText(), csStartTextField.getText(), csEndTextField.getText());
					
					if (cs.valid_EID == false) {
						csEmpTextField.clear();
						invalidEmp.setText("(Invalid Entry)");
						csGridPane.add(invalidEmp, 2, 0);
					}
					if (cs.dateValid == false) {
						csDateTextField.clear();
						invalidDate.setText("(Invalid Entry)");
						csGridPane.add(invalidDate, 2, 1);
					}
					if (cs.t_start == false) {
						csStartTextField.clear();
						invalidStart.setText("(Invalid Entry)");
						csGridPane.add(invalidStart, 2, 2);
					}
					if (cs.t_end == false) {
						csEndTextField.clear();
						invalidEnd.setText("(Invalid Entry)");
						invalidEnd.setTextFill(Color.RED);
						csGridPane.add(invalidEnd, 2, 3);
					}
					
					
					st.close();
					conn.close();
					
				} catch (Exception exc) {
					System.out.println(exc);
				}
				
			}
		};
		
		csButton.setOnAction(createShiftEvent);
	}
	
	public void approveShifts(ActionEvent e) {
		System.out.println("approveShifts");
		adminStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Approve Shifts");
		adminStackPane.getChildren().add(testLabel);
	}
	
	public void viewAnonymousReports(ActionEvent e) {
		System.out.println("viewAnonymousReports");
		adminStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of View Anonymous Reports");
		adminStackPane.getChildren().add(testLabel);
	}
	
	public void employeeLookup(ActionEvent e) {
		
		if (elVisited == false) {
			
			elAnchorPane.getChildren().add(elSearchTextField);
			elAnchorPane.getChildren().add(elSearchButton);
			elAnchorPane.getChildren().add(elSearchLabel);
			
			elSearchTextField.setPromptText("'Name' or 'ID'");
			elSearchTextField.setLayoutX(172.0);
			elSearchTextField.setLayoutY(37.0);
			AnchorPane.setLeftAnchor(elSearchTextField, 172.0);
			AnchorPane.setRightAnchor(elSearchTextField, 321.0);
			AnchorPane.setTopAnchor(elSearchTextField, 37.0);
			
			elSearchButton.setLayoutX(344.0);
			elSearchButton.setLayoutY(37.0);
			AnchorPane.setRightAnchor(elSearchButton, 246.0);
			AnchorPane.setTopAnchor(elSearchButton, 37.0);
			
			elSearchLabel.setMaxWidth(Double.MAX_VALUE);
			AnchorPane.setLeftAnchor(elSearchLabel, 0.0);
			AnchorPane.setRightAnchor(elSearchLabel, 0.0);
			AnchorPane.setTopAnchor(elSearchLabel, 200.0);
			elSearchLabel.setAlignment(Pos.CENTER);
			
			elVisited = true;
		}
		
		System.out.println("employeeLookup");
		adminStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Employee Lookup");
		//adminStackPane.getChildren().add(testLabel);
		
		adminStackPane.getChildren().add(elAnchorPane);
		
		EventHandler<ActionEvent> searchEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				elSearchLabel.setText("");
				System.out.println("SearchButtonClicked");
				text = "New employee line \nNew employee line \nNew employee line \nNew employee line \n";
				elSearchLabel.setText(text);
			}
		};
		
		elSearchButton.setOnAction(searchEvent);
	}
	
	public void logout(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Login.fxml"));		//Must make root the same as our Main Scene
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		String css = this.getClass().getResource("application.css").toExternalForm();
		scene.getStylesheets().add(css);
		stage.setMinHeight(580.0);
		stage.setMinWidth(896.0);
		stage.setMaxHeight(1440.0);
		stage.setMaxWidth(2560.0);
		stage.setScene(scene);
		stage.show();
	}
	
	public void displayName(String username) {								//Displays name on nameLabel
		nameLabel.setText("Welcome, " + username + "!");
	}
	
}