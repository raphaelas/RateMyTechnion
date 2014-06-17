package com.serverapi;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.serverapi.communicator.Communicator;
import com.serverapi.utilities.TechnionRankerFunctions;
import com.serverapi.utilities.TechnionRankerReturnCodes;
import com.technionrankerv1.Course;
import com.technionrankerv1.CourseComment;
import com.technionrankerv1.CourseRating;
import com.technionrankerv1.Professor;
import com.technionrankerv1.ProfessorComment;
import com.technionrankerv1.ProfessorRating;
import com.technionrankerv1.StudentProfessorCourse;
import com.technionrankerv1.StudentUser;

public class TechnionRankerAPI implements ITechnionRankerAPI {
	Gson gson = new Gson();
	String servlet = TechnionRankerFunctions.SERVLET.value();
	String function = TechnionRankerFunctions.FUNCTION.value();
	String course = TechnionRankerFunctions.COURSE.value();
	String professor = TechnionRankerFunctions.PROFESSOR.value();
	String professorRating = TechnionRankerFunctions.PROFESSOR_RATING.value();
	String studentUser = TechnionRankerFunctions.STUDENT_USER.value();
	String studentProfessorCourse = TechnionRankerFunctions.STUDENT_PROFESSOR_COURSE
			.value();
	String courseRating = TechnionRankerFunctions.COURSE_RATING.value();
	String professorComment = TechnionRankerFunctions.PROFESSOR_COMMENT.value();
	String courseComment = TechnionRankerFunctions.COURSE_COMMENT.value();

	@Override
	public TechnionRankerReturnCodes insertCourse(List<Course> cList) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.INSERT_COURSE.value(), course,
					gson.toJson(cList)));
		}
		catch (IllegalArgumentException e){
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public Course getCourse(Course c) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_COURSE.value(), course, gson.toJson(c)),
				Course.class);
	}

	@Override
	public TechnionRankerReturnCodes removeCourse(Course c) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.REMOVE_COURSE.value(), course,
					gson.toJson(c)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes dropAllCourses() {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.DROP_ALL_COURSES.value()));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes insertProfessor(List<Professor> pList) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.INSERT_PROFESSOR.value(), professor,
					gson.toJson(pList)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes removeProfessor(Professor p) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.REMOVE_PROFESSOR.value(), professor,
					gson.toJson(p)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes dropAllProfessors() {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.DROP_ALL_PROFESSORS.value()));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public Professor getProfessor(Professor p) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_PROFESSOR.value(), professor,
				gson.toJson(p)), Professor.class);
	}

	@Override
	public TechnionRankerReturnCodes insertStudentUser(StudentUser s) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.INSERT_STUDENT_USER.value(),
					studentUser, gson.toJson(s)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public StudentUser getStudentUser(StudentUser s) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_STUDENT_USER.value(), studentUser,
				gson.toJson(s)), StudentUser.class);
	}

	@Override
	public TechnionRankerReturnCodes removeStudentUser(StudentUser s) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.REMOVE_STUDENT_USER.value(),
					studentUser, gson.toJson(s)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes dropAllStudentUsers() {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.DROP_ALL_STUDENT_USERS.value()));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes insertStudentProfessorCourse(
			StudentProfessorCourse spc) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function,
					TechnionRankerFunctions.INSERT_STUDENT_PROFESSOR_COURSE.value(),
					studentProfessorCourse, gson.toJson(spc)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public StudentProfessorCourse getStudentProfessorCourse(
			StudentProfessorCourse spc) {
		return gson
				.fromJson(Communicator.execute(servlet, function,
						TechnionRankerFunctions.GET_STUDENT_PROFESSOR_COURSE.value(),
						studentProfessorCourse, gson.toJson(spc)),
						StudentProfessorCourse.class);
	}

	@Override
	public TechnionRankerReturnCodes removeStudentProfessorCourse(
			StudentProfessorCourse spc) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function,
					TechnionRankerFunctions.REMOVE_STUDENT_PROFESSOR_COURSE.value(),
					studentProfessorCourse, gson.toJson(spc)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes dropAllStudentProfessorCourses() {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function,
					TechnionRankerFunctions.DROP_ALL_STUDENT_PROFESSOR_COURSES.value()));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public List<Professor> getStudentProfessors(StudentUser s) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_STUDENT_PROFESSORS.value(), studentUser,
				gson.toJson(s)), new TypeToken<List<Professor>>() {
			// default usage
		}.getType());
	}

	@Override
	public List<Course> getStudentCourses(StudentUser s) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_STUDENT_COURSES.value(), studentUser,
				gson.toJson(s)), new TypeToken<List<Course>>() {
			// default usage
		}.getType());
	}

	@Override
	public TechnionRankerReturnCodes insertProfessorRating(ProfessorRating pr) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.INSERT_PROFESSOR_RATING.value(),
					professorRating, gson.toJson(pr)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public List<ProfessorRating> getProfessorRatingByProfessor(ProfessorRating pr) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_PROFESSOR_RATING_BY_PROFESSOR.value(),
				professorRating, gson.toJson(pr)),
				new TypeToken<List<ProfessorRating>>() {
			// default usage
		}.getType());
	}

	@Override
	public List<ProfessorRating> getProfessorRatingByStudent(ProfessorRating pr) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_PROFESSOR_RATING_BY_STUDENT.value(),
				professorRating, gson.toJson(pr)),
				new TypeToken<List<ProfessorRating>>() {
			// default usage
		}.getType());
	}

	@Override
	public TechnionRankerReturnCodes removeProfessorRating(ProfessorRating pr) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.REMOVE_PROFESSOR_RATING.value(),
					professorRating, gson.toJson(pr)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes dropAllProfessorRatings() {
		try {

			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.DROP_ALL_PROFESSOR_RATINGS.value()));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes insertCourseRating(CourseRating cr) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.INSERT_COURSE_RATING.value(),
					courseRating, gson.toJson(cr)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public List<CourseRating> getCourseRatingByCourse(CourseRating cr) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_COURSE_RATING_BY_COURSE.value(),
				courseRating, gson.toJson(cr)), new TypeToken<List<CourseRating>>() {
			// default usage
		}.getType());
	}

	@Override
	public List<CourseRating> getCourseRatingByStudent(CourseRating cr) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_COURSE_RATING_BY_STUDENT.value(),
				courseRating, gson.toJson(cr)), new TypeToken<List<CourseRating>>() {
			// default usage
		}.getType());
	}

	@Override
	public TechnionRankerReturnCodes removeCourseRating(CourseRating cr) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.REMOVE_COURSE_RATING.value(),
					courseRating, gson.toJson(cr)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes dropAllCourseRatings() {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.DROP_ALL_COURSE_RATINGS.value()));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes insertProfessorComment(ProfessorComment pc) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.INSERT_PROFESSOR_COMMENT.value(),
					professorComment, gson.toJson(pc)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public List<ProfessorComment> getProfessorCommentByStudent(ProfessorComment pc) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_PROFESSOR_COMMENT_BY_STUDENT.value(),
				professorComment, gson.toJson(pc)),
				new TypeToken<List<ProfessorComment>>() {
			// default usage
		}.getType());
	}

	@Override
	public List<ProfessorComment> getProfessorCommentByProfessor(
			ProfessorComment pc) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_PROFESSOR_COMMENT_BY_PROESSOR.value(),
				professorComment, gson.toJson(pc)),
				new TypeToken<List<ProfessorComment>>() {
			// default usage
		}.getType());
	}

	@Override
	public TechnionRankerReturnCodes removeProfessorComment(ProfessorComment pc) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.REMOVE_PROFESSOR_COMMENT.value(),
					professorComment, gson.toJson(pc)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes dropAllProfessorComments() {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.DROP_ALL_PROFESSOR_COMMENTS.value()));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes insertCourseComment(CourseComment cc) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.INSERT_COURSE_COMMENT.value(),
					courseComment, gson.toJson(cc)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public List<CourseComment> getCourseCommentByStudent(CourseComment cc) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_COURSE_COMMENT_BY_STUDENT.value(),
				courseComment, gson.toJson(cc)),
				new TypeToken<List<ProfessorComment>>() {
			// default usage
		}.getType());
	}

	@Override
	public List<CourseComment> getCourseCommentByCourse(CourseComment cc) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_COURSE_COMMENT_BY_COURSE.value(),
				courseComment, gson.toJson(cc)),
				new TypeToken<List<ProfessorComment>>() {
			// default usage
		}.getType());
	}

	@Override
	public TechnionRankerReturnCodes removeCourseComment(CourseComment cc) {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.REMOVE_COURSE_COMMENT.value(),
					courseComment, gson.toJson(cc)));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public TechnionRankerReturnCodes dropAllCourseComments() {
		try {
			return TechnionRankerReturnCodes.valueOf(Communicator.execute(servlet,
					function, TechnionRankerFunctions.DROP_ALL_COURSE_COMMENTS.value()));
		}
		catch (IllegalArgumentException e){
			e.printStackTrace();
			return TechnionRankerReturnCodes.valueOf("FAILED");
		}
	}

	@Override
	public List<Course> getCourseByCourseNumber(Course c) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_COURSE_BY_COURSE_NUMBER.value(), course,
				gson.toJson(c)), new TypeToken<List<Course>>() {
			// default usage
		}.getType());
	}

	@Override
	public List<Course> getAllCourses() {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_ALL_COURSES.value()),
				new TypeToken<List<Course>>() {
			// default usage
		}.getType());
	}

	@Override
	public List<Professor> getAllProfessors() {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_ALL_PROFESSORS.value()),
				new TypeToken<List<Professor>>() {
			// default usage
		}.getType());
	}

	@Override
	public List<Professor> getProfessorByProfessorName(Professor p) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_PROFESSOR_BY_NAME.value(), professor,
				gson.toJson(p)), new TypeToken<List<Professor>>() {
			// default usage
		}.getType());
	}

	@Override
	public List<Professor> getProfessorByProfessorHebrewName(Professor p) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_PROFESSOR_BY_HEBREW_NAME.value(),
				professor, gson.toJson(p)), new TypeToken<List<Professor>>() {
			// default usage
		}.getType());
	}

	@Override
	public Professor getProfessorForCourse(Course c) {
		return gson.fromJson(Communicator.execute(servlet, function,
				TechnionRankerFunctions.GET_PROFESSOR_FOR_COURSE.value(), course,
				gson.toJson(c)), Professor.class);
	}

}
