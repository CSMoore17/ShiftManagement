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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	
	@FXML	//Very important line
	TextField usernameTextField;
	@FXML	//Very important line
	TextField passwordTextField;
	@FXML 	//Very important line
	Label invalidLabel;


	private Stage stage;
	private Scene scene;
	private Parent root;
	private boolean admin = false;
	private boolean employee = false;
	
	//SQL variables
	private Connection c;
	
//	public void switchToLogin(ActionEvent e) throws IOException {
//		root = FXMLLoader.load(getClass().getResource("Login.fxml"));		//Must make root the same as our Main Scene
//		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		stage.setScene(scene);
//		stage.show();
//	}
	
	public void switchToScene(ActionEvent e) throws IOException {
		
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		String name = "";											//will reference this later on
		
		try {
			Connection conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			System.out.println("connected");
			
			//************************************************************************************************************************
			
			Login login = new Login(conn);							//object of Chris's Login class
			login.Run(usernameTextField.getText(), passwordTextField.getText());
			
			if (login.valid_login == true) {
				if (login.User_Type.equals("E")) {
					employee = true;																				//employee is true
					
					String sql_stat = String.format("Select * From Employee Where Login_ID='%s'", login.Login_ID);	//creating sql question string
                    ResultSet RS = st.executeQuery(sql_stat);														//returning result of sql question

                    if(RS.next() == true)
                    {
                        System.out.print("\nWelcome Employee: " + RS.getString("name"));
                        name = RS.getString("name");																//initialize name
                    }
				}
				
				//checking if the user is an Admin
				if (login.User_Type.equals("A")) {
					admin = true;																					//admin is true
					
					String sql_stat = String.format("Select * From Admin Where Login_ID='%s'", login.Login_ID);		//creating sql question string
                    ResultSet RS = st.executeQuery(sql_stat);														//returning result of sql question

                    //if there is a corresponding login to submitted information
                    if(RS.next() == true)
                    {
                        System.out.print("\nWelcome Admin: " + RS.getString("name"));
                        name = RS.getString("name");																//initialize name
                    }
				}
			}
			
			FXMLLoader loader;													//Declare a FXML Loader
			Controller controller;												//Declare a controller
			AdminController adminController;									//Declare an Admin controller
			if (employee == true) {												//If not an admin
				loader = new FXMLLoader(getClass().getResource("Main.fxml"));	//Prepares to load the employee page (Main.fxml)
				root = loader.load();											//Loads to root
				controller = loader.getController();							//creates controller object for Main.fxml
				controller.displayName(name);									//calls displayName method in Controller to display username on scene
				
				stage = (Stage)((Node)e.getSource()).getScene().getWindow();
				scene = new Scene(root);
				String css = this.getClass().getResource("application.css").toExternalForm();	//CSS styling for Main
				scene.getStylesheets().add(css);												//Adding CSS to scene
				stage.setMinHeight(580.0);
				stage.setMinWidth(896.0);
				stage.setMaxHeight(1440.0);
				stage.setMaxWidth(2560.0);
				stage.setScene(scene);
				stage.show();
			}
			else if (admin == true) {									        //If an admin
				loader = new FXMLLoader(getClass().getResource("Admin.fxml"));	//Prepares to load the admin page (Admin.fxml)
				root = loader.load();											//Loads to root
				adminController = loader.getController();						//creates controller object for Admin.fxml
				adminController.displayName(name);							//calls displayName method in Controller to display username on scene
				
				stage = (Stage)((Node)e.getSource()).getScene().getWindow();
				scene = new Scene(root);
				String css = this.getClass().getResource("application.css").toExternalForm();	//CSS styling for Main
				scene.getStylesheets().add(css);												//Adding CSS to scene
				stage.setMinHeight(580.0);
				stage.setMinWidth(896.0);
				stage.setMaxHeight(1440.0);
				stage.setMaxWidth(2560.0);
				stage.setScene(scene);
				stage.show();
			}
			else {
				System.out.println("Invalid login information");
				invalidLabel.setText("(Invalid)");
				
			}
			
			//root = FXMLLoader.load(getClass().getResource("Main.fxml"));		//Must make root the same as our Main Scene

			
			//************************************************************************************************************************
			
			st.close();
			conn.close();
			
		} catch(Exception ex) {
			System.out.println(ex);
		}
		
//		String username;
//		
//		if (usernameTextField.getText().equals("") || usernameTextField.getText().equals(null))
//			username = "ENTER NAME HERE";								//Retrieves text from the username text field
//		else
//			username = usernameTextField.getText();
//		
//		if (usernameTextField.getText().toLowerCase().equals("admin"))		//if username is admin
//			admin = true;													//admin = true
//		
//		FXMLLoader loader;													//Declare a FXML Loader
//		Controller controller;												//Declare a controller
//		AdminController adminController;									//Declare an Admin controller
//		if (admin == false) {												//If not an admin
//			loader = new FXMLLoader(getClass().getResource("Main.fxml"));	//Prepares to load the employee page (Main.fxml)
//			root = loader.load();											//Loads to root
//			controller = loader.getController();							//creates controller object for Main.fxml
//			controller.displayName(username);								//calls displayName method in Controller to display username on scene
//		}
//		else {																//If an admin
//			loader = new FXMLLoader(getClass().getResource("Admin.fxml"));	//Prepares to load the admin page (Admin.fxml)
//			root = loader.load();											//Loads to root
//			adminController = loader.getController();						//creates controller object for Admin.fxml
//			adminController.displayName(username);							//calls displayName method in Controller to display username on scene
//		}
//		
//		//root = FXMLLoader.load(getClass().getResource("Main.fxml"));		//Must make root the same as our Main Scene
//		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		String css = this.getClass().getResource("application.css").toExternalForm();	//CSS styling for Main
//		scene.getStylesheets().add(css);												//Adding CSS to scene
//		stage.setMinHeight(580.0);
//		stage.setMinWidth(896.0);
//		stage.setMaxHeight(1440.0);
//		stage.setMaxWidth(2560.0);
//		stage.setScene(scene);
//		stage.show();
	}
}