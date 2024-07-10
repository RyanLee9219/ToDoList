package application;
	
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
	private Connection conn;
	
	@Override
	public void start(Stage primaryStage){
		conn = dbConnector();
		ListView<String> lv = new ListView<String>();
		ObservableList<String> ov = FXCollections.observableArrayList();
		lv.setItems(ov);
		
		TextField addtf = new TextField();
		addtf.setPromptText("Add ToDo");

		TextField updatetf = new TextField();
		updatetf.setPromptText("Update Todo");
		
		Button addbt = new Button("Add");
		Button updatebt = new Button("Update");
		Button removebt = new Button("Remove");
		
		//db
		ObservableList<String> dbOv = new TodoListDb(conn).loadData();
		ov.addAll(dbOv);
		
		lv.getSelectionModel().selectedItemProperty().addListener((a,oldValue,newValue)->{
			updatetf.setText(newValue);
			
		});
		addtf.setOnAction(e ->{
			addbt.fire();
		});
		
		updatetf.setOnAction(e ->{
			updatebt.fire();
		});
		
		addbt.setOnAction(e ->{
			String todo = addtf.getText().trim();
			if(!todo.isEmpty() && !ov.contains(todo)) {
				ov.add(todo);
				addtf.clear();
				
				new TodoListDb(conn).insertData(todo);
			}
		});
		
		updatebt.setOnAction(e ->{
			
			String selected = lv.getSelectionModel().getSelectedItem();
			int index = lv.getSelectionModel().getSelectedIndex();
			String updated = updatetf.getText().trim();
			
			if(selected != null && !updated.isEmpty()) {
				ov.set(index, updated);
				new TodoListDb(conn).updateData(selected, updated);
				
			}
		});
		
		removebt.setOnAction(e ->{
			String selected = lv.getSelectionModel().getSelectedItem();
			if(selected != null) {
				ov.remove(selected);
				
				new TodoListDb(conn).deleteData(selected);
			}
		});
		
		HBox hBox = new HBox(addbt,updatebt,removebt);
		hBox.setSpacing(2);
		
		VBox vBox = new VBox(addtf,hBox,updatetf,lv);
		Scene scene = new Scene(vBox,500,500);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		vBox.setSpacing(2);
		vBox.setPadding(new Insets(2));
		
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setTitle("ToDo List");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public Connection dbConnector() {
	    try {
	        if (conn == null || conn.isClosed()) { // Check if connection is null or closed
	            Class.forName("org.sqlite.JDBC");
	            conn = DriverManager.getConnection("jdbc:sqlite:sql/TodoList.sqlite");
	            System.out.println("Db Connected");
	        }
	        return conn;
	    } catch (ClassNotFoundException e) {
	        System.err.println("SQLite JDBC driver not found.");
	    } catch (SQLException e) {
	        System.err.println("Failed to connect to the database: " + e.getMessage());
	    }
	    return null;
	}
	

}


