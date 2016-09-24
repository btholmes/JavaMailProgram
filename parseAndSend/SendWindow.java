package parseAndSend; 

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.TextArea; 

public class SendWindow extends Application {
//
//    public static void main(String[] args) {
//        launch(args);
//    }
	String userName; 
	String passWord; 
	
	public SendWindow(String user, String pass){
		userName = user; 
		passWord = pass; 
	}

    @Override
    public void start(Stage primaryStage) {
    	
        primaryStage.setTitle("Send Email");
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: linear-gradient(#6a5acd, #ffffff);");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Fill in the Form");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 50));
        scenetitle.setFill(Color.WHITE);
        scenetitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(scenetitle, 0, 0, 2, 1);

        Label destination = new Label("Send To: ");
        grid.add(destination, 0, 1);

        TextField userTextField = new TextField();
        userTextField.setPromptText("hello@iastate.edu"); 
        grid.add(userTextField, 1, 1);

        Label sbj = new Label("Subject: ");
        grid.add(sbj, 0, 2);

        TextField subject = new TextField();
        subject.setPrefHeight(20); 
        grid.add(subject, 1, 2);
        
        Label msg = new Label("Message: ");
        grid.add(msg, 0, 3);

        TextArea msgBox = new TextArea();
//        msgBox.setPrefHeight(20); 
        grid.add(msgBox, 1, 3);
        
        Label attach = new Label("Attachment: ");
        grid.add(attach, 0, 4);

        TextField item = new TextField();
        item.setPrefHeight(20); 
        grid.add(item, 1, 4);

        Button btn = new Button("Send");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 5);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {            	
            	SendMail checkDest = new SendMail(userTextField.getText(), msgBox.getText(), subject.getText(), item.getText(), userName, passWord); 
           
            	if(checkDest.recipient == true && ((checkDest.attached  && checkDest.fileExists) || (!checkDest.attached))){
//                	Thread send = new Thread(new SendMail(userTextField.getText(), msgBox.getText(), subject.getText(), item.getText(), userName, passWord)); 
//                    send.start(); 
                    actiontarget.setFill(Color.BLACK);
                    actiontarget.setStyle("-fx-font: 20 arial");
                    actiontarget.setText("Message Sent");
            	}else{
            		if(checkDest.attached  && !checkDest.fileExists){
            			actiontarget.setFill(Color.FIREBRICK);
            			   actiontarget.setStyle("-fx-font: 20 arial");
                        actiontarget.setText("Attachment Doesn't Exist");
            		}
            		else if(((checkDest.attached  && checkDest.fileExists) || !checkDest.attached) && !checkDest.recipient) {
            			actiontarget.setFill(Color.FIREBRICK);
            			   actiontarget.setStyle("-fx-font: 20 arial");
            			actiontarget.setText("Recipient Doesn't Exist");
            		}
            		else if((checkDest.attached && !checkDest.fileExists)  && !checkDest.recipient){
            			actiontarget.setFill(Color.FIREBRICK);
            			   actiontarget.setStyle("-fx-font: 20 arial");
            			actiontarget.setText("Recipient and Attachment Don't Exist");
            		}
            	}
            	
            	
            }
        });

        Scene scene = new Scene(grid, 700, 700);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(SendWindow.class.getResource("Login.css").toExternalForm()); 
        primaryStage.show();
    }

}