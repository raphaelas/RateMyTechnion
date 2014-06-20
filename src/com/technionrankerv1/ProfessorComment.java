package com.technionrankerv1;

import java.sql.Time;
import java.util.HashSet;

public class ProfessorComment {
	private Long id;
	private Long professorID;
	private Long studentID;
	private String comment;
	private Time datetime;
	private int likes;
	private HashSet<String> studentsWhoLikedThisComment;

	public ProfessorComment(Long professorID1, Long studentID1, String comment1,
			Time datetime1, int likes1) {
		studentID = studentID1;
		professorID = professorID1;
		comment = comment1;
		datetime = datetime1;
		likes = likes1;
		studentsWhoLikedThisComment = new HashSet<String>();
	}

	ProfessorComment() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id1
	 *          the id to set
	 */
	public void setId(Long id1) {
		id = id1;
	}

	/**
	 * @return the studentID
	 */
	public Long getStudentID() {
		return studentID;
	}

	/**
	 * @param studentID1
	 *          the studentID to set
	 */
	public void setStudentID(Long studentID1) {
		studentID = studentID1;
	}

	/**
	 * @return the professorID
	 */
	public Long getProfessorID() {
		return professorID;
	}

	/**
	 * @param professorID1
	 *          the professorID to set
	 */
	public void setProfessorID(Long professorID1) {
		professorID = professorID1;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment1
	 *          the comment to set
	 */
	public void setComment(String comment1) {
		comment = comment1;
	}

	/**
	 * @return the datetime
	 */
	public Time getDatetime() {
		return datetime;
	}

	/**
	 * @param datetime1
	 *          the datetime to set
	 */
	public void setDatetime(Time datetime1) {
		datetime = datetime1;
	}

	/**
	 * @return the likes
	 */
	public int getLikes() {
		return likes;
	}

	/**
	 * @param likes1
	 *          the likes to set
	 */
	public void setLikes(int likes1) {
		likes = likes1;
	}

	public void incrementLikes() {
		likes++;
	}

	/**
	 * @return the studentsWhoLikedThisComment
	 */
	public HashSet<String> getStudentsWhoLikedThisComment() {
		 if (studentsWhoLikedThisComment == null) {
			 return new HashSet<String>();
		 }
		return studentsWhoLikedThisComment;
	}

	/**
	 * @param studentsWhoLikedThisComment the studentsWhoLikedThisComment to set
	 */
	public void addStudentsWhoLikedThisComment(
			String studentWhoLikedThisComment) {
		this.studentsWhoLikedThisComment.add(studentWhoLikedThisComment);
	}

	/**
	 * @param studentsWhoLikedThisComment the studentsWhoLikedThisComment to set
	 */
	public void setStudentsWhoLikedThisComment(
			HashSet<String> studentsWhoLikedThisComment) {
		this.studentsWhoLikedThisComment = studentsWhoLikedThisComment;
	}

}