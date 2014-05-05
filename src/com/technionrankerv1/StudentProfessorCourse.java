package com.technionrankerv1;

public class StudentProfessorCourse {
  private Long id;
  private Long studentID;
  private Long professorID;
  private String courseID;

  public StudentProfessorCourse(Long sID, Long pID, String cID) {
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
   * @param id1
   *          the id to set
   */
  public void setId(Long id1) {
    id = id1;
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
  public String getCourseId() {
    return courseID;
  }

  /**
   * @param courseNumber
   *          the courseNumber to set
   */
  public void setCourseId(String _courseID) {
    courseID = _courseID;
  }

}