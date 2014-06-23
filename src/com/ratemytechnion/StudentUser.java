package com.ratemytechnion;


public class StudentUser {
  private Long id;
  private String studentID;
  private String passwordHash; 
  private String name;
  private boolean active;

  public StudentUser(Long _id, String sID, String n, String _passwordHash,
      boolean a) {
    id = _id;
    studentID = sID;
    name = n;
    active = a;
    passwordHash = _passwordHash;
  }

  StudentUser() {
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the studentID
   */
  public String getStudentID() {
    return studentID;
  }

  /**
   * @param studentID
   *          the studentID to set
   */
  public void setStudentID(String student_id) {
    studentID = student_id;
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
   * @return the passwordHash
   */
  public String getPasswordHash() {
    return passwordHash;
  }

  /**
   * @param passwordHash
   *          the passwordHash to set
   */
  public void setPasswordHash(String password_hash) {
    passwordHash = password_hash;
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
}