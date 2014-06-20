package com.technionrankerv1;

import java.util.HashMap;
import java.util.HashSet;

import android.app.Application;

public class ApplicationWithGlobalVariables extends Application {
	
    private int ratingsSubmitted = 0;
    private boolean loggedIn = false;
    private Long studentID;
    private HashMap<String, HashSet<String>> courseCommentsLiked = new HashMap<String, HashSet<String>>();
    private HashMap<String, HashSet<String>> professorCommentsLiked = new HashMap<String, HashSet<String>>();
    private String studentName = "";
    private int ratingsThreshold = 0;
    public HashMap<String, Integer> studentsToRatingsSubmitted = new HashMap<String, Integer>();
    public String[] professorsAndCourses = null;
    public String[] courseList = new String[0];
	public HashMap<String, String> facultyMap = new HashMap<String, String>();

    
	@Override
    public void onCreate() {
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
		this.studentID = Long.valueOf(studentName.hashCode());
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
	public HashSet<String> getCourseCommentsLiked() {
		return courseCommentsLiked.get(studentName);
	}
	
	public void resetCourseCommentsLiked() {
		this.courseCommentsLiked.put(studentName, new HashSet<String>());
	}
	
	public boolean isCourseCommentLiked(String cc) {
		return courseCommentsLiked.get(studentName).contains(cc);
	}
	
	public void likeCourseComment(String cc) {
		courseCommentsLiked.get(studentName).add(cc);
	}

	/**
	 * @return the professorCommentsLiked
	 */
	public HashSet<String> getProfessorCommentsLiked() {
		return professorCommentsLiked.get(studentName);
	}
	
	public void resetProfessorCommentsLiked() {
		this.professorCommentsLiked.put(studentName, new HashSet<String>());
	}
	
	public boolean isProfessorCommentLiked(String pc) {
		return professorCommentsLiked.get(studentName).contains(pc);
	}
	
	public void likeProfessorComment(String pc) {
		professorCommentsLiked.get(studentName).add(pc);
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



	public void decrementRatingsThreshold() {
		ratingsThreshold--;
	}

}
