package parseAndSend;

import javax.mail.MessagingException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage; 

public class SignInWindow extends Application {

	 public static void main(String[] args) {
	        launch(args);
	    }

	    @Override
	    public void start(Stage primaryStage) {
	        primaryStage.setTitle("Sign In");
	        GridPane grid = new GridPane();
	        grid.setAlignment(Pos.CENTER);
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(25, 25, 25, 25));

	        Text scenetitle = new Text("Please Sign in");
	        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	        grid.add(scenetitle, 0, 0, 2, 1);

	        Label user = new Label("Email: ");
	        grid.add(user, 0, 1);

	        TextField email = new TextField();
	        email.setPromptText("hello@iastate.edu"); 
	        grid.add(email, 1, 1);

	        Label pwd = new Label("Password: ");
	        grid.add(pwd, 0, 2);

	        PasswordField pass = new PasswordField();
	        pass.setPrefHeight(20); 
	        grid.add(pass, 1, 2);
	        

	        Button btn = new Button("Sign In");
	        HBox hbBtn = new HBox(10);
	        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	        hbBtn.getChildren().add(btn);
	        grid.add(hbBtn, 1, 5);

	        final Text actiontarget = new Text();
	        grid.add(actiontarget, 1, 6);

	        btn.setOnAction(new EventHandler<ActionEvent>() {
	        	
	            @Override
	            public void handle(ActionEvent e) {	            		

	                SendMail auth = null;
					try {
						auth = new SendMail(email.getText(), pass.getText());
					} catch (MessagingException e1) {
					} 
					if(auth.authenticated == true){
	            	//This was a workaround to launch another javaFX window, at first I thought
	            	//I could just implement Runnable in SendWindow() but that didn't work, I was getting
	            	//an error that said you can only call launch 1x within an application
	            	//This was the only way to start SendWindow() that I found. 					
					primaryStage.hide(); 
	            	actiontarget.setFill(Color.FIREBRICK);
	                actiontarget.setText("Signed In");	
		            	Platform.runLater(new Runnable() {
		            	       public void run() {             
		        	                new SendWindow(email.getText(), pass.getText()).start(new Stage());
		            	       }
		            	}); 
					}else{
						//False authentication, so ask user to sign in again
						actiontarget.setText("Wrong user or password");	
					}
	                
	            }
	        });

	        Scene scene = new Scene(grid, 300, 275);
	        primaryStage.setScene(scene);
	        scene.getStylesheets().add(SendWindow.class.getResource("Login.css").toExternalForm()); 
	        primaryStage.show();
	    }
	
}
