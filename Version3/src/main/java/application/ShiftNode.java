package application;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ShiftNode {

	@FXML
	private AnchorPane node;
	private Label name;
	private Label date;
	private Label start;
	private Label end;
	private Button offer;
	private Label valid;
	
	//Constructor
	public ShiftNode() {
		node = new AnchorPane();
		name = new Label();
		date = new Label();
		start = new Label();
		end = new Label();
		offer = new Button();
		valid = new Label();
		initialize();
	}
	
	//Initialize node layout
	public void initialize() {
		node.getChildren().add(name);
		node.getChildren().add(date);
		node.getChildren().add(start);
		node.getChildren().add(end);
		node.getChildren().add(offer);
		node.getChildren().add(valid);
		name.setLayoutY(5.0);
		name.setPrefHeight(18.0);
		name.setPrefWidth(200.0);
		name.setPadding(new Insets(0.0, 0.0, 0.0, 5.0));
		date.setLayoutY(23.0);
		date.setPrefHeight(18.0);
		date.setPrefWidth(200.0);
		date.setPadding(new Insets(0.0, 0.0, 0.0, 5.0));
		start.setLayoutY(41.0);
		start.setPrefHeight(18.0);
		start.setPrefWidth(200.0);
		start.setPadding(new Insets(0.0, 0.0, 0.0, 5.0));
		end.setLayoutY(59.0);
		end.setPrefHeight(18.0);
		end.setPrefWidth(200.0);
		end.setPadding(new Insets(0.0, 0.0, 0.0, 5.0));
		offer.setLayoutX(252.0);
		offer.setLayoutY(61.0);
	}

	public void addName(String name) {
		this.name.setText("Name: " + name);
	}
	
	public void addDate(String date) {
		this.date.setText("Date: " + date);
	}
	
	public void addStart(String start) {
		this.start.setText("Start: " + start);
	}
	
	public void addEnd(String end) {
		this.end.setText("End: " + end);
	}
	
	public void nameButton(String nb) {
		offer.setText(nb);
	}
	
	public void isValid(boolean offer) {
		if (offer)
			valid.setText("Offered!");
		else
			valid.setText("Claimed!");
		valid.setStyle("-fx-text-fill: green;");
		valid.setLayoutX(190.0);
		valid.setLayoutY(61.0);
	}
	
	public void notValid() {
		valid.setText("Invalid");
		valid.setStyle("-fx-text-fill: red;");
		valid.setLayoutX(200.0);
		valid.setLayoutY(61.0);
	}
	
	public void clearValid() {
		valid.setText("");
		valid.setStyle("");
	}
	
	public void kindaValid() {
		valid.setText("Pending..");
		valid.setStyle("-fx-text-fill: yellow;");
		valid.setLayoutX(190.0);
		valid.setLayoutY(61.0);
	}
	
	
	/********** GETTERS AND SETTERS ************/
	public AnchorPane getNode() {
		return node;
	}

	public void setNode(AnchorPane node) {
		this.node = node;
	}

	public Label getName() {
		return name;
	}

	public void setName(Label name) {
		this.name = name;
	}

	public Label getDate() {
		return date;
	}

	public void setDate(Label date) {
		this.date = date;
	}

	public Label getStart() {
		return start;
	}

	public void setStart(Label start) {
		this.start = start;
	}

	public Label getEnd() {
		return end;
	}

	public void setEnd(Label end) {
		this.end = end;
	}

	public Button getOffer() {
		return offer;
	}

	public void setOffer(Button offer) {
		this.offer = offer;
	}

	public Label getValid() {
		return valid;
	}

	public void setValid(Label valid) {
		this.valid = valid;
	}	
}