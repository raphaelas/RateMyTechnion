package com.serverapi;

import com.google.gson.Gson;
import com.serverapi.communicator.Communicator;
import com.serverapi.utilities.TechnionRankerFunctions;
import com.technionrankerv1.Course;

public class TechnionRankerAPI implements ITechnionRankerAPI {
  Gson gson = new Gson();

  @Override
  public String insertCourse(Course c) {
    return Communicator
        .execute("TechnionRankerServlet", "function",
            TechnionRankerFunctions.INSERT_COURSE.value(), "course",
            gson.toJson(c));
  }

}
