package com.technionrankerv1;

/*
 * Professors(
ID:integer,
Name:string,
Active:boolean
)
 */
public class Professor {
	private int id;
	private String name;
	private boolean active;
	
	public Professor(int i, String n, boolean a) {
		name = n;
		active = a;
		id = i;
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
