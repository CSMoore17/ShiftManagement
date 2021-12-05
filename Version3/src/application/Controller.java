package application;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Controller {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	//SQL variables
	private Connection c;
	
	private boolean schedule = false;
	private boolean hoursAndPay = false;
	private boolean claimShift = false;
	private boolean timeOff = false;
	private boolean anonymousReport = false;
	private int loginID;
	private int empID;
	private String name;
	
	
	@FXML	//Very important line
	StackPane mainStackPane;
	Label firstLabel = new Label("Representation of Home Screen");
	Label testLabel;
	
	@FXML	//Very important line
	Label nameLabel;
	
	@FXML //Schedule Objects
	Label sTitle = new Label("Schedule");
	AnchorPane sAnchorPane = new AnchorPane();
	GridPane sGridPane = new GridPane();
	ShiftNode[] shifts = new ShiftNode[8];
	
	
	@FXML //hoursAndPay objects
	Label hpTitle = new Label("Hours & Pay");
	AnchorPane hpAnchorPane = new AnchorPane();
	Label past = new Label("In the past ");
	String timeTitle = "Week";
	MenuButton time = new MenuButton(timeTitle);
	MenuItem week = new MenuItem("Week");
	MenuItem month = new MenuItem("Month");
	MenuItem year = new MenuItem("Year");
	Label worked = new Label("You have worked");
	Label hours = new Label();							//hours variable;
	Label earned = new Label("And earned");
	Label pay = new Label();							//pay variable
	
	@FXML //Time Off Objects
	Label toTitle = new Label("Request Time Off");
	AnchorPane toAnchorPane = new AnchorPane();
	GridPane toGridPane = new GridPane();
	Label toStart = new Label("Start: ");
	Label toEnd = new Label("End: ");
	DatePicker startDate = new DatePicker();
	DatePicker endDate = new DatePicker();
	Button toSubmit = new Button("Submit");
	Label toSubmitted = new Label();
	
	@FXML //Claim Shift Objects
	Label csTitle = new Label("Claim Shift");
	AnchorPane csAnchorPane = new AnchorPane();
	GridPane csGridPane = new GridPane();
	ShiftNode[] claims = new ShiftNode[8];
	
	@FXML //anonymousReport objects
	Label arTitle = new Label("Anonymous Report");
	AnchorPane arAnchorPane = new AnchorPane();
	TextArea report = new TextArea();
	Button submit = new Button("Submit");
	Label submitted = new Label();
	
	//@FXML	//Very important line
	//private Button logoutButton;						//Button for logout method
	//@FXML	//Very important line
	//private Pane scenePane;								//Also for logout method
	
	
	
	@FXML
	public void initialize() {	//native initialize method for FXML files
		mainStackPane.getChildren().add(firstLabel);
	}

	
	
	/************ SCHEDULE **************/
	public void schedule(ActionEvent e) {
		
		if (schedule == false) {
			
			for (int i = 0; i < 8; i++) {
				shifts[i] = new ShiftNode();
				shifts[i].nameButton("Offer");
			}
			
			sGridPane.add(shifts[0].getNode(), 0, 0);
			sGridPane.add(shifts[1].getNode(), 1, 0);
			sGridPane.add(shifts[2].getNode(), 0, 1);
			sGridPane.add(shifts[3].getNode(), 1, 1);
			sGridPane.add(shifts[4].getNode(), 0, 2);
			sGridPane.add(shifts[5].getNode(), 1, 2);
			sGridPane.add(shifts[6].getNode(), 0, 3);
			sGridPane.add(shifts[7].getNode(), 1, 3);
			sGridPane.setGridLinesVisible(true);
			
			sAnchorPane.setPrefHeight(mainStackPane.getHeight());
			sAnchorPane.setPrefWidth(mainStackPane.getWidth());
			sAnchorPane.getChildren().add(sGridPane);
			sGridPane.setMaxHeight(sAnchorPane.getHeight());
			sGridPane.setMaxWidth(sAnchorPane.getWidth());
			
			sAnchorPane.getChildren().add(sTitle);
			sTitle.setLayoutX(255.0);
			sTitle.setLayoutY(10.0);
			sTitle.setMinWidth(sAnchorPane.getWidth());
			sTitle.setAlignment(Pos.CENTER);
			sTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
			AnchorPane.setTopAnchor(sGridPane, 75.0);
			AnchorPane.setLeftAnchor(sGridPane, 15.0);
			AnchorPane.setRightAnchor(sGridPane, 5.0);
			

			schedule = true;
		}
		
		//System.out.println("schedule");					//Testing association with button
		mainStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Schedule");
		mainStackPane.getChildren().add(sAnchorPane);
		
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		
		try {
			
			Connection conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			//System.out.println("connected");
			
			Schedule s = new Schedule(conn);
			OfferShift os = new OfferShift(conn);
			
			s.Run(empID);
			
			for (int i = 0; i < 8; i++) {
				shifts[i].clearValid();
				shifts[i].addName(name);
				shifts[i].addDate(s.date.get(i));
				shifts[i].addStart(s.start_time.get(i));
				shifts[i].addEnd(s.end_time.get(i));
			}
			
			EventHandler<ActionEvent> offerEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					for (int i = 0; i < 8; i++) {
						if (e.getSource().equals(shifts[i].getOffer())) {
							String weekID = s.W_ID.get(i);
							os.Run(empID, weekID);
							if (os.invalid == true)
								shifts[i].kindaValid();
							else
								shifts[i].isValid(true);
						}
					}
				}
			};
			
			for (int i = 0; i < 8; i++) {
				shifts[i].getOffer().setOnAction(offerEvent);
			}
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
	}
	
	
	/************ HOURS AND PAY **************/
	public void hoursAndPay(ActionEvent e) {
		
		if (hoursAndPay == false) {
			hpAnchorPane.getChildren().add(past);
			hpAnchorPane.getChildren().add(time);
			hpAnchorPane.getChildren().add(worked);
			hpAnchorPane.getChildren().add(hours);
			hpAnchorPane.getChildren().add(earned);
			hpAnchorPane.getChildren().add(pay);
			
			hpAnchorPane.getChildren().add(hpTitle);
			hpTitle.setLayoutX(255.0);
			hpTitle.setLayoutY(10.0);
			hpTitle.setMinWidth(hpAnchorPane.getWidth());
			hpTitle.setAlignment(Pos.CENTER);
			hpTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
			past.setLayoutX(0.0);
			past.setLayoutY(101.0);
			past.setPrefHeight(18.0);
			past.setPrefWidth(330.0);
			past.setAlignment(Pos.CENTER_RIGHT);
			past.setTextAlignment(TextAlignment.RIGHT);
			past.setOpaqueInsets(new Insets(0.0, 5.0, 0.0, 0.0));
			
			time.setLayoutX(330.0);
			time.setLayoutY(98.0);
			time.getItems().add(week);
			time.getItems().add(month);
			time.getItems().add(year);
			
			worked.setLayoutX(234.0);
			worked.setLayoutY(187.0);
			worked.setPrefHeight(18.0);
			worked.setPrefWidth(187.0);
			worked.setAlignment(Pos.CENTER);
			worked.setTextAlignment(TextAlignment.CENTER);
			
			hours.setLayoutX(234.0);
			hours.setLayoutY(225.0);
			hours.setPrefHeight(18.0);
			hours.setPrefWidth(187.0);
			
			earned.setLayoutX(234.0);
			earned.setLayoutY(300.0);
			earned.setPrefHeight(18.0);
			earned.setPrefWidth(187.0);
			earned.setAlignment(Pos.CENTER);
			earned.setTextAlignment(TextAlignment.CENTER);
			
			pay.setLayoutX(234.0);
			pay.setLayoutY(338.0);
			pay.setPrefHeight(18.0);
			pay.setPrefWidth(187.0);
			
			hours.setStyle("-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-text-fill: green;");
			hours.setAlignment(Pos.CENTER);
			hours.setTextAlignment(TextAlignment.CENTER);
			
			pay.setStyle("-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-text-fill: green;");
			pay.setAlignment(Pos.CENTER);
			pay.setTextAlignment(TextAlignment.CENTER);
			
			hoursAndPay = true;
		}
		
		//System.out.println("hours&Pay");				//Testing association with button
		mainStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Hours&Pay");
		//mainStackPane.getChildren().add(testLabel);
		mainStackPane.getChildren().add(hpAnchorPane);
		
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		
		try {
			
			Connection conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			//System.out.println("connected");
			
			HoursWorked hw = new HoursWorked(conn);
			hw.Run(loginID);
			String weekHours = hw.pastWKhrs[0];
			String monthHours = hw.pastMnhrs[0];
			String yearHours = hw.pastYrhrs[0];
			
			CalculatePay cp = new CalculatePay(conn);
			cp.Run(loginID, hw.pastWKhrs, hw.pastMnhrs, hw.pastYrhrs);
			String weekPay = cp.totalWPay;
			String monthPay = cp.totalMPay;
			String yearPay = cp.totalYPay;
			
			hours.setText(weekHours + " Hours");
			pay.setText("$" + weekPay);
			
			EventHandler<ActionEvent> weekEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					//System.out.println("Week Selected");
					
					timeTitle = "Week";
					time.setText(timeTitle);

					hours.setText(weekHours + " Hours");
					hours.setStyle("-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-text-fill: green;");
					hours.setAlignment(Pos.CENTER);
					hours.setTextAlignment(TextAlignment.CENTER);
					
					pay.setText("$" + weekPay);
					pay.setStyle("-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-text-fill: green;");
					pay.setAlignment(Pos.CENTER);
					pay.setTextAlignment(TextAlignment.CENTER);
				}
			};
			
			EventHandler<ActionEvent> monthEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					//.println("Month Selected");
					
					timeTitle = "Month";
					time.setText(timeTitle);
					
					hours.setText(monthHours + " Hours");
					hours.setStyle("-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-text-fill: green;");
					hours.setAlignment(Pos.CENTER);
					hours.setTextAlignment(TextAlignment.CENTER);
					
					pay.setText("$" + monthPay);
					pay.setStyle("-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-text-fill: green;");
					pay.setAlignment(Pos.CENTER);
					pay.setTextAlignment(TextAlignment.CENTER);
				}
			};
			
			EventHandler<ActionEvent> yearEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					//System.out.println("Year Selected");
					
					timeTitle = "Year";
					time.setText(timeTitle);
					
					hours.setText(yearHours + " Hours");
					hours.setStyle("-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-text-fill: green;");
					hours.setAlignment(Pos.CENTER);
					hours.setTextAlignment(TextAlignment.CENTER);
					
					pay.setText("$" + yearPay);
					pay.setStyle("-fx-font-size: 25;" + "-fx-font-weight: bold;" + "-fx-text-fill: green;");
					pay.setAlignment(Pos.CENTER);
					pay.setTextAlignment(TextAlignment.CENTER);
				}
			};
			
			week.setOnAction(weekEvent);
			month.setOnAction(monthEvent);
			year.setOnAction(yearEvent);
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
		
	}

	
	/************ TIME OFF **************/
	public void timeOff(ActionEvent e) {
		
		if (timeOff == false) {
			
			toAnchorPane.getChildren().add(toGridPane);
			toGridPane.setLayoutX(164.0);
			toGridPane.setLayoutY(189.0);
			toGridPane.setPrefHeight(90.0);
			toGridPane.setPrefWidth(308.0);
			
			toStart.setPrefHeight(18.0);
			toStart.setPrefWidth(156.0);
			toStart.setAlignment(Pos.CENTER_RIGHT);
			toStart.setOpaqueInsets(new Insets(0.0, 5.0, 0.0, 0.0));
			
			toEnd.setPrefHeight(18.0);
			toEnd.setPrefWidth(156.0);
			toEnd.setAlignment(Pos.CENTER_RIGHT);
			toEnd.setOpaqueInsets(new Insets(0.0, 5.0, 0.0, 0.0));
			
			toGridPane.add(toStart, 0, 0);
			toGridPane.add(startDate, 1, 0);
			toGridPane.add(toEnd, 0, 1);
			toGridPane.add(endDate, 1, 1);
			toGridPane.add(toSubmit, 1, 2);
			toGridPane.add(toSubmitted, 1, 3);
			
			toSubmitted.setText("");
			
			toAnchorPane.getChildren().add(toTitle);
			toTitle.setLayoutX(240.0);
			toTitle.setLayoutY(10.0);
			toTitle.setMinWidth(toAnchorPane.getWidth());
			toTitle.setAlignment(Pos.CENTER);
			toTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
			timeOff = true;
		}
			
			 
		     startDate.setConverter(new StringConverter<LocalDate>() {
			 String pattern = "yyyy-MM-dd";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			 {
			     startDate.setPromptText(pattern.toLowerCase());
			 }

			 @Override public String toString(LocalDate date) {
			     if (date != null) {
			         return dateFormatter.format(date);
			     } else {
			         return "";
			     }
			 }

			 @Override public LocalDate fromString(String string) {
			     if (string != null && !string.isEmpty()) {
			         return LocalDate.parse(string, dateFormatter);
			     } else {
			         return null;
			     }
			 }
			});
		     
		     endDate.setConverter(new StringConverter<LocalDate>() {
			 String pattern = "yyyy-MM-dd";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			 {
			     endDate.setPromptText(pattern.toLowerCase());
			 }

			 @Override public String toString(LocalDate date) {
			     if (date != null) {
			         return dateFormatter.format(date);
			     } else {
			         return "";
			     }
			 }

			 @Override public LocalDate fromString(String string) {
			     if (string != null && !string.isEmpty()) {
			         return LocalDate.parse(string, dateFormatter);
			     } else {
			         return null;
			     }
			 }
			});
		
		//System.out.println("timeOff");					//Testing association with button
		mainStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Time Off");
		mainStackPane.getChildren().add(toAnchorPane);
		toSubmitted.setText("");
		
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		
		try {
			
			Connection conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			//System.out.println("connected");
			
			TimeOffRequest tor = new TimeOffRequest(conn);
			
			EventHandler<ActionEvent> submitEvent = new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e) {
					if(!startDate.equals(null) || !endDate.equals(null)) {
						tor.Run(String.valueOf(empID), startDate.getValue().toString(), endDate.getValue().toString());
						toSubmitted.setText("Submitted!");
						toSubmitted.setStyle("-fx-text-fill: green");
					}
				}
			};
			
			toSubmit.setOnAction(submitEvent);
			
		} catch(Exception exc) {
			System.out.println(exc);
		}
	}
	
	
	/************ CLAIM SHIFT **************/
	public void claimShift(ActionEvent e) {
		
		if (claimShift == false) {
			
			for (int i = 0; i < 8; i++) {
				claims[i] = new ShiftNode();
				claims[i].nameButton("Claim");
			}
			
			csGridPane.add(claims[0].getNode(), 0, 0);
			csGridPane.add(claims[1].getNode(), 1, 0);
			csGridPane.add(claims[2].getNode(), 0, 1);
			csGridPane.add(claims[3].getNode(), 1, 1);
			csGridPane.add(claims[4].getNode(), 0, 2);
			csGridPane.add(claims[5].getNode(), 1, 2);
			csGridPane.add(claims[6].getNode(), 0, 3);
			csGridPane.add(claims[7].getNode(), 1, 3);
			csGridPane.setGridLinesVisible(true);
			
			csAnchorPane.setPrefHeight(mainStackPane.getHeight());
			csAnchorPane.setPrefWidth(mainStackPane.getWidth());
			csAnchorPane.getChildren().add(csGridPane);
			csGridPane.setMaxHeight(csAnchorPane.getHeight());
			csGridPane.setMaxWidth(csAnchorPane.getWidth());
			
			csAnchorPane.getChildren().add(csTitle);
			csTitle.setLayoutX(250.0);
			csTitle.setLayoutY(10.0);
			csTitle.setMinWidth(arAnchorPane.getWidth());
			csTitle.setAlignment(Pos.CENTER);
			csTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
			AnchorPane.setTopAnchor(csGridPane, 75.0);
			AnchorPane.setLeftAnchor(csGridPane, 15.0);
			AnchorPane.setRightAnchor(csGridPane, 5.0);
	
			claimShift = true;
		}
		
		//System.out.println("claimShift");				//Testing association with button
		mainStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Claim Shift");
		mainStackPane.getChildren().add(csAnchorPane);
		
		for (int i = 0; i < 8; i++) {
			claims[i].addName("EMPTY");
			claims[i].addDate("EMPTY");
			claims[i].addStart("EMPTY");
			claims[i].addEnd("EMPTY");
			claims[i].clearValid();
		}
		
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		
		try {
			
			Connection conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			//System.out.println("connected");
			
			ClaimShift cs = new ClaimShift(conn, empID);
			
			for (int i = 0; i < 8; i++) {
				claims[i].addName(cs.giver_names.get(i));
				claims[i].addDate(cs.date.get(i));
				claims[i].addStart(cs.start_time.get(i));
				claims[i].addEnd(cs.end_time.get(i));
				claims[i].clearValid();
			}
			
			EventHandler<ActionEvent> claimEvent = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					//System.out.println(cs.status);
					for (int i = 0; i < 8; i++) {
						if (e.getSource().equals(claims[i].getOffer())) {
							if(cs.status.get(i).substring(0, 1).equals("A")) {
								cs.Run(empID, cs.SGA_ID.get(i));
								claims[i].isValid(false);
								claims[i].getOffer().setOnAction(null);
							} else {
								claims[i].notValid();
							}
							//somewhere here need to have a checker for the valid label
						}
					}
				}
			};
			
			for (int i = 0; i < 8; i++) {
				claims[i].getOffer().setOnAction(claimEvent);
			}
			
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	
	/************ ANONYMOUS REPORT **************/
	public void anonymousReport(ActionEvent e) {
		if (anonymousReport == false) {
			
			arAnchorPane.getChildren().add(report);
			arAnchorPane.getChildren().add(submit);
			arAnchorPane.getChildren().add(submitted);
			arAnchorPane.getChildren().add(arTitle);
			
			arTitle.setLayoutX(220.0);
			arTitle.setLayoutY(10.0);
			arTitle.setMinWidth(arAnchorPane.getWidth());
			arTitle.setAlignment(Pos.CENTER);
			arTitle.setStyle("-fx-font-size: 25;" + "-fx-underline: true;");
			
			report.setLayoutX(78.0);
			report.setLayoutY(62.0);
			report.setPrefHeight(318.0);
			report.setPrefWidth(483.0);
			report.setWrapText(true);
			
			submit.setLayoutX(505.0);
			submit.setLayoutY(391.0);
			
			submitted.setLayoutX(400.0);
			submitted.setLayoutY(391.0);
			
			anonymousReport = true;
		}
		
		submitted.setText("");
		//System.out.println("anonymousReport");			//Testing association with button
		mainStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Anonymous Report");
		//mainStackPane.getChildren().add(testLabel);
		mainStackPane.getChildren().add(arAnchorPane);
		
		report.lengthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than LIMIT
                    if (report.getText().length() >= 500) {

                        // if it's 11th character then just setText to previous
                        // one
                        report.setText(report.getText().substring(0, 499));
                    }
                }
            }
        });
		
		EventHandler<ActionEvent> submitEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				//System.out.println("Submit Button Pressed");
				
				String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
				
				try {
					
					Connection conn = DriverManager.getConnection(url);
					Statement st = conn.createStatement();
					//System.out.println("connected");
					
					CreateReport cr = new CreateReport(conn);
					
					if (report.getText().length() > 0) {
						cr.Run(report.getText());
						report.clear();
						submitted.setText("Submitted!");
						submitted.setStyle("-fx-text-fill: green");
					}
		
					
				} catch(Exception ex) {
					System.out.println(ex);
				}
				
			}
		};
		
		submit.setOnAction(submitEvent);
	}
	
	
	/************ SWITCH TO LOGIN **************/
	public void switchToLogin(ActionEvent e) throws IOException {
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
		name = username;
		nameLabel.setText("Welcome, " + username + "!");
	}
	
	public void setLoginID(int loginID) {
		this.loginID = loginID;
	}
	
	public void setEmpID(int empID) {
		this.empID = empID;
	}
	
}