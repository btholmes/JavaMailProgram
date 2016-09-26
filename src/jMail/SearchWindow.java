package jMail; 

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

public class SearchWindow extends Application {
//
    public static void main(String[] args) {
        launch(args);
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

        Text scenetitle = new Text("Search Window");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 50));
        scenetitle.setFill(Color.WHITE);
        scenetitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(scenetitle, 0, 0, 2, 1);

        Label sbj = new Label("Subject: ");
        grid.add(sbj, 0, 2);

        TextField subject = new TextField();
        subject.setPrefHeight(20); 
        grid.add(subject, 1, 2);
        
        Label results = new Label("Results: ");
        grid.add(results, 0, 3);

        TextArea msgBox = new TextArea();
//        msgBox.setPrefHeight(20); 
        grid.add(msgBox, 1, 3);

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

            	Thread thread;
				try {
					thread = new Thread(new SearchMail("bholmes6831@gmail.com", "jJazz743", subject.getText()));
					thread.start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
   	
            }
        });

        Scene scene = new Scene(grid, 700, 700);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(SearchWindow.class.getResource("Login.css").toExternalForm()); 
        primaryStage.show();
    }

}
