package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TodoListDb {
	private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;
    
    public TodoListDb(Connection conn) {
    	this.conn = conn;
    }


    public ObservableList<String> loadData() {
        ObservableList<String> tmpOv = FXCollections.observableArrayList();
        String query = "SELECT * FROM Todo";
        try {
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                String todo = rs.getString("Text");
                tmpOv.add(todo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tmpOv;
    }

    public void insertData(String data) {
        String query = "INSERT INTO Todo (Text) VALUES (?)";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, data);
            pst.execute();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        
        }
    }

    public void deleteData(String data) {
        String query = "DELETE FROM Todo WHERE Text = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, data);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateData(String oldValue, String newValue) {
        String query = "UPDATE Todo SET Text = ? WHERE Text = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, newValue);
            pst.setString(2, oldValue);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}