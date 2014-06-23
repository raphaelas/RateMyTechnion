package com.ratemytechnion;


public class CourseRating {
  private Long id;
  private Long studentID;
  private Long courseID;
  private double overallRating;
  private double enjoyability;
  private double difficulty;
  private double usefulness;

  public CourseRating(Long studentID1, Long courseID1, double overallRating1,
      double enjoyability1, double difficulty1, double usefulness1) {
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
   * @return the courseID
   */
  public Long getCourseId() {
    return courseID;
  }

  /**
   * @param courseID1
   *          the courseID to set
   */
  public void setCourseId(Long courseID1) {
    courseID = courseID1;
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
  public void setOverallRating(double overallRating1) {
    overallRating = overallRating1;
  }

  /**
   * @return the enjoyability
   */
  public double getEnjoyability() {
    return enjoyability;
  }

  /**
   * @param enjoyability1
   *          the enjoyability to set
   */
  public void setEnjoyability(double enjoyability1) {
    enjoyability = enjoyability1;
  }

  /**
   * @return the difficulty
   */
  public double getDifficulty() {
    return difficulty;
  }

  /**
   * @param difficulty1
   *          the difficulty to set
   */
  public void setDifficulty(double difficulty1) {
    difficulty = difficulty1;
  }

  /**
   * @return the usefulness
   */
  public double getUsefulness() {
    return usefulness;
  }

  /**
   * @param usefulness1
   *          the usefulness to set
   */
  public void setUsefulness(double usefulness1) {
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