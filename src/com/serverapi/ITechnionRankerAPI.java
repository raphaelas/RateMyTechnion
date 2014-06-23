package com.serverapi;

import java.util.List;

import com.ratemytechnion.Course;
import com.ratemytechnion.CourseComment;
import com.ratemytechnion.CourseRating;
import com.ratemytechnion.Professor;
import com.ratemytechnion.ProfessorComment;
import com.ratemytechnion.ProfessorRating;
import com.ratemytechnion.StudentProfessorCourse;
import com.ratemytechnion.StudentUser;
import com.serverapi.utilities.TechnionRankerReturnCodes;

public interface ITechnionRankerAPI {

  TechnionRankerReturnCodes insertCourse(List<Course> cList);

  Course getCourse(Course c);

  List<Course> getCourseByCourseNumber(Course c);

  List<Course> getAllCourses();

  TechnionRankerReturnCodes insertProfessor(List<Professor> pList);

  Professor getProfessor(Professor p);

  List<Professor> getAllProfessors();

  List<Professor> getProfessorByProfessorName(Professor p);

  Professor getProfessorForCourse(Course c);

  List<Professor> getProfessorByProfessorHebrewName(Professor p);

  TechnionRankerReturnCodes insertStudentUser(StudentUser s);

  StudentUser getStudentUser(StudentUser s);

  TechnionRankerReturnCodes insertStudentProfessorCourse(
      StudentProfessorCourse spc);

  StudentProfessorCourse getStudentProfessorCourse(StudentProfessorCourse spc);

  List<Professor> getStudentProfessors(StudentUser s);

  List<Course> getStudentCourses(StudentUser s);

  TechnionRankerReturnCodes insertProfessorRating(ProfessorRating pr);

  List<ProfessorRating> getProfessorRatingByProfessor(ProfessorRating pr);

  List<ProfessorRating> getProfessorRatingByStudent(ProfessorRating pr);

  TechnionRankerReturnCodes insertCourseRating(CourseRating cr);

  List<CourseRating> getCourseRatingByCourse(CourseRating cr);

  List<CourseRating> getCourseRatingByStudent(CourseRating cr);

  TechnionRankerReturnCodes insertProfessorComment(ProfessorComment pc);

  List<ProfessorComment> getProfessorCommentByStudent(ProfessorComment pc);

  List<ProfessorComment> getProfessorCommentByProfessor(ProfessorComment pc);

  TechnionRankerReturnCodes insertCourseComment(CourseComment cc);

  List<CourseComment> getCourseCommentByStudent(CourseComment cc);

  List<CourseComment> getCourseCommentByCourse(CourseComment cc);

  TechnionRankerReturnCodes removeCourse(Course c);

  TechnionRankerReturnCodes dropAllCourses();

  TechnionRankerReturnCodes dropAllCourseComments();

  TechnionRankerReturnCodes removeProfessor(Professor p);

  TechnionRankerReturnCodes dropAllProfessors();

  TechnionRankerReturnCodes removeStudentUser(StudentUser s);

  TechnionRankerReturnCodes dropAllStudentUsers();

  TechnionRankerReturnCodes removeStudentProfessorCourse(
      StudentProfessorCourse spc);

  TechnionRankerReturnCodes dropAllStudentProfessorCourses();

  TechnionRankerReturnCodes removeProfessorRating(ProfessorRating pr);

  TechnionRankerReturnCodes dropAllProfessorRatings();

  TechnionRankerReturnCodes removeCourseRating(CourseRating cr);

  TechnionRankerReturnCodes dropAllCourseRatings();

  TechnionRankerReturnCodes removeProfessorComment(ProfessorComment pc);

  TechnionRankerReturnCodes dropAllProfessorComments();

  TechnionRankerReturnCodes removeCourseComment(CourseComment cc);
}
