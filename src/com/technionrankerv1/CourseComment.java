package com.technionrankerv1;

import java.sql.Time;

/*
 * Course_Comment(
	ID:integer,
	Course_Number:integer,
	Student_ID:integer,
	Comment:string,
    DateTime:datetime
    Likes:integer
)
 */
public class CourseComment {
	private int id;
	private String courseNumber;
	private int studentID;
	private String comment;
	private Time datetime;
	private int likes;
	
	public CourseComment(int id, String courseNumber, int studentID,
			String comment, Time datetime, int likes) {
		this.id = id;
		this.courseNumber = courseNumber;
		this.studentID = studentID;
		this.comment = comment;
		this.datetime = datetime;
		this.likes = likes;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Time getDatetime() {
		return datetime;
	}

	public void setDatetime(Time datetime) {
		this.datetime = datetime;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}
}
