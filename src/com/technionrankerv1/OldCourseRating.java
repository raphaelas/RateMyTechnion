package com.technionrankerv1;

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
public class OldCourseRating {
	private int id;
	private int studentID;
	private String courseNumber;
	private int overallRating;
	private int enjoyability;
	private int difficulty;
	private int usefulness;
	
	public OldCourseRating(int id, int studentID, String courseNumber, int overallRating,
			int enjoyability, int difficulty, int usefulness) {
		this.id = id;
		this.studentID = studentID;
		this.courseNumber = courseNumber;
		this.overallRating = overallRating;
		this.enjoyability = enjoyability;
		this.difficulty = difficulty;
		this.usefulness = usefulness;
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
	 * @return the courseNumber
	 */
	public String getCourseNumber() {
		return courseNumber;
	}

	/**
	 * @param courseNumber the courseNumber to set
	 */
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	/**
	 * @return the overallRating
	 */
	public int getOverallRating() {
		return overallRating;
	}

	/**
	 * @param overallRating the overallRating to set
	 */
	public void setOverallRating(int overallRating) {
		this.overallRating = overallRating;
	}

	/**
	 * @return the enjoyability
	 */
	public int getEnjoyability() {
		return enjoyability;
	}

	/**
	 * @param enjoyability the enjoyability to set
	 */
	public void setEnjoyability(int enjoyability) {
		this.enjoyability = enjoyability;
	}

	/**
	 * @return the difficulty
	 */
	public int getDifficulty() {
		return difficulty;
	}

	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * @return the usefulness
	 */
	public int getUsefulness() {
		return usefulness;
	}

	/**
	 * @param usefulness the usefulness to set
	 */
	public void setUsefulness(int usefulness) {
		this.usefulness = usefulness;
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
}
