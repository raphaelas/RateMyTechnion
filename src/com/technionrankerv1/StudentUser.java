package com.technionrankerv1;

/*
 * Users(
    Student_ID:integer,
    Student_Password_Hash:string,
    Name:string,
    Active:boolean
)
 */
public class StudentUser {
	private int id;
	private int studentID;
	private String passwordHash; //TODO
	private String name;
	private boolean active;
	
	public StudentUser(int i, int sID, String ph, String n, boolean a) {
		id = i;
		studentID = sID;
		ph = passwordHash;
		name = n;
		active = a;
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
	public void setStudentID(int student_id) {
		this.studentID = student_id;
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
	 * @return the passwordHash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String password_hash) {
		this.passwordHash = password_hash;
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
}
