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

  TechnionRankerReturnCodes insertProfessor(Professor p);

  Professor getProfessor(Professor p);

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
}
