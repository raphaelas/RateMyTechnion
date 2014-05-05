package com.technionrankerv1;

import java.sql.Time;

/*
 * Professor_Comment(
	ID:integer,
	Student_ID,
	Professor_ID:integer,
	Comment:string,
    DateTime:datetime,
    Likes:integer
)
 */
public class OldProfessorComment {
	private int id;
	private int professorID;
	private int studentID;
	private String comment;
	private Time datetime;
	private int likes;

	public OldProfessorComment(int id, int professorID, int studentID, 
			String comment, Time datetime, int likes) {
		this.id = id;
		this.studentID = studentID;
		this.professorID = professorID;
		this.comment = comment;
		this.datetime = datetime;
		this.likes = likes;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the studentID
	 */
	public int getStudentID() {
		return studentID;
	}

	/**
	 * @param studentID the studentID to set
	 */
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	/**
	 * @return the professorID
	 */
	public int getProfessorID() {
		return professorID;
	}

	/**
	 * @param professorID the professorID to set
	 */
	public void setProfessorID(int professorID) {
		this.professorID = professorID;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the datetime
	 */
	public Time getDatetime() {
		return datetime;
	}

	/**
	 * @param datetime the datetime to set
	 */
	public void setDatetime(Time datetime) {
		this.datetime = datetime;
	}

	/**
	 * @return the likes
	 */
	public int getLikes() {
		return likes;
	}

	/**
	 * @param likes the likes to set
	 */
	public void setLikes(int likes) {
		this.likes = likes;
	}
}
