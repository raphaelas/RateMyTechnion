package com.technionrankerv1;

import java.util.HashSet;

import android.app.Application;

public class ApplicationWithGlobalVariables extends Application {
	
    private int ratingsSubmitted = 0;
    private boolean loggedIn = false;
    private Long studentID = Long.valueOf((long) (Math.random() * 100000000));
    private HashSet<CourseComment> courseCommentsLiked = new HashSet<CourseComment>();
    private HashSet<ProfessorComment> professorCommentsLiked = new HashSet<ProfessorComment>();
    private String studentName = null;

    

	/**
	 * @return the ratingsSubmitted
	 */
	public int getRatingsSubmitted() {
		return ratingsSubmitted;
	}

	/**
	 * @param ratingsSubmitted the ratingsSubmitted to set
	 */
	public void setRatingsSubmitted(int ratingsSubmitted) {
		this.ratingsSubmitted = ratingsSubmitted;
	}
	
	public void incrementRatingsSubmitted() {
		this.ratingsSubmitted++;
	}
	
	/**
	 * 
	 * @return Whether the user submitted fewer than the ratings threshold.
	 */
	public boolean canSubmitRatings() {
		int RATINGS_THRESHOLD = 10;
		return ratingsSubmitted < RATINGS_THRESHOLD;
	}

	/**
	 * @return the loggedIn
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @param loggedIn the loggedIn to set
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * @return the studentID
	 */
	public Long getStudentID() {
		return studentID;
	}

	/**
	 * @param studentID the studentID to set
	 */
	public void setStudentID(Long studentID) {
		this.studentID = studentID;
	}

	/**
	 * @return the courseCommentsLiked
	 */
	public HashSet<CourseComment> getCourseCommentsLiked() {
		return courseCommentsLiked;
	}

	/**
	 * @param courseCommentsLiked the courseCommentsLiked to set
	 */
	public void setCourseCommentsLiked(HashSet<CourseComment> courseCommentsLiked) {
		this.courseCommentsLiked = courseCommentsLiked;
	}
	
	public boolean isCourseCommentLiked(CourseComment cc) {
		return courseCommentsLiked.contains(cc);
	}
	
	public void likeCourseComment(CourseComment cc) {
		courseCommentsLiked.add(cc);
	}

	/**
	 * @return the professorCommentsLiked
	 */
	public HashSet<ProfessorComment> getProfessorCommentsLiked() {
		return professorCommentsLiked;
	}

	/**
	 * @param professorCommentsLiked the professorCommentsLiked to set
	 */
	public void setProfessorCommentsLiked(HashSet<ProfessorComment> professorCommentsLiked) {
		this.professorCommentsLiked = professorCommentsLiked;
	}
	
	public boolean isProfessorCommentLiked(ProfessorComment pc) {
		return professorCommentsLiked.contains(pc);
	}
	
	public void likeProfessorComment(ProfessorComment pc) {
		professorCommentsLiked.add(pc);
	}

	/**
	 * @return the studentName
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * @param studentName the studentName to set
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

}
