package com.technionrankerv1;


public class Professor {
  private Long id;
  private String name;
  private boolean active;
  private String faculty;
  private String hebrewName;

  public Professor(Long _id, String n, String f, String _hebrewName, boolean a) {
    id = _id;
    name = n;
    faculty = f;
    hebrewName = _hebrewName;
    active = a;
  }

  /**
   * @return the hebrewName
   */
  public String getHebrewName() {
    return hebrewName;
  }

  /**
   * @param hebrewName1
   *          the hebrewName to set
   */
  public void setHebrewName(String hebrewName1) {
    hebrewName = hebrewName1;
  }

  /**
   * @return the faculty
   */
  public String getFaculty() {
    return faculty;
  }

  /**
   * @param faculty1
   *          the faculty to set
   */
  public void setFaculty(String faculty1) {
    faculty = faculty1;
  }

  Professor() {
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name1
   *          the name to set
   */
  public void setName(String name1) {
    name = name1;
  }

  /**
   * @return the active
   */
  public boolean isActive() {
    return active;
  }

  /**
   * @param active1
   *          the active to set
   */
  public void setActive(boolean active1) {
    active = active1;
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

}