package com.technionrankerv1;

/*
 * Users_Professors_And_Courses(
	ID:integer,
    Student_ID:integer,
    Professor_ID:integer
)
 */
public class StudentProfessorCourse {
	private int id;
	private int studentID;
	private int professorID;
	private String courseNumber;
	
	public StudentProfessorCourse(int i, int sID, int pID, String cID) {
		id = i;
		studentID = sID;
		professorID = pID;
		courseNumber = cID;
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
	 * @return the courseNumber
	 */
	public String getCourseNumber() {
		return courseNumber;
	}

	/**
	 * @param courseNumber the courseNumber to set
	 */
	public void setCourseNumber(String courseID) {
		this.courseNumber = courseID;
	}

}
