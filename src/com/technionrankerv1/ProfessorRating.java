package com.technionrankerv1;


public class ProfessorRating {
  private Long id;
  private Long studentID;
  private Long professorID;
  private double overallRating;
  private double clarity;
  private double preparedness;
  private double interactivity;

  public ProfessorRating(Long _id, Long studentID1, Long professorID1,
      int overallRating1, int clarity1, int preparedness1, int interactivity1) {
    id = _id;
    studentID = studentID1;
    professorID = professorID1;
    overallRating = overallRating1;
    clarity = clarity1;
    preparedness = preparedness1;
    interactivity = interactivity1;
  }

  ProfessorRating() {
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the professorID
   */
  public Long getProfessorID() {
    return professorID;
  }

  /**
   * @param professorID1
   *          the professorID to set
   */
  public void setProfessorID(Long professorID1) {
    professorID = professorID1;
  }

  /**
   * @return the overallRating
   */
  public double getOverallRating() {
    return overallRating;
  }

  /**
   * @param overallRating1
   *          the overallRating to set
   */
  public void setOverallRating(int overallRating1) {
    overallRating = overallRating1;
  }

  /**
   * @return the clarity
   */
  public double getClarity() {
    return clarity;
  }

  /**
   * @param clarity1
   *          the clarity to set
   */
  public void setClarity(int clarity1) {
    clarity = clarity1;
  }

  /**
   * @return the preparedness
   */
  public double getPreparedness() {
    return preparedness;
  }

  /**
   * @param preparedness1
   *          the preparedness to set
   */
  public void setPreparedness(int preparedness1) {
    preparedness = preparedness1;
  }

  /**
   * @return the interactivity
   */
  public double getInteractivity() {
    return interactivity;
  }

  /**
   * @param interactivity1
   *          the interactivity to set
   */
  public void setInteractivity(int interactivity1) {
    interactivity = interactivity1;
  }

  /**
   * @return the studentID
   */
  public Long getStudentID() {
    return studentID;
  }

  /**
   * @param studentID1
   *          the studentID to set
   */
  public void setStudentID(Long studentID1) {
    studentID = studentID1;
  }

}