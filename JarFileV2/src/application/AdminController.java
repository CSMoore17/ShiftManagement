package application;
import java.io.*;
import java.sql.*;
import java.util.*;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
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
	private int repIndex = 0;
	private int elIndex = 0;
	
	//SQL variables
	private Connection c;
	
	@FXML	//Very important line
	StackPane adminStackPane;
	Label firstLabel = new Label("Representation of Home Screen");
	Label testLabel;
	
	@FXML	//Very important line
	Label nameLabel;
	
	/********* FXML OBJECTS FOR THE STACKPANE *********/
	
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
	
	@FXML //View Anonymous Reports Objects
	AnchorPane varAnchorPane = new AnchorPane();
	Button next = new Button("Next");
	Button previous = new Button("Previous");
	Button markRead = new Button("Mark Read");
	ScrollPane report = new ScrollPane();
	Label text = new Label();
	
	@FXML //Employee Lookup Objects
	AnchorPane elAnchorPane = new AnchorPane();
	TextField elSearchTextField = new TextField();
	Button elSearchButton = new Button("Search");
	ScrollPane elResult = new ScrollPane();
	Label elResultLabel = new Label();
	TextField elID = new TextField();
	TextField elName = new TextField();
	TextField elPayRate = new TextField();
	Button elChange = new Button("Change");
	Label elInvalidID = new Label();
	String elText;
	//String text = "";
	

	
	
	/*************************************************/
	
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
						invalidUsername.setStyle("-fx-text-fill: red");
						invalidPassword.setText("(Invalid Password)");
						invalidPassword.setStyle("-fx-text-fill: red");
						
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
						invalidEmp.setStyle("-fx-text-fill: red");
						csGridPane.add(invalidEmp, 2, 0);
					}
					if (cs.dateValid == false) {
						csDateTextField.clear();
						invalidDate.setText("(Invalid Entry)");
						invalidDate.setStyle("-fx-text-fill: red");
						csGridPane.add(invalidDate, 2, 1);
					}
					if (cs.t_start == false) {
						csStartTextField.clear();
						invalidStart.setText("(Invalid Entry)");
						invalidStart.setStyle("-fx-text-fill: red");
						csGridPane.add(invalidStart, 2, 2);
					}
					if (cs.t_end == false) {
						csEndTextField.clear();
						invalidEnd.setText("(Invalid Entry)");
						invalidEnd.setStyle("-fx-text-fill: red");
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
		
		if (varVisited == false) {
			varAnchorPane.getChildren().add(report);
			varAnchorPane.getChildren().add(next);
			varAnchorPane.getChildren().add(previous);
			varAnchorPane.getChildren().add(markRead);
			
			report.setHbarPolicy(ScrollBarPolicy.NEVER);
			report.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			report.setContent(text);
			report.setFitToWidth(true);
			text.setWrapText(true);
			report.setPadding(new Insets(5, 5, 5, 5));
			report.setLayoutX(37.0);
			report.setLayoutY(555.0);
			report.setPrefHeight(333.0);
			report.setPrefWidth(57.0);
			AnchorPane.setBottomAnchor(report, 76.0);
			AnchorPane.setLeftAnchor(report, 36.0);
			AnchorPane.setRightAnchor(report, 36.0);
			AnchorPane.setTopAnchor(report, 60.0);
			
			next.setLayoutX(538.0);
			next.setLayoutY(400.0);
			AnchorPane.setBottomAnchor(next, 43.0);
			AnchorPane.setRightAnchor(next, 36.0);
			
			previous.setLayoutX(468.0);
			previous.setLayoutY(400.0);
			AnchorPane.setBottomAnchor(previous, 43.0);
			AnchorPane.setRightAnchor(previous, 100.0);
			
			markRead.setLayoutX(38.0);
			markRead.setLayoutY(400.0);
			AnchorPane.setBottomAnchor(markRead, 43.0);
			AnchorPane.setLeftAnchor(markRead, 36.0);
			
			varVisited = true;
		}
		
		repIndex = 0;
		System.out.println("viewAnonymousReports");
		adminStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of View Anonymous Reports");
		//adminStackPane.getChildren().add(testLabel);
		adminStackPane.getChildren().add(varAnchorPane);
		
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		
		try {
			
			Connection conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			
			ViewReports vr = new ViewReports(conn);
			
			text.setText(vr.A_Report.get(repIndex));
			
			//When next button is pressed
			EventHandler<ActionEvent> nextEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					System.out.println("NextButtonClicked");
					if (vr.A_Report.size()-1 >= 0) {
						if (repIndex < vr.A_Report.size()-1)
							repIndex++;
						text.setText(vr.A_Report.get(repIndex));
					}
					else
						text.setText("");
					System.out.println(vr.A_Report);
				}
			};
			
			//When previous button is pressed
			EventHandler<ActionEvent> previousEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					System.out.println("PreviousButtonClicked");
					if (vr.A_Report.size()-1 >= 0) {
						if (repIndex > 0)
							repIndex--;
						text.setText(vr.A_Report.get(repIndex));
					}
					else
						text.setText("");
					System.out.println(vr.A_Report);
				}
			};
			
			//When markRead button is pressed
			EventHandler<ActionEvent> markReadEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {;
					System.out.println("MarkReadButtonClicked");
					if (vr.A_Report.size()-1 >= 0) {
						String id = vr.R_ID.get(repIndex);
						vr.Run(id); //deletes the current report
						if (!vr.A_Report.isEmpty()) {
							if (repIndex != 0)
								text.setText(vr.A_Report.get(--repIndex));
							else 
								text.setText(vr.A_Report.get(repIndex));
						}
						else {
							text.setText("");
						}
					}
					else {
						text.setText("");
						vr.clear();
					}
					System.out.println(vr.A_Report);
				}
			};
			
			next.setOnAction(nextEvent);
			previous.setOnAction(previousEvent);
			markRead.setOnAction(markReadEvent);
			
			//st.close();
			//conn.close();
			
			//vr.s.close();
			
		} catch(Exception exc) {
			
		}
	}
	
	public void employeeLookup(ActionEvent e) {
		
		if (elVisited == false) {
			
			elAnchorPane.getChildren().add(elSearchTextField);
			elAnchorPane.getChildren().add(elSearchButton);
			elAnchorPane.getChildren().add(elResult);
			elAnchorPane.getChildren().add(elID);
			elAnchorPane.getChildren().add(elName);
			elAnchorPane.getChildren().add(elPayRate);
			elAnchorPane.getChildren().add(elChange);
			elAnchorPane.getChildren().add(elInvalidID);
			
			elSearchTextField.setPromptText("'Name' or 'ID'");
			elSearchTextField.setLayoutX(172.0);
			elSearchTextField.setLayoutY(37.0);
			AnchorPane.setRightAnchor(elSearchTextField, 321.0);
			AnchorPane.setTopAnchor(elSearchTextField, 37.0);
			
			elSearchButton.setLayoutX(344.0);
			elSearchButton.setLayoutY(37.0);
			AnchorPane.setRightAnchor(elSearchButton, 246.0);
			AnchorPane.setTopAnchor(elSearchButton, 37.0);
			
			elResult.setLayoutX(56.0);
			elResult.setLayoutY(76.0);
			elResult.setPrefHeight(199.0);
			elResult.setPrefWidth(521.0);
			elResult.setHbarPolicy(ScrollBarPolicy.NEVER);
			elResult.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			elResult.setContent(elResultLabel);
			elResult.setFitToWidth(true);
			elResultLabel.setWrapText(true);
			elResult.setPadding(new Insets(5, 5, 5, 5));
			AnchorPane.setLeftAnchor(elResult, 60.0);
			AnchorPane.setRightAnchor(elResult, 60.0);
			AnchorPane.setTopAnchor(elResult, 76.0);
			
			elID.setLayoutX(60.0);
			elID.setLayoutY(370.0);
			elID.setPromptText("Enter 'ID'");
			AnchorPane.setBottomAnchor(elID, 73.0);
			AnchorPane.setRightAnchor(elID, 430.0);
			
			elName.setLayoutX(247.0);
			elName.setLayoutY(370.0);
			elName.setPromptText("Change 'Name'");
			AnchorPane.setBottomAnchor(elName, 73.0);
			AnchorPane.setRightAnchor(elName, 245.0);
			
			elPayRate.setLayoutX(432.0);
			elPayRate.setLayoutY(370.0);
			elPayRate.setPromptText("Change 'Pay Rate'");
			AnchorPane.setBottomAnchor(elPayRate, 73.0);
			AnchorPane.setRightAnchor(elPayRate, 60.0);
			
			elChange.setLayoutX(450.0);
			elChange.setLayoutY(412.0);
			AnchorPane.setRightAnchor(elChange, 134.0);
			AnchorPane.setBottomAnchor(elChange, 31.0);
			
			elInvalidID.setLayoutX(524.0);
			elInvalidID.setLayoutY(412.0);
			AnchorPane.setRightAnchor(elInvalidID, 60.0);
			AnchorPane.setBottomAnchor(elInvalidID, 31.0);
			
			
			elVisited = true;
		}
		
		elIndex = 0;
		elText = "";
		elResultLabel.setText(elText);
		System.out.println("employeeLookup");
		adminStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Employee Lookup");
		//adminStackPane.getChildren().add(testLabel);
		
		adminStackPane.getChildren().add(elAnchorPane);
		
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		
		try {
			
			Connection conn = DriverManager.getConnection(url);
			EmployeeLookup el = new EmployeeLookup(conn);
			ChangeEmployee ce = new ChangeEmployee(conn);
			
			EventHandler<ActionEvent> searchEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					
					elIndex = 0;
					elText = "";
					elResultLabel.setText(elText);
					//elResultLabel.setText("");
					System.out.println("SearchButtonClicked");
					//text += "New employee line \nNew employee line \nNew employee line \nNew employee line \n";
					//elResultLabel.setText(text);
					el.Run(elSearchTextField.getText());
					System.out.println("el.Run() reached");
					elIndex = el.EmpList_Hours.size();
					System.out.println(elIndex);
					elText += "${'";							//necessary heiroglyphs
					for (int i = 0; i < elIndex; i++) {
						elText += "ID: " + String.valueOf(el.EmpList_ID.get(i)) + " | Name: " + String.valueOf(el.EmpList_Name.get(i)) + " | PayRate: " + String.valueOf(el.EmpList_PayRate.get(i)) + " | Hours: " + String.valueOf(el.EmpList_Hours.get(i)) + "\n";
					}
					elText += "'}\"/>";							//necessary heiroglyphs
					
					el.clear();
					
					System.out.println(elText);
					
					elResultLabel.setText(elText.substring(3, elText.length()-6));
						
				}
			};
			
			EventHandler<ActionEvent> changeEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					System.out.println("SearchButtonClicked");
					
					elInvalidID.setText("");
					
					String id = elID.getText();
					String name = elName.getText();
					String pay = elPayRate.getText();
					
					ce.Run(id, name, pay);
					
					if (id.length() == 0 || ce.validEmp == false) {
						elInvalidID.setText("Invalid Entry");
						elInvalidID.setStyle("-fx-text-fill: red");
					}
					
					elIndex = 0;
					elText = "";
					elResultLabel.setText(elText);
					//elResultLabel.setText("");
					System.out.println("SearchButtonClicked");
					//text += "New employee line \nNew employee line \nNew employee line \nNew employee line \n";
					//elResultLabel.setText(text);
					el.Run(elSearchTextField.getText());
					System.out.println("el.Run() reached");
					elIndex = el.EmpList_Hours.size();
					System.out.println(elIndex);
					elText += "${'";							//necessary heiroglyphs
					for (int i = 0; i < elIndex; i++) {
						elText += "ID: " + String.valueOf(el.EmpList_ID.get(i)) + " | Name: " + String.valueOf(el.EmpList_Name.get(i)) + " | PayRate: " + String.valueOf(el.EmpList_PayRate.get(i)) + " | Hours: " + String.valueOf(el.EmpList_Hours.get(i)) + "\n";
					}
					elText += "'}\"/>";							//necessary heiroglyphs
					
					el.clear();
					
					System.out.println(elText);
					
					elResultLabel.setText(elText.substring(3, elText.length()-6));
				}
			};
			
			elSearchButton.setOnAction(searchEvent);
			elChange.setOnAction(changeEvent);
			
			//conn.close();
			//ce.s.close();
			
		} catch (Exception exce) {
			System.out.println(exce);
		}

	}
	
	public void logout(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Login.fxml"));		//Must make root the same as our Main Scene
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		String css = this.getClass().getResource("application.css").toExternalForm();
		scene.getStylesheets().add(css);
		stage.setMinHeight(580.0);
		stage.setMinWidth(896.0);
		stage.setMaxHeight(580.0);
		stage.setMaxWidth(896.0);
		stage.setScene(scene);
		stage.show();
	}
	
	public void displayName(String username) {								//Displays name on nameLabel
		nameLabel.setText("Welcome, " + username + "!");
	}
	
}