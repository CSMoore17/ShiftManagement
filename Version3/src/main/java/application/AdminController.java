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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
	private int asIndex = 0;
	
	//SQL variables
	private Connection c;
	
	@FXML	//Very important line
	StackPane adminStackPane;
	Label firstLabel = new Label();
	Label testLabel;
	
	@FXML	//Very important line
	Label nameLabel;
	
	/********* FXML OBJECTS FOR THE STACKPANE *********/
	
	@FXML	//Create User Objects
	Label cuTitle = new Label("Create User");
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
	Label csTitle = new Label("Create Shift");
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
	Label created = new Label();
	
	@FXML //Approve Shift Objects
	Label asTitle = new Label("Approve Shift");
	AnchorPane asAnchorPane = new AnchorPane();
	Label giver = new Label();
	Label taker = new Label();
	Label date = new Label();
	Label start = new Label();
	Label end = new Label();
	Button approve = new Button("Approve");
	Button deny = new Button("Deny");
	Button back = new Button("Back");
	Button next = new Button("Next");
	Label approved = new Label();
	String offset = "                              "; //30 spaces
	Label choice = new Label();
	
	@FXML //View Anonymous Reports Objects
	Label varTitle = new Label("View Anonymous Report");
	AnchorPane varAnchorPane = new AnchorPane();
	Button varNext = new Button("Next");
	Button previous = new Button("Previous");
	Button markRead = new Button("Mark Read");
	ScrollPane report = new ScrollPane();
	Label text = new Label();
	
	@FXML //Employee Lookup Objects
	Label elTitle = new Label("Employee Lookup");
	Label ceTitle = new Label("Change Employee");
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
	Label changed = new Label();
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
			
			cuAnchorPane.getChildren().add(cuTitle);
			cuTitle.setLayoutX(250.0);
			cuTitle.setLayoutY(10.0);
			cuTitle.setMinWidth(cuAnchorPane.getWidth());
			cuTitle.setAlignment(Pos.CENTER);
			cuTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
			cuVisited = true;
		}
		
		//System.out.println("createEmployee");
		adminStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Create Employee");
		//adminStackPane.getChildren().add(testLabel);
		adminStackPane.getChildren().add(cuAnchorPane);
		created.setText("");
		
		//Action when create button is clicked
		EventHandler<ActionEvent> createUserEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				//System.out.println("Create Button Clicked.");
				
				String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
				
				
				try {
					
					Connection conn = DriverManager.getConnection(url);
					Statement st = conn.createStatement();
					//System.out.println("connected");
					
					CreateLogin cl = new CreateLogin(conn);
					cl.Run(cuUsernameTextField.getText(), cuPasswordTextField.getText(), cuUserTypeTextField.getText(), cuNameTextField.getText());
					
					if (cl.valid_login == false) {
						
						//System.out.println("invalid entry");
						invalidUsername.setText("(Invalid Username)");
						invalidUsername.setStyle("-fx-text-fill: red");
						invalidPassword.setText("(Invalid Password)");
						invalidPassword.setStyle("-fx-text-fill: red");
						
						cuUsernameTextField.clear();
						cuPasswordTextField.clear();
						cuUserTypeTextField.clear();
						cuNameTextField.clear();
						created.setText("");
						
						if (cl.usernameExists == true) {
							cuGridPane.add(invalidUsername, 2, 0); 
						}
						
						if (cl.passwordExists == true) {
							cuGridPane.add(invalidPassword, 2, 1);
						}
						
						st.close();
						conn.close();
					} else {
						created.setText("Created!");
						created.setStyle("-fx-text-fill: green");
						cuGridPane.add(created, 0, 4);
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
			
			csAnchorPane.getChildren().add(csTitle);
			csTitle.setLayoutX(250.0);
			csTitle.setLayoutY(10.0);
			csTitle.setMinWidth(csAnchorPane.getWidth());
			csTitle.setAlignment(Pos.CENTER);
			csTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
			csVisited = true;
		}
		
		//System.out.println("createShift");
		adminStackPane.getChildren().remove(0);
		adminStackPane.getChildren().add(csAnchorPane);
		
		EventHandler<ActionEvent> createShiftEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				//System.out.println("CreateButtonClicked");
				invalidEmp.setText("");
				invalidDate.setText("");
				invalidStart.setText("");
				invalidEnd.setText("");
				created.setText("");
				
				String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
				
				try {
					
					Connection conn = DriverManager.getConnection(url);
					Statement st = conn.createStatement();
					//System.out.println("connected");
					
					CreateShift cs = new CreateShift(conn);
					cs.Run(csEmpTextField.getText(), csDateTextField.getText(), csStartTextField.getText(), csEndTextField.getText());
					
					if (cs.valid_EID == false) {
						csEmpTextField.clear();
						invalidEmp.setText("(Invalid Entry)");
						invalidEmp.setStyle("-fx-text-fill: red");
						csGridPane.add(invalidEmp, 2, 0);
						created.setText("");
					}
					if (cs.dateValid == false) {
						csDateTextField.clear();
						invalidDate.setText("(Invalid Entry)");
						invalidDate.setStyle("-fx-text-fill: red");
						csGridPane.add(invalidDate, 2, 1);
						created.setText("");
					}
					if (cs.t_start == false) {
						csStartTextField.clear();
						invalidStart.setText("(Invalid Entry)");
						invalidStart.setStyle("-fx-text-fill: red");
						csGridPane.add(invalidStart, 2, 2);
						created.setText("");
					}
					if (cs.t_end == false) {
						csEndTextField.clear();
						invalidEnd.setText("(Invalid Entry)");
						invalidEnd.setStyle("-fx-text-fill: red");
						csGridPane.add(invalidEnd, 2, 3);
						created.setText("");
					} else {
						created.setText("Created!");
						created.setStyle("-fx-text-fill: green");
						csGridPane.add(created, 0, 4);
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
		
		if (asVisited == false) {
			
			asAnchorPane.getChildren().add(giver);
			asAnchorPane.getChildren().add(taker);
			asAnchorPane.getChildren().add(date);
			asAnchorPane.getChildren().add(start);
			asAnchorPane.getChildren().add(end);
			asAnchorPane.getChildren().add(approve);
			asAnchorPane.getChildren().add(deny);
			asAnchorPane.getChildren().add(back);
			asAnchorPane.getChildren().add(next);
			
			giver.setLayoutY(162.0);
			giver.setPrefHeight(18.0);
			giver.setPrefWidth(637.0);
			giver.setAlignment(Pos.CENTER);
			//giver.setOpaqueInsets(new Insets(0.0, 0.0, 0.0, 200.0));
			//giver.setWrapText(true);
			
			taker.setLayoutY(180.0);
			taker.setPrefHeight(18.0);
			taker.setPrefWidth(637.0);
			taker.setAlignment(Pos.CENTER);
			
			date.setLayoutY(198.0);
			date.setPrefHeight(18.0);
			date.setPrefWidth(637.0);
			date.setAlignment(Pos.CENTER);
			
			start.setLayoutY(216.0);
			start.setPrefHeight(18.0);
			start.setPrefWidth(637.0);
			start.setAlignment(Pos.CENTER);
			
			end.setLayoutY(235.0);
			end.setPrefHeight(18.0);
			end.setPrefWidth(637.0);
			end.setAlignment(Pos.CENTER);
			
			approve.setLayoutX(337.0);
			approve.setLayoutY(279.0);
			
			deny.setLayoutX(428.0);
			deny.setLayoutY(279.0);
			
			back.setLayoutX(455.0);
			back.setLayoutY(400.0);
			
			next.setLayoutX(509.0);
			next.setLayoutY(400.0);
			
			approved.setLayoutX(387.0);
			approved.setLayoutY(326.0);
			
			asAnchorPane.getChildren().add(asTitle);
			asTitle.setLayoutX(235.0);
			asTitle.setLayoutY(10.0);
			asTitle.setMinWidth(asAnchorPane.getWidth());
			asTitle.setAlignment(Pos.CENTER);
			asTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
			asAnchorPane.getChildren().add(choice);
			choice.setLayoutX(380.0);
			choice.setLayoutY(320.0);
			choice.setText("");
			choice.setStyle("-fx-text-fill: green;");
			
			
			asVisited = true;
		}
		
		//System.out.println("approveShifts");
		adminStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Approve Shifts");
		adminStackPane.getChildren().add(asAnchorPane);
		choice.setText("");
		
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		
		try {
			
			Connection conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			//System.out.println("connected");
			
			ClaimApproval ca = new ClaimApproval(conn);
			
			asIndex = 0;
			giver.setText(offset + "Offered By: " + ca.Giver_Name.get(asIndex));
			taker.setText("Claimed By: " + ca.Taker_Name.get(asIndex));
			date.setText("Date: " + ca.Date.get(asIndex));
			start.setText("Start: " + ca.Start_Time.get(asIndex));
			end.setText("End: " + ca.End_Time.get(asIndex));
			
			EventHandler<ActionEvent> approveEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					ca.Run(ca.SC_ID.get(asIndex), true);
					ca.Update();
					choice.setText("Approved!");
					choice.setStyle("-fx-text-fill: green;");
					giver.setText(offset + "Offered By: " + ca.Giver_Name.get(asIndex));
					taker.setText("Claimed By: " + ca.Taker_Name.get(asIndex));
					date.setText("Date: " + ca.Date.get(asIndex));
					start.setText("Start: " + ca.Start_Time.get(asIndex));
					end.setText("End: " + ca.End_Time.get(asIndex));
				}
			};
			
			EventHandler<ActionEvent> denyEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					ca.Run(ca.SC_ID.get(asIndex), false);
					ca.Update();
					choice.setText("Denied!");
					choice.setStyle("-fx-text-fill: red;");
					giver.setText(offset + "Offered By: " + ca.Giver_Name.get(asIndex));
					taker.setText("Claimed By: " + ca.Taker_Name.get(asIndex));
					date.setText("Date: " + ca.Date.get(asIndex));
					start.setText("Start: " + ca.Start_Time.get(asIndex));
					end.setText("End: " + ca.End_Time.get(asIndex));
				}
			};
			
			EventHandler<ActionEvent> nextEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if (ca.Date.size()-1 >= 0) {
						if (asIndex < ca.Date.size()-1) 
							asIndex++;
						choice.setText("");
						giver.setText(offset + "Offered By: " + ca.Giver_Name.get(asIndex));
						taker.setText("Claimed By: " + ca.Taker_Name.get(asIndex));
						date.setText("Date: " + ca.Date.get(asIndex));
						start.setText("Start: " + ca.Start_Time.get(asIndex));
						end.setText("End: " + ca.End_Time.get(asIndex));
					}
				}
			};
			
			EventHandler<ActionEvent> backEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if (ca.Date.size()-1 >= 0) {
						if (asIndex > 0)
							asIndex--;
						choice.setText("");
						giver.setText(offset + "Offered By: " + ca.Giver_Name.get(asIndex));
						taker.setText("Claimed By: " + ca.Taker_Name.get(asIndex));
						date.setText("Date: " + ca.Date.get(asIndex));
						start.setText("Start: " + ca.Start_Time.get(asIndex));
						end.setText("End: " + ca.End_Time.get(asIndex));
					}
				}
			};
			
			approve.setOnAction(approveEvent);
			deny.setOnAction(denyEvent);
			next.setOnAction(nextEvent);
			back.setOnAction(backEvent);
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
	}
	
	public void viewAnonymousReports(ActionEvent e) {
		
		if (varVisited == false) {
			varAnchorPane.getChildren().add(report);
			varAnchorPane.getChildren().add(varNext);
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
			
			varNext.setLayoutX(538.0);
			varNext.setLayoutY(400.0);
			AnchorPane.setBottomAnchor(varNext, 43.0);
			AnchorPane.setRightAnchor(varNext, 36.0);
			
			previous.setLayoutX(468.0);
			previous.setLayoutY(400.0);
			AnchorPane.setBottomAnchor(previous, 43.0);
			AnchorPane.setRightAnchor(previous, 100.0);
			
			markRead.setLayoutX(38.0);
			markRead.setLayoutY(400.0);
			AnchorPane.setBottomAnchor(markRead, 43.0);
			AnchorPane.setLeftAnchor(markRead, 36.0);
			
			varAnchorPane.getChildren().add(varTitle);
			varTitle.setLayoutX(185.0);
			varTitle.setLayoutY(10.0);
			varTitle.setMinWidth(varAnchorPane.getWidth());
			varTitle.setAlignment(Pos.CENTER);
			varTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
			varVisited = true;
		}
		
		repIndex = 0;
		//System.out.println("viewAnonymousReports");
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
					//System.out.println("NextButtonClicked");
					if (vr.A_Report.size()-1 >= 0) {
						if (repIndex < vr.A_Report.size()-1)
							repIndex++;
						text.setText(vr.A_Report.get(repIndex));
					}
					else
						text.setText("");
					//System.out.println(vr.A_Report);
				}
			};
			
			//When previous button is pressed
			EventHandler<ActionEvent> previousEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					//System.out.println("PreviousButtonClicked");
					if (vr.A_Report.size()-1 >= 0) {
						if (repIndex > 0)
							repIndex--;
						text.setText(vr.A_Report.get(repIndex));
					}
					else
						text.setText("");
					//System.out.println(vr.A_Report);
				}
			};
			
			//When markRead button is pressed
			EventHandler<ActionEvent> markReadEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {;
					//System.out.println("MarkReadButtonClicked");
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
					//System.out.println(vr.A_Report);
				}
			};
			
			varNext.setOnAction(nextEvent);
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
			elSearchTextField.setLayoutY(72.0);
			AnchorPane.setRightAnchor(elSearchTextField, 321.0);
			AnchorPane.setTopAnchor(elSearchTextField, 72.0);
			
			elSearchButton.setLayoutX(344.0);
			elSearchButton.setLayoutY(72.0);
			AnchorPane.setRightAnchor(elSearchButton, 246.0);
			AnchorPane.setTopAnchor(elSearchButton, 72.0);
			
			elResult.setLayoutX(30.0);
			elResult.setLayoutY(111.0);
			elResult.setPrefHeight(125.0);
			elResult.setPrefWidth(661.0);
			elResult.setHbarPolicy(ScrollBarPolicy.NEVER);
			elResult.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
			elResult.setContent(elResultLabel);
			elResult.setFitToWidth(true);
			elResultLabel.setWrapText(true);
			elResult.setPadding(new Insets(5, 5, 5, 5));
			AnchorPane.setLeftAnchor(elResult, 30.0);
			AnchorPane.setRightAnchor(elResult, 30.0);
			AnchorPane.setTopAnchor(elResult, 111.0);
			
			elAnchorPane.getChildren().add(elTitle);
			elTitle.setLayoutX(240.0);
			elTitle.setLayoutY(10.0);
			elTitle.setMinWidth(elAnchorPane.getWidth());
			elTitle.setAlignment(Pos.CENTER);
			elTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
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
			
			elChange.setLayoutX(505.0);
			elChange.setLayoutY(412.0);
			//AnchorPane.setRightAnchor(elChange, 109.0);
			AnchorPane.setBottomAnchor(elChange, 31.0);
			
			elAnchorPane.getChildren().add(ceTitle);
			ceTitle.setLayoutX(240.0);
			ceTitle.setLayoutY(275.0);
			ceTitle.setMinWidth(elAnchorPane.getWidth());
			ceTitle.setAlignment(Pos.CENTER);
			ceTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
			elInvalidID.setLayoutX(425.0);
			elInvalidID.setLayoutY(412.0);
			//AnchorPane.setRightAnchor(elInvalidID, 60.0);
			AnchorPane.setBottomAnchor(elInvalidID, 31.0);
			
			elAnchorPane.getChildren().add(changed);
			changed.setLayoutX(425.0);
			changed.setLayoutY(412.0);
			changed.setText("");
			changed.setStyle("-fx-text-fill: green;");
			AnchorPane.setBottomAnchor(changed, 31.0);
			
			elVisited = true;
		}
		
		elIndex = 0;
		elText = "";
		elResultLabel.setText(elText);
		//System.out.println("employeeLookup");
		adminStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Employee Lookup");
		//adminStackPane.getChildren().add(testLabel);
		changed.setText("");
		
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
					//System.out.println("SearchButtonClicked");
					//text += "New employee line \nNew employee line \nNew employee line \nNew employee line \n";
					//elResultLabel.setText(text);
					el.Run(elSearchTextField.getText());
					//System.out.println("el.Run() reached");
					elIndex = el.EmpList_Hours.size();
					//System.out.println(elIndex);
					elText += "${'";							//necessary heiroglyphs
					for (int i = 0; i < elIndex; i++) {
						if (el.toTrue == true)
							elText += "ID: " + String.valueOf(el.EmpList_ID.get(i)) + " | Name: " + String.valueOf(el.EmpList_Name.get(i)) + " | PayRate: " + String.valueOf(el.EmpList_PayRate.get(i)) + " | Hours: " + String.valueOf(el.EmpList_Hours.get(i)) + " |  Time Off: " + String.valueOf(el.TO_StartDate.get(i)) + " - " + String.valueOf(el.TO_EndDate.get(i)) + "\n";
						else
							elText += "ID: " + String.valueOf(el.EmpList_ID.get(i)) + " | Name: " + String.valueOf(el.EmpList_Name.get(i)) + " | PayRate: " + String.valueOf(el.EmpList_PayRate.get(i)) + " | Hours: " + String.valueOf(el.EmpList_Hours.get(i)) + " |  Time Off: " + "N/A" +"\n";
					}
					elText += "'}\"/>";							//necessary heiroglyphs
					
					el.clear();
					
					//System.out.println(elText);
					
					elResultLabel.setText(elText.substring(3, elText.length()-6));
						
				}
			};
			
			EventHandler<ActionEvent> changeEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					//System.out.println("SearchButtonClicked");
					
					elInvalidID.setText("");
					
					String id = elID.getText();
					String name = elName.getText();
					String pay = elPayRate.getText();
					
					ce.Run(id, name, pay);
					
					if (id.length() == 0 || ce.validEmp == false) {
						elInvalidID.setText("Invalid Entry");
						elInvalidID.setStyle("-fx-text-fill: red");
						changed.setText("");
					} else {
						changed.setText("Changed!");
						changed.setStyle("-fx-text-fill: green;");
					}
					
					elIndex = 0;
					elText = "";
					elResultLabel.setText(elText);
					//elResultLabel.setText("");
					//System.out.println("SearchButtonClicked");
					//text += "New employee line \nNew employee line \nNew employee line \nNew employee line \n";
					//elResultLabel.setText(text);
					el.Run(elSearchTextField.getText());
					//System.out.println("el.Run() reached");
					elIndex = el.EmpList_Hours.size();
					//System.out.println(elIndex);
					elText += "${'";							//necessary heiroglyphs
					for (int i = 0; i < elIndex; i++) {
						elText += "ID: " + String.valueOf(el.EmpList_ID.get(i)) + " | Name: " + String.valueOf(el.EmpList_Name.get(i)) + " | PayRate: " + String.valueOf(el.EmpList_PayRate.get(i)) + " | Hours: " + String.valueOf(el.EmpList_Hours.get(i)) + "\n";
					}
					elText += "'}\"/>";							//necessary heiroglyphs
					
					el.clear();
					
					//System.out.println(elText);
					
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
