package com.technionrankerv1;

import java.util.HashMap;
import java.util.HashSet;

import android.app.Application;

public class ApplicationWithGlobalVariables extends Application {
	
    private int ratingsSubmitted = 0;
    private boolean loggedIn = false;
    private Long studentID = Long.valueOf((long) (Math.random() * 100000000));
    private HashSet<Long> courseCommentsLiked = new HashSet<Long>();
    private HashSet<Long> professorCommentsLiked = new HashSet<Long>();
    private String studentName = "";
    private int ratingsThreshold = 0;
    public HashMap<String, Integer> studentsToRatingsSubmitted = new HashMap<String, Integer>();
    
	@Override
    public void onCreate() {
    	studentsToRatingsSubmitted.put(studentName, 0);
    }
	
    

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
		return ratingsSubmitted < ratingsThreshold;
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
	public void resetStudentID() {
		this.studentID = Long.valueOf((long) (Math.random() * 100000000));
	}

	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(Long studentId) {
		this.studentID = studentId;
	}
	
	/**
	 * @return the courseCommentsLiked
	 */
	public HashSet<Long> getCourseCommentsLiked() {
		return courseCommentsLiked;
	}

	/**
	 * @param courseCommentsLiked the courseCommentsLiked to set
	 */
	public void setCourseCommentsLiked(HashSet<Long> courseCommentsLiked) {
		this.courseCommentsLiked = courseCommentsLiked;
	}
	
	public boolean isCourseCommentLiked(Long cc) {
		return courseCommentsLiked.contains(cc);
	}
	
	public void likeCourseComment(Long cc) {
		courseCommentsLiked.add(cc);
	}

	/**
	 * @return the professorCommentsLiked
	 */
	public HashSet<Long> getProfessorCommentsLiked() {
		return professorCommentsLiked;
	}

	/**
	 * @param professorCommentsLiked the professorCommentsLiked to set
	 */
	public void setProfessorCommentsLiked(HashSet<Long> professorCommentsLiked) {
		this.professorCommentsLiked = professorCommentsLiked;
	}
	
	public boolean isProfessorCommentLiked(Long pc) {
		return professorCommentsLiked.contains(pc);
	}
	
	public void likeProfessorComment(Long pc) {
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

	/**
	 * @return the ratingsThreshold
	 */
	public int getRatingsThreshold() {
		return ratingsThreshold;
	}

	/**
	 * @param ratingsThreshold the ratingsThreshold to set
	 */
	public void setRatingsThreshold(int ratingsThreshold) {
		this.ratingsThreshold = ratingsThreshold;
	}

}
