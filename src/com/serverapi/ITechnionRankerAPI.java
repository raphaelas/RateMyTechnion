package com.serverapi;

import java.util.List;

import com.technionrankerv1.Course;
import com.technionrankerv1.CourseComment;
import com.technionrankerv1.CourseRating;
import com.technionrankerv1.Professor;
import com.technionrankerv1.ProfessorComment;
import com.technionrankerv1.ProfessorRating;
import com.technionrankerv1.StudentProfessorCourse;
import com.technionrankerv1.StudentUser;
import com.serverapi.utilities.TechnionRankerReturnCodes;

public interface ITechnionRankerAPI {

  TechnionRankerReturnCodes insertCourse(Course c);

  Course getCourse(Course c);
  
  List<Course> getAllCourses();
  
  Course getCourseByCourseNumber(Course c);
  
  TechnionRankerReturnCodes removeCourse(Course c);
  
  TechnionRankerReturnCodes dropAllCourses();

  TechnionRankerReturnCodes insertProfessor(Professor p);

  Professor getProfessor(Professor p);
  
  Professor getProfessorByProfessorName(Professor p);
  
  List<Professor> getAllProfessors();
  
  TechnionRankerReturnCodes removeProfessor(Professor p);
  
  TechnionRankerReturnCodes dropAllProfessors();

  TechnionRankerReturnCodes insertStudentUser(StudentUser s);

  StudentUser getStudentUser(StudentUser s);
  
  List<StudentUser> getAllStudentUsers();
  
  TechnionRankerReturnCodes removeStudentUser(StudentUser s);
  
  TechnionRankerReturnCodes dropAllStudentUsers();

  TechnionRankerReturnCodes insertStudentProfessorCourse(
      StudentProfessorCourse spc);

  StudentProfessorCourse getStudentProfessorCourse(StudentProfessorCourse spc);
  
  List<StudentProfessorCourse> getAllStudentProfessorCourses();
  
  TechnionRankerReturnCodes removeStudentProfessorCourse(StudentProfessorCourse spc);
  
  TechnionRankerReturnCodes dropAllStudentProfessorCourses();

  List<Professor> getStudentProfessors(StudentUser s);

  List<Course> getStudentCourses(StudentUser s);

  TechnionRankerReturnCodes insertProfessorRating(ProfessorRating pr);

  List<ProfessorRating> getProfessorRatingByProfessor(ProfessorRating pr);

  List<ProfessorRating> getProfessorRatingByStudent(ProfessorRating pr);
  
  List<ProfessorRating> getAllProfessorRatings();
  
  TechnionRankerReturnCodes removeProfessorRating(ProfessorRating pr);
  
  TechnionRankerReturnCodes dropAllProfessorRatings();

  TechnionRankerReturnCodes insertCourseRating(CourseRating cr);

  List<CourseRating> getCourseRatingByCourse(CourseRating cr);

  List<CourseRating> getCourseRatingByStudent(CourseRating cr);
  
  List<CourseRating> getAllCourseRatings();
  
  TechnionRankerReturnCodes removeCourseRating(CourseRating cr);
  
  TechnionRankerReturnCodes dropAllCourseRatings();

  TechnionRankerReturnCodes insertProfessorComment(ProfessorComment pc);

  List<ProfessorComment> getProfessorCommentByStudent(ProfessorComment pc);

  List<ProfessorComment> getProfessorCommentByProfessor(ProfessorComment pc);
  
  List<ProfessorComment> getAllProfessorComments();
  
  TechnionRankerReturnCodes removeProfessorComment(ProfessorComment pc);
  
  TechnionRankerReturnCodes dropAllProfessorComments();

  TechnionRankerReturnCodes insertCourseComment(CourseComment cc);

  List<CourseComment> getCourseCommentByStudent(CourseComment cc);

  List<CourseComment> getCourseCommentByCourse(CourseComment cc);
  
  List<CourseComment> getAllCourseComments();
  
  TechnionRankerReturnCodes removeCourseComment(CourseComment cc);
  
  TechnionRankerReturnCodes dropAllCourseComments();
}
