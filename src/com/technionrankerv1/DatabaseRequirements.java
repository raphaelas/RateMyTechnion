package com.technionrankerv1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

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
	public boolean insertCourse(Course c) {
		//Query to determine if course already exists in DB.
		open();
		String SQL_QUERY = "INSERT INTO courses (name, number, professor_id, semester, active)" +
	                   "VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, c.getName());
			pst.setString(2, c.getNumber());
			pst.setInt(3, c.getProfessorID());
			pst.setString(4, c.getSemester());
			pst.setBoolean(5, c.isActive());
			return (pst.executeUpdate()>0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	 /* Courses(
				ID:integer, (auto-generated)
			    Name:string,
			    Number:string, (because courses may begin with 0 digit and 0s must be retained)
			    Professor_ID:integer,
			    Semester:string,
			    Active:boolean
			)*/
	public Course getCourse(String courseNumber) {
		open();
		String SQL_QUERY= "Select * from courses where number="+courseNumber;
		Statement stmt;
		Course c = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				c = new Course(rs.getInt("_id"), rs.getString("name"), courseNumber,
						rs.getInt("professor_id"), rs.getString("semester"),
						rs.getBoolean("active"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return c;
	}

	/*
	 * Professors(
    ID:integer,
    Name:string,
    Active:boolean
    )
	 */
	public boolean insertProfessor(Professor p) {
		//Query to determine if course already exists in DB.
		open();
		String SQL_QUERY = "INSERT INTO professors (name, active)" +
	                   "VALUES (?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, p.getName());
			pst.setBoolean(2, p.isActive());
			return (pst.executeUpdate()>0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	 /* Professors(
			    ID:integer,
			    Name:string,
			    Active:boolean
			    )
				 */
	public Professor getProfessor(int professorID) {
		open();
		String SQL_QUERY= "Select * from professors where _id="+professorID;
		Statement stmt;
		Professor p = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				p = new Professor(rs.getInt("_id"), rs.getString("name"), 
						rs.getBoolean("active"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return p;
	}
	
	
	/*
	 * Users(
	    Student_ID:integer,
	    Student_Password_Hash:string,
	    Name:string,
	    Active:boolean
	)
	 */
	public boolean insertUser(StudentUser u) {
		//Query to determine if course already exists in DB.
		open();
		String SQL_QUERY = "INSERT INTO users (student_id, password_hash, name, active)" +
	                   "VALUES (?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setInt(1, u.getStudentID());
			pst.setString(2, u.getPasswordHash());
			pst.setString(3, u.getName());
			pst.setBoolean(4, u.isActive());
			return (pst.executeUpdate()>0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * Users(
	    Student_ID:integer,
	    Student_Password_Hash:string,
	    Name:string,
	    Active:boolean
	)
	 */
	public StudentUser getUser(int userID) {
		open();
		String SQL_QUERY= "Select * from users where _id="+userID;
		Statement stmt;
		StudentUser u = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				u = new StudentUser(rs.getInt("_id"), rs.getInt("student_id"), rs.getString("password_hash"),
						rs.getString("name"), rs.getBoolean("active"));
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return u;
	}
	
	/*
	 * Users_Professors_And_Courses(
	ID:integer,
    Student_ID:integer,
    Professor_ID:integer,
    Course_ID:integer
)
	 */
	public boolean insertUserProfessorCourse(StudentProfessorCourse upc) {
		//Query to determine if course already exists in DB.
		open();
		String SQL_QUERY = "INSERT INTO users_professors_courses (student_id, professor_id, course_number)" +
	                   "VALUES (?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setInt(1, upc.getStudentID());
			pst.setInt(2, upc.getProfessorID());
			pst.setString(3, upc.getCourseNumber());
			return (pst.executeUpdate()>0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * Users_Professors_And_Courses(
	ID:integer,
    Student_ID:integer,
    Professor_ID:integer,
    Course_ID:integer
)
	 */
	//Note to self: We never should have to use this method - just adding in case.
	public StudentProfessorCourse getUserProfessorCourse(int studentID) {
		open();
		String SQL_QUERY= "Select * from users_professors_courses where student_id="+studentID;
		Statement stmt;
		StudentProfessorCourse upc = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				upc = new StudentProfessorCourse(rs.getInt("_id"), rs.getInt("student_id"),
						rs.getInt("professor_id"), rs.getString("course_number"));
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return upc;
	}
	
	/*
	 * Users_Professors_And_Courses(
	ID:integer,
    Student_ID:integer,
    Professor_ID:integer,
    Course_ID:integer
)
	 */
	public HashSet<Professor> getStudentProfessors(int studentID) {
		open();
		String SQL_QUERY= "Select * from users_professors_courses where student_id="+studentID;
		Statement stmt;
		StudentProfessorCourse upc = null;
		HashSet<Professor> pSet = new HashSet<Professor>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			while(rs.next()) {
				upc = new StudentProfessorCourse(rs.getInt("_id"), rs.getInt("student_id"),
						rs.getInt("professor_id"), rs.getString("course_id"));
				Professor tempP = getProfessor(upc.getProfessorID());
				pSet.add(tempP);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return pSet;
	}
	
	/*
	 * Users_Professors_And_Courses(
	ID:integer,
    Student_ID:integer,
    Professor_ID:integer,
    Course_ID:integer
)
	 */
	public HashSet<Course> getStudentCourses(int studentID) {
		open();
		String SQL_QUERY= "Select * from users_professors_courses where student_id="+studentID;
		Statement stmt;
		StudentProfessorCourse upc = null;
		HashSet<Course> cSet = new HashSet<Course>();
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			while(rs.next()) {
				upc = new StudentProfessorCourse(rs.getInt("_id"), rs.getInt("student_id"),
						rs.getInt("professor_id"), rs.getString("course_id"));
				Course tempC = getCourse(upc.getCourseNumber());
				cSet.add(tempC);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return cSet;
	}
	
	/*
	 * Professor_Rating(
	    ID:integer,
	    Professor_ID:integer,
	    Overall_Rating:integer,
	    Clarity:integer,
	    Preparedness:integer,
	    Interactivity:integer,
	)
	 */
	public boolean insertProfessorRating(ProfessorRating pr) {
		open();
		String SQL_QUERY = "INSERT INTO professor_ratings (professor_id, student_id, "
				+ "overall_rating, clarity, preparedness, interactivity)" +
	                   "VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setInt(1, pr.getProfessorID());
			pst.setInt(1, pr.getStudentID());
			pst.setInt(3, pr.getOverallRating());
			pst.setInt(4, pr.getClarity());
			pst.setInt(5, pr.getPreparedness());
			pst.setInt(6, pr.getInteractivity());
			return (pst.executeUpdate()>0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * Professor_Rating(
	    ID:integer,
	    Professor_ID:integer,
	    Overall_Rating:integer,
	    Clarity:integer,
	    Preparedness:integer,
	    Interactivity:integer,
	)
	 */
	public HashSet<ProfessorRating> getProfessorRatingsByProfessor(int professorID) {
		open();
		String SQL_QUERY= "Select * from professor_ratings where professor_id="+professorID;
		Statement stmt;
		HashSet<ProfessorRating> prSet = new HashSet<ProfessorRating>();
		ProfessorRating pr = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				pr = new ProfessorRating(rs.getInt("_id"), rs.getInt("student_id"),
						rs.getInt("professor_id"), rs.getInt("overall_rating"),
						rs.getInt("clarity"), rs.getInt("preparedness"),
						rs.getInt("interactivity"));
				prSet.add(pr);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return prSet;
	}
	
	/*
	 * Professor_Rating(
	    ID:integer,
	    Professor_ID:integer,
	    Overall_Rating:integer,
	    Clarity:integer,
	    Preparedness:integer,
	    Interactivity:integer,
	)
	 */
	public HashSet<ProfessorRating> getProfessorRatingsByStudent(int studentID) {
		open();
		String SQL_QUERY= "Select * from professor_ratings where student_id="+studentID;
		Statement stmt;
		HashSet<ProfessorRating> prSet = new HashSet<ProfessorRating>();
		ProfessorRating pr = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				pr = new ProfessorRating(rs.getInt("_id"), rs.getInt("student_id"),
						rs.getInt("professor_id"), rs.getInt("overall_rating"),
						rs.getInt("clarity"), rs.getInt("preparedness"),
						rs.getInt("interactivity"));
				prSet.add(pr);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return prSet;
	}
	
	/*
	 * Course_Rating(
    ID:integer,
    Course_Number:string,
    Overall_Rating:integer,
    Enjoyability:integer,
    Difficulty:integer,
    Usefulness:integer,
)
	 */
	public boolean insertCourseRating(CourseRating cr) {
		open();
		String SQL_QUERY = "INSERT INTO course_ratings (course_number, student_id,"
				+ " overall_rating, enjoyability, difficulty, usefulness)" +
	                   "VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, cr.getCourseNumber());
			pst.setInt(2, cr.getStudentID());
			pst.setInt(3, cr.getOverallRating());
			pst.setInt(4, cr.getEnjoyability());
			pst.setInt(5, cr.getDifficulty());
			pst.setInt(6, cr.getUsefulness());
			return (pst.executeUpdate()>0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * Course_Rating(
    ID:integer,
    Course_Number:string,
    Overall_Rating:integer,
    Enjoyability:integer,
    Difficulty:integer,
    Usefulness:integer,
)
	 */
	public HashSet<CourseRating> getCourseRatingsByCourse(String courseNumber) {
		open();
		String SQL_QUERY= "Select * from course_ratings where course_number="+courseNumber;
		Statement stmt;
		HashSet<CourseRating> crSet = new HashSet<CourseRating>();
		CourseRating cr = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				cr = new CourseRating(rs.getInt("_id"), rs.getInt("student_id"),
						rs.getString("course_number"), rs.getInt("overall_rating"),
						rs.getInt("clarity"), rs.getInt("preparedness"),
						rs.getInt("interactivity"));
				crSet.add(cr);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return crSet;
	}
	
	/*
	 * Course_Rating(
    ID:integer,
    Course_Number:string,
    Overall_Rating:integer,
    Enjoyability:integer,
    Difficulty:integer,
    Usefulness:integer,
)
	 */
	public HashSet<CourseRating> getCourseRatingsByStudent(int studentID) {
		open();
		String SQL_QUERY= "Select * from course_ratings where student_id="+studentID;
		Statement stmt;
		HashSet<CourseRating> crSet = new HashSet<CourseRating>();
		CourseRating cr = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				cr = new CourseRating(rs.getInt("_id"), rs.getInt("student_id"),
						rs.getString("course_number"), rs.getInt("overall_rating"),
						rs.getInt("clarity"), rs.getInt("preparedness"),
						rs.getInt("interactivity"));
				crSet.add(cr);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return crSet;
	}
	
	/*
	 * Professor_Comment(
	ID:integer,
	Professor_ID:integer,
	Student_ID,
	Comment:string,
    DateTime:time,
    Likes:integer
)
	 */
	public boolean insertProfessorComment(ProfessorComment pc) {
		open();
		String SQL_QUERY = "INSERT INTO professor_comments (professor_id, student_id, "
				+ "comment, datetime, likes)" +
	                   "VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setInt(1, pc.getProfessorID());
			pst.setInt(2, pc.getStudentID());
			pst.setString(3, pc.getComment());
			pst.setTime(4, pc.getDatetime());
			pst.setInt(5, pc.getLikes());
			return (pst.executeUpdate()>0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * Professor_Comment(
	ID:integer,
	Professor_ID:integer,
	Student_ID,
	Comment:string,
    DateTime:time,
    Likes:integer
)
	 */
	public HashSet<ProfessorComment> getProfessorCommentsByStudent(int studentID) {
		open();
		String SQL_QUERY= "Select * from professor_comments where student_id="+studentID;
		Statement stmt;
		HashSet<ProfessorComment> pcSet = new HashSet<ProfessorComment>();
		ProfessorComment pc = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				pc = new ProfessorComment(rs.getInt("_id"), rs.getInt("professor_id"),
						rs.getInt("student_id"), rs.getString("comment"),
						rs.getTime("datetime"), rs.getInt("likes"));
				pcSet.add(pc);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return pcSet;
	}
	
	/*
	 * Professor_Comment(
	ID:integer,
	Professor_ID:integer,
	Student_ID,
	Comment:string,
    DateTime:time,
    Likes:integer
)
	 */
	public HashSet<ProfessorComment> getProfessorCommentsByProfessor(int professorID) {
		open();
		String SQL_QUERY= "Select * from professor_comments where professor_id="+professorID;
		Statement stmt;
		HashSet<ProfessorComment> pcSet = new HashSet<ProfessorComment>();
		ProfessorComment pc = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				pc = new ProfessorComment(rs.getInt("_id"), rs.getInt("professor_id"),
						rs.getInt("student_id"), rs.getString("comment"),
						rs.getTime("datetime"), rs.getInt("likes"));
				pcSet.add(pc);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return pcSet;
	}
	
	/*
	 * Course_Comment(
	ID:integer,
	Student_ID,
	Course_Number:integer,
	Comment:string,
    DateTime:datetime
    Likes:integer
)
	 */
	public boolean insertCourseComment(CourseComment cc) {
		open();
		String SQL_QUERY = "INSERT INTO course_comments (course_number, student_id, "
				+ "comment, datetime, likes)" +
	                   "VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pst;
		try {
			pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, cc.getCourseNumber());
			pst.setInt(2, cc.getStudentID());
			pst.setString(3, cc.getComment());
			pst.setTime(4, cc.getDatetime());
			pst.setInt(5, cc.getLikes());
			return (pst.executeUpdate()>0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * Course_Comment(
	ID:integer,
	Student_ID,
	Course_Number:integer,
	Comment:string,
    DateTime:datetime
    Likes:integer
)
	 */
	public HashSet<CourseComment> getCourseCommentsByStudent(int studentID) {
		open();
		String SQL_QUERY= "Select * from course_comments where student_id="+studentID;
		Statement stmt;
		HashSet<CourseComment> ccSet = new HashSet<CourseComment>();
		CourseComment cc = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				cc = new CourseComment(rs.getInt("_id"), rs.getString("course_number"),
						rs.getInt("student_id"), rs.getString("comment"),
						rs.getTime("datetime"), rs.getInt("likes"));
				ccSet.add(cc);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return ccSet;
	}
	
	/*
	 * Course_Comment(
	ID:integer,
	Student_ID,
	Course_Number:integer,
	Comment:string,
    DateTime:datetime
    Likes:integer
)
	 */
	public HashSet<CourseComment> getCourseCommentsByCourse(String courseNumber) {
		open();
		String SQL_QUERY= "Select * from course_comments where course_number="+courseNumber;
		Statement stmt;
		HashSet<CourseComment> ccSet = new HashSet<CourseComment>();
		CourseComment cc = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			if(rs.next()) {
				cc = new CourseComment(rs.getInt("_id"), rs.getString("course_number"),
						rs.getInt("student_id"), rs.getString("comment"),
						rs.getTime("datetime"), rs.getInt("likes"));
				ccSet.add(cc);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	return ccSet;
	}
}
