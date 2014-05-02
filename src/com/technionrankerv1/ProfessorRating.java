package com.technionrankerv1;

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
public class ProfessorRating {
	private int id;
	private int studentID;
	private int professorID;
	private int overallRating;
	private int clarity;
	private int preparedness;
	private int interactivity;
	
	public ProfessorRating(int id, int studentID, int professorID, int overallRating,
			int clarity, int preparedness, int interactivity) {
		this.id = id;
		this.studentID = studentID;
		this.professorID = professorID;
		this.overallRating = overallRating;
		this.clarity = clarity;
		this.preparedness = preparedness;
		this.interactivity = interactivity;
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
	 * @return the clarity
	 */
	public int getClarity() {
		return clarity;
	}

	/**
	 * @param clarity the clarity to set
	 */
	public void setClarity(int clarity) {
		this.clarity = clarity;
	}

	/**
	 * @return the preparedness
	 */
	public int getPreparedness() {
		return preparedness;
	}

	/**
	 * @param preparedness the preparedness to set
	 */
	public void setPreparedness(int preparedness) {
		this.preparedness = preparedness;
	}

	/**
	 * @return the interactivity
	 */
	public int getInteractivity() {
		return interactivity;
	}

	/**
	 * @param interactivity the interactivity to set
	 */
	public void setInteractivity(int interactivity) {
		this.interactivity = interactivity;
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
