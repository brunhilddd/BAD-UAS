package main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/latihan";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "";
	
	private TextField tf_name;
	private TextArea ta_address;
	private TextField tf_price;
	private ComboBox<String> cb_af;
	private Spinner<String> sp_age;
	
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	primaryStage.setTitle("Figure");
    	
    	//layout
    	GridPane gp = new GridPane();
    	gp.setPadding(new Insets(20,20,20,20));
    	gp.setVgap(10);
    	gp.setHgap(10);
    	gp.setAlignment(Pos.CENTER);
    	
    	//title
    	Label lbl_title = new Label("Action Figure Shop");
    	lbl_title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
    	gp.add(lbl_title, 15, 0);
    	
    	//name
    	Label lbl_name = new Label("Name :");
    	tf_name = new TextField();
    	gp.add(lbl_name, 15, 1);
    	gp.add(tf_name, 15, 2);
    	
    	//adress
    	Label lbl_address = new Label("Address : ");
    	ta_address = new TextArea();
    	ta_address.setWrapText(true);
    	gp.add(lbl_address, 15, 3);
    	gp.add(ta_address, 15, 4);
    	
    	//action figure
    	Label lbl_af = new Label("Action Figure :");
    	cb_af = new ComboBox<>();
    	populateComboBox();
    	gp.add(lbl_af, 15, 5);
    	gp.add(cb_af, 15, 6);
    	
    	//quantity
    	Label lbl_quantity = new Label ("Quantity :");
    	sp_age = new Spinner<>(1, 10, 1); 
    	gp.add(lbl_quantity, 15, 7);
    	gp.add(sp_age, 15, 8);
    	
    	//price
    	Label lbl_price = new Label("Price :");
    	tf_price = new TextField();
    	tf_price.setEditable(false);
    	gp.add(lbl_price, 15, 9);
    	gp.add(tf_price, 15,10);
    	
    	//button exit
    	Button btn_exit = new Button("Exit");
    	gp.add(btn_exit, 15, 20);
    	
    	//button submit
    	Button btn_submit = new Button("Submit");
    	btn_submit.setOnAction(e -> saveData());
    	gp.add(btn_submit, 16, 20);
    	
    	//menubar
    	MenuBar menuBar = new MenuBar();
    	Menu menu = new Menu("Menu");
    	Menu logout = new Menu("Logout");
    	MenuItem menuItem = new MenuItem("Cart");
    	menu.getItems().add(menuItem);
    	menuBar.getMenus().addAll(menu, logout);
    	
    	//nampilin price dari action figure
        cb_af.setOnAction(event -> {
            updatePriceTextField();
        });
    	
        //layout menubar
    	VBox vBox = new VBox();
    	vBox.getChildren().addAll(menuBar, gp);
    	
    	VBox.setVgrow(menuBar, Priority.ALWAYS);
    	VBox.setVgrow(gp, Priority.ALWAYS);
    	
    	Scene sc = new Scene(vBox, 800, 800);
    	primaryStage.setScene(sc);
    	primaryStage.show();
    }

    private void populateComboBox() {
        try {
        	Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            String query = "SELECT name FROM products";
            try (PreparedStatement preparedStatement = con.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
                ObservableList<String> figureNames = FXCollections.observableArrayList();

                while (resultSet.next()) {
                    String figureName = resultSet.getString("name");
                    figureNames.add(figureName);
                }

                cb_af.setItems(figureNames);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void updatePriceTextField() {
        String selectedFigure = cb_af.getValue();
        if (selectedFigure != null && !selectedFigure.isEmpty()) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "SELECT price FROM products WHERE name = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, selectedFigure);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int price = resultSet.getInt("price");
                            tf_price.setText(String.valueOf(price));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void saveData() {
    	try {
    		
            String customerName = tf_name.getText();
            String customerAddress = ta_address.getText();
            String quantity = tf_price.getText();

            if (customerName.length() > 20) {
                showPopUp("Nama tidak boleh lebih dari 20 karakter");
                return; // Keluar dari metode jika nama tidak valid
            }
            
            if (customerAddress.length() > 60) {
				showPopUp("Alamat tidak boleh lebih dari 60 karakter");
				return;
			}
            
                       
			Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			String sql = "INSERT INTO orders(CustomerName, CustomerAddress, Quantity) VALUES (?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(sql);
			
			
			ps.setString(1, tf_name.getText());
			ps.setString(2, ta_address.getText());
			ps.setString(3, tf_price.getText());
			
			ps.executeUpdate();
			
			ps.close();
			con.close();
			
			showPopUp("Data Berhasil Tersimpan");
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
    }

    private void showPopUp(String message) {
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	alert.setTitle("Information");
    	alert.setHeaderText(null);
    	alert.setContentText(message);
    	alert.showAndWait();
    }
    

}