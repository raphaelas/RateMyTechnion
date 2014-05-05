package com.technionrankerv1;

public class CourseRating {
  private Long id;
  private Long studentID;
  private String courseID;
  private int overallRating;
  private int enjoyability;
  private int difficulty;
  private int usefulness;

  public CourseRating(Long studentID1, String courseID1, int overallRating1,
      int enjoyability1, int difficulty1, int usefulness1) {
    studentID = studentID1;
    courseID = courseID1;
    overallRating = overallRating1;
    enjoyability = enjoyability1;
    difficulty = difficulty1;
    usefulness = usefulness1;
  }

  CourseRating() {
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
   * @return the courseNumber
   */
  public String getCourseId() {
    return courseID;
  }

  /**
   * @param courseNumber1
   *          the courseNumber to set
   */
  public void setCourseId(String courseNumber1) {
    courseID = courseNumber1;
  }

  /**
   * @return the overallRating
   */
  public int getOverallRating() {
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
   * @return the enjoyability
   */
  public int getEnjoyability() {
    return enjoyability;
  }

  /**
   * @param enjoyability1
   *          the enjoyability to set
   */
  public void setEnjoyability(int enjoyability1) {
    enjoyability = enjoyability1;
  }

  /**
   * @return the difficulty
   */
  public int getDifficulty() {
    return difficulty;
  }

  /**
   * @param difficulty1
   *          the difficulty to set
   */
  public void setDifficulty(int difficulty1) {
    difficulty = difficulty1;
  }

  /**
   * @return the usefulness
   */
  public int getUsefulness() {
    return usefulness;
  }

  /**
   * @param usefulness1
   *          the usefulness to set
   */
  public void setUsefulness(int usefulness1) {
    usefulness = usefulness1;
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