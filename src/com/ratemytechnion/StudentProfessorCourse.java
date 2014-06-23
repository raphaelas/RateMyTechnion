package com.ratemytechnion;

public class StudentProfessorCourse {
  private Long id;
  private Long studentID;
  private Long professorID;
  private Long courseID;

  public StudentProfessorCourse(Long _id, Long sID, Long pID, Long cID) {
    id = _id;
    studentID = sID;
    professorID = pID;
    courseID = cID;
  }

  StudentProfessorCourse() {
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
   * @return the courseNumber
   */
  public Long getCourseId() {
    return courseID;
  }

  /**
   * @param courseNumber
   *          the courseNumber to set
   */
  public void setCourseId(Long _courseID) {
    courseID = _courseID;
  }

}