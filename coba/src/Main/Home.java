package Main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class Home extends Application{
	
	private TextField tf_name;
	private TextField tf_age;
	private ListView<String> lv;
	private Stage primaryStage;
	
	public Home(Stage primaryStage) {
		this.primaryStage = primaryStage;
		start(primaryStage);
	}


	@Override
	public void start(Stage primaryStage){
		primaryStage.setTitle("HOME");
		
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
        
		lv = new ListView<>();
		gp.add(lv, 0, 3);
		

		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Menu");
		MenuItem menuItem = new MenuItem("Home");
		menu.getItems().add(menuItem);
		menuBar.getMenus().add(menu);
		
		
		Button btn_save = new Button("Save");

		gp.add(btn_save, 3, 4);
		
		Button btn_back = new Button("Back");
		gp.add(btn_back, 0, 4);
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(menuBar, gp);
		
		VBox.setVgrow(menuBar, Priority.ALWAYS);
		VBox.setVgrow(gp, Priority.ALWAYS);
		vBox.setAlignment(Pos.TOP_CENTER);
		
		Scene sc = new Scene(vBox, 500, 450);
		primaryStage.setScene(sc);
		primaryStage.show();
		
	}

}
