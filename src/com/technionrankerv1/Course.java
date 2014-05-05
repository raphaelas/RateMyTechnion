package com.technionrankerv1;

public class Course {
  private Long id;
  private String name;
  private String number;
  private Long professorID;
  private String semester;
  private boolean active;

  public Course(String na, String nu, Long pID, String se, boolean a) {
    name = na;
    number = nu;
    professorID = pID;
    semester = se;
    active = a;
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
}
