package com.technionrankerv1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseRequirements {	
	private Connection con;
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "technionrankerdb";
	private String driver = "com.mysql.jdbc.Driver";
	private String userName = "root";
	private String password = "";
	private boolean isOpen = false;
    
	public void open()
	{
		if (isOpen) return;
		
        try {
	        Class.forName(driver).newInstance();
	        con = DriverManager.getConnection(url+dbName,userName,password);
	        isOpen = true; 
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	public void close()
	{
		isOpen = false;
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
 * Courses(
	ID:integer, (auto-generated)
    Name:string,
    Number:string, (because courses may begin with 0 digit and 0s must be retained)
    Professor_ID:integer,
    Semester:string,
    Active:boolean
)*/
	public boolean insertCourse(String name, String number, int professor_id, String semester, boolean active) {
		//Query to determine if course already exists in DB.
		String SQL_QUERY= "Select number from course where number=" + number;
		open();
		SQL_QUERY = "INSERT INTO course (name, number, professor_id, semester, active)" +
	                   "VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, name);
			pst.setString(2, number);
			pst.setInt(3, professor_id);
			pst.setString(4, semester);
			pst.setBoolean(5, active);
			return (pst.executeUpdate()>0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	
	public Node getNodeData(String nodeID) {
		open();
		String SQL_QUERY= "Select * from Node where node_id='"+nodeID+"'";
		Statement stmt;
		Node n = null;
		
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			
			
			
			if(rs.next()) {
				n = new Node(nodeID, rs.getString(2), rs.getString(3), rs.getString(4));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		return n;
	}

	public Collection<NodeTag> getAllNodeTags(String nodeID) {
		String SQL_QUERY= "Select * from nodetag where node_id='"+nodeID+"'";
		Statement stmt;
		
		ArrayList<NodeTag> tags = new ArrayList<NodeTag>();
		open();
		
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
						
			while(rs.next()) {
				NodeTag tag = new NodeTag(rs.getString(3), rs.getString(4));
				tags.add(tag);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tags;
	}

	

	
	public boolean saveNodeTag(String nodeID, String tag) {
		open();
		
		try {
			String SQL_QUERY = "INSERT INTO nodetag (node_id, key, value)" +
	                   "VALUES (?, ?, ?)";
			PreparedStatement pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, nodeID);
			//pst.setString(2, tag.getKey());
			//pst.setString(3, tag.getValue());
			return (pst.executeUpdate()>0);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}*/
}
