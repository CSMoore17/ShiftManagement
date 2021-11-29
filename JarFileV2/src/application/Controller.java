package application;
import java.io.*;
import java.sql.*;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

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
	
	
	@FXML	//Very important line
	StackPane mainStackPane;
	Label firstLabel = new Label("Representation of Home Screen");
	Label testLabel;
	
	@FXML	//Very important line
	Label nameLabel;
	
	@FXML //hoursAndPay objects
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
	
	@FXML //anonymousReport objects
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

	public void schedule(ActionEvent e) {
		System.out.println("schedule");					//Testing association with button
		mainStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Schedule");
		mainStackPane.getChildren().add(testLabel);
		
	}
	
	public void hoursAndPay(ActionEvent e) {
		
		if (hoursAndPay == false) {
			hpAnchorPane.getChildren().add(past);
			hpAnchorPane.getChildren().add(time);
			hpAnchorPane.getChildren().add(worked);
			hpAnchorPane.getChildren().add(hours);
			hpAnchorPane.getChildren().add(earned);
			hpAnchorPane.getChildren().add(pay);
			
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
		
		System.out.println("hours&Pay");				//Testing association with button
		mainStackPane.getChildren().remove(0);
		//testLabel = new Label("Representation of Hours&Pay");
		//mainStackPane.getChildren().add(testLabel);
		mainStackPane.getChildren().add(hpAnchorPane);
		
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		
		try {
			
			Connection conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			System.out.println("connected");
			
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
					System.out.println("Week Selected");
					
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
					System.out.println("Month Selected");
					
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
					System.out.println("Year Selected");
					
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
	
	public void timeOff(ActionEvent e) {
		System.out.println("timeOff");					//Testing association with button
		mainStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Time Off");
		mainStackPane.getChildren().add(testLabel);
	}
	
	public void claimShift(ActionEvent e) {
		System.out.println("claimShift");				//Testing association with button
		mainStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Claim Shift");
		mainStackPane.getChildren().add(testLabel);
	}
	
	public void anonymousReport(ActionEvent e) {
		if (anonymousReport == false) {
			arAnchorPane.getChildren().add(report);
			arAnchorPane.getChildren().add(submit);
			arAnchorPane.getChildren().add(submitted);
			
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
		System.out.println("anonymousReport");			//Testing association with button
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
                        report.setText(report.getText().substring(0, 500));
                    }
                }
            }
        });
		
		EventHandler<ActionEvent> submitEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.out.println("Submit Button Pressed");
				
				String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
				
				try {
					
					Connection conn = DriverManager.getConnection(url);
					Statement st = conn.createStatement();
					System.out.println("connected");
					
					CreateReport cr = new CreateReport(conn);
					
					if (report.getText().length() > 0) {
						cr.Run(report.getText());
						report.clear();
						submitted.setText("Submitted");
						submitted.setStyle("-fx-text-fill: green");
					}
		
					
				} catch(Exception ex) {
					System.out.println(ex);
				}
				
			}
		};
		
		submit.setOnAction(submitEvent);
	}
	
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
	
//	public void switchToScene(ActionEvent e) throws IOException {
//		root = FXMLLoader.load(getClass().getResource("Main.fxml"));		//Must make root the same as our Main Scene
//		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		String css = this.getClass().getResource("application.css").toExternalForm();
//		scene.getStylesheets().add(css);
//		stage.setScene(scene);
//		stage.show();
//	}
	
	public void displayName(String username) {								//Displays name on nameLabel
		nameLabel.setText("Welcome, " + username + "!");
	}
	
	public void setLoginID(int loginID) {
		this.loginID = loginID;
	}
	
//	public void logout(ActionEvent e) {										//Method for logging out
//		
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle("Logout");
//		alert.setHeaderText("You're about the logout!");
//		alert.setContentText("Are you sure you'd like to leave?");
//		
//		if(alert.showAndWait().get() == ButtonType.OK) {
//			stage = (Stage) scenePane.getScene().getWindow();
//			System.out.println("You successfully logged out!");
//			stage.close();
//		}
//	}
}
