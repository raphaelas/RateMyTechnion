package com.technionrankerv1;

/*
* Courses(
ID:integer, (auto-generated)
Name:string,
Number:string, (because courses may begin with 0 digit and 0s must be retained)
Professor_ID:integer,
Semester:string,
Active:boolean
)*/
public class OldCourse {
	private int id;
	private String name;
	private String number;
	private int professorID;
	private String semester;
	private boolean active;
	
	public OldCourse(int i, String na, String nu, int pID, String se, boolean a) {
		id = i;
		name = na;
		number = nu;
		professorID = pID;
		semester = se;
		active = a;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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
	public void setProfessorID(int professor_id) {
		this.professorID = professor_id;
	}

	/**
	 * @return the semester
	 */
	public String getSemester() {
		return semester;
	}

	/**
	 * @param semester the semester to set
	 */
	public void setSemester(String semester) {
		this.semester = semester;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
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
	
}
