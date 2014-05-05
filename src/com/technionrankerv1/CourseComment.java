package com.technionrankerv1;

import java.sql.Time;

public class CourseComment {
  private Long id;
  private String courseNumber;
  private Long studentID;
  private String comment;
  private Time datetime;
  private int likes;

  public CourseComment(String courseNumber1, Long studentID1, String comment1,
      Time datetime1, int likes1) {
    courseNumber = courseNumber1;
    studentID = studentID1;
    comment = comment1;
    datetime = datetime1;
    likes = likes1;
  }

  CourseComment() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id1) {
    id = id1;
  }

  public String getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(String courseNumber1) {
    courseNumber = courseNumber1;
  }

  public Long getStudentID() {
    return studentID;
  }

  public void setStudentID(Long studentID1) {
    studentID = studentID1;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment1) {
    comment = comment1;
  }

  public Time getDatetime() {
    return datetime;
  }

  public void setDatetime(Time datetime1) {
    datetime = datetime1;
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes1) {
    likes = likes1;
  }
}