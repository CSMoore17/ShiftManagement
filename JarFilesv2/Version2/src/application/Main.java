package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Main extends Application {					//Application class is the parent class
	
	private String empName = "EmployeeName";
	
	public static void main(String[] args) {
		launch(args);									//launch is a static method inherited from application called to launch the app
	}

	@Override
	public void start(Stage stage) throws Exception {	//abstract start method from Application class is needed to run

		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		//Stage stage = new Stage();
		//Group root = new Group();						//Group is imported from javafx.scene -> need to pass a root to scene
		Scene scene = new Scene(root);		//Creating a scene and passing it the "Group" root node, and color background
		//Scene scene = new Scene (root,600,600,Color.BLACK) //makes the scene 600x600 pixels -> the stage will adjust with the screen
		
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		String css = this.getClass().getResource("application.css").toExternalForm();
		scene.getStylesheets().add(css);
		
//		Text text = new Text();							//Text is a type of node that needs to be added to the root node
//		text.setText("Welcome, " + empName + "!");
//		text.setX(50);									
//		text.setY(50);
//		text.setFont(Font.font("Times New Roman", 50));
		//text.setFill(Color.FORESTGREEN); 				
//		
//		Line line = new Line();							//Line is a type of node that needs to be added to the root node
//		line.setStartX(50);
//		line.setStartY(70);
//		line.setEndX(610);
//		line.setEndY(70);
//		line.setStrokeWidth(1);							//Thickness
//		line.setStroke(Color.BLACK);					//Color
//		//line.setOpacity(0.5);							//Opacity -> makes an object transparent -> "0.5" means 50% transparent
//		//line.setRotate(45); 							//Rotates line 45 degrees
//		
//		Rectangle rectangle = new Rectangle();			//Rectangle is a type of node that needs to be added to the root node
//		rectangle.setX(100);
//		rectangle.setY(100);
//		rectangle.setWidth(100);
//		rectangle.setHeight(100);
//		rectangle.setFill(Color.BLUE);
//		rectangle.setStrokeWidth(1);
//		rectangle.setStroke(Color.BLACK);
//		rectangle.setOpacity(0.01);
//		
//		Polygon triangle = new Polygon();				//Triangle is a type of node that needs to be added to the root node
//		triangle.getPoints().setAll(
//				200.0,200.0,
//				300.0,300.0,
//				200.0,300.0
//				);
//		triangle.setFill(Color.YELLOW);
//		
//		Circle circle = new Circle();					//Circle is a type of node that needs to be added to the root node
//		circle.setCenterX(350);
//		circle.setCenterY(350);
//		circle.setRadius(50);
//		circle.setFill(Color.ORANGE);
//		
//		Image image = new Image("GetWoostered.png");	//Image must be saved in src folder
//		ImageView imageView = new ImageView(image);		//image must be passed into an imageView object
//		imageView.setX(940);
//		imageView.setY(20);
//		imageView.setPreserveRatio(true); 				//Needed to resize
//		imageView.setFitHeight(100);					//height in pixels
//		imageView.setFitWidth(100);						//width in pixels
//		
//		//root.getChildren().add(text);					//adds text node to the root node
//		//root.getChildren().add(line);					//adds line node to the root node
//		//root.getChildren().add(rectangle);			//adds rectangle to the root node
//		//root.getChildren().add(circle);				//adds circle to the root node
//		//root.getChildren().add(triangle);				//adds triangle to the root node
//		//root.getChildren().add(imageView);
//		
		Image icon = new Image("GetWoostered.png");		//MUST HAVE IMAGE INSIDE SRC FOLDER
		stage.getIcons().add(icon);						//Adds icon to the stage
		stage.setTitle("WorkWeek");						//Gives the stage a title
		stage.setMinHeight(580.0);
		stage.setMinWidth(896.0);
		stage.setMaxHeight(580.0);
		stage.setMaxWidth(896.0);
//		stage.setWidth(1080);							//Sets width to 1080 pixels
//		stage.setHeight(720);							//Sets height to 720 pixels
//		//stage.setResizable(false)						//Makes it impossible to adjust size of screen
//		//stage.setX(50);								//Set specific x value for stage to open at (50 = right side of screen)
//		//stage.setY(50);								//Set specific y value for stage to open at (50 = top side of screen)
//		//stage.setFullScreen(true); 						//Makes stage launch in full screen mode by default
//		//stage.setFullScreenExitHint("YOU CAN'T ESCAPE UNLESS YOU PRESS Q");
//		//stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("Q"));
//		
		stage.setScene(scene);							//Passing our scene to our stage
		stage.show();									//Showing our stage
		
	}
}

//Follow steps of Bro Code: https://www.youtube.com/watch?v=9XJicRt_FaI 
//Add JavaFX to the Module-Path instead of the Class-Path
//Add the VM argument: --module-path "C:\Users\Hayden\Downloads\openjfx-17.0.0.1_windows-x64_bin-sdk\javafx-sdk-17.0.0.1\lib" --add-modules javafx.controls,javafx.fxml
//MAKE SURE JDK 17 STAYS IN DOWNLOADS FOLDER

//Stage - a top level container (a window) similar to JFrame. Holds the scenes
//Scene - added to the stage. A drawing surface for content. Similar to JPanel
//Scene-Graph - a hierarchical tree of nodes. Nodes being content elements (at least need 1 root node)
//