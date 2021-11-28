package application;
import java.io.*;
import java.sql.*;
import java.util.*;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Controller {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	//SQL variables
	private Connection c;
	
	@FXML	//Very important line
	StackPane mainStackPane;
	Label firstLabel = new Label("Representation of Home Screen");
	Label testLabel;
	
	@FXML	//Very important line
	Label nameLabel;
	
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
	
	public void hoursWorked(ActionEvent e) {
		System.out.println("hoursWorked");				//Testing association with button
		mainStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Hours Worked");
		mainStackPane.getChildren().add(testLabel);
	}
	
	public void calculatePay(ActionEvent e) {
		System.out.println("calculatePay");				//Testing association with button
		mainStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Calculate Pay");
		mainStackPane.getChildren().add(testLabel);
	}
	
	public void timeOff(ActionEvent e) {
		System.out.println("timeOff");					//Testing association with button
		mainStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Time Off");
		mainStackPane.getChildren().add(testLabel);
	}
	
	public void offerShift(ActionEvent e) {
		System.out.println("offerShift");				//Testing association with button
		mainStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Offer Shift");
		mainStackPane.getChildren().add(testLabel);
	}
	
	public void claimShift(ActionEvent e) {
		System.out.println("claimShift");				//Testing association with button
		mainStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Claim Shift");
		mainStackPane.getChildren().add(testLabel);
	}
	
	public void anonymousReport(ActionEvent e) {
		System.out.println("anonymousReport");			//Testing association with button
		mainStackPane.getChildren().remove(0);
		testLabel = new Label("Representation of Anonymous Report");
		mainStackPane.getChildren().add(testLabel);
	}
	
	public void switchToLogin(ActionEvent e) throws IOException {
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
