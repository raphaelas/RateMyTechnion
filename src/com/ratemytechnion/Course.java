package com.ratemytechnion;


public class Course {
  private Long id;
  private String name;
  private String number;
  private Long professorID;
  private String semester;
  private boolean active;
  private String faculty;

  public Course(Long _id, String na, String nu, Long pID, String se,
      String _faculty, boolean a) {
    id = _id;
    name = na;
    number = nu;
    professorID = pID;
    semester = se;
    faculty = _faculty;
    active = a;
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

  Course() {
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
   * @return the number
   */
  public String getNumber() {
    return number;
  }

  /**
   * @param number1
   *          the number to set
   */
  public void setNumber(String number1) {
    number = number1;
  }

  /**
   * @return the professorID
   */
  public Long getProfessorID() {
    return professorID;
  }

  /**
   * @param professorID
   *          the professorID to set
   */
  public void setProfessorID(Long professor_id) {
    professorID = professor_id;
  }

  /**
   * @return the semester
   */
  public String getSemester() {
    return semester;
  }

  /**
   * @param semester1
   *          the semester to set
   */
  public void setSemester(String semester1) {
    semester = semester1;
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
  
  @Override
  public String toString() {
	  String toReturn = "" + name + " " + number + " " + professorID.toString() + " " + faculty;
	  return toReturn;
  }

}
