package Main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/coba";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "";
	
	private TextField tf_name;
	private TextField tf_age;
	private ListView<String> lv;
	
	
	public static void main(String[] args) {
	    launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("COBA");
		
		// layout
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(20,20,20,20));
		gp.setVgap(10);
		gp.setHgap(20);
		
		//nama
		Label lb_nama = new Label("Nama : ");
		tf_name = new TextField();
		gp.add(lb_nama, 0, 1);
		gp.add(tf_name, 1, 1);
		
		//umurr
        Label umurMember = new Label("Umur : ");
        tf_age = new TextField();
        gp.add(umurMember, 0, 2);
        gp.add(tf_age, 1, 2);
        
        //listview
		lv = new ListView<>();
		gp.add(lv, 0, 3);
		
		//menubar
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Menu");
		MenuItem menuItem = new MenuItem("Home");
		menu.getItems().add(menuItem);
		menuBar.getMenus().add(menu);
		
		// save button
		Button btn_save = new Button("Save");
		btn_save.setOnAction(e -> saveData());
		gp.add(btn_save, 3, 4);
		
		// back button
		Button btn_back = new Button("Back");
		gp.add(btn_back, 0, 4);
		
		// munculin menubar
		VBox vBox = new VBox();
		vBox.getChildren().addAll(menuBar, gp);
		
		VBox.setVgrow(menuBar, Priority.ALWAYS);
		VBox.setVgrow(gp, Priority.ALWAYS);
		vBox.setAlignment(Pos.TOP_CENTER);
		
		Scene sc = new Scene(vBox, 500, 450);
		primaryStage.setScene(sc);
		primaryStage.show();
		
		//pindah scene
		menuItem.setOnAction(e -> {
			new Home(primaryStage);
		});
		
	}
	
	private void saveData() {
		try {
			Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			
			String sql = "INSERT INTO member (memberName, memberAge) VALUES (?, ?)";
			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setString(1, tf_name.getText());
			ps.setString(2, tf_age.getText());
			
			ps.executeUpdate();
			
			ps.close();
			con.close();
			
			listView();
			
			showPopup("Data Berhasil disimpan");
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private void listView() {
		try {
			Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			String sql = "SELECT memberName FROM member";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			List<String> memberNames = new ArrayList<>();
			while(rs.next()) {
				memberNames.add(rs.getString("memberName"));
				
			}
		lv.getItems().setAll(memberNames);
		
		ps.close();
		rs.close();
		con.close();
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	private void showPopup(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
